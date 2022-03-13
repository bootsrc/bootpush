# bootpush

## 技术栈
1.Spring Boot 5
2.Netty4
3.序列化到字节的框架kryo 5.2.0

## Demo
Intelij IDEA导入项目后
启动bootpush-server的ServerApp
再启动bootpush-client的ClientApp
查看bootpush-server的client日志

测试从server推送消息到client
浏览器访问
## Demo1,推送消息到java-cient
http://localhost:9101/api/push?regId=reg-id-001&msg=This-is-pushed-msg

## Demo2,推送消息到android-cient
安装doc/bootpush.apk到自己的android手机上
Host填写自己电脑的ip就行（bootpush-server的ip, 确保电脑和手机连接的是同一个WIFI）
类似于下图
![alt 图1](/doc/android-client-1.jpeg)
然后在App上点击按钮"连接"
<br/><br/><br/>
在电脑浏览器上访问如下url
http://localhost:9101/api/push?regId=reg-id-android-001&msg=This-is-pushed-msg

查看Android手机上是否弹出推送的消息。
![alt 图2](/doc/android-client-2.jpeg)