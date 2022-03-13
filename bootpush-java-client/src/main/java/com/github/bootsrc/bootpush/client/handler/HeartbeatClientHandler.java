package com.github.bootsrc.bootpush.client.handler;

import com.github.bootsrc.bootpush.api.enums.MessageType;
import com.github.bootsrc.bootpush.api.enums.RegisterState;
import com.github.bootsrc.bootpush.api.model.StandardHeader;
import com.github.bootsrc.bootpush.api.model.StandardMessage;
import com.github.bootsrc.bootpush.client.config.AppContextUtil;
import com.github.bootsrc.bootpush.client.config.PushClientConfig;
import com.github.bootsrc.bootpush.client.constant.GsonSingleton;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;
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
            StandardHeader header = message.getHeader();
            // 握手成功， 主动发送心跳信息
            if (header != null) {

                if (header.getType() == MessageType.REGISTER_RESPONSE.getValue()
                        && RegisterState.SUCCESS.getCode().equals(header.getResultCode())) {
                    String regId = header.getRegId();
                    LOGGER.info("===Start to send HEARTBEAT to Server>>>");
                    scheduledFuture = ctx.executor().scheduleAtFixedRate(new HeartbeatTask(ctx)
                            , 0, 5000, TimeUnit.MILLISECONDS);
                } else if (header.getType() == MessageType.HEARTBEAT_RESPONSE.getValue()) {
                    LOGGER.info("<== READ HEARTBEAT RESPONSE from Server, msg={}", GsonSingleton.getGson().toJson(message));
                }
            }
        }

        ctx.fireChannelRead(msg);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        if (scheduledFuture != null) {
            scheduledFuture.cancel(true);
            scheduledFuture = null;
        }
        LOGGER.error(cause.getMessage(), cause);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        LOGGER.info("HeartbeatServerHandler#userEventTriggered invoked.");
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent idleStateEvent = (IdleStateEvent) evt;
            if (IdleStateEvent.READER_IDLE_STATE_EVENT.state() == idleStateEvent.state()) {
                // ctx.close();
                // 如果收不到客户端的心跳消息，则说明客户端已经失联了，断开跟客户端的channel连接
                ctx.channel().close();
                LOGGER.info("Missing connection with Server, close the channel!");
            }
        } else {
            LOGGER.info("NOT IdleStateEvent");
        }
        super.userEventTriggered(ctx, evt);
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
            header.setType(MessageType.HEARTBEAT_REQUEST.getValue());
            header.setSessionId(null);
            header.setPriority(1);
            header.setAppId(null);
            header.setClientToken(null);
            message.setHeader(header);
            return message;
        }
    }
}
