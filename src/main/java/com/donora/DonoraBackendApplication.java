package com.donora;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EntityScan("com.donora.entity")
@SpringBootApplication(scanBasePackages = "com.donora")
public class DonoraBackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(DonoraBackendApplication.class, args);
	}
}