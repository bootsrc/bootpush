package com.appjishu.fpush.server.util;

import com.appjishu.fpush.server.service.PassportService;

public class PassportUtil {
	private static PassportService getPassportService() {
		return AppContextHolder.getBean(PassportService.class);
	}
	
	public static boolean checkByClientToken(long appId, String clientToken) {
		return getPassportService().checkByClientToken(appId, clientToken);
	}
	
	public static boolean checkByAppToken(long appId, String appToken) {
		return getPassportService().checkByAppToken(appId, appToken);
	}
}
