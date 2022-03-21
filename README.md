# weforward-devops
weforward控制台项目

# 安装

## 方式一:两键安装
wget https://gitee.com/weforward/weforward-devops/raw/main/bin/install.sh 

bash install.sh

## 方式二:分开运行

### mongodb
docker run --log-opt max-size=100m --restart=unless-stopped -v /data/mongo:/data/db -d -p 27017:27017 --name mongo mongo:4.4.11-rc1-focal

### influxdb
docker run --log-opt max-size=100m --restart=unless-stopped  -v /data/influxdb/meta:/var/lib/influxdb/meta  -v /data/influxdb/data:/var/lib/influxdb/data  -v /data/influxdb/wal:/var/lib/influxdb/wal -d -p 8086:8086 --name influxdb influxdb:1.7.11

### gateway
docker run  --log-opt max-size=100m --restart=unless-stopped -d -e WEFORWARD_PASSWORD=888888 --net host --name weforward-gateway weforward/gateway

### devops
docker run  --log-opt max-size=100m --restart=unless-stopped -d  -e WEFORWARD_PASSWORD=888888 --net host --name weforward-devops weforward/devops

## 方式三:集群方式

### mongodb
需调整成副本集方式

### influxdb
无需调整

### gateway
需调整成集群方法，步骤如下

#### 创建配置文件

- 创建配置目录
	
	mkdir -p /wf/gw/conf/

- 创建文件/wf/gw/conf/gateway.properties

	 gateway.host=[当前网关的ip]
	 
	 gateway.port=[当前网关的端口]
	 
	 gateway.brothers=[{"id":"x0002","h":"[当前网关的ip]","p":当前网关的端口},{"id":"x0003","h":"[其它网关的ip]","p":[其它网关的端口]}]
	 
	 metrics.meterUrl=http://[控制台ip]:1500/metrics.j
	 
	 metrics.traceUrl=http://[控制台ip]:1500/trace.j

- 创建文件/wf/gw/conf/plugin/mongodb.properties
	
	db.connection=mongodb://[数据库ip]:[端口]/?replicaSet=[副本集名]&readpreference=nearest
	
	db.name=[数据库名]


#### 挂载目录文件

docker run  --log-opt max-size=100m --restart=unless-stopped -d -e WEFORWARD_PASSWORD=888888 -v /wf/gw/conf:/home/boot/conf --net host --name weforward-gateway weforward/gateway

**在另一台服务器重复以上操作，注意调整对应ip**

### devops
需更改配置，步骤如下

#### 创建配置文件和数据目录

- 创建配置目录

	mkdir -p /wf/weforward-devops/conf/

- 创建数据目录

	mkdir /data/
	
	chown 1008:1008 /data/

- 创建文件/wf/ms/weforward-devops/conf/devops.properties

	mongodb.url=mongodb://[数据库ip]:[端口]/?replicaSet=[副本集名]&readpreference=nearest
	
	metrics.influxdb.url=http://[数据库ip]::[端口]

#### 挂载目录文件并指定网关ip

docker run  --log-opt max-size=100m --restart=unless-stopped -d  -e "WEFORWARD_PASSWORD=888888" -e "WF_GATEWAY_URL=http://[A网关ip]:[A网关端口]/;http://[B网关ip]:[B网关端口]/" -v /wf/ms/weforward-devops/conf:/home/boot/conf -v /data/:/home/boot/data/ --net host --name weforward-devops weforward/devops

**控制台仅用于运维部署，只部署一台服务即可**

## 访问

地址： http://127.0.0.1:1400/devops/



