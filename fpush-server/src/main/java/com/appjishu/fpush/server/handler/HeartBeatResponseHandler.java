package com.appjishu.fpush.server.handler;

import com.appjishu.fpush.server.channel.NettyChannelMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appjishu.fpush.core.constant.FMessageType;
import com.appjishu.fpush.core.proto.FHeader;
import com.appjishu.fpush.core.proto.FMessage;
import com.appjishu.fpush.server.util.PassportUtil;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatResponseHandler extends ChannelInboundHandlerAdapter {
	private static final Logger log = LoggerFactory.getLogger(HeartBeatResponseHandler.class);
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FMessage) {
        	FMessage message = (FMessage) msg;
            // 返回心跳应答消息
            if (message.getHeader() != null && message.getHeader().getType() == FMessageType.HEARTBEAT_REQ.value()) {
            	boolean passed = false;
//            	log.info("---Receive client heartbeat message : ---> " + message);
            	FHeader header = message.getHeader();
            	if (header != null && header.getAppId() > 0) {
            		passed = PassportUtil.checkByClientToken(header.getAppId(), header.getClientToken());
            	} 
            	if (passed) {
        			FMessage heartBeat = buildHeartBeat(header);

//        			log.info("---Send heartbeat response  message to client : ---> " + heartBeat);
                	ctx.writeAndFlush(heartBeat);	
                	ctx.fireChannelRead(msg);
                	return ;
        		} else {
        			log.info(">>>PASSPORT_FAILED!!! appId={}, clientToken={}>>>", header.getAppId(), header.getClientToken());
        			ctx.close();
        			return ;
        		}
            } 
            ctx.fireChannelRead(msg);
		} else {
			ctx.fireChannelRead(msg);
		} 
    }
	
	private FMessage buildHeartBeat(FHeader header) {
		FMessage.Builder builder = FMessage.newBuilder();
		FHeader.Builder headerBuilder = header.toBuilder();
		headerBuilder.setType(FMessageType.HEARTBEAT_RESP.value());
    	builder.setHeader(headerBuilder.build());	
    	FMessage fMessage = builder.build();
    	return fMessage;
	}
}
