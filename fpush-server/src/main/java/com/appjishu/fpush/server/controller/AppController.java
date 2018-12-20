package com.appjishu.fpush.server.controller;

import com.appjishu.fpush.core.model.MsgData;
import com.appjishu.fpush.server.constant.ExceptionMsg;
import com.appjishu.fpush.server.singleton.ToSendMap;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.appjishu.fpush.core.model.ResponseData;
import com.appjishu.fpush.server.service.AppAccountService;

import java.util.LinkedList;
import java.util.List;

/**
 * 应用服务器访问的api
 * 应用服务器指的是使用这个推送服务的Java服务端
 *
 * @author liushaoming
 */
@RestController
@RequestMapping("/app")
public class AppController {
    private static final Logger log = LoggerFactory.getLogger(AppController.class);

    @Autowired
    private AppAccountService appAccountService;


    @RequestMapping("/registerAccount")
    public String registerAccount(String mobilePhone) {
        ResponseData responseData = appAccountService.registerAccount(mobilePhone);
        return JSON.toJSONString(responseData);
    }

    @RequestMapping("/secretToken")
    public String getSecretToken(long appId, String appSecretKey) {
        ResponseData responseData = appAccountService.getSecretToken(appId, appSecretKey);
        return JSON.toJSONString(responseData);
    }

    /**
     * @param receiverAlias 例如lsm001
     * @param title
     * @param desc
     * @param data
     * @return
     */
    @RequestMapping("/push")
    public String push(String receiverAlias, String title, String desc, String data) {
        if (StringUtils.isEmpty(title) || StringUtils.isEmpty(desc) || StringUtils.isEmpty(data)) {
            log.info(ExceptionMsg.PUSH_PARAM_EMPTY);
            return ExceptionMsg.PUSH_PARAM_EMPTY;
        }

        MsgData msgData = new MsgData();
        msgData.setTitle(title);
        msgData.setDescription(desc);

        List<MsgData> list = new LinkedList<MsgData>();
        list.add(msgData);
        // 发送给某个用户，一般用这个用户的应用系统中的userId设置成alias，适用于单用户单设备在线聊天的应用场景
        // 这里是发送给alias为"lsm001"的客户端对应的设备一则消息， title和description被指定
        ToSendMap.aliasMap.put(receiverAlias, list);
        return "OK";
    }
}
