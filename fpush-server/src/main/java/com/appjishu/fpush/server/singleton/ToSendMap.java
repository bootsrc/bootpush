package com.appjishu.fpush.server.singleton;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import com.appjishu.fpush.core.model.MsgData;

public class ToSendMap {
	public static final ConcurrentHashMap<String, List<MsgData>> aliasMap = 
			new ConcurrentHashMap<String, List<MsgData>>();
}
