package com.appjishu.fpush.server.service;

import com.appjishu.fpush.core.constant.FMessageType;
import com.appjishu.fpush.core.model.MsgData;
import com.appjishu.fpush.core.proto.FBody;
import com.appjishu.fpush.core.proto.FHeader;
import com.appjishu.fpush.core.proto.FMessage;
import com.appjishu.fpush.server.channel.NettyChannelMap;
import com.appjishu.fpush.server.constant.ChannelAttrKey;
import com.appjishu.fpush.server.singleton.ToSendMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class PushService {
    private static final Logger log = LoggerFactory.getLogger(PushService.class);

//    private FMessage buildPushMessage(String alias, String account) {
//        if (StringUtils.isEmpty(alias)) {
//            return null;
//        }
//
//        if (ToSendMap.aliasMap.containsKey(alias)) {
//            List<MsgData> msgList = ToSendMap.aliasMap.get(alias);
//            if (msgList.size()>0) {
//                FMessage.Builder builder = FMessage.newBuilder();
//                FHeader.Builder headerBuilder = FHeader.newBuilder();
//                headerBuilder.setAlias(alias);
//                headerBuilder.setAccount(account);
//                headerBuilder.setType(FMessageType.PUSH.value());
//                headerBuilder.setSessionId(1234);
//                headerBuilder.setPriority(9);
//                builder.setHeader(headerBuilder.build());
//
//                FBody.Builder fBodyBuilder = FBody.newBuilder();
//                if (msgList.get(0) != null) {
//                    MsgData data = msgList.get(0);
//                    fBodyBuilder.setTitle(data.getTitle());
//                    fBodyBuilder.setDescription(data.getDescription());
//                    builder.setBody(fBodyBuilder);
//                }
//                FMessage fMessage = builder.build();
//                // TODO msgList.remove(0);
//                msgList.remove(0);
//                return fMessage;
//            }
//        }
//        return null;
//    }

    private FMessage buildPushMessage(List<MsgData> msgList, String alias) {
        if (msgList != null && msgList.size() > 0) {
            FMessage.Builder builder = FMessage.newBuilder();
            FHeader.Builder headerBuilder = FHeader.newBuilder();
            headerBuilder.setAlias(alias);
            headerBuilder.setType(FMessageType.PUSH.value());
            headerBuilder.setSessionId(1234);
            headerBuilder.setPriority(9);
            builder.setHeader(headerBuilder.build());

            FBody.Builder fBodyBuilder = FBody.newBuilder();
            if (msgList.get(0) != null) {
                MsgData data = msgList.get(0);
                fBodyBuilder.setTitle(data.getTitle());
                fBodyBuilder.setDescription(data.getDescription());
                builder.setBody(fBodyBuilder);
            }
            FMessage fMessage = builder.build();
            // TODO msgList.remove(0);
//            msgList.remove(0);
            return fMessage;
        } else {
            return null;
        }
    }

    public void doPush(List<MsgData> msgList, String alias) {
        FMessage fMessage = buildPushMessage(msgList, alias);
        if (fMessage != null) {
            log.info("---TringToDoPush()--->");
            Channel channel = NettyChannelMap.get(alias);
            if (channel == null) {
                log.info("------channelIsNull---");
            } else if (!channel.isWritable()) {
                log.info("------channelIsNotWritable---");
            } else {
                ChannelFuture future = channel.writeAndFlush(fMessage);
                log.info("------msgWriten!!!---");
                future.addListener(new ChannelFutureListener() {
                    public void operationComplete(final ChannelFuture future)
                            throws Exception {
                        if (msgList.size() > 0) {
                            msgList.remove(0);
                            log.info("------removeAreadySentMsg!!!---");
                        }
                    }
                });
            }
        }
    }
}
