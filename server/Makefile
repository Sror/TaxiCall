
.SUFFIXES: .cpp 
#CXX=/usr/local/gcc-411/bin/g++
CXX=g++
CXX_FLAGS=-Wall -O2 -DNDEBUG -Wno-unused 
OBJ = taxi_server.o
LDFLAGS= -L../boost_1_47_0_bin/lib -L/usr/lib -lboost_system -lboost_thread -lpthread -static-libgcc -lmysqlclient

BOOST_INCLUDE_PATH=-I../boost_1_47_0_bin/include -I. -I/usr/include
# LIBJPEG_LIB_PATH=-L../../../../lib/libjpeg

all: taxi_server

%.o:%.cpp
	${CXX} ${CXX_FLAGS} ${BOOST_INCLUDE_PATH} -c $<

taxi_server: ${OBJ}
	${CXX} ${OBJ} -o $@ ${LDFLAGS}
	
.PHONY: clean
clean:
	rm -f *.o *.exe taxi_server

