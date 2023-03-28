# 介绍

| 模块名称 | 用途 | 介绍 |
| --- | --- | --- |
| wsframework-web-dependencies | 框架 | 仅pom，定义jar包依赖的版本 |
| wsframework-web-api | 服务提供方、消费方 |给api模块使用，用于提供feign api等用到的公用类。内容非常少，依赖最轻。 |
| wsframework-web-core | 服务提供方 | 包含web项目常用功能。重度依赖spring web 。一般服务端|
| wsframework-web-openfeign | 服务提供方、消费方 | 包含openfeing。重度依赖spring cloud openfeign |
| wsframework-web-spring-boot-starter | 服务提供方 | 聚合其它模块的starter，**一般项目直接使用这个start即可**。 |