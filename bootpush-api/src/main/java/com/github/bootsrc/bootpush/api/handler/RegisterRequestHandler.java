package com.github.bootsrc.bootpush.api.handler;

import com.github.bootsrc.bootpush.api.enums.MessageType;
import com.github.bootsrc.bootpush.api.enums.RegisterState;
import com.github.bootsrc.bootpush.api.model.StandardHeader;
import com.github.bootsrc.bootpush.api.model.StandardMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegisterRequestHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterRequestHandler.class);

    public RegisterRequestHandler() {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ctx.channel().writeAndFlush(buildRegisterRequest());
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof StandardMessage) {
            StandardMessage receivedMsg = (StandardMessage) msg;
            StandardHeader receivedHeader = receivedMsg.getHeader();
            if (receivedHeader != null && receivedHeader.getType() == MessageType.REGISTER_RESP.value()) {
                if (RegisterState.SUCCESS.equals(receivedHeader.getResultCode())) {
                    LOGGER.info("---client:register_SUCCESS---");
                    String regId =receivedHeader.getRegistrationId();
                    LOGGER.info("get receivedMsg={}, regId={}", null,regId);
                    ctx.fireChannelRead(msg);
                    return;
                } else {
                    LOGGER.info("---client:register_FAILED---DisconnectIt!!!--");
                    ctx.close();
                }
            } else {
                ctx.fireChannelRead(msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    private StandardMessage buildRegisterRequest() {
        StandardMessage message = new StandardMessage();
        StandardHeader header = new StandardHeader();
        header.setType(MessageType.REGISTER_REQ.value());
        header.setSessionId(null);
        header.setPriority(9);
        header.setAppId(null);
        header.setClientToken(null);
        message.setHeader(header);
        return message;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.fireExceptionCaught(cause);
    }
}
