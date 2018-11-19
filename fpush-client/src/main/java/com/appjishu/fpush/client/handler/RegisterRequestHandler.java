package com.appjishu.fpush.client.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.appjishu.fpush.core.constant.FMessageType;
import com.appjishu.fpush.core.constant.RegisterState;
import com.appjishu.fpush.core.model.MsgUser;
import com.appjishu.fpush.core.proto.FBody;
import com.appjishu.fpush.core.proto.FHeader;
import com.appjishu.fpush.core.proto.FMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RegisterRequestHandler extends ChannelInboundHandlerAdapter {
	private static final Logger log = LoggerFactory.getLogger(RegisterRequestHandler.class);
			
	private MsgUser msgUser;
	
	public RegisterRequestHandler(MsgUser msgUser) {
		this.msgUser = msgUser;
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
        			log.info("---client:register_SUCCESS---");
        			ctx.fireChannelRead(msg);
        			return;
        		} else {
        			log.info("---client:register_FAILED---DisconnectIt!!!--");
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
    	headerBuilder.setAlias(msgUser.getAlias());
    	headerBuilder.setAccount(msgUser.getAccount());
    	headerBuilder.setType(FMessageType.REGISTER_REQ.value());
    	headerBuilder.setSessionId(1234);
    	headerBuilder.setPriority(9);
    	builder.setHeader(headerBuilder.build());
    	
//    	FBody.Builder fBodyBuilder = FBody.newBuilder();
//    	fBodyBuilder.setTitle("TestTitle");
//    	fBodyBuilder.setDescription("This is a Description");
//    	fBodyBuilder.setExtra("{\"k\":\"v\"}");
//    	builder.setBody(fBodyBuilder.build());
    	FMessage fMessage = builder.build();
    	return fMessage;
	}
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
