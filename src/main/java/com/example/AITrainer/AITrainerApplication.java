package com.example.AITrainer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

@SpringBootApplication
@RestController
@EnableTransactionManagement
@EnableEurekaClient
@EnableFeignClients(basePackages= {"com.example.client"})
@ServletComponentScan
public class AITrainerApplication {
	public static void main(String[] args) {
		SpringApplication.run(AITrainerApplication.class, args);
	}
}
