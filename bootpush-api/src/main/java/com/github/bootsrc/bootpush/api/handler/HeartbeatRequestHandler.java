package com.github.bootsrc.bootpush.api.handler;

import com.github.bootsrc.bootpush.api.enums.MessageType;
import com.github.bootsrc.bootpush.api.model.StandardHeader;
import com.github.bootsrc.bootpush.api.model.StandardMessage;
import com.github.bootsrc.bootpush.api.util.IdUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class HeartbeatRequestHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatRequestHandler.class);

    private volatile ScheduledFuture<?> scheduledFuture;

    public HeartbeatRequestHandler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof StandardMessage) {
            StandardMessage message = (StandardMessage) msg;
            // 握手成功， 主动发送心跳信息
            if (message.getHeader() != null && message.getHeader().getType() == MessageType.REGISTER_RESP.value()) {
                scheduledFuture = ctx.executor().scheduleAtFixedRate(new HeartbeatRequestHandler.HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
            } else if (message.getHeader() != null && message.getHeader().getType() == MessageType.HEARTBEAT_RESP.value()) {
                LOGGER.info("Client receive heartbeat response message from server : ---> " + message);
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
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
        }
        ctx.fireExceptionCaught(cause);
    }

    private static class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            StandardMessage heartBeat = buildHeartbeat();
            ctx.writeAndFlush(heartBeat);
        }

        private StandardMessage buildHeartbeat() {
            StandardMessage message = new StandardMessage();
            StandardHeader header = new StandardHeader();
            header.setType(MessageType.HEARTBEAT_REQ.value());
            header.setSessionId(IdUtil.newUUID());
            header.setPriority(1);
            header.setAppId(null);
            header.setClientToken(null);
            message.setHeader(header);
            return message;
        }
    }
}
