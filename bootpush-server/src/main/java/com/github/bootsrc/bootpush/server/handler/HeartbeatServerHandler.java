package com.github.bootsrc.bootpush.server.handler;

import com.github.bootsrc.bootpush.api.enums.MessageType;
import com.github.bootsrc.bootpush.api.model.StandardBody;
import com.github.bootsrc.bootpush.api.model.StandardHeader;
import com.github.bootsrc.bootpush.api.model.StandardMessage;
import com.github.bootsrc.bootpush.server.boot.GsonSingleton;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 跟ReadTimeoutHandler配合起来检测客户端发过来的心跳消息
 */
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatServerHandler.class);

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof StandardMessage) {
            StandardMessage message = (StandardMessage) msg;
            if (null != message.getHeader() && MessageType.HEARTBEAT_REQUEST.getValue() == message.getHeader().getType()) {
                LOGGER.info("<== READ heartbeat from client, heartbeat={}", GsonSingleton.getGson().toJson(message));
                StandardMessage outMsg = buildHeartbeat(message);
                ctx.channel().writeAndFlush(outMsg);
                LOGGER.info("==> WRITE heartbeat response to client, heartbeat={}", GsonSingleton.getGson().toJson(outMsg));
            }
        }
        ctx.fireChannelRead(msg);
    }

    private StandardMessage buildHeartbeat(StandardMessage reqMsg) {
        StandardMessage msg = new StandardMessage();
        StandardHeader reqHeader = reqMsg.getHeader();
        StandardHeader header = new StandardHeader();
        header.setType(MessageType.HEARTBEAT_RESPONSE.getValue());
        header.setRegId(reqHeader.getRegId());
        StandardBody body = new StandardBody();
        msg.setHeader(header);
        msg.setBody(body);
        return msg;
    }
}
