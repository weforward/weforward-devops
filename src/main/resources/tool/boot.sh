#!/bin/bash

NAME=$PROJECT_NAME

SERVER_ID=$SERVER_ID

OURLINC_SERVER_ID=$OURLINC_SERVER_ID

RLOG_URL=$RLOG_URL

_term() {
   kill -TERM $child
   kill_child=$!
   wait "$kill_child"
}

trap _term TERM

DEFAULT_JAVA_OPTIONS="-Dweforward.serverid=$SERVER_ID -Dourlinc.serverid=$OURLINC_SERVER_ID -Dproject_name=$NAME -Drlog.url=$RLOG_URL"

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