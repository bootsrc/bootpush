package com.appjishu.fpush.client.singleton;

import com.appjishu.fpush.core.model.MsgUser;

public class CurrentUser {
    private static CurrentUser ourInstance = new CurrentUser();

    public static CurrentUser getInstance() {
        return ourInstance;
    }

    private CurrentUser() {
    }

    private MsgUser info;

    public static MsgUser getInfo() {
        return ourInstance.info;
    }

    public static void setInfo(MsgUser info) {
        ourInstance.info = info;
    }
}
