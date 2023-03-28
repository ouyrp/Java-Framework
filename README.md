### 简介 ###

http://confluence.wusong.com/pages/viewpage.action?pageId=61178637


## Monitoring
### 健康检测
```shell
http://localhost:8081/actuator/health
```
### 指标
Prometheus 采集地址：
```shell
http://localhost:8081/actuator/prometheus
```
### 日志
#### 发送日志到 Kafka
可以配置日志直接发送到kafka，减少本地磁盘占用。配置如下(操作系统环境变量或者启动参数均可)
```shell
logging.kafka.bootstrapServers=kafka01-dev.wusong.com:9092 # 必选
logging.kafka.topic=OPT_LOGS_DEV # 可选，如果不配置，默认OPT_LOGS
logging.kafka.compressionType=gzip # 可选，如果不配置则默认none不压缩。
```

#### logback 日志
1. 删除 logback.xml，框架会自动配置日志目录、格式、历史备份、异步日志等。
2. 如果要自定义日志级别，增加配置 `logging.level.com.wusong=debug`
3. 单条日志内容默认最大长度为5000，超出部分会丢弃（warn、error日志不会）。如果要加长可配置`wsframework.monitoring.maxLogLength=6666`

#### SQL 日志
1. 线下打印所有SQL，以及占位符。可能泄露隐私
2. 生产环境打印SQL，但不打印占位符的实际内容
3. 如果只想打印慢SQL，增加配置 `wsframework.monitoring.sql.executionthreshold=100`，表示只打印超过100ms的sql
#### HTTP 日志
1. 默认打印所有http请求的进、出日志
2. 可通过参数修改
```yaml
wsframework.monitoring:
  urlFilterPatterns: /*
  excludeAntPatterns: /static/**,**/no/log/**
```
#### Controller 日志
1. 默认打印所有Controller和RestController的日志，可以通过下面参数禁用
```yaml
wsframework.monitoring.log:
  controller: 
    enable: false
  rest: 
    enable: false
```
#### Monitoring 自定义注解日志
1. 如果要对 service component 等自定义的bean的方法加调用日志，可以对类或者方法加注解 @Monitoring
2. 线下环境打印出入参数。生产不打印参数和返回值，防止泄密。

## 配置中心
接入配置中心后，你的应用只需要1个配置参数即可开始开发(Apollo里的应用名必须和spring.application.name一致)。

配置中心会做下面这些事情：
1. 根据 spring.application.name 配置Apollo的app.id
2. 从CMDB拉取configserver地址
3. 从configserver读取框架全局配置
4. 从CMDB拉取中间件地址，以及账号密码信息
5. 自动拉取应用配置

```shell
spring.application.name=your-app-name
```
默认它会选择 dev 环境的Apollo。如果要使用其它环境，请增加启动参数
```shell
-Dspring.active.profiles=test
```
对于符合规范的应用（Apollo环境名称和spring.active.profiles一致的应用），不用做任何修改。

### 配置中心和 CMDB
框架会拉取cmdb中的数据库密码等信息，如果不需要 cmdb，可以增加下面配置。
**但是要注意**，框架是从CMDB获取Apollo配置中心的地址的，如果禁用CMDB，你需要自己配置`apollo.meta=http://apollo`参数
```yaml
wsframework:
  cmdb:
    enable: false
```

## Web

### 接口相应格式
TODO
### 验签、加解密
TODO
## 数据库
并没有做多余的事情，引入 mybatisplus
## Redis
并没有做多余的事情，引入 redisson。使用时，按标准spring boot方式配置即可，你可以继续使用RedisTemplate，它和redisson兼容。

# cmdb 配置
```properties
# apollo
apollo.meta=${cmdb.apollo.url}
#jobcenter
xxl.job.admin.addresses=${cmdb.xxljob.url:}
#redis
spring.redis.host=${cmdb.redis.hostname:}
spring.redis.port=${cmdb.redis.port:}
spring.redis.password=${cmdb.redis.password:}
#mysql
spring.datasource.url=jdbc:mysql://${cmdb.mysql.hostname:}:${cmdb.mysql.port:}/${cmdb.mysql.database:}?characterEncoding=utf-8&allowMultiQueries=true&useSSL=false
spring.datasource.username=${cmdb.mysql.username:}
spring.datasource.password=${cmdb.mysql.password:}
# es
spring.elasticsearch.rest.uris=${cmdb.elasticsearch.url:}
spring.elasticsearch.rest.username=${cmdb.elasticsearch.username:}
spring.elasticsearch.rest.password=${cmdb.elasticsearch.password:}
# kafka
spring.kafka.bootstrapServers=${cmdb.kafka.url:}
# mongo
spring.data.mongodb.host=${cmdb.mongodb.hostname:}
spring.data.mongodb.port=${cmdb.mongodb.port:}
spring.data.mongodb.database=${cmdb.mongodb.database:}
spring.data.mongodb.username=${cmdb.mongodb.username:}
spring.data.mongodb.password=${cmdb.mongodb.password:}
```