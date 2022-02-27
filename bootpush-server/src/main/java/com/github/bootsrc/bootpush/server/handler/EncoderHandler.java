package com.github.bootsrc.bootpush.server.handler;

import com.github.bootsrc.bootpush.api.model.StandardMessage;
import com.github.bootsrc.bootpush.api.util.KryoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EncoderHandler extends MessageToByteEncoder<StandardMessage> {
    private static final Logger LOGGER = LoggerFactory.getLogger(EncoderHandler.class);

    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext, StandardMessage standardMessage, ByteBuf byteBuf) throws Exception {
        byte[] bytes = KryoUtil.writeObjectToByteArray(standardMessage);
        byteBuf.writeInt(bytes.length);
        byteBuf.writeBytes(bytes);
    }
}
