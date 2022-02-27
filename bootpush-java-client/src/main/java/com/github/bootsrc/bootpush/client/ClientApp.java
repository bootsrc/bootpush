package com.github.bootsrc.bootpush.client;

import com.github.bootsrc.bootpush.client.biz.Client;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ClientApp implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(ClientApp.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        Client.start();
    }
}
