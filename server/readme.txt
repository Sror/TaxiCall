How to build
------------
1. This application is built against Boost 1.47.0 with ASIO. Please visit www.boost.org to find out how to build Boost;
2. libmysqlclient should be installed as well;
3. Run "make all" to build all, and "make clean" to clean the build.


System Configuration
-------------
Add these commands to /etc/rc.d/rc.local and restart server:
echo 60 > /proc/sys/net/ipv4/tcp_keepalive_time
echo 20 > /proc/sys/net/ipv4/tcp_keepalive_intvl
echo 2 > /proc/sys/net/ipv4/tcp_keepalive_probes

or edit /etc/sysctl.conf directly and restart network.
