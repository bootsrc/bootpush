package com.appjishu.fpush.server.channel;

import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class NettyChannelMap {
    private static final Map<String, Channel> map = new ConcurrentHashMap<String, Channel>();

    /**
     *
     * @param clientId 是FHeader.getAlias()
     * @param channel
     */
    public static void put(String clientId, Channel channel) {
        map.put(clientId, channel);
    }

    /**
     * 是FHeader.getAlias()
     * @param clientId
     * @return
     */
    public static Channel get(String clientId) {
        Channel c = null;
        return map.get(clientId);
    }

    public static void remove(Channel channel) {
        for (Map.Entry<String, Channel> entry : map.entrySet()) {
            if(channel.compareTo(entry.getValue()) == 0){
                map.remove(entry.getKey());
            }
        }
    }

    public static Map<String, Channel> getMap() {
        return map;
    }
}
