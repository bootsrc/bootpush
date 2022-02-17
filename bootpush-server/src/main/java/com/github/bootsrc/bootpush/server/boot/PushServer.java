package com.github.bootsrc.bootpush.server.boot;

import com.github.bootsrc.bootpush.api.handler.DecoderHandler;
import com.github.bootsrc.bootpush.api.handler.EncoderHandler;
import com.github.bootsrc.bootpush.server.config.BootpushServerConfig;
import com.github.bootsrc.bootpush.server.handler.RegisterResponseHandler;
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
        bootstrap = new ServerBootstrap();
        group = new NioEventLoopGroup(config.getIoThreads());
        bootstrap.group(group);

        EncoderHandler encoderHandler = new EncoderHandler();
        DecoderHandler decoderHandler = new DecoderHandler();
        bootstrap.channel(NioServerSocketChannel.class).childHandler(new ChannelInitializer<SocketChannel>() {
            @Override
            protected void initChannel(SocketChannel socketChannel) throws Exception {
                ChannelPipeline pipeline = socketChannel.pipeline();
                // out
                pipeline.addLast(encoderHandler);

                // in
                pipeline.addLast(new ReadTimeoutHandler(60));
                pipeline.addLast(decoderHandler);
                pipeline.addLast(new RegisterResponseHandler());
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
