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

_term() { 
  kill -TERM $child
  kill_child=$!
  wait "$kill_child"
}
 
trap _term TERM

DEFAULT_JAVA_OPTIONS="-Dweforward.serverid=$SERVER_ID -Dweforward.apiUrl=$WF_GATEWAY_URL -Dweforward.service.accessId=$WF_SERVICE_ACCESSID -Dweforward.service.accessKey=$WF_SERVICE_ACCESSKEY -Dweforward.host=$WF_HOST -Dweforward.port=$WF_PORT -Drlog.url=$RLOG_URL -Dlog.path=$LOG_PATH -Dproject_name=$NAME"

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