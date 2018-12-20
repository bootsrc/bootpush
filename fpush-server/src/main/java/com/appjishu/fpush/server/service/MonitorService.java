package com.appjishu.fpush.server.service;

import com.appjishu.fpush.server.channel.NettyChannelMap;
import com.appjishu.fpush.server.model.ChannelInfo;
import io.netty.channel.Channel;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.appjishu.fpush.server.channel.NettyChannelMap.getMap;

@Service
public class MonitorService {
    public List<ChannelInfo> getChannelList() {
        Map<String, Channel> map = NettyChannelMap.getMap();
        List<ChannelInfo> channelInfoList = new ArrayList<ChannelInfo>();
        if (map!= null && map.size()>0) {
            for (Map.Entry<String, Channel> entry: map.entrySet()){
                ChannelInfo channelInfo=new ChannelInfo();
                channelInfo.setAlias(entry.getKey());
                Channel channel = entry.getValue();
                boolean active = channel == null? false: channel.isActive();
                channelInfo.setActive(active);
                channelInfoList.add(channelInfo);
            }
        }
        return channelInfoList;
    }
}
