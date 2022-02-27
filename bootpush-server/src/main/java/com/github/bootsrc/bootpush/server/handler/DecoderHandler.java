package com.github.bootsrc.bootpush.server.handler;

import com.github.bootsrc.bootpush.api.model.StandardMessage;
import com.github.bootsrc.bootpush.api.util.KryoUtil;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class DecoderHandler extends ByteToMessageDecoder {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext, ByteBuf byteBuf, List<Object> list) throws Exception {
        int length = byteBuf.readInt();
        byte[] bytes = new byte[length];
        byteBuf.readBytes(bytes);
        StandardMessage message = KryoUtil.readObjectFromByteArray(bytes, StandardMessage.class);
        list.add(message);
    }
}
