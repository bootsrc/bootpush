package com.appjishu.fpush.server.entity;

import java.io.Serializable;
import java.util.Date;

public class AppAccount implements Serializable {
    private static final long serialVersionUID = 2085724763291169990L;

    private long appId;
    private String appKey;
    private String appSecretKey;
    /**
     * 客户端发送appId + appKey，经后台鉴定权限通过后，获取到clientToken
     */
    private String clientToken;
    /**
     * 应用服务端发送appId + appSecretKey，经后台鉴定权限通过后，获取到appToken
     */
    private String appToken;
    private Date createTime;
    private Date updateTime;
    private Date clientTokenExpire;
    private Date appTokenExpire;
    private String mobilePhone;

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

    public String getAppSecretKey() {
        return appSecretKey;
    }

    public void setAppSecretKey(String appSecretKey) {
        this.appSecretKey = appSecretKey;
    }

    public String getClientToken() {
        return clientToken;
    }

    public void setClientToken(String clientToken) {
        this.clientToken = clientToken;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public Date getClientTokenExpire() {
        return clientTokenExpire;
    }

    public void setClientTokenExpire(Date clientTokenExpire) {
        this.clientTokenExpire = clientTokenExpire;
    }

    public Date getAppTokenExpire() {
        return appTokenExpire;
    }

    public void setAppTokenExpire(Date appTokenExpire) {
        this.appTokenExpire = appTokenExpire;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }
}
