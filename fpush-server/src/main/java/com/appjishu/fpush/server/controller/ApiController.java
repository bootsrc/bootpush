package com.appjishu.fpush.server.controller;

import java.util.LinkedList;
import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.appjishu.fpush.core.model.MsgData;
import com.appjishu.fpush.server.singleton.ToSendMap;

@RestController
@RequestMapping("/api")
public class ApiController {

	@RequestMapping("/pushTest")
	public String push() {
		MsgData msgData = new MsgData();
		msgData.setTitle("fpush-Demo");
		msgData.setDescription("这是一条推送给lsm001的消息!");
		List<MsgData> list = new LinkedList<MsgData> ();
		list.add(msgData);
		// 发送给某个用户，一般用这个用户的应用系统中的userId设置成alias，适用于单用户单设备在线聊天的应用场景
		// 这里是发送给alias为"lsm001"的客户端对应的设备一则消息， title和description被指定
		String alias = "lsm001";
		ToSendMap.aliasMap.put(alias, list);
		return "OK";
	}
}
