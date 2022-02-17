package com.github.bootsrc.bootpush.server.channel;

import com.github.bootsrc.bootpush.api.model.StandardHeader;
import io.netty.channel.Channel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ChannelMap {
    private static final ConcurrentMap<String, Channel> map = new ConcurrentHashMap<String, Channel>();

    /**
     * @param registrationId 是{@link StandardHeader#getRegistrationId()}
     * @param channel        channel
     */
    public static void put(String registrationId, Channel channel) {
        map.put(registrationId, channel);
    }

    /**
     * 是FHeader.getAlias()
     *
     * @param registrationId clientId
     * @return Channel
     */
    public static Channel get(String registrationId) {
        return map.get(registrationId);
    }

    public static void remove(Channel channel) {
        for (Map.Entry<String, Channel> entry : map.entrySet()) {
            if (channel.compareTo(entry.getValue()) == 0) {
                map.remove(entry.getKey());
            }
        }
    }

    public static Map<String, Channel> getMap() {
        return map;
    }
}
