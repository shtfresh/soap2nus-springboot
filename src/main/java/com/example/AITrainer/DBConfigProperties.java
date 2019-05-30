/* Copyright © 2016 Oracle and/or its affiliates. All rights reserved. */

package com.example.AITrainer;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;


@Component
@ConfigurationProperties
@PropertySource({"classpath:application.properties"})
public class DBConfigProperties {
	 @Value("${db.driver}")
	 private String driver;
	 @Value("${db.url}")
	 private String url;
	 @Value("${db.username}")
	 private String username;
	 @Value("${db.password}")
	 private String password;

	 public String getDriver() {
	        return driver;
	 }

	 public String getUrl() {
	        return url;
	 }

	 public String getUsername() {
	        return username;
	 }

	 public String getPassword() {
	        return password;
	 }
}

