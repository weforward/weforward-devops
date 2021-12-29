#!/bin/bash

SERVER_ID=$SERVER_ID

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

INTERNAL_ACCESS_SECRET=$INTERNAL_ACCESS_SECRET
ACCESSKEY_SECRET=$ACCESSKEY_SECRET

_term() { 
  kill -TERM $child
  kill_child=$!
  wait "$kill_child"
}
 
trap _term TERM

DEFAULT_JAVA_OPTIONS="-Dweforward.serverid=$SERVER_ID -Dweforward.apiUrl=$WF_GATEWAY_URL -Dweforward.service.accessId=$WF_SERVICE_ACCESSID -Dweforward.service.accessKey=$WF_SERVICE_ACCESSKEY -Dweforward.host=$WF_HOST -Dweforward.port=$WF_PORT -DinternalAccess.secret=$INTERNAL_ACCESS_SECRET -DaccessKey.secret=$ACCESSKEY_SECRET -Drlog.url=$RLOG_URL -Dlog.path=$LOG_PATH -Dproject_name=$NAME  -Dhttp.maxConnections=100 -Xms$Xms -Xmx$Xmx -XX:MaxDirectMemorySize=$MAX_DIRECT_MEMORY_SIZE -Xss256k -XX:+UseG1GC -XX:InitiatingHeapOccupancyPercent=75 -XX:G1ReservePercent=5"

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