package com.appjishu.fpush.client;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.appjishu.fpush.client.app.FpushClient;

@SpringBootApplication
public class FpushClientApp implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(FpushClientApp.class, args);
	}
	
	@Override
	public void run(String... args) throws Exception {
		FpushClient.start();
	}
}
