package com.github.bootsrc.bootpush.server.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class BootpushServerConfig {
    @Value("${bootpush.server.ioThreads:3}")
    private int ioThreads;

    @Value("${bootpush.server.tcpPort:9111}")
    private int tcpPort;

    public int getIoThreads() {
        return ioThreads;
    }

    public int getTcpPort() {
        return tcpPort;
    }
}
