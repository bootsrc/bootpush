# fpush

fpush是即时消息推送服务程序. <br/>
旨在做一个类似于极光推送，小米推送之类的Java程序开源实现。基于Netty + protobuf
<br/>

## 技术栈
1. JDK1.8 <br/>
2. Netty-4.1.31.Final <br/>
3. protobuf-java 3.6.1 <br/>

## 代码简介
fpush-core 核心类库，protobuf原型类 <br/>
fpush-server server端， 接受来自自己的应用服务器的http推送请求，并把请求转换成netty的socket发送给fpush-client
实现消息推送，即时通讯技术。<br/>
fpush-client 客户端，模拟App或者网页，或者桌面应用的客户端 <br/>

## 运行
eclipse/IDEA里 <br/>
Step1 右键run as--java application-- FpushServer.java <br/>
Step2 右键run as--java application-- FpushClient.java <br/>

Step3 后台发送消息给fpush-client (用来模拟android，ios或者网页，或者java应用的消息客户端)
浏览器访问    <a href="http://localhost:10200">http://localhost:10200</a> <br/>
显示Welcome to fpush application!，  说明server运行起来了<br/>
然后浏览器请求
 <a href="http://localhost:10200/api/pushTest">http://localhost:10200/api/pushTest</a>
 <br/>
 如果浏览器返回OK
 并且fpush-client打印出下面的信息，说明推送消息成功
<pre><code>
2018-11-19 14:28:44.792  INFO 27780 --- [ntLoopGroup-2-1] c.a.f.client.handler.PushConfirmHandler  : --->>>这是推送到客户端的消息:title=fpush-Demo
2018-11-19 14:29:17.067  INFO 27780 --- [ntLoopGroup-2-1] c.a.f.client.handler.PushConfirmHandler  : --->>>这是推送到客户端的消息:description=这是一条推送给lsm001的消息!
</code></pre>

<br/>

server效果图<br/>
![](doc/server.png)
<br/>
client在eclipse上调试的效果图-eclipse console可以显示中文字符<br/>
![](doc/client.png)
<br/>

## 测试
注册一个应用账号，手机号是15600000000
<br/>
http://localhost:10200/app/registerAccount?mobilePhone=15600000000

http://localhost:10200/app/secretToken?appId=517723931931574272&appSecretKey=cb2eb85b362941f1b3e1

<br/>
http://localhost:10200/app/keyToken?appId=517723931931574272&appKey=9f5d74bb0f68
<br/>

## Done List
1. 客户端长连接的鉴权 <br/>
客户端(即fpush-client)发送appId + appKey，经后台鉴定权限通过后，获取到clientToken <br/>
fpush-client与fpush-server通信的时候, RegisterRequestHandler和HeartBeatRequestHandler里面需要带上<br/>
appId+clientToken
建立长连接后，最好所有的RequestHandler需要带上appId+clientToken <br/>

2. 应用服务端的http连接的鉴权 <br/>
应用服务端（即app server）发送appId + appSecretKey，经后台鉴定权限通过后，获取到appToken
应用服务端每次调用fpush-server的api都需要带上appId+appToken
<br/>

## TODO list
1. 需要在FHeader里增加msgId
<br/>
2. 增加IdleStateHandler来对heartbeat进行监控，设定的时间间隔内没有收到心跳，就断开连接
Netty的IdleStateHandler会根据用户的使用场景，启动三类定时任务，分别是：ReaderIdleTimeoutTask、WriterIdleTimeoutTask和AllIdleTimeoutTask，它们都会被加入到NioEventLoop的Task队列中被调度和执行。
