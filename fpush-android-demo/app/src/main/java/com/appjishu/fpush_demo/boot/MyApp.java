package com.appjishu.fpush_demo.boot;

import android.app.Application;

public class MyApp extends Application {
    public static MyApp getInstance() {
        return instance;
    }

    private static MyApp instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
