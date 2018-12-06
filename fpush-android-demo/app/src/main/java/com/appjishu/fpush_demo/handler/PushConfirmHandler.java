package com.appjishu.fpush_demo.handler;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.SupportActivity;
import android.util.Log;

import com.appjishu.fpush.core.constant.FMessageType;
import com.appjishu.fpush.core.model.MsgData;
import com.appjishu.fpush.core.proto.FBody;
import com.appjishu.fpush.core.proto.FHeader;
import com.appjishu.fpush.core.proto.FMessage;
import com.appjishu.fpush_demo.activity.MainActivity;
import com.appjishu.fpush_demo.boot.MyApp;
import com.appjishu.fpush_demo.constant.SysConstant;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class PushConfirmHandler extends ChannelInboundHandlerAdapter {
    private static final String MY_TAG = "PushConfmHandler";

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (msg instanceof FMessage) {
            FMessage message = (FMessage) msg;
            // 返回心跳应答消息
            if (message.getHeader() != null &&
                    message.getHeader().getType() == FMessageType.PUSH.value()) {
                FHeader header = message.getHeader();
                if (header.getType() == FMessageType.PUSH.value()) {
                    FBody receiveBody = message.getBody();
                    if (receiveBody != null) {
                        // 这是推送到客户端的消息的title和描述description
                        String receivedTitle = receiveBody.getTitle();
                        String receivedDesc = receiveBody.getDescription();
                        Log.d(MY_TAG, "--->>>这是推送到客户端的消息:title=" + receivedTitle);
                        Log.d(MY_TAG, "--->>>这是推送到客户端的消息:description=" + receivedDesc);

//                        Intent intent = new Intent("msgBroadcastReceiver");
//                        Intent intent = new Intent();
//                        intent.setAction("msgBroadcastReceiver");
//                        intent.putExtra(SysConstant.KEY_MSG_DESC, receivedDesc);
//                        MyApp.getInstance().sendBroadcast(intent);

//						Object data;//服务器返回的数据data
//						Intent intent = new Intent();//创建Intent对象
//						intent.setAction("aaa");
//						intent.putExtra("data", receivedDesc);
//						sendBroadcast(intent);//发送广播

						Message androidMsg = new Message();
						Bundle bundle = new Bundle();
						bundle.putString(SysConstant.KEY_MSG_TITLE, receivedTitle);
						bundle.putString(SysConstant.KEY_MSG_DESC, receivedDesc);
                        androidMsg.setData(bundle);
                        androidMsg.what = MainActivity.MSG_WHAT_MSG;
                        MainActivity mainActivity = MainActivity.getInstance();
                        if (mainActivity!=null) {
                            mainActivity.sendMessage(androidMsg);
                        }

                        ctx.fireChannelRead(msg);
                    }
                }
            }
        }
        ctx.fireChannelRead(msg);
    }

    private FMessage buildPushConfirmMessage(String alias, String account) {
//		if (StringUtils.isEmpty(alias)) {
//			return null;
//		}
//		
//		
//	
//		FMessage.Builder builder = FMessage.newBuilder();
//    	FHeader.Builder headerBuilder = FHeader.newBuilder();
//    	headerBuilder.setAlias(alias);
//    	headerBuilder.setAccount(account);
//    	headerBuilder.setType(FMessageType.PUSH.value());
//    	headerBuilder.setSessionId(1234);
//    	headerBuilder.setPriority(9);
//    	builder.setHeader(headerBuilder.build());
//    	
//    	FBody.Builder fBodyBuilder = FBody.newBuilder();
//    	if (msgList.get(0) != null) {
//    		MsgData data = msgList.get(0);
//    		fBodyBuilder.setTitle(data.getTitle());
//    		fBodyBuilder.setDescription(data.getDescription());
//    		builder.setBody(fBodyBuilder);
//    	}
//    	FMessage fMessage = builder.build();
//    	// TODO msgList.remove(0);
//		msgList.remove(0);
//		return fMessage;


        return null;
    }
}
