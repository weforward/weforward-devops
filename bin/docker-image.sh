#!/bin/bash

NAME=${0%/*}
NAME=${NAME%/*}
NAME=${NAME##*/}

IMAGE_NAME=weforward/devops

VERSION=`docker exec $NAME java VersionUtil $NAME.jar`

if [ $? -ne 0 ] ; then 
	echo 'VERSION ERROR'
	exit 1
fi

if [ $# -lt  2 ] ; then
	read -p "Input Docker User:" DOCKERUSER
	echo $DOCKERUSER
	read -s -p "Input Docker Password:" DOCKERPASSWORD
	echo ''
	read -p "Input Docker Hub default is hub.docker.com:" DOCKER_HUB
	echo $DOCKER_HUB
	read -p "Input Version default is $VERSION:" INPUT_VERSION
	if [ $INPUT_VERSION ] ; then
		VERSION=$INPUT_VERSION
	fi
else
	DOCKERUSER=$1
	DOCKERPASSWORD=$2
	DOCKER_HUB=$3
fi 

echo "Version "$VERSION

if   [ $DOCKER_HUB ] ;
then 
	TAG_NAME=$DOCKER_HUB/$IMAGE_NAME:$VERSION
else 
	TAG_NAME=$IMAGE_NAME:$VERSION
fi

docker tag $IMAGE_NAME $TAG_NAME

docker login -u $DOCKERUSER -p $DOCKERPASSWORD $DOCKER_HUB 

if [ $? -ne 0 ] ; then 
	echo 'User or Password ERROR'
	exit 1
fi

docker push $TAG_NAME

docker logout $DOCKER_HUB
if [ $? -ne 0 ] ; then 
	echo "上传$TAG_NAME失败"
else
	echo "成功上传$TAG_NAME"
fi

  