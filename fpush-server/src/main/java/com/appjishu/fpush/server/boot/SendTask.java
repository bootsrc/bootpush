package com.appjishu.fpush.server.boot;

import com.appjishu.fpush.core.model.MsgData;
import com.appjishu.fpush.server.service.PushService;
import com.appjishu.fpush.server.singleton.ToSendMap;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class SendTask {
    @Autowired
    private PushService pushService;

//    @Scheduled(cron = "0/20 * * * * ?") // 每20秒执行一次
//    public void scheduler() {
//        System.out.println(">>>>>>>>>>>>> scheduled ... ");
//    }


    @Scheduled(fixedRate = 5000)
    public void scan() {
        for (Map.Entry<String, List<MsgData>> entry: ToSendMap.aliasMap.entrySet()) {
            String alias = entry.getKey();
            List<MsgData> msgList = entry.getValue();
            if (StringUtils.isNotEmpty(alias) && msgList != null && msgList.size() > 0) {
                pushService.doPush(msgList, alias);
            }
        }

    }


}
