package com.example.helloworld;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.google.common.collect.Lists;

@Configuration
@Component
public class RestURLFilterConfig {
	
    @Autowired
    private AuthenticationFilter filter;
    
    @Autowired
    private RestControllerFilter filter1;

    @Bean
    public FilterRegistrationBean filterRegistrationBean() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(filter);

        //设置（模糊）匹配的url
        List<String> urlPatterns = Lists.newArrayList();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);

        registrationBean.setOrder(2);
        registrationBean.setEnabled(true);

        return registrationBean;
    }
    
    @Bean
    public FilterRegistrationBean filterRegistrationBean1() {
        FilterRegistrationBean registrationBean = new FilterRegistrationBean();
        registrationBean.setFilter(filter1);

        //设置（模糊）匹配的url
        List<String> urlPatterns = Lists.newArrayList();
        urlPatterns.add("/*");
        registrationBean.setUrlPatterns(urlPatterns);

        registrationBean.setOrder(1);
        registrationBean.setEnabled(true);

        return registrationBean;
    }
}