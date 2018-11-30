package com.appjishu.fpush.client.handler;

        import java.util.concurrent.ScheduledFuture;
        import java.util.concurrent.TimeUnit;

        import com.appjishu.fpush.client.singleton.CurrentUser;
        import com.appjishu.fpush.core.constant.FMessageType;
        import com.appjishu.fpush.core.model.MsgUser;
        import com.appjishu.fpush.core.proto.FHeader;
        import com.appjishu.fpush.core.proto.FMessage;

        import io.netty.channel.ChannelHandlerContext;
        import io.netty.channel.ChannelInboundHandlerAdapter;

public class HeartBeatRequestHandler extends ChannelInboundHandlerAdapter {
    private volatile ScheduledFuture<?> heartBeat;

    public HeartBeatRequestHandler() {

    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FMessage) {
            FMessage message = (FMessage) msg;
            // 握手成功， 主动发送心跳信息
            if (message.getHeader() != null && message.getHeader().getType() == FMessageType.REGISTER_RESP.value()) {
                heartBeat = ctx.executor().scheduleAtFixedRate(new HeartBeatRequestHandler.HeartBeatTask(ctx), 0, 5000, TimeUnit.MILLISECONDS);
            } else if (message.getHeader() != null && message.getHeader().getType() == FMessageType.HEARTBEAT_RESP.value()) {
                System.out.println("Client receive server heartbeat message : ---> " + message);
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
        if (heartBeat != null) {
            heartBeat.cancel(true);
            heartBeat = null;
        }
        ctx.fireExceptionCaught(cause);
    }

    private class HeartBeatTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public HeartBeatTask(ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            FMessage heartBeat = buildHeartBeat();
            System.out.println("Client send heart beat message to server : ---> " + heartBeat);
            ctx.writeAndFlush(heartBeat);
        }

        private FMessage buildHeartBeat() {
            FMessage.Builder builder = FMessage.newBuilder();
            FHeader.Builder headerBuilder = FHeader.newBuilder();
            headerBuilder.setAlias(CurrentUser.getInfo().getAlias());
            headerBuilder.setAccount(CurrentUser.getInfo().getAccount());
            headerBuilder.setType(FMessageType.HEARTBEAT_REQ.value());
            headerBuilder.setSessionId(1234);
            headerBuilder.setPriority(9);
            headerBuilder.setAppId(CurrentUser.getInfo().getAppId());
            headerBuilder.setClientToken(CurrentUser.getInfo().getAppToken());
            builder.setHeader(headerBuilder.build());

            FMessage fMessage = builder.build();
            return fMessage;
        }
    }
}
