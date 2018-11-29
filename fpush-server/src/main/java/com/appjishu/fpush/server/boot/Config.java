package com.appjishu.fpush.server.boot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.appjishu.fpush.server.util.SnowflakeWorker;

@Configuration
public class Config {
    @Bean
    public SnowflakeWorker snowflakeWorker() {
        SnowflakeWorker idWorker = new SnowflakeWorker(0, 0);
        return idWorker;
    }
}
