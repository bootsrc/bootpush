package com.appjishu.fpush.server.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.appjishu.fpush.core.constant.FMessageType;
import com.appjishu.fpush.core.constant.RegisterState;
import com.appjishu.fpush.core.proto.FBody;
import com.appjishu.fpush.core.proto.FHeader;
import com.appjishu.fpush.core.proto.FMessage;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class RegisterResponseHandler extends ChannelInboundHandlerAdapter {
	private static final Logger log = LoggerFactory.getLogger(RegisterResponseHandler.class);
	
	@Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FMessage) {
			FMessage message = (FMessage) msg;
			FHeader header = message.getHeader();
			if (header != null
					&& header.getType() == FMessageType.REGISTER_REQ.value()
					&& ! StringUtils.isEmpty(header.getAlias())) {
				String registeredAlias = header.getAlias();
				log.info("---registeredAlias={}", registeredAlias);
				FMessage targetMessage = buildRegisterResponse(registeredAlias, header.getAccount(), 
						RegisterState.SUCCESS, "");
				ctx.writeAndFlush(targetMessage);
			} else {
				ctx.fireChannelRead(msg);
			}
		} else {
			ctx.fireChannelRead(msg);
		}
    }
	
	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.fireExceptionCaught(cause);
    }
	
	private FMessage buildRegisterResponse(String alias, String account, String resultCode, 
			String resultText) {
		FMessage.Builder builder = FMessage.newBuilder();
    	FHeader.Builder headerBuilder = FHeader.newBuilder();
    	headerBuilder.setAlias(alias);
    	headerBuilder.setAccount(account);
    	headerBuilder.setType(FMessageType.REGISTER_RESP.value());
    	headerBuilder.setSessionId(1234);
    	headerBuilder.setPriority(9);
    	headerBuilder.setResultCode(resultCode);
    	headerBuilder.setResultText(resultText);
    	builder.setHeader(headerBuilder.build());
    	
    	FBody.Builder fBodyBuilder = FBody.newBuilder();
    	fBodyBuilder.setTitle("TestTitle");
    	fBodyBuilder.setDescription("This is a Description");
    	fBodyBuilder.setExtra("{\"k\":\"v\"}");
    	builder.setBody(fBodyBuilder.build());
    	FMessage fMessage = builder.build();
    	return fMessage;
	}
}
