## **Java开发推送服务fpush**

## 1.概述
基于SDK的消息推送服务。旨在做一个类似于极光推送、各推、小米推送之类的Java程序开源实现。开发平台是Java。<br/>
用于Java服务端推送实时和离线消息到Android/iOS客户端。 适用于推送消息和App端的即时通讯im场景。<br/>
<br/><br/>
fpush监控后台<br/>
👉 [http://fpush-admin.appjishu.com](http://fpush-admin.appjishu.com)
<br/>

## 2.技术选型
要满足大量的连接数、同时支持双全工通信，并且性能也得有保障。我对Java语言比较擅长，于是选择了Java来实现。在 Java 技术栈中进行选型<br/>
首先自然是排除掉了传统IO,那就只有选NIO了，在这个层面其实选择也不多，考虑到社区、资料维护等方面最终选择了Netty。 序列化框架，选择了大名鼎鼎的<br/>
protobuf。具体的版本号如下
```
1. JDK1.8 
2. Netty-4.1.31.Final
3. protobuf-java 3.6.1
```
<br/>

## 3.系统架构

### 3.1系统部署架构图如下：
<br/><br/>
![](doc/arch-1.png)
<br/>
<br/>
<br/>

### 3.2移动客户端鉴定权限原理
<br/><br/>
![](doc/client-passport.png)
<br/>
<br/>
<br/>

### 3.3 server端推送消息到client端的原理 <br/>
tcp通信图如下:
<br/><br/>
![](doc/tcp.png)
<br/>

## 4.代码实现
### 4.1协议定制
HTTP协议和XMPP协议的字节数太多，带宽消耗大，不适合推送服务。我们的推送服务需要自己定制的二进制协议。
下面是编写的protobuf配置文件
[fmessage.proto](https://github.com/liushaoming/fpush/blob/master/fpush-core/src/main/java/fmessage.proto)

进入本地的目录,例如<code>D:\git\flylib\fpush\fpush-core\src\main\java</code>执行
```shell
protoc --java_out=. fmessage.proto
```
就可以生成FMessage之类的protobuf实体类，例如
<code>com.appjishu.fpush.core.proto.FMessage</code>

### 4.2应用服务器的注册

每个应用服务器需要在fpush-server上注册一个账号, 得到相应的三个参数APP_ID, APP_KEY, APP_SECRET_KEY <br/>
应用服务器应该请求fpush-server的这个http接口<code>http://serverHost:10200/app/registerAccount</code>
<br/>

### 4.3长连接注册鉴权


Android和iOS客户端通过访问http接口<code>http://serverHost:10200/app/keyToken</code> 获取到clientToken
参数是<code>long appId, String appKey</code> 。 这里就是上面提到的APP_ID, APP_KEY
Android端代码如下:
```java
public synchronized NettyClient connect() throws Exception {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();

        FormBody formBody = new FormBody.Builder().add("appId", appId + "")
                .add("appKey", appKey).build();

        String urlStr = "http://" + NetConstant.PUSH_HOST + ":" + NetConstant.API_PORT + NetConstant.KEY_TOKEN_URL;
        Request request = new Request.Builder().url(urlStr)
                .post(formBody)
                .build();

        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(MY_TAG, "http failed.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String responseStr = response.body().string();
//                    ResponseData responseData = JSON.parseObject(responseStr, ResponseData.class);
                    ResponseData responseData = gson.fromJson(responseStr, ResponseData.class);

                    if (responseData.getCode() == 0) {
                        String clientToken = (String) responseData.getData();
                        try {
//                            MainActivity mainActivity = MainActivity.getInstance();
//                            if (mainActivity != null) {
//                                Toast.makeText(MainActivity.getInstance(), "连接成功！", Toast.LENGTH_SHORT).show();
//                            }
                            doStart(appId, clientToken);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });
        return this;
    }
```
<br/>
Android使用自己的APP_ID, clientToken, MsgUser.alias（一般用应用系统里的userId作为alias标识一个移动设备）注册到fpush-server代码RegisterRequestHandler.java

```java
package com.appjishu.fpush_demo.handler;

import android.util.Log;

import com.appjishu.fpush.core.constant.FMessageType;
import com.appjishu.fpush.core.constant.RegisterState;
import com.appjishu.fpush.core.model.MsgUser;
import com.appjishu.fpush.core.proto.FBody;
import com.appjishu.fpush.core.proto.FHeader;
import com.appjishu.fpush.core.proto.FMessage;
import com.appjishu.fpush_demo.singleton.CurrentUser;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RegisterRequestHandler extends ChannelInboundHandlerAdapter {
	private static final String MY_TAG = "RegisterHandler";

	public RegisterRequestHandler() {

	}
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(buildRegisterRequest());
	}
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FMessage) {
        	FMessage receivedMsg = (FMessage) msg;
        	FHeader receivedHeader = receivedMsg.getHeader();
        	if (receivedHeader!=null && receivedHeader.getType() == FMessageType.REGISTER_RESP.value()) {
        		if (RegisterState.SUCCESS.equals(receivedHeader.getResultCode())) {
        			Log.d(MY_TAG, "---client:register_SUCCESS---");
        			ctx.fireChannelRead(msg);
        			return;
        		} else {
					Log.d(MY_TAG, "---client:register_FAILED---DisconnectIt!!!--");
        			ctx.close();
        		}
        	} else {
        		ctx.fireChannelRead(msg);
        	}
        } else {
        	ctx.fireChannelRead(msg);
        }
	}
	
	private FMessage buildRegisterRequest() {
		FMessage.Builder builder = FMessage.newBuilder();
    	FHeader.Builder headerBuilder = FHeader.newBuilder();
    	headerBuilder.setAlias(CurrentUser.getInfo().getAlias());
    	headerBuilder.setAccount(CurrentUser.getInfo().getAccount());
    	headerBuilder.setType(FMessageType.REGISTER_REQ.value());
    	headerBuilder.setSessionId(1234);
    	headerBuilder.setPriority(9);
		headerBuilder.setAppId(CurrentUser.getInfo().getAppId());
		headerBuilder.setClientToken(CurrentUser.getInfo().getAppToken());
    	builder.setHeader(headerBuilder.build());
    	
    	FMessage fMessage = builder.build();
    	return fMessage;
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}

```
fpush-server的RegisterResponseHandler会进行后台鉴权处理, 原理图
<br/><br/>
![](doc/client-passport.png)
<br/>
<br/>
<br/>

### 4.4应用服务器鉴权
应用服务端的http连接的鉴权 <br/>
应用服务端（即app server）发送appId + appSecretKey到http接口<code>http://serverHost:10200/app/secretToken</code>
，经后台鉴定权限通过后，获取到appToken
应用服务端每次调用fpush-server的http api都需要带上appId+appToken


### 4.5心跳机制与TCP超时处理
tcp通信图如下:
<br/><br/>
![](doc/tcp.png)
<br/>
客户端使用的是HeartBeatRequestHandler并定期发送Heartbeat ping,fpush-server使用HeartBeatResponseHandler<br/>
发送hearbeat pong进行回应 <br/>

### 4.6保存fpush-server端的通道映射关系
RegisterResponseHandler中channelRead()里，如果客户端注册成功则把channel对象保存到NettyChannelMap这个Map里去
```java
String clientId = header.getAlias();
			ctx.channel().attr(ChannelAttrKey.KEY_CLIENT_ID).set(clientId);
			NettyChannelMap.put(clientId, ctx.channel());
```
保存了通道的映射关系后，就可以在收到应用服务器发给fpush-server的消息后，通过Netty框架来实时把消息推送到Android客户端<br/>
<code>channel.writeAndFlush(message) </code>
<br/>

### 4.7推送过程

应用服务器通过访问 http接口 http://localhost:10200/api/** <br/>
例如
<a href="http://localhost:10200/app/push?receiverAlias=lsm001&title=系统提醒&desc=消息内容001&data=abc">
http://localhost:10200/app/push?receiverAlias=lsm001&title=系统提醒&desc=消息内容001&data=abc
 </a>
<br/>
就是发送一条测试消息，描述为<code>消息内容001</code>,标题为<code>系统提醒</code>，发送给Android/iOS客户端,<br/>
接收者的alias为<code>lsm001</code> <br/>


后台ApiController把消息的内容写入缓存ToSendMap.aliasMap中去，如
<code>ToSendMap.aliasMap.put(alias, list);</code> <br/>

定时任务<code>com.appjishu.fpush.server.boot.SendTask#scan</code>每隔一定的时间间隔，会扫描ToSendMap.aliasMap<br/>
里的待发送的消息.  遍历后，会通过<code>NettyChannelMap.get(alias)</code>获取到Channel,然后 channel.writeAndFlush(message) <br/>
发送出去

```java
@Scheduled(fixedRate = 5000)
    public void scan() {
        for (Map.Entry<String, List<MsgData>> entry: ToSendMap.aliasMap.entrySet()) {
            String alias = entry.getKey();
            List<MsgData> msgList = entry.getValue();
            if (StringUtils.isNotEmpty(alias) && msgList != null && msgList.size() > 0) {
                pushService.doPush(msgList, alias);
            }
        }

    }
```


```java
public void doPush(List<MsgData> msgList, String alias) {
        FMessage fMessage = buildPushMessage(msgList, alias);
        if (fMessage != null) {
            log.info("---TringToDoPush()--->");
            Channel channel = NettyChannelMap.get(alias);
            if (channel == null) {
                log.info("------channelIsNull---");
            } else if (!channel.isWritable()) {
                log.info("------channelIsNotWritable---");
            } else {
                ChannelFuture future = channel.writeAndFlush(fMessage);
                log.info("------msgWriten!!!---");
                future.addListener(new ChannelFutureListener() {
                    public void operationComplete(final ChannelFuture future)
                            throws Exception {
                        if (msgList.size() > 0) {
                            msgList.remove(0);
                            log.info("------removeAreadySentMsg!!!---");
                        }
                    }
                });
            }
        }
    }
```
<br/>
<br/>

