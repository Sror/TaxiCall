#ifndef _mysql_client_private_h
#define _mysql_client_private_h

#include <string>
#include <sstream>
#include <stdexcept> 
#include <boost/shared_ptr.hpp>
#include <mysql/mysql.h>

namespace mysql_private
{
        using namespace std;
	typedef boost::shared_ptr<MYSQL> ConnPtr;
	typedef boost::shared_ptr<MYSQL_RES> ResPtr;
	
	//
	// Try to use this close function instead of 'mysql_close' to 
	// avoid dependency of the mysqlclient library and increase 
	// debuggability
	//
	static void CloseConnection(MYSQL* conn)
	{
            if (conn)
            {
                mysql_close(conn);
//					cerr<<"mysql_close is called"<<endl;
            }
	}

        static void CloseResultSet(MYSQL_RES* res)
        {
            if (res != NULL)
            {
                mysql_free_result(res);
//					cerr<<"CloseResultSet is called"<<endl;
            }
        }

	
	//
	// This function may throw run time error
	//
	static ConnPtr GetConnection(string const& hostName,  string const& dbName, string const&username, string const& password)
	{
		MYSQL* conn=mysql_init(NULL);

		if (!conn) 
			throw std::runtime_error ("Unable to initialize the connection in mysql_init");
                
                if (0 != mysql_options(conn, MYSQL_SET_CHARSET_NAME, "utf8"))
			throw std::runtime_error ("Unable to set charset in mysql_options");

		boost::shared_ptr<MYSQL> shared_conn(conn,CloseConnection);

		if (mysql_real_connect(conn,
							hostName.c_str(),
							username.c_str(),
							password.c_str(),
							dbName.c_str(),
							0, // default port
							"/tmp/mysqld.sock",// default socket name,
							0 //connection flag, none
							)==NULL)
		{
			ostringstream os;
                        os << "error: " << mysql_errno(conn) << "(" << mysql_error(conn) << ")" << endl;
			os<<"Unable to connect db '"<<dbName<<"'"<<" of "<<hostName;
			throw std::runtime_error (os.str());
		}

		return shared_conn;
	}

	static int GetLastErrorCode(ConnPtr ptr) {return mysql_errno(ptr.get());}
	static string GetLastErrorString(ConnPtr ptr) {return mysql_error(ptr.get());}
}

#endif

