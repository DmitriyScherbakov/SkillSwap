package com.example.skillswap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Configuration;

@SpringBootApplication
public class SkillSwapApplication {

	public static void main(String[] args) {
		SpringApplication.run(SkillSwapApplication.class, args);
	}

}
