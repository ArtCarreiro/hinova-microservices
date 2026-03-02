package com.example.sign_service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SignServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SignServiceApplication.class, args);
	}

}
