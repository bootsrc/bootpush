package com.appjishu.fpush.client.app;

import com.appjishu.fpush.client.handler.ClientHandler;
import com.appjishu.fpush.core.proto.FMessage;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class FpushClient {

	public static void main(String[] args) throws Exception {

		EventLoopGroup workgroup = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(workgroup).channel(NioSocketChannel.class)
		.option(ChannelOption.TCP_NODELAY, true)
		.handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				sc.pipeline().addLast(new ProtobufVarint32FrameDecoder());
				sc.pipeline().addLast(new ProtobufDecoder(FMessage.getDefaultInstance()));
				sc.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
				sc.pipeline().addLast(new ProtobufEncoder());
				sc.pipeline().addLast(new ClientHandler());
			}
		});

		ChannelFuture cf1 = b.connect("127.0.0.1", 9910).sync();

		// buf
//		cf1.channel().writeAndFlush(Unpooled.copiedBuffer("我是客户端信息:777".getBytes()));

		cf1.channel().closeFuture().sync();
		workgroup.shutdownGracefully();

	}
}
