package com.zjutennis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.retry.annotation.EnableRetry;

@SpringBootApplication
@EnableJpaAuditing
@EnableRetry
@EnableCaching
public class ZjutennisApplication {
	public static void main(String[] args) {
		SpringApplication.run(ZjutennisApplication.class, args);
	}
}
