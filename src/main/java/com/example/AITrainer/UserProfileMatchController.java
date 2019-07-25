package com.example.AITrainer;

import java.text.SimpleDateFormat;

import java.util.*;

import com.example.UserMatch.UserMatch;
import com.example.UserMatch.UserMatchResponseArray;
import com.example.mapper.UserMatchEnrollMapper;
import com.example.service.UserMatchEnrollService;
import com.example.service.UserMatchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.example.Results.Results;
import com.example.TrainingPlan.TrainingPlan;
import com.example.service.TPService;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class UserProfileMatchController {
	
    @Autowired
	UserMatchService userMatchService;

    @Autowired
    UserMatchEnrollService userMatchEnrollService;
    
    @Autowired
    TPService tpService;

	@RequestMapping(value="/marathonMatch",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
	public UserMatchResponseArray getMarathonMatch() {
		List<UserMatch> matchList = userMatchService.getMatchList();
		if (matchList.size() > 0) {
			return new UserMatchResponseArray("00", "success", matchList.toArray(new UserMatch[0]));
		} else {
			return new UserMatchResponseArray("01", String.format("fail: no match list"), null);
		}
	}

	@RequestMapping(value="/registerMatch",method=RequestMethod.POST,produces="application/json;charset=UTF-8" )
	public String enrollMatch(@RequestBody String matchRecord) {
		Gson gson = new Gson();
		Map<String, Object> map = new HashMap<String, Object>();
		map = gson.fromJson(matchRecord, map.getClass());
		try {
			userMatchEnrollService.enrolledMatch(map);
		} catch (Exception e) {
			return Results.failReturnJsonObject(String.format("fail: registerMatch")).toString();
		}
		return Results.successReturnJsonObject(null).toString();
	}

	@RequestMapping(value="/registrationList",method=RequestMethod.GET,produces="application/json;charset=UTF-8" )
	public String getUserMatchRecord() {
		JsonArray registeredMatchListjsonArray = new JsonArray();
		List<Map<String, Object>> returnUserMatch = null;
		returnUserMatch = userMatchEnrollService.getAllEnrolledMatchList();

		registeredMatchListjsonArray = new JsonParser().parse(new Gson().toJson(returnUserMatch)).getAsJsonArray();
		return Results.successReturnJsonObjectArray(registeredMatchListjsonArray).toString();
	}

    @RequestMapping(value="/loadUserContext/{userId}",method=RequestMethod.GET,produces="application/json;charset=UTF-8")
    public String loadUserContext(@PathVariable String userId) {
    	JsonObject resultJsonObject = new JsonObject();
    	Gson gson = new Gson();
    	Map<String , Object> map = null;

    	// userInfo
    	try {
    		map = userMatchService.getUserInfoByUserId(userId);
    	} catch (Exception e) {
    		return Results.failReturnJsonObject(String.format("fail: getUserInfoByUserId inaccessible", userId)).toString();
    	}

    	if (map == null) {
			return Results.failReturnJsonObject(String.format("fail: userID %s does not exist", userId)).toString();
		}
		JsonObject userInfoJsonObject = new JsonParser().parse(gson.toJson(map)).getAsJsonObject();
    	resultJsonObject.add("userProfile", userInfoJsonObject);
    	//System.out.println(userInfoJsonObject.toString());
    	
    	// registeredMatchList
    	JsonArray registeredMatchListjsonArray = new JsonArray();
        List<Map<String, Object>> returnEnrolledMatch = null;

        returnEnrolledMatch = userMatchEnrollService.queryEnrolledMatchList(userId);
        if(returnEnrolledMatch.size() > 0) {
            for (Map<String, Object> matchMap: returnEnrolledMatch) {
                for (String key: matchMap.keySet()) {
                    UserMatch returnMatch;
                    //System.out.println(key+":"+matchMap.get(key));
                    try {
                        returnMatch = userMatchService.queryReportMatchList(matchMap.get(key).toString());
                    } catch (Exception e) {
                        return Results.failReturnJsonObject(String.format("fail: queryReportMatchList inaccessible", userId)).toString();
                    }
                    if (returnMatch == null) {
                        return Results.failReturnJsonObject(String.format("queryReportMatchList Fail: userID: %s " +
								"doesn't enroll match %s", userId, matchMap.get(key).toString())).toString();
                    }

                    //registeredMatchListjsonArray = new JsonParser().parse(new Gson().toJson(returnMatchList)).getAsJsonArray();
                    JsonObject matchJsonObject = new JsonParser().parse(gson.toJson(returnMatch)).getAsJsonObject();
                    registeredMatchListjsonArray.add(matchJsonObject);
                }
            }
        }
        resultJsonObject.add("registeredMatchList", registeredMatchListjsonArray);

    	System.out.println(registeredMatchListjsonArray.toString());

    	// getStatus for a training plan
    	JsonObject statusJsonObject = new JsonObject();
    	JsonArray weeksjsonArray = null;
    	TrainingPlan trainingPlan = tpService.getActivePlan(userId);
    	if (trainingPlan == null) {
    		System.out.println(String.format("Fail: userID: %s does not exist in tp", userId));
    	} else {
	    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
	    	String requestDate = df.format(new Date());
	    	System.out.println(requestDate);
	    	weeksjsonArray = new JsonParser().parse(trainingPlan.getWeeks()).getAsJsonArray();
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
    	}

    	resultJsonObject.add("isTrainingDoneToday", statusJsonObject);
    	
    	// activeTrainingPlan
    	// trainingPlan.clearWeeks();
    	JsonObject trainingPlanJsonObject = new JsonObject();
    	if (trainingPlan != null) {
        	trainingPlanJsonObject = new JsonParser().parse(gson.toJson(trainingPlan)).getAsJsonObject();
        	trainingPlanJsonObject.add("weeks", weeksjsonArray);
        	System.out.println(trainingPlanJsonObject.toString());
    	}

    	resultJsonObject.add("activeTrainingPlan", trainingPlanJsonObject);
    	return Results.successReturnJsonObject(resultJsonObject).toString();
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

    @RequestMapping(value="/userProfile/{userId}",method=RequestMethod.PUT,produces="application/json;charset=UTF-8" )
    public String updateUserInfo(@PathVariable String userId, @RequestBody String userInfo) {
    	JsonObject upUserInfoJsonObject = new JsonParser().parse(userInfo).getAsJsonObject();
    	Map<String , Object> map = null;
    
    	try {
    		userMatchService.updateUser(Integer.valueOf(userId),
        			upUserInfoJsonObject.get("age").toString().replace("\"", ""),
        			Integer.valueOf(upUserInfoJsonObject.get("gender").toString().replace("\"", "")),
        			upUserInfoJsonObject.get("height").toString().replace("\"", ""),
        			upUserInfoJsonObject.get("weight").toString().replace("\"", ""));
    	} catch (Exception e) {
    		return Results.failReturnJsonObject("fail: update user profile").toString();
    	}
		return Results.successReturnJsonObject(null).toString();
    }
}