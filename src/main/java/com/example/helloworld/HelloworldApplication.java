package com.example.helloworld;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
@EnableTransactionManagement
@EnableEurekaClient
@ServletComponentScan
public class HelloworldApplication {
    @RequestMapping("/")
    public String index(){
        return "Hello World!";
    }

	public static void main(String[] args) {
		SpringApplication.run(HelloworldApplication.class, args);
	}
}
