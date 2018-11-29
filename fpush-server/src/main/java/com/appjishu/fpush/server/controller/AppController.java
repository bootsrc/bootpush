package com.appjishu.fpush.server.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;
import com.appjishu.fpush.core.model.ResponseData;
import com.appjishu.fpush.server.service.AppAccountService;

/**
 * 应用服务器访问的api
 * 应用服务器指的是使用这个推送服务的Java服务端
 * @author liushaoming
 *
 */
@RestController
@RequestMapping("/app")
public class AppController {
	@Autowired
	private AppAccountService appAccountService;
	
	
	@RequestMapping("/registerAccount")
	public String registerAccount(String mobilePhone) {
		ResponseData responseData = appAccountService.registerAccount(mobilePhone);
		return JSON.toJSONString(responseData);
	}
	
//	@RequestMapping("/login")
//	public String login() {
//
//		return "";
//	}
}
