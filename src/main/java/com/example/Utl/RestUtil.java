package com.example.Utl;

import java.time.Instant;
import java.util.List;
import java.util.Map;

import org.apache.tomcat.util.codec.binary.Base64;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.util.StringUtils;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;








@Configuration
public class RestUtil {

	private static final Logger logger = LoggerFactory.getLogger(RestUtil.class);


	   
	@Autowired
	RestTemplate restTemplate;



	public ResponseEntity<String> restGetAuth(String url, String identityDomainId) {

		HttpEntity<String> entity = new HttpEntity<>("parameters", new HttpHeaders());
		return invokeRestTemplete(url, HttpMethod.GET, entity);
	}

	public ResponseEntity<String> restGet(String url) {

		HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
		return invokeRestTemplete(url, HttpMethod.GET, entity);
	}

	public ResponseEntity<String> restGet(String url,String baseAuth) {

		byte[] plainCredsBytes = baseAuth.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		
		HttpEntity<String> entity = new HttpEntity<>(headers);
		return invokeRestTemplete(url, HttpMethod.GET, entity);
	}
	
	public ResponseEntity<String> restPut(String url) {

		HttpEntity<String> entity = new HttpEntity<>(new HttpHeaders());
		return invokeRestTemplete(url, HttpMethod.PUT, entity);
	}

	public ResponseEntity<String> restPost(String url, Object body) {

		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		HttpEntity<Object> entity = new HttpEntity<>(body, new HttpHeaders());
		return invokeRestTemplete(url, HttpMethod.POST, entity);
	}

	public ResponseEntity<String> restPost(String url, Object body,String baseAuth) {

		byte[] plainCredsBytes = baseAuth.getBytes();
		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
		String base64Creds = new String(base64CredsBytes);

		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", "Basic " + base64Creds);
		
	
		

		
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		HttpEntity<Object> entity = new HttpEntity<>(body, headers);

		System.out.println(entity.getBody());
		
		return invokeRestTemplete(url, HttpMethod.POST, entity);
	}
	
	public ResponseEntity<String> restDelete(String url, Map<String, String> allRequestParams) {

		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		HttpEntity<Map<String, String>> entity = new HttpEntity<>(allRequestParams, new HttpHeaders());
		return invokeRestTemplete(url, HttpMethod.DELETE, entity);

	}

	private <T> ResponseEntity<String> invokeRestTemplete(String url, HttpMethod method, HttpEntity<T> entity) {

		long startTime = Instant.now().toEpochMilli();
		logger.info("######Method-invokeRestTemplete start######");

		ResponseEntity<String> response = null;
		try {
			logger.info("######Oracle Cloud Url:[" + url + "]#######");
			logger.info("######Oracle Cloud Method:[" + method + "]#######");
			logger.info("######Invoke Oracle Cloud Api Start ...#######");
			response = restTemplate.exchange(url, method, entity, String.class);
			logger.info("######Invoke Oracle Cloud Api Normal End ...#######");
		} catch (HttpClientErrorException e) {
			logger.error("######Invoke Oracle Cloud Api Abormal End ...#######");
			return handleException(e);
		} catch (HttpServerErrorException e) {
			logger.error("######Invoke Oracle Cloud Api Abormal End ...#######");
			return handleException(e);
		} finally {
			long endTime = Instant.now().toEpochMilli();
			logger.info("######程序运行时间： " + (endTime - startTime) + "毫秒######");
			logger.info("######Method-invokeRestTemplete end######");
		}

		return response;

	}

	public ResponseEntity<String> handleException(HttpClientErrorException e) {

		String body = e.getResponseBodyAsString();
		if (StringUtils.isEmpty(body)) {
			body = e.getStatusText();
		}

		HttpHeaders headers = e.getResponseHeaders();
		HttpStatus statusCode = e.getStatusCode();

		ResponseEntity<String> response = new ResponseEntity<>(body, headers, statusCode);
		return response;
	}

	public ResponseEntity<String> handleException(HttpServerErrorException e) {

		String body = e.getResponseBodyAsString();
		if (StringUtils.isEmpty(body)) {
			body = e.getStatusText();
		}

		HttpHeaders headers = e.getResponseHeaders();
		HttpStatus statusCode = e.getStatusCode();

		ResponseEntity<String> response = new ResponseEntity<>(body, headers, statusCode);
		return response;
	}

	public ResponseEntity<String> restGetWithCookieAuth(String url, List<String> cookieList) {

		HttpHeaders headers = new HttpHeaders();
		headers.put(HttpHeaders.COOKIE, cookieList);
		HttpEntity<String> entity = new HttpEntity<>(headers);
		return invokeRestTemplete(url, HttpMethod.GET, entity);
	}

	public ResponseEntity<String> restPutWithCookieAuth(String url, List<String> cookieList, Object body) {

		HttpHeaders headers = new HttpHeaders();
		headers.put(HttpHeaders.COOKIE, cookieList);
		restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
		HttpEntity<Object> entity = new HttpEntity<>(body, headers);
		return invokeRestTemplete(url, HttpMethod.PUT, entity);
	}



}
