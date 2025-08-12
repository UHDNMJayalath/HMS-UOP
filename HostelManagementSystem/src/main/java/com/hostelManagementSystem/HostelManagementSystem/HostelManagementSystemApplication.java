package com.hostelManagementSystem.HostelManagementSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = "com.hostelManagementSystem.HostelManagementSystem.entity")
public class HostelManagementSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(HostelManagementSystemApplication.class, args);
	}
}

