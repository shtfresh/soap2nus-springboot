package com.example.helloworld;

import java.util.concurrent.atomic.AtomicLong;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.db.TrainingPlanTemplateDbDeclaration;
import com.example.TrainingPlan.TrainingPlan;
import com.example.TrainingPlanTemplate.TrainingPlanTemplate;
import com.example.db.DBConnectionMysql;
import com.example.db.TrainingPlanDbDeclaration;
import com.example.db.TrainingPlanTemplateEnumDbDeclaration;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class TrainingPlanController {

    private final AtomicLong counter = new AtomicLong();
    
    TrainingPlanDbDeclaration edaoTp = new TrainingPlanDbDeclaration();

    @ResponseBody
    @RequestMapping(value="/activetp/{tpOwnerId}",method=RequestMethod.GET)
    public TrainingPlan getPlanActive(@PathVariable String tpOwnerId) {
    	checkConnection(edaoTp);
    	TrainingPlan tp;
        tp = edaoTp.getActivePlan(tpOwnerId);
        return tp;
    }
    
    @ResponseBody
    @RequestMapping(value="/alltp/{tpOwnerId}",method=RequestMethod.GET)
    public TrainingPlan[] getAllPlan(@PathVariable String tpOwnerId) {
    	checkConnection(edaoTp);
    	return edaoTp.getAllPlan(tpOwnerId).toArray(new TrainingPlan[0]);
    }
    
    @ResponseBody
    @RequestMapping(value="/tp/{tpId}",method=RequestMethod.GET)
    public TrainingPlan getPlanOnTP(@PathVariable String tpId) {
    	checkConnection(edaoTp);
    	TrainingPlan tp;
        tp = edaoTp.getPlanOnTP(tpId);
        //System.out.println(tpt.getWeeks());
        return tp;
    }
    
    @RequestMapping(value="/tp",method=RequestMethod.POST)
    public TrainingPlan createPlan(@RequestBody String tpItem) {
    	checkConnection(edaoTp);
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        TrainingPlanTemplateEnumDbDeclaration edaoTpEnum = new TrainingPlanTemplateEnumDbDeclaration();
    	JsonObject tptEnumJsonObject = edaoTpEnum.getPlanTemplateEnum();
    	
    	JsonObject tpItemJsonObject = new JsonParser().parse(tpItem).getAsJsonObject();
    	tpItemJsonObject.addProperty("tpId", "tpXXX"+counter.incrementAndGet());
    	tpItemJsonObject.addProperty("tpPublishedAt", df.format(new Date()));
    	tpItemJsonObject.addProperty("tpUpdateAt", "");
    	tpItemJsonObject.addProperty("tpVersionNo", 1);
    	tpItemJsonObject.addProperty("tpStatus", "active");
    	
    	// get tpt
    	TrainingPlanTemplate tpt;
    	//System.out.println(tpItemJsonObject.get("tptId"));
        TrainingPlanTemplateDbDeclaration edaoTpt = new TrainingPlanTemplateDbDeclaration();
    	tpt = edaoTpt.getPlanTemplate(tpItemJsonObject.get("tptId").toString().replace("\"", ""));
    	if (tpt == null) {
    		//return "Fail: "+tpItemJsonObject.get("tptId").toString()+" Not Found in TrainingPlan Template!";
    		return null;
    	}
    	
    	String tpOwnerId = tpItemJsonObject.get("tpOwnerId").toString().replace("\"", "");
    	TrainingPlan tp = edaoTp.getActivePlan(tpOwnerId);
    	if (tp != null) {
    		// update status to inactive
    		String sqlString = String.format("UPDATE t_oracle_tp SET tpStatus = \"%s\" where tpId = \"%s\"", 
     				"inactive", tp.gettpId());
    		edaoTp.update(sqlString);
    	}

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
        				//for (Map.Entry<String, JsonElement> entrysubTask: pb.getAsJsonObject().get("task").getAsJsonArray().get(0).getAsJsonObject().entrySet()) {
        				    //System.out.println("Key: "+entrysubTask.getKey()+" Value: "+entrysubTask.getValue());
        		        		String task_key = subTaskentry.getKey().toString();
        		  
        		        		value = Integer.parseInt(subTaskentry.getValue().toString());
        		        		if (task_key.equals(new String("kilometre"))) {
        		        			break;
        		        		}
        		
        		        		String tempString = tptEnumJsonObject.get(task_key).toString().replace("\\", "");
        		        		tempString = tempString.substring(1, tempString.length()-1);
        		        		//System.out.println(tempString);
        		        		min = Integer.parseInt(new JsonParser().parse(tempString).getAsJsonObject().get("min").toString().replace("\"", ""));
        		        		max = Integer.parseInt(new JsonParser().parse(tempString).getAsJsonObject().get("max").toString().replace("\"", ""));
        		        		
        		        		//System.out.println(new JsonParser().parse(tptEnumJsonObject.get(entrysubTask.getKey()).toString()).getAsJsonObject().get("max"));
        		        	}
        		        	if (min != 0 && max != 0) {
        		        		minKilometre = minKilometre + ((1000/min) * value)*repeat;
            		        	maxKilometre = maxKilometre + ((1000/max)* value)*repeat;
        		        	} else {
        		        		minKilometre = minKilometre + value*repeat*1000;
            		        	maxKilometre = maxKilometre + value*repeat*1000;
        		        	}
        				}
    		        	
    		        	//System.out.println(pb.getAsJsonObject().get("task"));

        				//System.out.println("minKilometre = "+division(minKilometre, 1000));
            			//System.out.println("maxKilometre = "+division(maxKilometre, 1000));
            			entry.getValue().getAsJsonObject().addProperty("minKilometre", division(minKilometre, 1000));
            			entry.getValue().getAsJsonObject().addProperty("maxKilometre", division(maxKilometre, 1000));
        				entry.getValue().getAsJsonObject().addProperty("finished", "0");
        					
        				entry.getValue().getAsJsonObject().addProperty("status", "planed");
        				//System.out.println(startTime.plusDays(1).toString());
        				//tpItemJsonObject.get("tpStart").toString()
        				//System.out.println(entry.getValue().getAsJsonObject().toString());
        			}
        		
        		}
				entry.getValue().getAsJsonObject().addProperty("date", startTime.toString());
				startTime = startTime.plusDays(1);
        	}
    	}
    	
    	//System.out.println("******************");
    	System.out.println(weeksjsonArray.toString());
    	boolean result;
        
    	TrainingPlan tpNew = new TrainingPlan(
    			tpItemJsonObject.get("tpId").toString().replace("\"", ""),
    			tpItemJsonObject.get("tpOwnerId").toString().replace("\"", ""),
    			tpItemJsonObject.get("tpPublishedAt").toString().replace("\"", ""),
    			tpItemJsonObject.get("tpUpdateAt").toString().replace("\"", ""),
    			tpItemJsonObject.get("tpOwner").toString().replace("\"", ""),
    			tpItemJsonObject.get("tpStatus").toString().replace("\"", ""),
    			tpItemJsonObject.get("tpStart").toString().replace("\"", ""),
    			tpItemJsonObject.get("tpEnd").toString().replace("\"", ""),
    			tpItemJsonObject.get("tpTargetType").toString().replace("\"", ""),
    			tpItemJsonObject.get("tpTargetMatchid").toString().replace("\"", ""),
    			tpItemJsonObject.get("tpVersionNo").getAsInt(),
    			tpt.gettptId(),
    			tpt.gettptTile(),
    			tpt.gettptType(),
    			tpt.gettptDescrition(),
    			weeksjsonArray.toString());
    
    	result = edaoTp.add(tpNew);
    	if (result) {
    		return tpNew;
    	} else {
    		return null;
    	}
    }
    
    @ResponseBody
    @RequestMapping(value="/tpstatus",method=RequestMethod.POST,produces="application/json;charset=UTF-8")
    public String updatePlanStatus(@RequestBody String tpItem) {
    	
    	Map<String, String> mapNeedModify = new HashMap<String, String>();
    	checkConnection(edaoTp);
    	
    	JsonObject tpItemJsonObject = new JsonParser().parse(tpItem).getAsJsonObject();
    	JsonObject returnJsonObject = new JsonObject();
    	boolean result;
    	String tpOwnerId = tpItemJsonObject.get("tpOwnerId").toString().replace("\"", "");
    	//TrainingPlan tp = edaoTp.getAllPlan(tpOwnerId).get(0);
    	TrainingPlan tp = edaoTp.getActivePlan(tpOwnerId);
    	if (tp == null) {
    		return String.format("{\"return\": \"ignored\", \"reason\": \" %s Active TP not found\"}", tpOwnerId);
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
    	for (JsonElement pa : weeksjsonArray) {
    		JsonObject temp = pa.getAsJsonObject();
    		
        	Set<Map.Entry<String, JsonElement>> entries = temp.entrySet();
        	for (Map.Entry<String, JsonElement> entry: entries) {
        		JsonElement taskItem = entry.getValue().getAsJsonObject().get("tasks");
        		if (taskItem != null) {
        			//System.out.println(entry.getValue().getAsJsonObject().get("date").toString().replace("\"", ""));
        			//System.out.println(tpItemJsonObject.get("tpDate").toString());
        			if (entry.getValue().getAsJsonObject().get("date").toString().replace("\"", "").equals(requestDate)) {
        				returnJsonObject.addProperty("date", requestDate);
        				mapNeedModify.put("day", entry.getKey());
        				mapNeedModify.put("index", Integer.toString(count));
        				float kilometers = Float.parseFloat((tpItemJsonObject.get("kilometers").toString().replace("\"", "")));
	    				float minKilometre = Float.parseFloat(entry.getValue().getAsJsonObject().get("minKilometre").toString().replace("\"", ""));
	    				//float maxKilometre = Float.parseFloat(entry.getValue().getAsJsonObject().get("maxKilometre").toString().replace("\"", ""));
	    				//System.out.println("kilometers = "+kilometers+" minKilometre = "+minKilometre);
	    				if (kilometers > Float.parseFloat(entry.getValue().getAsJsonObject().get("finished").toString().replace("\"", ""))) {
	    					entry.getValue().getAsJsonObject().addProperty("finished", Float.toString(kilometers));
	    					mapNeedModify.put("finished", Float.toString(kilometers));
	    				}
		        		if (kilometers > minKilometre) {
		        			//System.out.println("change status");
		        			//System.out.println(entry.getValue().getAsJsonObject().get("status").toString());
		        			entry.getValue().getAsJsonObject().addProperty("status", "completed");
		        			mapNeedModify.put("status", "completed");
		        			returnJsonObject.addProperty("status", "completed");
		        		} else {
		        			entry.getValue().getAsJsonObject().addProperty("status", "incompleted");
		        			mapNeedModify.put("status", "incompleted");
		        			returnJsonObject.addProperty("status", "incompleted");
		        		}
		        	}
        		}
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
         		sqlString = String.format("UPDATE t_oracle_tp SET tpUpdateAt = \"%s\", weeks = JSON_REPLACE(weeks,'$[%d].%s.status', \"%s\")"
         				+ " where tpOwnerId = \"%s\"", df.format(new Date()),
         				Integer.parseInt(mapNeedModify.get("index")), mapNeedModify.get("day"), mapNeedModify.get("status"), tpOwnerId);
        	}
    	} else {
    		return String.format("{\"return\": \"failed\", \"reason\": \"Request Date %s not found or with no task\"}", requestDate);
    	}
    	
    	//System.out.println(weeksjsonArray.toString());
    	System.out.println(mapNeedModify.toString());
    	//result = edaoTpEnum.update(tpOwnerId, weeksjsonArray.toString());
    	TrainingPlanTemplateEnumDbDeclaration edaoTpEnum = new TrainingPlanTemplateEnumDbDeclaration();
    	result = edaoTpEnum.updateNew(sqlString);
        if (result && returnJsonObject.entrySet().size() != 0) {
        	return returnJsonObject.toString();
        } else {
        	return "{\"return\": \"failed\"}";
        }
    }
    
    @RequestMapping(value="/tp/{tpId}", method=RequestMethod.DELETE)
    public String deletePlanTemplate(@PathVariable String tpId) {
    	checkConnection(edaoTp);
    	boolean result;
        result = edaoTp.deletePlan(tpId);
        if (result) {
        	return "success";
        } else {
        	return "fail";
        }
       
    }
    
	public static String division(int a ,int b){
        String result = "";
        float num =(float)a/b;

        DecimalFormat df = new DecimalFormat("0.0");

        result = df.format(num);

        return result;

    }
	
	public static void checkConnection(TrainingPlanDbDeclaration tp) {
    	try {
			if(tp.conn.isClosed()) {
				tp.conn = DBConnectionMysql.getInstance().getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}