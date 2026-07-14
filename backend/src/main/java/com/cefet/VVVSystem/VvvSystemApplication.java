package com.cefet.VVVSystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class VvvSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(VvvSystemApplication.class, args);
	}

}

//test