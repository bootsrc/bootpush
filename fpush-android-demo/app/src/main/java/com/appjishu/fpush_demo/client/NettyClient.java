package com.appjishu.fpush_demo.client;

import android.util.Log;
import android.widget.Toast;

import com.appjishu.fpush.core.model.MsgUser;
import com.appjishu.fpush.core.model.ResponseData;
import com.appjishu.fpush.core.proto.FMessage;
import com.appjishu.fpush_demo.activity.MainActivity;
import com.appjishu.fpush_demo.boot.MyApp;
import com.appjishu.fpush_demo.constant.NetConstant;
import com.appjishu.fpush_demo.constant.OneUser;
import com.appjishu.fpush_demo.handler.HeartBeatRequestHandler;
import com.appjishu.fpush_demo.handler.PushConfirmHandler;
import com.appjishu.fpush_demo.handler.RegisterRequestHandler;
import com.appjishu.fpush_demo.singleton.CurrentUser;
import com.google.gson.Gson;

import java.io.IOException;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
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
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NettyClient {
    private static final String MY_TAG = "NettyClient";
    private static NettyClient nettyClient = new NettyClient();

    private static long appId = NetConstant.APP_ID;
    private static String appKey = NetConstant.APP_KEY;
    private static Gson gson;
    private EventLoopGroup group;
    private boolean isConnect = false;
    private Channel channel;
    private int reconnectNum = Integer.MAX_VALUE;
    private long reconnectIntervalTime = 5000;

    public NettyClient() {
        gson = new Gson();
    }

    public static NettyClient getInstance() {
        return nettyClient;
    }

    public synchronized NettyClient connect() throws Exception {
        OkHttpClient httpClient = new OkHttpClient.Builder().build();

        FormBody formBody = new FormBody.Builder().add("appId", appId + "")
                .add("appKey", appKey).build();

        String urlStr = "http://" + NetConstant.PUSH_HOST + ":" + NetConstant.API_PORT + NetConstant.KEY_TOKEN_URL;
        Request request = new Request.Builder().url(urlStr)
                .post(formBody)
                .build();

        Call call = httpClient.newCall(request);

        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(MY_TAG, "http failed.");
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.code() == 200) {
                    String responseStr = response.body().string();
//                    ResponseData responseData = JSON.parseObject(responseStr, ResponseData.class);
                    ResponseData responseData = gson.fromJson(responseStr, ResponseData.class);

                    if (responseData.getCode() == 0) {
                        String clientToken = (String) responseData.getData();
                        try {
//                            MainActivity mainActivity = MainActivity.getInstance();
//                            if (mainActivity != null) {
//                                Toast.makeText(MainActivity.getInstance(), "连接成功！", Toast.LENGTH_SHORT).show();
//                            }
                            doStart(appId, clientToken);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                }

            }
        });
        return this;
    }

    private void doStart(long appId, String clientToken) throws Exception {
        MsgUser msgUser = new MsgUser();
        msgUser.setAlias(OneUser.ALIAS);
        msgUser.setAccount(OneUser.ACCOUNT);
        msgUser.setAppId(appId);
        msgUser.setAppToken(clientToken);
        String logText = "---register_config_appId=" + appId + ",clientToken=" + clientToken;
        Log.d(MY_TAG, logText);
        CurrentUser.setInfo(msgUser);

        group = new NioEventLoopGroup();
        Bootstrap b = new Bootstrap();
        b.group(group).channel(NioSocketChannel.class)
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

        ChannelFuture cf1 = b.connect(NetConstant.PUSH_HOST, NetConstant.PUSH_PORT).sync();
        if (cf1 != null && cf1.isSuccess()) {
            channel = cf1.channel();
            isConnect = true;
        } else {
            isConnect = false;
        }

        cf1.channel().closeFuture().sync();
        group.shutdownGracefully();
    }

    public void disconnect() {
        group.shutdownGracefully();
    }

    private void reconnect() {
        if (reconnectNum > 0 && !isConnect) {
            reconnectNum--;
            try {
                Thread.sleep(reconnectIntervalTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            disconnect();
            try {
                connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            disconnect();
        }
    }

    public Channel getChannel() {
        return channel;
    }

    /**
     * 设置重连次数
     *
     * @param reconnectNum 重连次数
     */
    public void setReconnectNum(int reconnectNum) {
        this.reconnectNum = reconnectNum;
    }

    /**
     * 设置重连时间间隔
     *
     * @param reconnectIntervalTime 时间间隔
     */
    public void setReconnectIntervalTime(long reconnectIntervalTime) {
        this.reconnectIntervalTime = reconnectIntervalTime;
    }

    public boolean getConnectStatus() {
        return isConnect;
    }

    /**
     * 设置连接状态
     *
     * @param status
     */
    public void setConnectStatus(boolean status) {
        this.isConnect = status;
    }

//    public void setListener(NettyListener listener) {
//        if (listener == null) {
//            throw new IllegalArgumentException("listener == null ");
//        }
//        this.listener = listener;
//    }
}