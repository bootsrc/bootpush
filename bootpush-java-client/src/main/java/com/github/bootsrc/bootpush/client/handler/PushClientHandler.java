package com.github.bootsrc.bootpush.client.handler;

import com.github.bootsrc.bootpush.api.enums.MessageType;
import com.github.bootsrc.bootpush.api.model.StandardMessage;
import com.github.bootsrc.bootpush.client.constant.GsonSingleton;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushClientHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushClientHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof StandardMessage) {
            StandardMessage message = (StandardMessage) msg;
            if (null != message.getHeader() && MessageType.PUSH.value() == message.getHeader().getType()) {
                LOGGER.info("<== RECEIVED PUSHED MESSAGE from Server, msg data={}"
                        , GsonSingleton.getGson().toJson(message));
            }
        }
        ctx.fireChannelRead(msg);
    }
}
