package com.github.bootsrc.bootpush.server;

import com.github.bootsrc.bootpush.server.boot.AppContextHolder;
import com.github.bootsrc.bootpush.server.boot.PushServer;
import com.github.bootsrc.bootpush.server.config.BootpushServerConfig;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ServerApp implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ServerApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        BootpushServerConfig config = AppContextHolder.getAppContext().getBean(BootpushServerConfig.class);
        PushServer pushServer = new PushServer(config);
        pushServer.start();
    }
}
