package com.appjishu.fpush.server.handler;

import java.util.List;

import org.springframework.util.StringUtils;

import com.appjishu.fpush.core.constant.FMessageType;
import com.appjishu.fpush.core.model.MsgData;
import com.appjishu.fpush.core.proto.FBody;
import com.appjishu.fpush.core.proto.FHeader;
import com.appjishu.fpush.core.proto.FMessage;
import com.appjishu.fpush.server.singleton.ToSendMap;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PushHandler extends ChannelInboundHandlerAdapter {
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FMessage) {
        	FMessage message = (FMessage) msg;
            // 返回心跳应答消息
            if (message.getHeader() != null && 
            		message.getHeader().getType() == FMessageType.HEARTBEAT_REQ.value()) {
            	
            	FHeader header = message.getHeader();
            	if (!StringUtils.isEmpty(header.getAlias()) || !StringUtils.isEmpty(header.getAccount()) ) {
            		FMessage pushedMessage = buildPushMessage(header.getAlias(), header.getAccount());
                	if (pushedMessage == null) {
                		ctx.fireChannelRead(msg);
                		return;
                	} else {
                		System.out.println("---Send pushed message to client : ---> " + pushedMessage);
                		ctx.writeAndFlush(pushedMessage);
                	}
                	
            	} else {
            		ctx.fireChannelRead(msg);
            	}
            }  else {
            	ctx.fireChannelRead(msg);
            }
		} else {
			ctx.fireChannelRead(msg);
		}
        
    }
	
	private FMessage buildPushMessage(String alias, String account) {
		if (StringUtils.isEmpty(alias)) {
			return null;
		}
		
		if (ToSendMap.aliasMap.containsKey(alias)) {
			List<MsgData> msgList = ToSendMap.aliasMap.get(alias);	
			if (msgList.size()>0) {
				FMessage.Builder builder = FMessage.newBuilder();
		    	FHeader.Builder headerBuilder = FHeader.newBuilder();
		    	headerBuilder.setAlias(alias);
		    	headerBuilder.setAccount(account);
		    	headerBuilder.setType(FMessageType.PUSH.value());
		    	headerBuilder.setSessionId(1234);
		    	headerBuilder.setPriority(9);
		    	builder.setHeader(headerBuilder.build());
		    	
		    	FBody.Builder fBodyBuilder = FBody.newBuilder();
		    	if (msgList.get(0) != null) {
		    		MsgData data = msgList.get(0);
		    		fBodyBuilder.setTitle(data.getTitle());
		    		fBodyBuilder.setDescription(data.getDescription());
		    		builder.setBody(fBodyBuilder);
		    	}
		    	FMessage fMessage = builder.build();
		    	// TODO msgList.remove(0);
				msgList.remove(0);
				return fMessage;
			} 
		}
		return null;
	}
}
