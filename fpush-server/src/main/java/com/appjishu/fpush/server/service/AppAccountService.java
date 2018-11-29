package com.appjishu.fpush.server.service;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appjishu.fpush.core.model.ResponseData;
import com.appjishu.fpush.server.entity.AppAccount;
import com.appjishu.fpush.server.repository.AppAccountRepository;
import com.appjishu.fpush.server.util.DateUtil;
import com.appjishu.fpush.server.util.IdUtil;
import com.appjishu.fpush.server.util.MD5Util;
import com.appjishu.fpush.server.util.SnowflakeWorker;

@Service
public class AppAccountService {
	@Autowired
	private AppAccountRepository appAccountRepository;
	@Autowired
	private SnowflakeWorker snowflakeWorker;

	public AppAccount add(String mobilePhone) {
		if (StringUtils.isEmpty(mobilePhone)) {
			return null;
		}

		long appId = snowflakeWorker.nextId();
		String str = IdUtil.newId();
		String appSecretKey = str.substring(0, 20);
		String appKey = str.substring(20);
		Date currentDate = new Date();
		Date expireDate = DateUtil.addByYear(currentDate, 3);

		AppAccount appAccount = new AppAccount();
		appAccount.setAppId(appId);
		appAccount.setAppKey(appKey);
		appAccount.setAppSecretKey(appSecretKey);
		appAccount.setKeyToken(MD5Util.md5(appKey));
		appAccount.setSecretToken(MD5Util.md5(appSecretKey));
		appAccount.setCreateTime(currentDate);
		appAccount.setUpdateTime(currentDate);
		appAccount.setKeyTokenExpire(expireDate);
		appAccount.setSecretTokenExpire(expireDate);
		appAccount.setMobilePhone(mobilePhone);
		appAccountRepository.add(appAccount);
		return appAccount;
	}

	public ResponseData registerAccount(String mobilePhone) {
		ResponseData responseData = new ResponseData();
		if (StringUtils.isEmpty(mobilePhone)) {
			responseData.setCode(1);
			responseData.setText("手机号为空");
			return responseData;
		}

		AppAccount exist = appAccountRepository.findByMobilePhone(mobilePhone);
		if (exist == null) {
			AppAccount added = add(mobilePhone);
			responseData.setCode(0);
			responseData.setText("注册成功");
			responseData.setData(added);
		} else {
			responseData.setCode(2);
			responseData.setText("使用这个手机号的账号已经存在");
			responseData.setData(exist);
		}
		return responseData;
	}
}
