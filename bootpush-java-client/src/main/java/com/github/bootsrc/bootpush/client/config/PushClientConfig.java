package com.github.bootsrc.bootpush.client.config;


import org.springframework.stereotype.Component;

@Component
public class PushClientConfig {
    private String regId = "reg-id-001";

    public String getRegId() {
        return regId;
    }
}
