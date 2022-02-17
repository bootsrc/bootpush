package com.github.bootsrc.bootpush.api.enums;

public enum MessageType {
    // 利用构造函数传参
    LOGIN_REQ((byte) 0x1),
    LOGIN_RESP((byte) 0x2),

    REGISTER_REQ((byte) 0x3),
    REGISTER_RESP((byte) 0x4),

    HEARTBEAT_REQ((byte) 0x5),
    HEARTBEAT_RESP((byte) 0x6),

    PUSH((byte) 0x7),
    PUSH_CONFIRM((byte) 0x8);

    // 定义私有变量

    private final int value;

    // 构造函数，枚举类型只能为私有

    private MessageType(int value) {

        this.value = value;

    }

    @Override
    public String toString() {

        return String.valueOf(this.value);

    }

    public int value() {
        return value;
    }
}
