package com.example.helloworld;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import com.google.common.hash.Hashing;
import com.google.common.io.CharStreams;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

//@Component
public class AuthenticationFilter implements Filter {
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        System.out.println("########## Initiating Authentication filter ##########");
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        
        long currentTimeStamp = (long) (System.currentTimeMillis());
        long requestTimeStamp = 0;
        if (request.getHeader("negotiatetimestamp") != null) {
        	requestTimeStamp = Long.parseLong(request.getHeader("negotiatetimestamp"));
        }
        
        //System.out.println("tptCategory" + request.getParameter("tptCategory"));
        //System.out.println("tptType: " + request.getParameter("tptType"));
        /*request.getParameterMap().forEach((key, value) -> {
        	System.out.println(String.format("Header %s = %s", key, value.toString()));
		});*/
        
        if (currentTimeStamp - requestTimeStamp > 30000) {
        	//System.out.println(String.format("delta: %d", currentTimeStamp - requestTimeStamp));
        	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Request - TimeStamp");
        	return;
        }
        
        String queryParameter = null;
        String key = "3f67e6821de6893e8cb3135d946aaa5f57d4f0f57ba2c6306048208a360628f3";
        
        String method = request.getMethod();   
        String verifyString = null;
        if (method.equals(new String("GET"))) {
        	if (request.getQueryString() != null) {
        		queryParameter = request.getRequestURI()+"?"+ request.getQueryString();
        	} else {
        		queryParameter = request.getRequestURI();
        	}
        	verifyString = String.format("<%s>-<%s>-<%s>", queryParameter, String.valueOf(requestTimeStamp), key);
        } else if (method.equals(new String("PUT")) || method.equals(new String("POST"))) {
        	String payload = CharStreams.toString(request.getReader());
        	payload = payload.replaceAll("\r|\n|\t|\\s*", "");
        	verifyString = String.format("<%s>-<%s>-<%s>", payload, String.valueOf(requestTimeStamp), key);
        	
        } else {
        	response.sendError(HttpServletResponse.SC_FORBIDDEN, method+" FORBIDDEN");
        	return;
        }
        
        System.out.println(verifyString);
        String sha256hex = Hashing.sha256()
        		  .hashString(verifyString, StandardCharsets.UTF_8)
        		  .toString();
        System.out.println(sha256hex);
        if (sha256hex.equals(request.getHeader("digest"))) {
            //call next filter in the filter chain
            filterChain.doFilter(request, response);
        } else {
        	response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized Request - digest");
        }
    }

    @Override
    public void destroy() {
        // TODO:
    }
}