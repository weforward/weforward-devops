#!/bin/bash

NAME=${0%/*}
NAME=${NAME%/*}
NAME=${NAME##*/}

IMAGE_NAME=$NAME

VERSION=`docker exec $NAME java VersionUtil $NAME.jar`

if [ $? -ne 0 ] ; then 
	echo 'VERSION ERROR'
	exit 1
fi

echo "Version "$VERSION

if [ $# -lt  2 ] ; then
	read -p "Input Docker HUB:" DOCKER_HUB
	echo $DOCKER_HUB
	read -p "Input Docker User:" DOCKERUSER
	echo $DOCKERUSER
	read -s -p "Input Docker Password:" DOCKERPASSWORD
else
	DOCKERUSER=$1
	DOCKERPASSWORD=$2
fi 

docker tag $IMAGE_NAME $DOCKER_HUB/$IMAGE_NAME:$VERSION

docker login -u $DOCKERUSER -p $DOCKERPASSWORD $DOCKER_HUB 

if [ $? -ne 0 ] ; then 
	echo 'User or Password ERROR'
	exit 1
fi

docker push $DOCKER_HUB/$IMAGE_NAME:$VERSION

docker logout $DOCKER_HUB
if [ $? -ne 0 ] ; then 
	echo "上传$DOCKER_HUB/$IMAGE_NAME:$VERSION失败"
else
	echo "成功上传$DOCKER_HUB/$IMAGE_NAME:$VERSION"
fi

  