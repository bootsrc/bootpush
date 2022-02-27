package com.github.bootsrc.bootpush.server.handler;

import com.github.bootsrc.bootpush.api.enums.MessageType;
import com.github.bootsrc.bootpush.api.model.StandardMessage;
import com.github.bootsrc.bootpush.server.boot.GsonSingleton;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 跟ReadTimeoutHandler配合起来检测客户端发过来的心跳消息
 */
public class HeartbeatServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatServerHandler.class);

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        LOGGER.info("HeartbeatServerHandler#userEventTriggered invoked.");
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (IdleStateEvent.READER_IDLE_STATE_EVENT.state() == idleStateEvent.state()) {
                // ctx.close();
                // 如果收不到客户端的心跳消息，则说明客户端已经失联了，断开跟客户端的channel连接
                ctx.channel().close();
                LOGGER.info("Missing connection from client, close the channel!");
            }
        } else {
            LOGGER.info("NOT IdleStateEvent");
        }
        super.userEventTriggered(ctx, evt);
    }

    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof StandardMessage) {
            StandardMessage message = (StandardMessage) msg;
            if (null != message.getHeader() && MessageType.HEARTBEAT_REQ.value() == message.getHeader().getType()) {
                LOGGER.info("<== READ heartbeat from client, heartbeat={}", GsonSingleton.getGson().toJson(message));
            }
        }
        ctx.fireChannelRead(msg);
    }
}
