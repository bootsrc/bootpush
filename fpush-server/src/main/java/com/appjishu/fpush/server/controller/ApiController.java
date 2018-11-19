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
		ToSendMap.aliasMap.put("lsm001", list);
		return "OK";
	}
}
