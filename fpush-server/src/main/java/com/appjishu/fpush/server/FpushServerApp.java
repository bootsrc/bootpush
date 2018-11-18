package com.appjishu.fpush.server;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.appjishu.fpush.server.app.FpushServer;

@SpringBootApplication
public class FpushServerApp implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(FpushServerApp.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		FpushServer.start();
	}
}
