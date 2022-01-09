# weforward-devops
weforward控制台项目

# 安装

## 两键安装
wget https://gitee.com/weforward/weforward-devops/raw/main/bin/install.sh 

bash install.sh

## 分开运行

### mongodb
docker run --log-opt max-size=100m --restart=unless-stopped -v /data/mongo:/data/db -d -p 27017:27017 --name mongo mongo:4.4.11-rc1-focal

### influxdb
docker run --log-opt max-size=100m --restart=unless-stopped  -v /data/influxdb/meta:/var/lib/influxdb/meta  -v /data/influxdb/data:/var/lib/influxdb/data  -v /data/influxdb/wal:/var/lib/influxdb/wal -d -p 8086:8086 --name influxdb influxdb:1.7.11

### gateway
docker run  --log-opt max-size=100m --restart=unless-stopped -d -e WEFORWARD_PASSWORD=888888 --net host --name weforward-gateway weforward/gateway

### devops
docker run  --log-opt max-size=100m --restart=unless-stopped -d  -e WEFORWARD_PASSWORD=888888 --net host --name weforward-devops weforward/devops

## 访问

地址: http://127.0.0.1:1400/devops/

