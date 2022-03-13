package com.github.bootsrc.bootpush.client.handler;

import com.github.bootsrc.bootpush.api.enums.MessageType;
import com.github.bootsrc.bootpush.api.enums.RegisterState;
import com.github.bootsrc.bootpush.api.model.StandardHeader;
import com.github.bootsrc.bootpush.api.model.StandardMessage;
import com.github.bootsrc.bootpush.client.config.AppContextUtil;
import com.github.bootsrc.bootpush.client.config.PushClientConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 此ChannelInboundHandler工作在tcp client
 */
public class RegisterClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(RegisterClientHandler.class);

    public RegisterClientHandler() {

    }


    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        LOGGER.info("RegisterClientHandler#channelActive");
        StandardMessage message = buildRegisterRequest();
        ctx.channel().writeAndFlush(message);
        LOGGER.info("==> WRITE msg to server, msg={}", message);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof StandardMessage) {
            StandardMessage message = (StandardMessage) msg;
            StandardHeader receivedHeader = message.getHeader();
            if (receivedHeader != null && receivedHeader.getType() == MessageType.REGISTER_RESPONSE.getValue()) {
                if (RegisterState.SUCCESS.getCode().equals(receivedHeader.getResultCode())) {
                    LOGGER.info("<== READ REGISTER_RESP message. msg={}", message);
                    ctx.fireChannelRead(msg);
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
        // TODO regId是客户端登陆后从服务器端获取到的
        String regId = AppContextUtil.getAppContext().getBean(PushClientConfig.class).getRegId();
        StandardMessage message = new StandardMessage();
        StandardHeader header = new StandardHeader();
        // 用regId去push server注册，以建立长连接，方便后面进行tcp通信
        header.setRegId(regId);
        header.setType(MessageType.REGISTER_REQUEST.getValue());
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
