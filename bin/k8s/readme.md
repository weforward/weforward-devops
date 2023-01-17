#k8s初始化
 
 启动
 
 1、kubectl apply -f mongodb.yaml
 
 2、kubectl apply -f secret.yaml
 
 注意修改password
  
 3、kubectl apply -f gateway.yaml
 
 4、kubectl apply -f devops.yaml
 
 清理
 
 1、kubectl delete -f mongodb.yaml
 
 2、kubectl delete -f secret.yaml
 
 3、kubectl delete -f gateway.yaml
 
 4、kubectl delete -f devops.yaml
 
 