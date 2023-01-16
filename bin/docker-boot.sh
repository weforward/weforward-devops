#!/bin/bash

SERVER_ID=$SERVER_ID

if   [ $SERVER_ID ] ;
then 
	SERVER_ID=$SERVER_ID;
else 
	SERVER_ID="x0001"
fi

PROJECT_NAME=weforward-devops

if   [ $PROJECT_NAME ] ;
then 
	NAME=$PROJECT_NAME
else 
	NAME=weforward-devops
fi

if   [ $LOG_PATH ] ;
then 
	LOG_PATH=$LOG_PATH;
else 
	LOG_PATH=./log
fi

if   [ $RLOG_URL ] ;
then
	RLOG_URL=$RLOG_URL
else 
	RLOG_URL=http://127.0.0.1:1200/logs.j
fi

if [ ! -n "$Xms" ]; then 
	Xms=384m
else 
	Xms=$Xms
fi 

if [ ! -n "$Xmx" ]; then 
	Xmx=512m
else 
	Xmx=$Xmx
fi 

if [ ! -n "$MAX_DIRECT_MEMORY_SIZE" ]; then 
	MAX_DIRECT_MEMORY_SIZE=512m
else 
	MAX_DIRECT_MEMORY_SIZE=$MAX_DIRECT_MEMORY_SIZE
fi 

if [ ! -n "$INTERNAL_ACCESS_SECRET" ]; then 
	INTERNAL_ACCESS_SECRET=$WEFORWARD_PASSWORD
else 
	INTERNAL_ACCESS_SECRET=$INTERNAL_ACCESS_SECRET
fi 

if [ ! -n "$ACCESSKEY_SECRET" ]; then 
	ACCESSKEY_SECRET=$WEFORWARD_PASSWORD
else 
	ACCESSKEY_SECRET=$ACCESSKEY_SECRET
fi 

_term() { 
  kill -TERM $child
  kill_child=$!
  wait "$kill_child"
}
 
trap _term TERM

DEFAULT_JAVA_OPTIONS=""

if   [ $SERVER_ID ] ;
then 
	DEFAULT_JAVA_OPTIONS=$DEFAULT_JAVA_OPTIONS"-Dweforward.serverid=$SERVER_ID "
fi

if   [ $WF_GATEWAY_URL ] ;
then 
	DEFAULT_JAVA_OPTIONS=$DEFAULT_JAVA_OPTIONS"-Dweforward.apiUrl=$WF_GATEWAY_URL "
fi

if   [ $WF_SERVICE_ACCESSID ] ;
then 
	DEFAULT_JAVA_OPTIONS=$DEFAULT_JAVA_OPTIONS"-Dweforward.service.accessId=$WF_SERVICE_ACCESSID "
fi

if   [ $WF_SERVICE_ACCESSKEY ] ;
then 
	DEFAULT_JAVA_OPTIONS=$DEFAULT_JAVA_OPTIONS"-Dweforward.service.accessKey=$WF_SERVICE_ACCESSKEY "
fi

if   [ $WF_HOST ] ;
then 
	DEFAULT_JAVA_OPTIONS=$DEFAULT_JAVA_OPTIONS"-Dweforward.host=$WF_HOST "
fi

if   [ $WF_PORT ] ;
then 
	DEFAULT_JAVA_OPTIONS=$DEFAULT_JAVA_OPTIONS"-Dweforward.port=$WF_PORT "
fi

if   [ $INTERNAL_ACCESS_SECRET ] ;
then 
	DEFAULT_JAVA_OPTIONS=$DEFAULT_JAVA_OPTIONS"-DinternalAccess.secret=$INTERNAL_ACCESS_SECRET "
fi

if   [ $ACCESSKEY_SECRET ] ;
then 
	DEFAULT_JAVA_OPTIONS=$DEFAULT_JAVA_OPTIONS"-DaccessKey.secret=$ACCESSKEY_SECRET "
fi

if   [ $MONGODB_CONNECTION ] ;
then 
	DEFAULT_JAVA_OPTIONS=$DEFAULT_JAVA_OPTIONS"-Dmongodb.url=$MONGODB_CONNECTION "
fi

if   [ $MONGODB_DB_NAME ] ;
then 
	DEFAULT_JAVA_OPTIONS=$DEFAULT_JAVA_OPTIONS"-Dmongodb.dbname=$MONGODB_DB_NAME "
fi

DEFAULT_JAVA_OPTIONS=$DEFAULT_JAVA_OPTIONS"-Drlog.url=$RLOG_URL -Dlog.path=$LOG_PATH -Dlogback.configurationFile=./logback.xml -Dproject_name=$NAME  -Dhttp.maxConnections=100 -Xms$Xms -Xmx$Xmx -XX:MaxDirectMemorySize=$MAX_DIRECT_MEMORY_SIZE -Xss256k -XX:+UseG1GC -XX:InitiatingHeapOccupancyPercent=75 -XX:G1ReservePercent=5"

if   [ -n "$WF_JAVA_OPTIONS" ] ;
then 
	JAVA_OPTIONS=$WF_JAVA_OPTIONS" "$DEFAULT_JAVA_OPTIONS
else 
	JAVA_OPTIONS=$DEFAULT_JAVA_OPTIONS
fi

ulimit -n 10000

java $JAVA_OPTIONS -jar $NAME.jar &

child=$!
wait "$child"