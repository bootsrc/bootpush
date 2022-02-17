package com.github.bootsrc.bootpush.api.enums;

public enum RegisterState {
    SUCCESS("000", "成功");

    private final String code;
    private final String text;

    private RegisterState(String code, String text) {
        this.code = code;
        this.text = text;
    }

    public String getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
