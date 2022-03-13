package com.github.bootsrc.bootpush.server.handler;

import com.github.bootsrc.bootpush.api.enums.MessageType;
import com.github.bootsrc.bootpush.api.model.StandardHeader;
import com.github.bootsrc.bootpush.api.model.StandardMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MainServerHandler extends ChannelInboundHandlerAdapter {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainServerHandler.class);

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof StandardMessage) {
            StandardMessage message = (StandardMessage) msg;
            StandardHeader header = message.getHeader();
            if (header != null
                    && header.getType() == MessageType.REGISTER_REQUEST.getValue()) {
                // TODO 针对各种message type的分别处理

            } else {
                ctx.fireChannelRead(msg);
            }
        } else {
            ctx.fireChannelRead(msg);
        }
    }
}
