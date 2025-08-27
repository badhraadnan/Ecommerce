package com.ecom.apiGateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EntityScan("com.ecom.CommonEntity")
@EnableJpaRepositories("com.ecom.commonRepository.repo")
@ComponentScan(basePackages ={
        "com.ecom.apiGateway",
        "com.ecom.commonRepository.dao",
        "com.ecom.commonRepository.repo",
        "com.ecom.CommonEntity"
})
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}
