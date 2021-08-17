package com.userregistration.main;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableEurekaClient
@ComponentScan("com.userregistration.main")
@EnableJpaRepositories("com.userregistration.main.repository")
public class UserRegistrationBookStoreApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserRegistrationBookStoreApplication.class, args);
	}

}
