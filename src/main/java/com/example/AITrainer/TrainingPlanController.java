package com.example.AITrainer;

import java.util.concurrent.atomic.AtomicLong;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Results.Results;
import com.example.TrainingPlan.TrainingPlan;
import com.example.TrainingPlan.TrainingPlanResponse;
import com.example.TrainingPlan.TrainingPlanResponseArray;
import com.example.TrainingPlanTemplate.TrainingPlanTemplate;
import com.example.service.TPService;
import com.example.service.TPTEnumService;
import com.example.service.TPTService;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class TrainingPlanController {

    private final AtomicLong counter = new AtomicLong();
    private final String randomTPID = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
    
    @Autowired
    TPService tpService;
    
    @Autowired
    TPTService tptService;
    
    @Autowired
    TPTEnumService tptEnumService;

    @ResponseBody
    @RequestMapping(value="/activetp/{tpOwnerId}",method=RequestMethod.GET)
    public TrainingPlanResponse getPlanActive(@PathVariable String tpOwnerId) {
    	TrainingPlan tp;
        tp = tpService.getActivePlan(tpOwnerId);
        if (tp != null) {
        	return new TrainingPlanResponse("00", "success", tp);
        } else {
        	return new TrainingPlanResponse("01", String.format("fail: %s no active tp", tpOwnerId), tp);
        }
    }
    
    @ResponseBody
    @RequestMapping(value="/alltp/{tpOwnerId}",method=RequestMethod.GET)
    public TrainingPlanResponseArray getAllPlan(@PathVariable String tpOwnerId) {
    	List<TrainingPlan> tplist = tpService.getAllPlan(tpOwnerId);
    	if (tplist.size() > 0) {
    		return new TrainingPlanResponseArray("00", "success", tplist.toArray(new TrainingPlan[0]));
    	} else {
    		return new TrainingPlanResponseArray("01",  String.format("fail: %s no tp", tpOwnerId), null);
    	}
    }
    
    @ResponseBody
    @RequestMapping(value="/tp/{tpId}",method=RequestMethod.GET)
    public TrainingPlanResponse getPlanOnTP(@PathVariable String tpId) {
    	TrainingPlan tp;
        tp = tpService.getPlanOnTP(tpId);
        if (tp != null) {
        	return new TrainingPlanResponse("00", "success", tp);
        } else {
        	return new TrainingPlanResponse("01", String.format("fail: %s no such tp", tpId), tp);
        }
    }
    
    @RequestMapping(value="/tp",method=RequestMethod.POST)
    public TrainingPlanResponse createPlan(@RequestBody String tpItem) {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    	JsonObject tpItemJsonObject = null;
    	try {
    		tpItemJsonObject = new JsonParser().parse(tpItem).getAsJsonObject();
    	} catch (Exception e) {
    		return new TrainingPlanResponse("01", "fail: invalid options", null);
    	}
    	
    	// get Enum
    	JsonObject tptEnumJsonObject = tptEnumService.getPlanTemplateEnum();

    	// get tpt
    	TrainingPlanTemplate tpt;
    	tpt = tptService.getPlanTemplate(tpItemJsonObject.get("tptId").toString().replace("\"", ""));
    	if (tpt == null) {
    		return new TrainingPlanResponse("01", String.format("fail: %s tpt not found", tpItemJsonObject.get("tptId").toString()), null);
    	}
    	System.out.println("111111111111111111111111111111111111111111111111111111111111");
    	String tpOwnerId = tpItemJsonObject.get("tpOwnerId").toString().replace("\"", "");
    	TrainingPlan tp = tpService.getActivePlan(tpOwnerId);
    	if (tp != null) {
    		// update status to inactive
    		try {
				tpService.updateTPStatus("inactive", tp.gettpId());
			} catch (SQLException e) {
				return new TrainingPlanResponse("01", "fail: add tp update status to inactive", null);
			}
    	}

    	int totalminKilometre = 0;
    	int totalmaxKilometre = 0;
    	int totalDays = 0;
    	int totalTrainingDays = 0;
    	LocalDate startTime = LocalDate.parse(tpItemJsonObject.get("tpStart").toString().replace("\"", ""));
    	JsonArray weeksjsonArray = new JsonParser().parse(tpt.getWeeks()).getAsJsonArray();
      	System.out.println("222222222222222222222222222222222222222222222222222222222222");
    	for (JsonElement pa : weeksjsonArray) {
    		JsonObject temp = pa.getAsJsonObject();
          	System.out.println("333333333333333333333333333333");
        	Set<Map.Entry<String, JsonElement>> entries = temp.entrySet();

        	for (Map.Entry<String, JsonElement> entry: entries) {
        		JsonElement taskItem = entry.getValue().getAsJsonObject().get("tasks");
              	System.out.println("444444444444444444444444444444444444444");
        		if (taskItem != null) {
    				int minKilometre = 0;
    				int maxKilometre = 0;
        			for (JsonElement pb : taskItem.getAsJsonArray()) {
        				int repeat = 0;
        				repeat = Integer.parseInt(pb.getAsJsonObject().get("Re").toString());
        	          	System.out.println("555555555555555555555555555555555");
        				for (JsonElement pasubTask : pb.getAsJsonObject().get("task").getAsJsonArray()) {
        		          	System.out.println("666666666666666666666666666666666666666666666");
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
        		        		System.out.println("task_key:"+task_key);
        		        		System.out.println("tptEnumJsonObject:"+tptEnumJsonObject.toString());
        		        		
        		        		String tempString = tptEnumJsonObject.get(task_key).toString().replace("\\", "");
        		           		System.out.println("tempString----:"+tempString);
        		        		tempString = tempString.substring(1, tempString.length()-1);
        		        		System.out.println("tempString:"+tempString);
        		        		System.out.println("task_key:"+task_key);
        		        		System.out.println("????????????????????????????????????????????????????");
        		        		min = Integer.parseInt(new JsonParser().parse(tempString).getAsJsonObject().get("min").toString().replace("\"", ""));
        		        		System.out.println("7777777777777777777777777777777777777777777");
        		        		max = Integer.parseInt(new JsonParser().parse(tempString).getAsJsonObject().get("max").toString().replace("\"", ""));
        		        		System.out.println("8888888888888888888888888888888888888888888888888");
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
            			System.out.println("9999999999999999999999999999999999999");
            			entry.getValue().getAsJsonObject().addProperty("maxKilometre", String.valueOf(maxKilometre));	
            			System.out.println("10101010101010101010101");
        				entry.getValue().getAsJsonObject().addProperty("status", "planned");
        				System.out.println("hhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhhh");
        				
        				totalminKilometre += minKilometre;
        		    	totalmaxKilometre += maxKilometre;
        			}
        			totalTrainingDays += 1;
        			entry.getValue().getAsJsonObject().addProperty("completeStatus", 1);
        		} else {
        			entry.getValue().getAsJsonObject().addProperty("status", "rest");
        			entry.getValue().getAsJsonObject().addProperty("completeStatus", 5);
        			System.out.println("yyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyyy");
        		}
				entry.getValue().getAsJsonObject().addProperty("finished", "0");
				entry.getValue().getAsJsonObject().addProperty("date", startTime.toString());
				startTime = startTime.plusDays(1);
				totalDays += 1;
				System.out.println("kkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkkk");
        	}
    	}
    	
    	//System.out.println(weeksjsonArray.toString());
    	TrainingPlan tpNew = null;
    	try {
    		
    	   	System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzz:"+tpItemJsonObject.toString());
    	   	System.out.println("mmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmmm:"+tpt.toString());
    	   	
    	   	
        	String tpId_="tp"+randomTPID+counter.incrementAndGet();
        	System.out.println("111");
        	String tpOwnerId_=tpItemJsonObject.get("tpOwnerId").toString().replace("\"", "");
        	System.out.println("222");
        	String tpPublishedAt_=df.format(new Date());
        	System.out.println("333");
        	String tpUpdateAt_="";
      
        	String tpOwner_=tpItemJsonObject.get("tpOwner").toString().replace("\"", "");
          	System.out.println("444");
        	String tpStatus_="active";
        	String tpStart_=tpItemJsonObject.get("tpStart").toString().replace("\"", "");
        	System.out.println("555");
        	String tpEnd_=tpItemJsonObject.get("tpEnd").toString().replace("\"", "");
        	System.out.println("6666");
        	String tpTargetType_=tpItemJsonObject.get("tpTargetType").toString().replace("\"", "");
        	
        	System.out.println("7777");
        	String tpTargetMatchid_=tpItemJsonObject.get("tpTargetMatchid").toString().replace("\"", "");
        	
        	System.out.println("888");
        	String tpTargetMatchName_=tpItemJsonObject.get("tpTargetMatchName").toString().replace("\"", "");
        	System.out.println("9999");
        	String tpVersionNo_=Integer.toString(1);
        	System.out.println("1010101");
        	String minKilometre_=String.valueOf(totalminKilometre);
        	System.out.println("1111111111111111111111111");
        	String maxKilometre_=Integer.toString(totalDays - totalTrainingDays);
        	System.out.println("1212121212");
        	String totalDays_=tpt.gettptId();
        	System.out.println("13131313131313");

        	String tptTile_=tpt.gettptTile();
        	System.out.println("14141414141");
        	String tptType_=tpt.gettptType();
        	System.out.println("1515151515");
        	String tptDescrition_=tpt.gettptDescrition();
        	System.out.println("161616161616");
        	String weeks_=weeksjsonArray.toString(); 
        	System.out.println("171717171717");
    	   	
	    	tpNew = new TrainingPlan(
	    			tpId_,
	    			tpOwnerId_,
	    			tpPublishedAt_,
	    			tpUpdateAt_,
	    			tpOwner_,
	    			tpStatus_,
	    			tpStart_,
	    			tpEnd_,
	    			tpTargetType_,
	    			tpTargetMatchid_,
	    			tpTargetMatchName_,
	    			tpVersionNo_,
	    			minKilometre_,
	    			minKilometre_,
	    			maxKilometre_,
	    			totalDays_,
	    			tptTile_,
	    			tptType_,
	    			tptDescrition_,
	    			weeks_);
	    	
//	    	tpNew = new TrainingPlan(
//	    			"tp"+randomTPID+counter.incrementAndGet(),
//	    			tpItemJsonObject.get("tpOwnerId").toString().replace("\"", ""),
//	    			df.format(new Date()),
//	    			"",
//	    			tpItemJsonObject.get("tpOwner").toString().replace("\"", ""),
//	    			"active",
//	    			tpItemJsonObject.get("tpStart").toString().replace("\"", ""),
//	    			tpItemJsonObject.get("tpEnd").toString().replace("\"", ""),
//	    			tpItemJsonObject.get("tpTargetType").toString().replace("\"", ""),
//	    			tpItemJsonObject.get("tpTargetMatchid").toString().replace("\"", ""),
//	    			tpItemJsonObject.get("tpTargetMatchName").toString().replace("\"", ""),
//					Integer.toString(1),
//	    			String.valueOf(totalminKilometre),
//	    			String.valueOf(totalmaxKilometre),
//					Integer.toString(totalDays - totalTrainingDays),
//	    			tpt.gettptId(),
//	    			tpt.gettptTile(),
//	    			tpt.gettptType(),
//	    			tpt.gettptDescrition(),
//	    			weeksjsonArray.toString());
	    	System.out.println("ttttttttttttttttttttttttttttttttttttttttt");
    	} catch (Exception e) {
    		System.out.println(e);
    		return new TrainingPlanResponse("01", "fail: invalid options", null);
    	}
    	
    	try {
    		tpService.addTP(tpNew);
    	} catch (Exception e) {
    		return new TrainingPlanResponse("01", "fail: add tp", null);
    	}
    	return new TrainingPlanResponse("00", "success", tpNew);
    	/*
    	if (result) {
    		return new TrainingPlanResponse("00", "success", tpNew);
    	} else {
    		return new TrainingPlanResponse("01", "fail: add tp", null);
    	}*/
    }
    
    @ResponseBody
    @RequestMapping(value="/tpstatus",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String updatePlanStatus(@RequestBody String tpItem) {
    	Map<String, String> mapNeedModify = new HashMap<String, String>();
    	JsonObject tpItemJsonObject = null;
    	try {
    		tpItemJsonObject = new JsonParser().parse(tpItem).getAsJsonObject();
    	} catch (Exception e) {
    		return Results.failReturnJsonObject("fail: invalid options").toString();
    	}
    	JsonObject resultJsonObject = new JsonObject();
    	//boolean result;
    	String tpOwnerId = tpItemJsonObject.get("tpOwnerId").toString().replace("\"", "");
    	TrainingPlan tp = tpService.getActivePlan(tpOwnerId);
    	if (tp == null) {
    		return Results.failReturnJsonObject(String.format("fail: %s no active tp", tpOwnerId)).toString();
    	}
    	String requestDate = tpItemJsonObject.get("tpDate").toString().replace("\"", "");
    	
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
    				int kilometers = Double.valueOf(tpItemJsonObject.get("kilometers").toString().replace("\"", "")).intValue();
    				int finished = Double.valueOf(entry.getValue().getAsJsonObject().get("finished").toString().replace("\"", "")).intValue();
    				if (kilometers > finished) {
    					mapNeedModify.put("finished", String.valueOf(kilometers));
    					entry.getValue().getAsJsonObject().addProperty("finished", String.valueOf(kilometers));
    				}
        			if (taskItem != null) {
	    				int minKilometre = Integer.parseInt(entry.getValue().getAsJsonObject().get("minKilometre").toString().replace("\"", ""));
	    				int maxKilometre = Integer.parseInt(entry.getValue().getAsJsonObject().get("maxKilometre").toString().replace("\"", ""));
		        		if (kilometers > minKilometre) {
		        			mapNeedModify.put("status", "completed");
		        			resultJsonObject.addProperty("status", "completed");
		        			entry.getValue().getAsJsonObject().addProperty("status", "completed");
		        			if (kilometers > maxKilometre) {
		        				entry.getValue().getAsJsonObject().addProperty("completeStatus", 4);
		        			} else {
		        				entry.getValue().getAsJsonObject().addProperty("completeStatus", 3);
		        			}
		        		} else {
		        			mapNeedModify.put("status", "incompleted");
		        			resultJsonObject.addProperty("status", "incompleted");
		        			entry.getValue().getAsJsonObject().addProperty("status", "incompleted");
		        			entry.getValue().getAsJsonObject().addProperty("completeStatus", 2);
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
    	//String sqlString;
    	if (mapNeedModify.containsKey("day")) {
    		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    		mapNeedModify.put("tpUpdateAt", df.format(new Date()));
    		mapNeedModify.put("weeks", weeksjsonArray.toString());
    		
    		/*if (mapNeedModify.containsKey("finished")) {
        		sqlString = String.format("UPDATE t_oracle_tp SET tpUpdateAt = \"%s\", weeks = JSON_REPLACE(weeks,'$[%d].%s.status', \"%s\", "
            			+ "'$[%d].%s.finished', \"%s\") "
            			+ "where tpOwnerId = \"%s\"", df.format(new Date()),
            			Integer.parseInt(mapNeedModify.get("index")), mapNeedModify.get("day"), mapNeedModify.get("status"), 
            			Integer.parseInt(mapNeedModify.get("index")), mapNeedModify.get("day"), mapNeedModify.get("finished"),
            			tpOwnerId);
        	} else {
         		sqlString = String.format("UPDATE t_oracle_tp SET tpUpdateAt = \"%s\", weeks = JSON_REPLACE(weeks,'$[%d].%s.status', \"%s\")"
         				+ " where tpOwnerId = \"%s\"", df.format(new Date()),
         				Integer.parseInt(mapNeedModify.get("index")), mapNeedModify.get("day"), mapNeedModify.get("status"), tpOwnerId);
        	}*/
    		if (mapNeedModify.get("status").equals(new String("rest"))) {
    			return Results.successReturnJsonObject(resultJsonObject).toString();
    		}
    		
    		try {
				tpService.updateTP(tpOwnerId, mapNeedModify);
			} catch (SQLException e) {
				return Results.failReturnJsonObject("fail: update status").toString();
			}
    	} else {
    		return Results.failReturnJsonObject(String.format("fail: request date %s not found or with no task", requestDate)).toString();
    	}
    	
    	/*System.out.println(mapNeedModify.toString());
    	try {
			tpService.updateTP(tpOwnerId, mapNeedModify);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			return Results.failReturnJsonObject("fail: update status").toString();
		}*/
    	return Results.successReturnJsonObject(resultJsonObject).toString();
    	/*
        if (result && resultJsonObject.entrySet().size() != 0) {
        	return Results.successReturnJsonObject(resultJsonObject).toString();
        } else {
    		return Results.failReturnJsonObject("fail: update status").toString();
        }*/
    }
    
    @RequestMapping(value="/tp/{tpId}", method=RequestMethod.DELETE, produces="application/json;charset=UTF-8")
    public String deletePlanTemplate(@PathVariable String tpId) {
    	tpService.deleteTP(tpId);
        //return Results.failReturnJsonObject("fail: delete tp").toString();

        return Results.successReturnJsonObject(null).toString();
    }
    
    @RequestMapping(value="/tp/flushPlanStatus", method=RequestMethod.PUT, produces="application/json;charset=UTF-8")
    public String flushPlanStatusToIncomplete() {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	LocalDate cTime = LocalDate.parse(df.format(new Date()));
    	cTime = cTime.minusDays(1);
    	/*
    	String querySql = String.format("select * from t_oracle_tp where tpStatus = \"active\" and JSON_CONTAINS(weeks->'$[*].*',JSON_OBJECT(\"date\", \"%s\"));", 
    			cTime.toString());
    	List<TrainingPlan> resultTPList =  edatpServiceoTp.getAllActivePlan(querySql);
    	*/
    	List<TrainingPlan> resultTPList =  tpService.getAllActivePlan(cTime.toString());
    	if (resultTPList == null) {
    		return Results.failReturnJsonObject("fail: flushPlanStatusToIncomplete, no such tp").toString();
    	}
    	
    	for (TrainingPlan tp : resultTPList) {
    		int count = 0;
    		boolean found = false;
    	   	JsonArray weeksjsonArray = new JsonParser().parse(tp.getWeeks()).getAsJsonArray();
    	   	Map<String, String> mapNeedModify = new HashMap<String, String>();

        	for (JsonElement pa : weeksjsonArray) {
        		JsonObject temp = pa.getAsJsonObject();
            	Set<Map.Entry<String, JsonElement>> entries = temp.entrySet();
            	for (Map.Entry<String, JsonElement> entry: entries) {
            		JsonElement taskItem = entry.getValue().getAsJsonObject().get("tasks");
            		if (taskItem != null &&
            				entry.getValue().getAsJsonObject().get("date").toString().replace("\"", "").equals(new String(cTime.toString()))) {
            			if (entry.getValue().getAsJsonObject().get("status").toString().replace("\"", "").equals(new String("planned"))) {
            				/*
                     		String updateSql = String.format("UPDATE t_oracle_tp SET weeks = JSON_REPLACE(weeks,'$[%d].%s.status', \"%s\")"
                     				+ " where tpOwnerId = \"%s\"", 
                     				count, entry.getKey(), "incomplete", tp.gettpOwnerId());*/
            				entry.getValue().getAsJsonObject().addProperty("status", "incomplete");
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
	    	mapNeedModify.put("tpUpdateAt", df1.format(new Date()));
	   		mapNeedModify.put("weeks", weeksjsonArray.toString());
	    	try {
				tpService.updateTP(tp.gettpOwnerId(), mapNeedModify);
			} catch (SQLException e) {
				return Results.failReturnJsonObject("fail: flushPlanStatusToIncomplete, fail update").toString();
			}
        }
        return Results.successReturnJsonObject(null).toString();	
    }
}
    	