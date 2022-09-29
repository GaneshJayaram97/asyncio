package com.example.asyncio;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AsyncioApplication {

	public static void main(String[] args) {
		SpringApplication.run(AsyncioApplication.class, args);
		DeviceUpgradeTest.main(args);
	}
}
