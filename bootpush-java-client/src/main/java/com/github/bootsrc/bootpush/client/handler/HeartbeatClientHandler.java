package com.github.bootsrc.bootpush.client.handler;

import com.github.bootsrc.bootpush.api.enums.MessageType;
import com.github.bootsrc.bootpush.api.enums.RegisterState;
import com.github.bootsrc.bootpush.api.model.StandardHeader;
import com.github.bootsrc.bootpush.api.model.StandardMessage;
import com.github.bootsrc.bootpush.api.util.IdUtil;
import com.github.bootsrc.bootpush.client.config.AppContextUtil;
import com.github.bootsrc.bootpush.client.config.PushClientConfig;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * 此ChannelInboundHandler工作在tcp client
 */
public class HeartbeatClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(HeartbeatClientHandler.class);

    private volatile ScheduledFuture<?> scheduledFuture;

    public HeartbeatClientHandler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof StandardMessage) {
            StandardMessage message = (StandardMessage) msg;
            // 握手成功， 主动发送心跳信息
            if (message.getHeader() != null && message.getHeader().getType() == MessageType.REGISTER_RESP.value()
                    && RegisterState.SUCCESS.getCode().equals(message.getHeader().getResultCode())) {
                String regId = message.getHeader().getRegId();
                LOGGER.info("===Start to send HEARTBEAT to Server>>>");
                scheduledFuture = ctx.executor().scheduleAtFixedRate(new HeartbeatTask(ctx)
                        , 0, 5000, TimeUnit.MILLISECONDS);
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
//        ctx.fireExceptionCaught(cause);
        LOGGER.error(cause.getMessage(), cause);
    }

    private static class HeartbeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartbeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            StandardMessage heartbeat = buildHeartbeat();
            ctx.channel().writeAndFlush(heartbeat);
        }

        private StandardMessage buildHeartbeat() {
            String regId = AppContextUtil.getAppContext().getBean(PushClientConfig.class).getRegId();

            StandardMessage message = new StandardMessage();
            StandardHeader header = new StandardHeader();
            header.setRegId(regId);
            header.setType(MessageType.HEARTBEAT_REQ.value());
            header.setSessionId(null);
            header.setPriority(1);
            header.setAppId(null);
            header.setClientToken(null);
            message.setHeader(header);
            return message;
        }
    }
}
