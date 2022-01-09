#!/bin/sh

cd `dirname $0`;

NAME=${0%/*}
NAME=${NAME%/*}
NAME=${NAME##*/}

docker stop $NAME

docker start "$NAME-old"
