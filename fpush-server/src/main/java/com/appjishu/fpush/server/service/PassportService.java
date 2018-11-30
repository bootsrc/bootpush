package com.appjishu.fpush.server.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.appjishu.fpush.server.entity.AppAccount;
import com.appjishu.fpush.server.repository.AppAccountRepository;

@Service
public class PassportService {

    @Autowired
    private AppAccountRepository appAccountRepository;

    /**
     * 客户端设备登录鉴权, Android, ios端需要鉴定权限
     *
     * @param appId
     * @param clientToken
     * @return
     */
    public boolean checkByClientToken(long appId, String clientToken) {
        if (appId < 1 || StringUtils.isEmpty(clientToken)) {
            return false;
        }
        AppAccount appAccount = appAccountRepository.findByAppId(appId);
        return appAccount != null && clientToken.equals(appAccount.getClientToken());
    }

    /**
     * 应用服务器端登录鉴权， 客户的应用后台需要鉴定权限
     *
     * @param appId
     * @param appToken
     * @return
     */
    public boolean checkByAppToken(long appId, String appToken) {
        if (appId < 1 || StringUtils.isEmpty(appToken)) {
            return false;
        }
        AppAccount appAccount = appAccountRepository.findByAppId(appId);
        return appAccount != null && appToken.equals(appAccount.getAppToken());
    }
}
