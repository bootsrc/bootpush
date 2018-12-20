package com.appjishu.fpush_demo.util;

import android.content.ContextWrapper;
import android.content.SharedPreferences;
import android.util.Log;

import com.appjishu.fpush_demo.constant.SharedPreferencesKey;
import com.appjishu.fpush_demo.constant.SharedPreferencesName;
import com.appjishu.fpush_demo.model.NetConfig;

public class SharedPreferencesUtil {
    public static void storeNetConstant(ContextWrapper contextWrapper, NetConfig netConfig) {
        SharedPreferences sp = contextWrapper.getSharedPreferences(SharedPreferencesName.NET_CONSTANT, 0);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(SharedPreferencesKey.PUSH_HOST, netConfig.getPushHost());
        editor.putInt(SharedPreferencesKey.PUSH_PORT, netConfig.getPushPort());
        editor.putInt(SharedPreferencesKey.HTTP_PORT, netConfig.getHttpPort());
        editor.commit();
    }

    public static NetConfig queryNetConstant(ContextWrapper contextWrapper) {
        if (contextWrapper == null) {
            return null;
        }
        SharedPreferences sp = contextWrapper.getSharedPreferences(SharedPreferencesName.NET_CONSTANT, 0);
        NetConfig netConfig = new NetConfig();
        String pushHost = sp.getString(SharedPreferencesKey.PUSH_HOST, "");
        int pushPort = sp.getInt(SharedPreferencesKey.PUSH_PORT, 0);
        int httpPort = sp.getInt(SharedPreferencesKey.HTTP_PORT, 0);
        if (pushPort == 0 || httpPort == 0) {
            return null;
        }
        netConfig.setPushHost(pushHost);
        netConfig.setPushPort(pushPort);
        netConfig.setHttpPort(httpPort);
        return netConfig;
    }
}
