package com.appjishu.fpush.core.model;

import java.io.Serializable;

public class MsgUser implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 用别名来标识一个用户。一个设备只能绑定一个别名
     * 推送消息给某个用户，一般用这个用户的应用系统中的userId设置成alias，适用于单用户单设备在线聊天的应用场景
     */
    private String alias;
    /**
     * 推送消息给某个用户，一般用这个用户的应用系统中的userId设置成alias，适用于单用户多设备同时在线聊天的应用场景
     */
    private String account;
    private long appId;
    private String appToken;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public long getAppId() {
        return appId;
    }

    public void setAppId(long appId) {
        this.appId = appId;
    }

    public String getAppToken() {
        return appToken;
    }

    public void setAppToken(String appToken) {
        this.appToken = appToken;
    }
}
