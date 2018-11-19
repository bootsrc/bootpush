package com.appjishu.fpush.server.handler;

import org.springframework.util.StringUtils;

import com.appjishu.fpush.core.constant.FMessageType;
import com.appjishu.fpush.core.proto.FHeader;
import com.appjishu.fpush.core.proto.FMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatResponseHandler extends ChannelInboundHandlerAdapter {
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FMessage) {
        	FMessage message = (FMessage) msg;
            // 返回心跳应答消息
            if (message.getHeader() != null && message.getHeader().getType() == FMessageType.HEARTBEAT_REQ.value()) {
            	System.out.println("---Receive client heartbeat message : ---> " + message);
            	FHeader header = message.getHeader();
            	FMessage heartBeat = buildHeartBeat(header.getAlias(), header.getAccount());
            	System.out.println("---Send heartbeat response  message to client : ---> " + heartBeat);
            	ctx.writeAndFlush(heartBeat);	
            	ctx.fireChannelRead(msg);
            }  else {
            	ctx.fireChannelRead(msg);
            }
		} else {
			ctx.fireChannelRead(msg);
		}
        
    }
	
	private FMessage buildHeartBeat(String alias, String account) {
		FMessage.Builder builder = FMessage.newBuilder();
    	FHeader.Builder headerBuilder = FHeader.newBuilder();
    	headerBuilder.setAlias(alias);
    	headerBuilder.setAccount(account);
    	headerBuilder.setType(FMessageType.HEARTBEAT_RESP.value());
    	headerBuilder.setSessionId(1234);
    	headerBuilder.setPriority(9);
    	builder.setHeader(headerBuilder.build());
    	
    	FMessage fMessage = builder.build();
    	return fMessage;
	}
}
