#include <boost/asio.hpp>
#include <boost/array.hpp>
#include <boost/bind.hpp>
#include <boost/shared_ptr.hpp>
#include <boost/enable_shared_from_this.hpp>
#include <iostream>
#include<fstream>
#include <string>
#include "mysql_client_conn.h" 

using namespace boost;
using namespace mysql_connection;
using boost::asio::ip::tcp; 


boost::shared_ptr<mysql_connection::CMysqlConn> g_DBConn;

namespace third_party_lib {

// Simulation of a third party library that wants to perform read and write
// operations directly on a socket. It needs to be polled to determine whether
// it requires a read or write operation, and notified when the socket is ready
// for reading or writing.
class session
{
public:
  session(tcp::socket& socket)
    : socket_(socket),
      state_(writing),
      write_buffer_("abcdef", 6)
  {
  }

  // Returns true if the third party library wants to be notified when the
  // socket is ready for reading.
  bool want_read() const
  {
    return state_ == reading;
  }

  // Notify that third party library that it should perform its read operation.
  void do_read(boost::system::error_code& ec)
  {
    if (std::size_t len = socket_.read_some(boost::asio::buffer(data_), ec))
    {
      std::cout << data_.c_array() << std::endl;
      write_buffer_ = boost::asio::buffer(data_, len);
      state_ = writing;
    }
  }

  // Returns true if the third party library wants to be notified when the
  // socket is ready for writing.
  bool want_write() const
  {
    return state_ == writing;
  }

  // Notify that third party library that it should perform its write operation.
  void do_write(boost::system::error_code& ec)
  {
    if (std::size_t len = socket_.write_some(
          boost::asio::buffer(write_buffer_), ec))
    {
      write_buffer_ = write_buffer_ + len;
      state_ = boost::asio::buffer_size(write_buffer_) > 0 ? writing : reading;
    }
  }

private:
  tcp::socket& socket_;
  enum { reading, writing } state_;
  boost::array<char, 128> data_;
  boost::asio::const_buffer write_buffer_;
};

} // namespace third_party_lib

// The glue between asio's sockets and the third party library.
class connection
  : public boost::enable_shared_from_this<connection>
{
public:
  typedef boost::shared_ptr<connection> pointer;

  static pointer create(boost::asio::io_service& io_service)
  {
    return pointer(new connection(io_service));
  }

  tcp::socket& socket()
  {
    return socket_;
  }

  void start()
  {
    // Put the socket into non-blocking mode.
    socket_.non_blocking(true);

    boost::asio::ip::tcp::no_delay nd(true);
    socket_.set_option(nd);
    boost::asio::socket_base::keep_alive ka(true);
    socket_.set_option(ka);

    start_operations();
  }

private:
  connection(boost::asio::io_service& io_service)
    : socket_(io_service),
      session_impl_(socket_),
      read_in_progress_(false),
      write_in_progress_(false)
  {
  }

  void start_operations()
  {
    // Start a read operation if the third party library wants one.
    if (session_impl_.want_read() && !read_in_progress_)
    {
      read_in_progress_ = true;
      socket_.async_read_some(
          boost::asio::null_buffers(),
          boost::bind(&connection::handle_read,
            shared_from_this(),
            boost::asio::placeholders::error));
    }

    // Start a write operation if the third party library wants one.
    if (session_impl_.want_write() && !write_in_progress_)
    {
      write_in_progress_ = true;
      socket_.async_write_some(
          boost::asio::null_buffers(),
          boost::bind(&connection::handle_write,
            shared_from_this(),
            boost::asio::placeholders::error));
    }
  }

  void handle_read(boost::system::error_code ec)
  {
    read_in_progress_ = false;

    // Notify third party library that it can perform a read.
    if (!ec)
      session_impl_.do_read(ec);

    // The third party library successfully performed a read on the socket.
    // Start new read or write operations based on what it now wants.
    if (!ec || ec == boost::asio::error::would_block)
      start_operations();

    // Otherwise, an error occurred. Closing the socket cancels any outstanding
    // asynchronous read or write operations. The connection object will be
    // destroyed automatically once those outstanding operations complete.
    else
      socket_.close();
  }

  void handle_write(boost::system::error_code ec)
  {
    write_in_progress_ = false;

    // Notify third party library that it can perform a write.
    if (!ec)
      session_impl_.do_write(ec);

    // The third party library successfully performed a write on the socket.
    // Start new read or write operations based on what it now wants.
    if (!ec || ec == boost::asio::error::would_block)
      start_operations();

    // Otherwise, an error occurred. Closing the socket cancels any outstanding
    // asynchronous read or write operations. The connection object will be
    // destroyed automatically once those outstanding operations complete.
    else
      socket_.close();
  }

private:
  tcp::socket socket_;
  third_party_lib::session session_impl_;
  bool read_in_progress_;
  bool write_in_progress_;
};

