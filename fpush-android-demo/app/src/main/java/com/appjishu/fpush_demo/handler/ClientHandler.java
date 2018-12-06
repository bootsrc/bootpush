package com.appjishu.fpush_demo.handler;

import com.appjishu.fpush.core.app.FpushCoreApp;
import com.appjishu.fpush.core.constant.FMessageType;
import com.appjishu.fpush.core.proto.FBody;
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
    	headerBuilder.setAlias("118");
    	headerBuilder.setType(FMessageType.LOGIN_REQ.value());
    	headerBuilder.setSessionId(1234);
    	headerBuilder.setPriority(9);
    	builder.setHeader(headerBuilder.build());
    	
    	FBody.Builder fBodyBuilder = FBody.newBuilder();
    	fBodyBuilder.setTitle("TestTitle");
    	fBodyBuilder.setDescription("This is a Description");
    	fBodyBuilder.setExtra("{\"k\":\"v\"}");
    	builder.setBody(fBodyBuilder.build());
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
