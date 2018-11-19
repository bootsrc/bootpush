package com.appjishu.fpush.client.handler;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.appjishu.fpush.core.constant.FMessageType;
import com.appjishu.fpush.core.model.MsgData;
import com.appjishu.fpush.core.proto.FBody;
import com.appjishu.fpush.core.proto.FHeader;
import com.appjishu.fpush.core.proto.FMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PushConfirmHandler extends ChannelInboundHandlerAdapter {
	private static final Logger log = LoggerFactory.getLogger(PushConfirmHandler.class);
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FMessage) {
        	FMessage message = (FMessage) msg;
            // 返回心跳应答消息
            if (message.getHeader() != null && 
            		message.getHeader().getType() == FMessageType.PUSH.value()) {
            	FHeader header = message.getHeader();
            	if (header.getType() == FMessageType.PUSH.value()) {
            		FBody receiveBody = message.getBody();
            		if (receiveBody != null) {
            			// 这是推送到客户端的消息的title和描述description
            			String receivedTitle = receiveBody.getTitle();
            			String receivedDesc = receiveBody.getDescription();
            			log.info("--->>>这是推送到客户端的消息:title={}", receivedTitle);
            			log.info("--->>>这是推送到客户端的消息:description={}", receivedDesc);
            			ctx.fireChannelRead(msg);
            		}
            	}
            }  
		} 
		ctx.fireChannelRead(msg);
    }
	
	private FMessage buildPushConfirmMessage(String alias, String account) {
//		if (StringUtils.isEmpty(alias)) {
//			return null;
//		}
//		
//		
//	
//		FMessage.Builder builder = FMessage.newBuilder();
//    	FHeader.Builder headerBuilder = FHeader.newBuilder();
//    	headerBuilder.setAlias(alias);
//    	headerBuilder.setAccount(account);
//    	headerBuilder.setType(FMessageType.PUSH.value());
//    	headerBuilder.setSessionId(1234);
//    	headerBuilder.setPriority(9);
//    	builder.setHeader(headerBuilder.build());
//    	
//    	FBody.Builder fBodyBuilder = FBody.newBuilder();
//    	if (msgList.get(0) != null) {
//    		MsgData data = msgList.get(0);
//    		fBodyBuilder.setTitle(data.getTitle());
//    		fBodyBuilder.setDescription(data.getDescription());
//    		builder.setBody(fBodyBuilder);
//    	}
//    	FMessage fMessage = builder.build();
//    	// TODO msgList.remove(0);
//		msgList.remove(0);
//		return fMessage;


		return null;
	}
}
