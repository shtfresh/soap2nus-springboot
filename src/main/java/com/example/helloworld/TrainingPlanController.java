package com.example.helloworld;

import java.util.concurrent.atomic.AtomicLong;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.db.TrainingPlanTemplateDbDeclaration;
import com.example.Results.Results;
import com.example.TrainingPlan.TrainingPlan;
import com.example.TrainingPlan.TrainingPlanResponse;
import com.example.TrainingPlan.TrainingPlanResponseArray;
import com.example.TrainingPlanTemplate.TrainingPlanTemplate;
import com.example.db.TrainingPlanDbDeclaration;
import com.example.db.TrainingPlanTemplateEnumDbDeclaration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class TrainingPlanController {

    private final AtomicLong counter = new AtomicLong();
    private final String randomTPID = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
    
    static TrainingPlanDbDeclaration edaoTp = new TrainingPlanDbDeclaration();

    @ResponseBody
    @RequestMapping(value="/activetp/{tpOwnerId}",method=RequestMethod.GET)
    public TrainingPlanResponse getPlanActive(@PathVariable String tpOwnerId) {
    	TrainingPlan tp;
        tp = edaoTp.getActivePlan(tpOwnerId);
        if (tp != null) {
        	return new TrainingPlanResponse("00", "success", tp);
        } else {
        	return new TrainingPlanResponse("01", String.format("fail: %s no active tp", tpOwnerId), tp);
        }
    }
    
    @ResponseBody
    @RequestMapping(value="/alltp/{tpOwnerId}",method=RequestMethod.GET)
    public TrainingPlanResponseArray getAllPlan(@PathVariable String tpOwnerId) {
    	List<TrainingPlan> tplist = edaoTp.getAllPlan(tpOwnerId);
    	if (tplist != null) {
    		return new TrainingPlanResponseArray("00", "success", tplist.toArray(new TrainingPlan[0]));
    	} else {
    		return new TrainingPlanResponseArray("01",  String.format("fail: %s no tp", tpOwnerId), null);
    	}
    }
    
    @ResponseBody
    @RequestMapping(value="/tp/{tpId}",method=RequestMethod.GET)
    public TrainingPlanResponse getPlanOnTP(@PathVariable String tpId) {
    	TrainingPlan tp;
        tp = edaoTp.getPlanOnTP(tpId);
        if (tp != null) {
        	return new TrainingPlanResponse("00", "success", tp);
        } else {
        	return new TrainingPlanResponse("01", String.format("fail: %s no such tp", tpId), tp);
        }
    }
    
    @RequestMapping(value="/tp",method=RequestMethod.POST)
    public TrainingPlanResponse createPlan(@RequestBody String tpItem) {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TrainingPlanTemplateEnumDbDeclaration edaoTpEnum = new TrainingPlanTemplateEnumDbDeclaration();
    	JsonObject tptEnumJsonObject = edaoTpEnum.getPlanTemplateEnum();
    	
    	JsonObject tpItemJsonObject = new JsonParser().parse(tpItem).getAsJsonObject();
    	
    	// get tpt
    	TrainingPlanTemplate tpt;
        TrainingPlanTemplateDbDeclaration edaoTpt = new TrainingPlanTemplateDbDeclaration();
    	tpt = edaoTpt.getPlanTemplate(tpItemJsonObject.get("tptId").toString().replace("\"", ""));
    	if (tpt == null) {
    		return new TrainingPlanResponse("01", String.format("fail: %s tpt not found", tpItemJsonObject.get("tptId").toString()), null);
    	}
    	
    	String tpOwnerId = tpItemJsonObject.get("tpOwnerId").toString().replace("\"", "");
    	RestControllerFilter.checkTPConnection(edaoTp);
    	TrainingPlan tp = edaoTp.getActivePlan(tpOwnerId);
    	if (tp != null) {
    		// update status to inactive
    		String sqlString = String.format("UPDATE t_oracle_tp SET tpStatus = \"%s\" where tpId = \"%s\"", 
     				"inactive", tp.gettpId());
    		RestControllerFilter.checkTPConnection(edaoTp);
    		edaoTp.update(sqlString);
    	}

    	int totalminKilometre = 0;
    	int totalmaxKilometre = 0;
    	LocalDate startTime = LocalDate.parse(tpItemJsonObject.get("tpStart").toString().replace("\"", ""));
    	JsonArray weeksjsonArray = new JsonParser().parse(tpt.getWeeks()).getAsJsonArray();
    	for (JsonElement pa : weeksjsonArray) {
    		JsonObject temp = pa.getAsJsonObject();
        	//List<String> listInfo = new ArrayList<String>();
        	Set<Map.Entry<String, JsonElement>> entries = temp.entrySet();
        	for (Map.Entry<String, JsonElement> entry: entries) {
        		//System.out.println(entry.getKey());

        		JsonElement taskItem = entry.getValue().getAsJsonObject().get("tasks");
        		if (taskItem != null) {
    				int minKilometre = 0;
    				int maxKilometre = 0;
        			for (JsonElement pb : taskItem.getAsJsonArray()) {
        				int repeat = 0;
        				repeat = Integer.parseInt(pb.getAsJsonObject().get("Re").toString());
        				
        				//System.out.println(repeat);
        				for (JsonElement pasubTask : pb.getAsJsonObject().get("task").getAsJsonArray()) {
            				int min = 0;
            				int max = 0;
            				int value = 0;
        					JsonObject temp1 = pasubTask.getAsJsonObject();
        			       	Set<Map.Entry<String, JsonElement>> subTaskentries = temp1.entrySet();
        		        	for (Map.Entry<String, JsonElement> subTaskentry: subTaskentries) {
        		        		String task_key = subTaskentry.getKey().toString();
        		  
        		        		value = Integer.parseInt(subTaskentry.getValue().toString());
        		        		if (task_key.equals(new String("kilometre"))) {
        		        			break;
        		        		}
        		
        		        		String tempString = tptEnumJsonObject.get(task_key).toString().replace("\\", "");
        		        		tempString = tempString.substring(1, tempString.length()-1);
        		        		min = Integer.parseInt(new JsonParser().parse(tempString).getAsJsonObject().get("min").toString().replace("\"", ""));
        		        		max = Integer.parseInt(new JsonParser().parse(tempString).getAsJsonObject().get("max").toString().replace("\"", ""));
        		        	}
        		        	if (min != 0 && max != 0) {
        		        		minKilometre = minKilometre + ((1000/min) * value)*repeat;
            		        	maxKilometre = maxKilometre + ((1000/max)* value)*repeat;
        		        	} else {
        		        		minKilometre = minKilometre + value*repeat*1000;
            		        	maxKilometre = maxKilometre + value*repeat*1000;
        		        	}
        				}
    		        	
            			entry.getValue().getAsJsonObject().addProperty("minKilometre", String.valueOf(minKilometre));
            			entry.getValue().getAsJsonObject().addProperty("maxKilometre", String.valueOf(maxKilometre));	
        				entry.getValue().getAsJsonObject().addProperty("status", "planned");
        				
        				totalminKilometre += minKilometre;
        		    	totalmaxKilometre += maxKilometre;
        			}
        		} else {
        			entry.getValue().getAsJsonObject().addProperty("status", "rest");
        		}
				entry.getValue().getAsJsonObject().addProperty("finished", "0");
				entry.getValue().getAsJsonObject().addProperty("date", startTime.toString());
				startTime = startTime.plusDays(1);
        	}
    	}
    	
    	//System.out.println("******************");
    	System.out.println(weeksjsonArray.toString());
    	boolean result;

    	TrainingPlan tpNew = new TrainingPlan(
    			"tp"+randomTPID+counter.incrementAndGet(),
    			tpItemJsonObject.get("tpOwnerId").toString().replace("\"", ""),
    			df.format(new Date()),
    			"",
    			tpItemJsonObject.get("tpOwner").toString().replace("\"", ""),
    			"active",
    			tpItemJsonObject.get("tpStart").toString().replace("\"", ""),
    			tpItemJsonObject.get("tpEnd").toString().replace("\"", ""),
    			tpItemJsonObject.get("tpTargetType").toString().replace("\"", ""),
    			tpItemJsonObject.get("tpTargetMatchid").toString().replace("\"", ""),
    			1,
    			String.valueOf(totalminKilometre),
    			String.valueOf(totalmaxKilometre),
    			tpt.gettptId(),
    			tpt.gettptTile(),
    			tpt.gettptType(),
    			tpt.gettptDescrition(),
    			weeksjsonArray.toString());
    	
    	RestControllerFilter.checkTPConnection(edaoTp);
    	result = edaoTp.add(tpNew);
    	if (result) {
    		return new TrainingPlanResponse("00", "success", tpNew);
    	} else {
    		return new TrainingPlanResponse("01", "fail: add tp", null);
    	}
    }
    
    @ResponseBody
    @RequestMapping(value="/tpstatus",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String updatePlanStatus(@RequestBody String tpItem) {
    	
    	Map<String, String> mapNeedModify = new HashMap<String, String>();
    	JsonObject tpItemJsonObject = new JsonParser().parse(tpItem).getAsJsonObject();
    	JsonObject resultJsonObject = new JsonObject();
    	boolean result;
    	String tpOwnerId = tpItemJsonObject.get("tpOwnerId").toString().replace("\"", "");
    	RestControllerFilter.checkTPConnection(edaoTp);
    	TrainingPlan tp = edaoTp.getActivePlan(tpOwnerId);
    	if (tp == null) {
    		return Results.failReturnJsonObject(String.format("fail: %s no active tp", tpOwnerId)).toString();
    	}
    	String requestDate = tpItemJsonObject.get("tpDate").toString().replace("\"", "");
    	/*
    	LocalDate tpStartTime = LocalDate.parse(tp.gettpStart().replace("\"", ""));
    	LocalDate tpUpdateDate = LocalDate.parse(tpItemJsonObject.get("tpDate").toString().replace("\"", ""));
    	
    	int daysDelta = (int)Duration.between(tpStartTime.atStartOfDay(), tpUpdateDate.atStartOfDay()).toDays();
    	int week = daysDelta/7;
    	String days = "day"+daysDelta%7;
    	System.out.println("week = "+week+" days = "+days);
    	*/
    	
    	JsonArray weeksjsonArray = new JsonParser().parse(tp.getWeeks()).getAsJsonArray();
    	int count = 0;
    	boolean found = false;
    	for (JsonElement pa : weeksjsonArray) {
    		JsonObject temp = pa.getAsJsonObject();
    		
        	Set<Map.Entry<String, JsonElement>> entries = temp.entrySet();
        	for (Map.Entry<String, JsonElement> entry: entries) {
        		if (entry.getValue().getAsJsonObject().get("date").toString().replace("\"", "").equals(requestDate)) {
        			JsonElement taskItem = entry.getValue().getAsJsonObject().get("tasks");
            		resultJsonObject.addProperty("date", requestDate);
    				mapNeedModify.put("day", entry.getKey());
    				mapNeedModify.put("index", Integer.toString(count));
    				int kilometers = Double.valueOf(tpItemJsonObject.get("kilometers").toString().replace("\"", "")).intValue() * 1000;
    				int finished = Double.valueOf(entry.getValue().getAsJsonObject().get("finished").toString().replace("\"", "")).intValue();
    				if (kilometers > finished) {
    					mapNeedModify.put("finished", String.valueOf(kilometers));
    				}
        			if (taskItem != null) {
	    				int minKilometre = Integer.parseInt(entry.getValue().getAsJsonObject().get("minKilometre").toString().replace("\"", ""));
		        		if (kilometers > minKilometre) {
		        			mapNeedModify.put("status", "completed");
		        			resultJsonObject.addProperty("status", "completed");
		        		} else {
		        			mapNeedModify.put("status", "incompleted");
		        			resultJsonObject.addProperty("status", "incompleted");
		        		}
		        	} else {
		        		mapNeedModify.put("status", "rest");
	        			resultJsonObject.addProperty("status", "rest");
	        		}
            		found = true;
            		break;
        		}
        	}
        	if (found) {
        		break;
        	}
        	count = count +1;
        }
    	String sqlString;
    	if (mapNeedModify.containsKey("day")) {
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		if (mapNeedModify.containsKey("finished")) {
        		sqlString = String.format("UPDATE t_oracle_tp SET tpUpdateAt = \"%s\", weeks = JSON_REPLACE(weeks,'$[%d].%s.status', \"%s\", "
            			+ "'$[%d].%s.finished', \"%s\") "
            			+ "where tpOwnerId = \"%s\"", df.format(new Date()),
            			Integer.parseInt(mapNeedModify.get("index")), mapNeedModify.get("day"), mapNeedModify.get("status"), 
            			Integer.parseInt(mapNeedModify.get("index")), mapNeedModify.get("day"), mapNeedModify.get("finished"),
            			tpOwnerId);
        	} else {
        		if (mapNeedModify.get("status").equals(new String("rest"))) {
        			return Results.successReturnJsonObject(resultJsonObject).toString();
        		}
         		sqlString = String.format("UPDATE t_oracle_tp SET tpUpdateAt = \"%s\", weeks = JSON_REPLACE(weeks,'$[%d].%s.status', \"%s\")"
         				+ " where tpOwnerId = \"%s\"", df.format(new Date()),
         				Integer.parseInt(mapNeedModify.get("index")), mapNeedModify.get("day"), mapNeedModify.get("status"), tpOwnerId);
        	}
    	} else {
    		return Results.failReturnJsonObject(String.format("fail: request date %s not found or with no task", requestDate)).toString();
    	}
    	
    	System.out.println(mapNeedModify.toString());
    	RestControllerFilter.checkTPConnection(edaoTp);
    	result = edaoTp.update(sqlString);
        if (result && resultJsonObject.entrySet().size() != 0) {
        	return Results.successReturnJsonObject(resultJsonObject).toString();
        } else {
    		return Results.failReturnJsonObject("fail: update status").toString();
        }
    }
    
    @RequestMapping(value="/tp/{tpId}", method=RequestMethod.DELETE, produces="application/json;charset=UTF-8")
    public String deletePlanTemplate(@PathVariable String tpId) {
    	boolean result;
        result = edaoTp.deletePlan(tpId);
        if (result) {
        	return Results.successReturnJsonObject(null).toString();
        } else {
        	return Results.failReturnJsonObject("fail: delete tp").toString();
        }
    }
    
    @RequestMapping(value="/tp/flushPlanStatus", method=RequestMethod.PUT, produces="application/json;charset=UTF-8")
    public String flushPlanStatusToIncomplete() {
    	boolean result;
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    	LocalDate cTime = LocalDate.parse(df.format(new Date()));
    	cTime = cTime.minusDays(1);
    	String querySql = String.format("select * from t_oracle_tp where tpStatus = \"active\" and JSON_CONTAINS(weeks->'$[*].*',JSON_OBJECT(\"date\", \"%s\"));", 
    			cTime.minusDays(1).toString());
    	List<TrainingPlan> resultTPList =  edaoTp.getAllActivePlan(querySql);
    	if (resultTPList == null) {
    		return Results.failReturnJsonObject("fail: flushPlanStatusToIncomplete, no such tp").toString();
    	}
    	
    	for (TrainingPlan tp : resultTPList) {
    		int count = 0;
    		boolean found = false;
    	   	JsonArray weeksjsonArray = new JsonParser().parse(tp.getWeeks()).getAsJsonArray();
        	for (JsonElement pa : weeksjsonArray) {
        		JsonObject temp = pa.getAsJsonObject();
        		
            	Set<Map.Entry<String, JsonElement>> entries = temp.entrySet();
            	for (Map.Entry<String, JsonElement> entry: entries) {
            		JsonElement taskItem = entry.getValue().getAsJsonObject().get("tasks");
            		if (taskItem != null &&
            				entry.getValue().getAsJsonObject().get("date").toString().replace("\"", "").equals(new String(cTime.toString()))) {
            			if (entry.getValue().getAsJsonObject().get("status").toString().replace("\"", "").equals(new String("planned"))) {
                     		String updateSql = String.format("UPDATE t_oracle_tp SET weeks = JSON_REPLACE(weeks,'$[%d].%s.status', \"%s\")"
                     				+ " where tpOwnerId = \"%s\"", 
                     				count, entry.getKey(), "incomplete", tp.gettpOwnerId());
                     		result = edaoTp.update(updateSql);
                     		if (!result) {
                     			Results.failReturnJsonObject("fail: flushPlanStatusToIncomplete, fail update").toString();
                     		} else {
                     			found = true;
                     			break;
                     		}
            			} else {
            				// ignore
            				found = true;
            				break;
            			}
            		}
            	}
            	if (found) {
            		break;
            	}
            	count = count +1;
            }
    	}
        return Results.successReturnJsonObject(null).toString();
    }
    
	public static String division(int a ,int b){
        String result = "";
        float num =(float)a/b;

        DecimalFormat df = new DecimalFormat("0.0");
        result = df.format(num);

        return result;
    }
}