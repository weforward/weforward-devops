#!/bin/bash

docker -v > /dev/null

if [ $? -ne 0 ]; then
	echo '请先安装docker!!'
	exit 1
fi

echo '开始安装.'

read -s -p "请输入一个自定义的密码:" PASSWORD

echo '开始初始化环境.'

mkdir -p /var/log/weforward-gateway/

chown 1008:1008 /var/log/weforward-gateway/

mkdir -p /var/log/weforward-devops/

chown 1008:1008 /var/log/weforward-devops/

mkdir -p /data/rlog.data/

mkdir -p /data/dist.data/

chown 1008:1008 /data/rlog.data/

chown 1008:1008 /data/dist.data/

echo '初始化环境结束.'

echo '开始安装monogdb.'
docker run --log-opt max-size=100m --restart=unless-stopped -v /data/mongo:/data/db -d -p 27017:27017 --name mongo mongo:4.4.11-rc1-focal
echo '安装monogdb完成.'

echo '开始安装influxdb.'
docker run --log-opt max-size=100m --restart=unless-stopped -v /data/influxdb/meta:/var/lib/influxdb/meta  -v /data/influxdb/data:/var/lib/influxdb/data  -v /data/influxdb/wal:/var/lib/influxdb/wal -d -p 8086:8086 --name influxdb influxdb:1.7.11
echo '安装influxdb完成.'

echo '开始安装gateway.'
docker run --log-opt max-size=100m --restart=unless-stopped -v /var/log/weforward-gateway/:/home/boot/log/  -d -e WEFORWARD_PASSWORD=$PASSWORD --net host --name weforward-gateway weforward/gateway
echo '安装gateway完成.'

sleep 10

echo '开始安装devops.'
docker run --log-opt max-size=100m --restart=unless-stopped -v /var/log/weforward-devops/:/home/boot/log/ -v /data/:/home/boot/data/ -d  -e WEFORWARD_PASSWORD=$PASSWORD --net host --name weforward-devops weforward/devops
echo '安装devops完成.'

echo '安装完成.输入http://127.0.0.1:1400/devops/访问控制台'
