package com.github.bootsrc.bootpush.api.enums;

public enum MessageType {
    // 利用构造函数传参
    LOGIN_REQUEST((byte) 0x1, "LOGIN_REQUEST"),
    LOGIN_RESPONSE((byte) 0x2, "LOGIN_RESPONSE"),

    REGISTER_REQUEST((byte) 0x3, "REGISTER_REQUEST"),
    REGISTER_RESPONSE((byte) 0x4, "REGISTER_RESPONSE"),

    HEARTBEAT_REQUEST((byte) 0x5, "HEARTBEAT_REQUEST"),
    HEARTBEAT_RESPONSE((byte) 0x6, "HEARTBEAT_RESPONSE"),

    PUSH((byte) 0x7, "PUSH"),
    PUSH_CONFIRM((byte) 0x8, "PUSH_CONFIRM");

    // 定义私有变量

    private final int value;
    private final String text;

    // 构造函数，枚举类型只能为私有

    private MessageType(int value, String text) {
        this.value = value;
        this.text = text;
    }

    public int getValue() {
        return value;
    }

    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return "MessageType{" +
                "value=" + value +
                ", text='" + text + '\'' +
                '}';
    }
}
