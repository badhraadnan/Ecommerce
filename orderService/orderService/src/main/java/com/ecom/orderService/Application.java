package com.ecom.orderService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
//@EnableAsync
@EntityScan("com.ecom.CommonEntity")
@EnableJpaRepositories(basePackages = {
		"com.ecom.commonRepository.repo"
})
@ComponentScan(basePackages = {
		"com.ecom.orderService",
		"com.ecom.commonRepository.dao",
		"com.ecom.commonRepository.repo",
		"com.ecom.CommonEntity"
})
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
