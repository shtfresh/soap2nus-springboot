package com.example.AITrainer;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;


@Component
//@Order(1)
//@WebFilter(urlPatterns = {"/*"})
public class RestControllerFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("########## Initiating RestController filter ##########");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String requestURI = request.getRequestURI();
        if (requestURI.contains("tptemplates")) {
        	if (request.getMethod().equals(new String("GET")) &&
        			requestURI.startsWith("/tptemplates/")) {
        		response.setHeader("Access-Control-Allow-Origin", "*");
        	}
        } else if (requestURI.contains("tp")) {
        	if (request.getMethod().equals(new String("GET")) &&
        			requestURI.contains("activetp")) {
        		response.setHeader("Access-Control-Allow-Origin", "*");
        	}
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {
        // TODO:
    }
}