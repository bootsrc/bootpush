package com.appjishu.fpush.server.handler;

import com.appjishu.fpush.server.channel.NettyChannelMap;
import com.appjishu.fpush.server.constant.ChannelAttrKey;
import com.appjishu.fpush.server.util.PassportUtil;
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
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		NettyChannelMap.remove(ctx.channel());
		ctx.fireChannelInactive();
	}
	
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
				FMessage targetMessage = buildRegisterResponse(header, ctx);
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
	
	private FMessage buildRegisterResponse(FHeader header, ChannelHandlerContext ctx) {
		FMessage.Builder builder = FMessage.newBuilder();

    	FHeader.Builder headerBuilder = header.toBuilder();
    	headerBuilder.setType(FMessageType.REGISTER_RESP.value());
		boolean passed = PassportUtil.checkByClientToken(header.getAppId(), header.getClientToken());

    	if (passed){
			String clientId = header.getAlias();
			ctx.channel().attr(ChannelAttrKey.KEY_CLIENT_ID).set(clientId);
			NettyChannelMap.put(clientId, ctx.channel());
			headerBuilder.setResultCode(RegisterState.SUCCESS);
			headerBuilder.setResultText("成功");
			log.info("---Server:deviceRegisterSuccess.");
		} else {
			headerBuilder.setResultCode("001");
			headerBuilder.setResultText("客户端设备注册失败");
			log.info("---Server:deviceRegisterFailed.");
		}

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