class server
{
public:
  server(boost::asio::io_service& io_service, const char* ip, const char* port)
    : query_(tcp::v4(), ip, port),
      resolver_(io_service)
  {
    conn = connection::create(io_service);
    resolver_.async_resolve(query_,
        boost::bind(&server::handle_resolve, this,
          boost::asio::placeholders::error,
          boost::asio::placeholders::iterator));
  }

private:
  void handle_resolve(const boost::system::error_code& err,
      tcp::resolver::iterator endpoint_iterator)
  {
    if (!err)
    {
      // Attempt a connection to each endpoint in the list until we
      // successfully establish a connection.
      boost::asio::async_connect(conn->socket(), endpoint_iterator,
          boost::bind(&server::handle_connect, this,
            boost::asio::placeholders::error));
    }
    else
    {
      std::cout << "Error: " << err.message() << "\n";
      std::cout << "Trying to connect again..." << "\n";
      resolver_.async_resolve(query_,
          boost::bind(&server::handle_resolve, this,
            boost::asio::placeholders::error,
            boost::asio::placeholders::iterator));
    }
  }

  void handle_connect(const boost::system::error_code& err)
  {
    if (!err)
    {
      // The connection was successful.
      std::cout << "connection established." << "\n";
        
      conn->start();
    }
    else
    {
      std::cout << "Error: " << err.message() << "\n";
      std::cout << "Trying to connect again..." << "\n";
      resolver_.async_resolve(query_,
          boost::bind(&server::handle_resolve, this,
            boost::asio::placeholders::error,
            boost::asio::placeholders::iterator));
    }
  }

  tcp::resolver::query query_;
  tcp::resolver resolver_;
  connection::pointer conn;
};

int main(int argc, char* argv[])
{
  try
  {
    g_DBConn.reset(new mysql_connection::CMysqlConn("localhost",
        "taxi",
        "root",
        "sa@index"
        ));


    string updateQuery = "INSERT INTO `tbl_user_orders` (`order_id`, `surname`, `gender`, `phone_number`, `start_address`, `end_address`, `expected_time`, `passenger_count`, `is_vip`, `notes`, `creation_ts`, `taxi_plate`, `result`) VALUES(11111, '李', 0, '13501341544', '光华路soho 1716', '', NULL, 1, 0, '', '2011-09-09 14:06:37', NULL, 0)";
    g_DBConn->Query<NoData>(updateQuery);

//    ofstream outf("/home/calvin/test.log");
	std::string query = "select * from tbl_user_orders";
    CMysqlSet rset = g_DBConn->Query<WithData>(query);
	if (rset)
	{
		cout << "I got a result" << endl;
		unsigned size = rset.NumberOfRecords();
		cout << size <<" response with " << rset.NumberOfFields() << " columns from the query '" << query << "'" <<endl;
		for (unsigned int i=0;i<size; i++)
		{
			CMysqlRow row = rset.GetNextRow();
			if (row)
			{
				for (unsigned int j=0; j<row.NumberOfFields();j++)
				{
					cout << "\t" << row[j] << ":" << strlen(row[j].c_str());
//					outf << "\t" << row[j];
				}
				cout << endl;
//				outf << endl;
			}
		}
	}
		
    if (argc != 3)
    {
      std::cerr << "Usage: taxi_server <server> <port>\n";
      return 1;
    }

    using namespace std; // For atoi.
    
    while (1) 
    {
      boost::asio::io_service io_service;
      server s(io_service, argv[1], argv[2]);
      io_service.run();
    }
  }
  catch (std::exception& e)
  {
    std::cerr << "Exception: " << e.what() << "\n";
  }

  return 0;
}
