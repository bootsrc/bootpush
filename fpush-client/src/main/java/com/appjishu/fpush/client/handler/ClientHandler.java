package com.appjishu.fpush.client.handler;

import com.appjishu.fpush.core.app.FpushCoreApp;
import com.appjishu.fpush.core.constant.FMessageType;
import com.appjishu.fpush.core.proto.FHeader;
import com.appjishu.fpush.core.proto.FMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ClientHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelActive(ChannelHandlerContext ctx) {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		FMessage.Builder builder = FMessage.newBuilder();
    	FHeader.Builder headerBuilder = FHeader.newBuilder();
    	headerBuilder.setCrcCode(118);
    	headerBuilder.setType(FMessageType.LOGIN_REQ.value());
    	headerBuilder.setSessionId(1234);
    	headerBuilder.setLength(6);
    	headerBuilder.setPriority(9);
    	builder.setHeader(headerBuilder.build());
    	builder.setBody("这是对方发过来的一段中文字符111111");
    	FMessage fMessage = builder.build();
    	
		System.out.println("---Client_is-SendingAFMessageToServer--->");
		ctx.writeAndFlush(fMessage);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("---readDataFromServer--->");
		ctx.fireChannelRead(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
