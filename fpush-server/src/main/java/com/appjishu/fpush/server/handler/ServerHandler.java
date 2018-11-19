package com.appjishu.fpush.server.handler;

import com.appjishu.fpush.core.proto.FHeader;
import com.appjishu.fpush.core.proto.FMessage;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof FMessage) {
			FMessage fMessage = (FMessage) msg;
			if (fMessage.getHeader() != null) {
				FHeader fHeader = fMessage.getHeader();
				System.out.println("---fHeader.getAlias:" + fHeader.getAlias());
				System.out.println("---fHeader.getSessionId:" + fHeader.getSessionId());
				System.out.println("---fHeader.getType:" + fHeader.getType());
				System.out.println("---fHeader.getPriority:" + fHeader.getPriority());
				System.out.println("---fMessage.getBody:" + fMessage.getBody());
			}
		} else {

		}
		
		ctx.fireChannelRead(msg);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}
}
