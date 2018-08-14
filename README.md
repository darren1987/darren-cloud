

### dev环境
                            | ip        | 端口  | 说明 
----------------------------|-----------|-------|------------------------------
dr-basic-zuul-server        | localhost | 10000 | zuul网关对外端口
                            | localhost | 10050 | zuul网关整合异构语言, 端口
dr-basic-eureka-server      | localhost | 10100 | eureka注册中心对外端口
dr-basic-monitoring-server  | localhost | 10200 | 监控中心对外端口
Zipkin server               | localhost | 9411  | zipkin链路跟踪server端
dr-auth-server              | localhost | 20000 | 授权服务对外端口
dr-scheduling-server        | localhost | 20100 | 定时调度服务对外端口
dr-movie-server             | localhost | 20200 | 电影服务对外端口
dr-order-server             | localhost | 20300 | 订单服务对外端口
dr-user-server              | localhost | 20400 | 用户服务对外端口

### dev url
- eureka主页: http://localhost:10100/
- eureka服务注册地址: http://192.168.82.64:10100/eureka/
- spsring-boot-admin监控home: http://localhost:10200/


### zipkin链路跟踪器
- Zipkin 的服务端，在 Spring Boot 2.x 后官方不推荐自行定制编译, 直接提供 jar包使用
- @EnableZipkinServer也已经被打上了@Deprecated
- 访问url http://localhost:9411/zipkin/ 

```shell
# zipkin私有服务
curl -sSL https://zipkin.io/quickstart.sh | bash -s
java -jar zipkin.jar

# docker模式
docker run -d -p 9411:9411 openzipkin/zipkin
```


### todo
- mysql 数据库
- mongo 数据库
- hbase 数据库
- redis 缓存
- kafka 消息队列
- spark 实时计算
- Elk   日志分析


### url
- https://github.com/dyc87112/SpringCloud-Learning


