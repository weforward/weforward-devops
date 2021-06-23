#!/bin/bash

NAME=${0%/*}
NAME=${NAME%/*}
NAME=${NAME##*/}

read -p "input DockerHub" DOCKER_HUB

read -p "Input Version:" VERSION

cd `dirname $0`;

IMAGE_NAME=$DOCKER_HUB/$NAME:$VERSION

docker pull $IMAGE_NAME

if [ $? -ne 0 ] ; then
        echo 'PULL ERROR'
        exit 1
fi

docker rm "$NAME-old"

docker stop $NAME

docker rename $NAME "$NAME-old"

CONF_DIR=/wf/ms/$NAME/conf

SCRIPT_DIR=/wf/ms/$NAME/script

SITE_DIR=/wf/ms/$NAME/site

LOG_DIR=/var/log/$NAME

CA_DIR=/wf/ca/

DATA_DIR=/data/

V_OPT=""

V_OPT=""

if [ -d $CONF_DIR ]; then
        V_OPT=" -v $CONF_DIR:/home/boot/conf/"
fi

if [ -d $SCRIPT_DIR ]; then
        V_OPT=$V_OPT" -v $SCRIPT_DIR:/home/boot/script/"
fi

if [ -d $CA_DIR ]; then
        V_OPT=$V_OPT" -v $CA_DIR:/home/boot/ca/"
fi

if [ -d $SITE_DIR ]; then
        V_OPT=$V_OPT" -v $SITE_DIR:/home/boot/site/"
fi

if [ -d $LOG_DIR ]; then
        V_OPT=$V_OPT" -v $LOG_DIR:/home/boot/log/"
fi

if [ -d $DATA_DIR ]; then
        V_OPT=$V_OPT" -v $DATA_DIR:/home/boot/data/"
fi

EVN_OPT=""

if [ -f ./my.env ]; then
   EVN_OPT="--env-file ./my.env"
fi

docker run --restart=unless-stopped -d $V_OPT -v /etc/localtime:/etc/localtime:ro --env PROJECT_VERSION=$VERSION $EVN_OPT --net host --name $NAME $IMAGE_NAME

exit 0