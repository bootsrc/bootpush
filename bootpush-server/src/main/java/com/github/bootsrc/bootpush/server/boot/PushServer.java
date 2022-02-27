package com.github.bootsrc.bootpush.server.boot;

import com.github.bootsrc.bootpush.server.handler.DecoderHandler;
import com.github.bootsrc.bootpush.server.handler.EncoderHandler;
import com.github.bootsrc.bootpush.server.config.BootpushServerConfig;
import com.github.bootsrc.bootpush.server.handler.HeartbeatServerHandler;
import com.github.bootsrc.bootpush.server.handler.RegisterServerHandler;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.timeout.ReadTimeoutHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushServer {
    private static final Logger LOGGER= LoggerFactory.getLogger(PushServer.class);

    private BootpushServerConfig config;
    private ServerBootstrap bootstrap;
    private EventLoopGroup group;


    public PushServer(BootpushServerConfig config) {
        this.config = config;
    }

    public void start() {
        LOGGER.info("bootpush-server is starting...");

        bootstrap = new ServerBootstrap();
        group = new NioEventLoopGroup(config.getIoThreads());
        bootstrap.group(group);
        bootstrap.channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                // out
                pipeline.addLast(new EncoderHandler());

                // in
                pipeline.addLast(new ReadTimeoutHandler(40));
                pipeline.addLast(new DecoderHandler());
                pipeline.addLast(new HeartbeatServerHandler());
                pipeline.addLast(new RegisterServerHandler());
                // TODO add PushHandler

            }
        });
        // TODO bootstrap option
        bootstrap.bind(config.getTcpPort());
        LOGGER.info("bootpush-server started>>>tcp-port={}", config.getTcpPort());
    }

    public void stop() {

    }
}
