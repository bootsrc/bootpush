package com.appjishu.fpush.server.model;

import io.netty.channel.Channel;

import java.io.Serializable;

public class ChannelInfo implements Serializable {
    private String alias;
    private boolean active;

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}
