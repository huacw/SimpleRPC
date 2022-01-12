# SimpleRPC
**简易RPC项目**
```
基于netty4实现的rpc框架，数据格式为xml格式。使用xstream实现xml与java对象的转换，通过Jboss Marshaller序列化实现数据传输。<br>
使用xml转换对象屏蔽了jboss-marshalling-serial序列化时要求实现Serializable接口的问题。<br>
注册中心目前有zookeeper、nacos两种实现，服务默认3s与注册中心进行心跳连接，该配置可以通过【rpc.server.config.heartPeriod】进行修改，单位毫秒。
```