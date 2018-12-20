package com.appjishu.fpush_demo.model;

public class NetConfig {
    private String pushHost;
    private int pushPort;
    private int httpPort;

    public String getPushHost() {
        return pushHost;
    }

    public void setPushHost(String pushHost) {
        this.pushHost = pushHost;
    }

    public int getPushPort() {
        return pushPort;
    }

    public void setPushPort(int pushPort) {
        this.pushPort = pushPort;
    }

    public int getHttpPort() {
        return httpPort;
    }

    public void setHttpPort(int httpPort) {
        this.httpPort = httpPort;
    }
}
