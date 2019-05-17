package com.example.helloworld;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.TrainingPlan.TrainingPlan;
import com.example.client.ReadyGoClient;
import com.example.db.DBConnectionMysql;
import com.example.db.TrainingPlanTemplateDbDeclaration;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import com.example.helloworld.TrainingPlanController;

@RestController
public class UserProfileMatchController {
	
    @Autowired
    ReadyGoClient readygoClient;

    @RequestMapping(value="/loadUserContext/{userId}",method=RequestMethod.GET)
    public String loadUserContext(@PathVariable String userId) {
    	JsonObject returnJsonObject = new JsonObject();
    	
    	// getStatus for a training plan
    	TrainingPlan trainingPlan = new TrainingPlanController().getPlanActive(userId);
    	if (trainingPlan == null) {
    		return String.format("Fail: userID: %s does not exist", userId);
    	}
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	String requestDate = df.format(new Date());
    	System.out.println(requestDate);
    	JsonObject statusJsonObject = new JsonObject();
    	JsonArray weeksjsonArray = new JsonParser().parse(trainingPlan.getWeeks()).getAsJsonArray();
    	for (JsonElement pa : weeksjsonArray) {
    		JsonObject temp = pa.getAsJsonObject();
    		
        	Set<Map.Entry<String, JsonElement>> entries = temp.entrySet();
        	for (Map.Entry<String, JsonElement> entry: entries) {
        		String currDate = entry.getValue().getAsJsonObject().get("date").toString().replace("\"", "");
        		if (currDate.equals(requestDate)) {
        			statusJsonObject.addProperty("date", requestDate);
        			if (entry.getValue().getAsJsonObject().has("status")) {
        				statusJsonObject.addProperty("status", entry.getValue().getAsJsonObject().get("status").toString().replace("\"", ""));
        			} else {
        				statusJsonObject.addProperty("status", "completed");
        			}
        			break;
        		}
        	}
        }

    	returnJsonObject.add("isTrainingDoneToday", statusJsonObject);
    	System.out.println(statusJsonObject.toString());
    	
    	// activeTrainingPlan
    	Gson gson = new Gson();
    	trainingPlan.clearWeeks();
    	JsonObject trainingPlanJsonObject = new JsonParser().parse(gson.toJson(trainingPlan)).getAsJsonObject();
    	trainingPlanJsonObject.add("weeks", weeksjsonArray);
    	returnJsonObject.add("activeTrainingPlan", trainingPlanJsonObject);
    	//returnJsonObject.add("activeTrainingPlan", weeksjsonArray);
    	System.out.println(trainingPlanJsonObject.toString());

    	// userInfo
    	Map<String , Object> map = readygoClient.getUserInfoByUserId(userId);
    	if (!map.get("code").equals(new String("00"))) {
    		return String.format("getUserInfoByUserId Fail: userID: %s does not exist", userId);
    	}
    	JsonObject userInfoJsonObject = new JsonParser().parse(gson.toJson(map.get("results"))).getAsJsonObject();
    	returnJsonObject.add("userProfile", userInfoJsonObject);
    	System.out.println(userInfoJsonObject.toString());
    	
    	// registeredMatchList
    	Map<String , Object> map1 = readygoClient.queryReportMatchList(userId);
    	if (!map1.get("code").equals(new String("00"))) {
    		return String.format("queryReportMatchList Fail: userID: %s does not exist", userId);
    	}
    	JsonArray registeredMatchListjsonArray = new JsonParser().parse(gson.toJson(map1.get("results"))).getAsJsonArray();
    	returnJsonObject.add("registeredMatchList", registeredMatchListjsonArray);
    	System.out.println(registeredMatchListjsonArray.toString());
   
    	return returnJsonObject.toString();
    }
    
    /*
    @RequestMapping(value="/match/{matchId}",method=RequestMethod.GET)
    public String getAmatch(@PathVariable String matchId) {
    	Map<String , Object> map = readygoClient.queryReportMatchDetail(userId, matchId);
    	if (map.get("code").equals(new String("00"))) {
    		return (String) map.get("msg");
    	} else {
    		return "Failed";
    	}		
    }*/
    
    @RequestMapping(value="/userProfile/{userId}",method=RequestMethod.PUT)
    public String updateUserInfo(@PathVariable String userId, @RequestBody String userInfo) {
    	JsonObject upUserInfoJsonObject = new JsonParser().parse(userInfo).getAsJsonObject();
    	Map<String , Object> map = readygoClient.updateUser(Integer.valueOf(userId), 
    			upUserInfoJsonObject.get("age").toString().replace("\"", ""),
    			Integer.valueOf(upUserInfoJsonObject.get("gender").toString().replace("\"", "")), 
    			upUserInfoJsonObject.get("height").toString().replace("\"", ""), 
    			upUserInfoJsonObject.get("weight").toString().replace("\"", ""));
    	if (map.get("code").equals(new String("00"))) {
    		return (String) map.get("msg");
    	} else {
    		return "Failed";
    	}
    }
}