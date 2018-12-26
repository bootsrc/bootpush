package com.appjishu.fpush.server.controller;

import java.util.LinkedList;
import java.util.List;

import com.alibaba.fastjson.JSON;
import com.appjishu.fpush.server.model.ChannelInfo;
import com.appjishu.fpush.server.service.MonitorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.appjishu.fpush.core.model.MsgData;
import com.appjishu.fpush.server.singleton.ToSendMap;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/monitor")
public class MonitorController {
    @Autowired
    private MonitorService monitorService;

    @RequestMapping("/channelList")
    public String channelList() {
        List<ChannelInfo> infoList = monitorService.getChannelList();
        return JSON.toJSONString(infoList);
    }
}
