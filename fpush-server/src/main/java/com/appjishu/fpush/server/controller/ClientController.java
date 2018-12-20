package com.appjishu.fpush.server.controller;

import com.alibaba.fastjson.JSON;
import com.appjishu.fpush.core.model.ResponseData;
import com.appjishu.fpush.server.service.AppAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Android, iOS客户端访问的api
 */
@RestController
@RequestMapping("/client")
public class ClientController {
    @Autowired
    private AppAccountService appAccountService;

    @RequestMapping("/keyToken")
    public String getKeyToken(long appId, String appKey) {
        ResponseData responseData = appAccountService.getKeyToken(appId, appKey);
        return JSON.toJSONString(responseData);
    }
}
