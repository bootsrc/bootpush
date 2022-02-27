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
http://localhost:9101/api/push?regId=reg-id-001&msg=This-is-pushed-msg
