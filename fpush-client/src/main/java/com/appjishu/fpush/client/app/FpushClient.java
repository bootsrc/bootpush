package com.appjishu.fpush.client.app;

import com.alibaba.fastjson.JSON;
import com.appjishu.fpush.client.constant.NetConstant;
import com.appjishu.fpush.client.constant.OneUser;
import com.appjishu.fpush.client.handler.HeartBeatRequestHandler;
import com.appjishu.fpush.client.handler.PushConfirmHandler;
import com.appjishu.fpush.client.handler.RegisterRequestHandler;
import com.appjishu.fpush.client.singleton.CurrentUser;
import com.appjishu.fpush.core.model.MsgUser;
import com.appjishu.fpush.core.model.ResponseData;
import com.appjishu.fpush.core.proto.FMessage;

import io.netty.bootstrap.Bootstrap;
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
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class FpushClient {
    private static final Logger log = LoggerFactory.getLogger(FpushClient.class);

    public static void start() throws Exception {
        long appId = NetConstant.APP_ID;
        String appKey = NetConstant.APP_KEY;
        OkHttpClient httpClient = new OkHttpClient.Builder().build();

        FormBody formBody = new FormBody.Builder().add("appId", appId + "")
                .add("appKey", appKey).build();

        Request request = new Request.Builder().url(NetConstant.SITE + NetConstant.KEY_TOKEN_URL)
                .post(formBody)
                .build();

        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String responseStr = response.body().string();
                    ResponseData responseData = JSON.parseObject(responseStr, ResponseData.class);
                    if (responseData.getCode() == 0) {
                        String clientToken = (String) responseData.getData();
                        try {
                            doStart(appId, clientToken);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });

    }

    private static void doStart(long appId, String clientToken) throws Exception {
        MsgUser msgUser = new MsgUser();
        msgUser.setAlias(OneUser.ALIAS);
        msgUser.setAccount(OneUser.ACCOUNT);
        msgUser.setAppId(appId);
        msgUser.setAppToken(clientToken);
        log.info("---register_config_appId={}, keyToken={}", appId, clientToken);
        CurrentUser.setInfo(msgUser);

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
                        sc.pipeline().addLast(new RegisterRequestHandler());
                        sc.pipeline().addLast(new HeartBeatRequestHandler());
                        sc.pipeline().addLast(new PushConfirmHandler());
                    }
                });

        ChannelFuture cf1 = b.connect("127.0.0.1", 9910).sync();

        // buf
//		cf1.channel().writeAndFlush(Unpooled.copiedBuffer("我是客户端信息:777".getBytes()));

        cf1.channel().closeFuture().sync();
        workgroup.shutdownGracefully();
    }
}
