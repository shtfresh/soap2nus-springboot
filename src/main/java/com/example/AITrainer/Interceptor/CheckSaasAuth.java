package com.example.AITrainer.Interceptor;

import java.util.Base64;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class CheckSaasAuth implements HandlerInterceptor {

	final Base64.Decoder decoder = Base64.getDecoder();
	
	public CheckSaasAuth() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
try {

		String baseAuth = request.getHeader("Authorization");	
		String  baseAuth_1[] = baseAuth.split(" ");
		String authCode = baseAuth_1[1];
		System.out.println(authCode);
		String userName_passWord =  new String(decoder.decode(authCode));
		if(authCode.equals("se-hub:abc123")) {
			
			int httpCode = 401;
	        response.sendError(httpCode,"userName or passWord is wrong");
	        
			return false;
			
		}else {
			return true;
		}

}catch(Exception e) {
	
	int httpCode = 401;
    response.sendError(httpCode,"userName or passWord is wrong");
    
	return false;
}

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

}
