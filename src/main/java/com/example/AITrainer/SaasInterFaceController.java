package com.example.AITrainer;

import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSONObject;
import com.example.TrainingPlan.TrainingPlan;
import com.example.TrainingPlan.TrainingPlanResponse;
import com.example.Utl.RestUtil;
import com.example.mapper.SaasInformationMapper;
import com.example.model.ChangePassWordBean;



@RestController
@RequestMapping("/getSaas")
public class SaasInterFaceController {

	@Autowired
	RestTemplate restTemplate;
	
	@Autowired
	RestUtil restUtil;	
	
	@Autowired
	SaasInformationMapper saasInfomationMapper;
	
	public SaasInterFaceController() {
		// TODO Auto-generated constructor stub
	}
	
	
	@RequestMapping(value="/changePassWord",method=RequestMethod.POST)
    public ResponseEntity<HttpStatus> changeSaasInfomation(@RequestBody ChangePassWordBean changePassWordBean) {
		
		System.out.println(changePassWordBean.getUserName());
		System.out.println(changePassWordBean.getPassWord());
		System.out.println(changePassWordBean.getDomain());
		saasInfomationMapper.deleteSaasInfomation();
		saasInfomationMapper.insertSysRole(changePassWordBean);
		
		
		ResponseEntity responseEntity = new ResponseEntity(HttpStatus.ACCEPTED);
		return responseEntity;
	}
	
	
	@RequestMapping(value="/getEmpByName",method=RequestMethod.GET)
    public ResponseEntity<String> getEmpByName(String userName) {
		System.out.println(userName);
		ChangePassWordBean changePassWordBean = saasInfomationMapper.getSaasInfomation();
		
		String url=changePassWordBean.getDomain()+"/hcmRestApi/resources/latest/emps?q=UserName="+userName;
		System.out.println(url);
		//String url ="https://adc3-zinw-fa-ext.oracledemos.com:443/hcmRestApi/resources/latest/emps?q=UserName="+name;
		
		String plainCreds = changePassWordBean.getUserName()+":"+changePassWordBean.getPassWord();
		System.out.println(plainCreds);
	//	String plainCreds = "John.dunbar:xvK78793";
//		byte[] plainCredsBytes = plainCreds.getBytes();
//		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
//		String base64Creds = new String(base64CredsBytes);
//
//		HttpHeaders headers = new HttpHeaders();
//		headers.add("Authorization", "Basic " + base64Creds);
		
		return restUtil.restGet(url,plainCreds);

    }
	
	
	@RequestMapping(value="/getCrmRestApi",method=RequestMethod.GET)
    public ResponseEntity<String> getCrmRestApi() {

		ChangePassWordBean changePassWordBean = saasInfomationMapper.getSaasInfomation();
		
		String url=changePassWordBean.getDomain()+"/crmRestApi/resources/latest/InsurancePolicies_c";


		String plainCreds = changePassWordBean.getUserName()+":"+changePassWordBean.getPassWord();


		
		return restUtil.restGet(url,plainCreds);

    }
	
	@RequestMapping(value="/postCrmRestApi",method=RequestMethod.POST)
    public ResponseEntity<String> postCrmRestApi(@RequestBody JSONObject jsonobject) {
		
		
		

		//PostCrmRestApiBean postCrmRestApiBean= (PostCrmRestApiBean)JSONObject.toJavaObject(jsonobject, PostCrmRestApiBean.class);
		

		ChangePassWordBean changePassWordBean = saasInfomationMapper.getSaasInfomation();
		

		String url=changePassWordBean.getDomain()+"/crmRestApi/resources/latest/InsurancePolicies_c";


		String plainCreds = changePassWordBean.getUserName()+":"+changePassWordBean.getPassWord();

		return restUtil.restPost(url, jsonobject, plainCreds);
		
		

    }
	

}
