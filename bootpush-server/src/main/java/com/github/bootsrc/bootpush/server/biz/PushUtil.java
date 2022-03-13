package com.github.bootsrc.bootpush.server.biz;

import com.github.bootsrc.bootpush.api.enums.MessageType;
import com.github.bootsrc.bootpush.api.model.StandardBody;
import com.github.bootsrc.bootpush.api.model.StandardHeader;
import com.github.bootsrc.bootpush.api.model.StandardMessage;
import com.github.bootsrc.bootpush.server.boot.GsonSingleton;
import com.github.bootsrc.bootpush.server.channel.ChannelMap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PushUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PushUtil.class);

    public static void push(String regId, String text) {
        StandardMessage msg = new StandardMessage();
        StandardHeader header = new StandardHeader();
        StandardBody body = new StandardBody();
        header.setRegId(regId);
        header.setType(MessageType.PUSH.getValue());
        msg.setHeader(header);

        body.setContentType("text");
        body.setContent(text);
        msg.setBody(body);


        // 从缓存中根据regId获取到对应的Channel
        Channel channel = ChannelMap.get(regId);
        if (null != channel) {
            ChannelFuture channelFuture = channel.writeAndFlush(msg);
            LOGGER.info("==> PUSH MESSAGE msg={}", GsonSingleton.getGson().toJson(msg));
        } else {
            LOGGER.warn("push text_msg failed, causing is 'channel is null' with regId={}, msg={}"
                    , regId, GsonSingleton.getGson().toJson(msg));
        }
    }
}
