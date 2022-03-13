package com.github.bootsrc.bootpush.client.biz;

import com.github.bootsrc.bootpush.client.handler.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Client {
    private static final Logger LOGGER = LoggerFactory.getLogger(Client.class);

    public static void start() {
        doStart();
    }

    private static void doStart() {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup workgroup = new NioEventLoopGroup();
        bootstrap.group(workgroup).channel(NioSocketChannel.class).option(ChannelOption.TCP_NODELAY, true).handler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                socketChannel.pipeline()
                        // out
                        .addLast(new EncoderHandler())
                        // in
                        .addLast(new ReadTimeoutHandler(40))
                        .addLast(new DecoderHandler())
                        .addLast(new RegisterClientHandler())
                        .addLast(new HeartbeatClientHandler())
                        .addLast(new PushClientHandler());
            }
        });

        try {
            ChannelFuture future = bootstrap.connect("127.0.0.1", 9111);
            LOGGER.info("Client connected to 127.0.0.1, 9111");
            ChannelFuture future1 = future.sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
