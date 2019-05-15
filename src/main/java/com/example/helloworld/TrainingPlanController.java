package com.example.helloworld;

import java.util.concurrent.atomic.AtomicLong;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.db.TrainingPlanTemplateDbDeclaration;
import com.example.TrainingPlan.TrainingPlan;
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
    
    TrainingPlanTemplateDbDeclaration edaoTpt = new TrainingPlanTemplateDbDeclaration();
    
    TrainingPlanDbDeclaration edaoTp = new TrainingPlanDbDeclaration();
    TrainingPlanTemplateEnumDbDeclaration edaoTpEnum = new TrainingPlanTemplateEnumDbDeclaration();

    @RequestMapping(value="/activetp/{tpOwnerId}",method=RequestMethod.GET)
    public TrainingPlan getPlanActive(@PathVariable String tpOwnerId) {
    	TrainingPlan tp;
        tp = edaoTp.getActivePlan(tpOwnerId);
        //System.out.println(tpt.getWeeks());
        return tp;
    }
    
    @RequestMapping(value="/alltp/{tpOwnerId}",method=RequestMethod.GET)
    public TrainingPlan[] getAllPlan(@PathVariable String tpOwnerId) {
    	return edaoTp.getAllPlan(tpOwnerId).toArray(new TrainingPlan[0]);
    }
    
    @RequestMapping(value="/tp/{tpId}",method=RequestMethod.GET)
    public TrainingPlan getPlanOnTP(@PathVariable String tpId) {
    	TrainingPlan tp;
        tp = edaoTp.getPlanOnTP(tpId);
        //System.out.println(tpt.getWeeks());
        return tp;
    }
    
    @RequestMapping(value="/tp",method=RequestMethod.POST)
    public String createPlan(@RequestBody String tpItem) {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
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
    	tpt = edaoTpt.getPlanTemplate(tpItemJsonObject.get("tptId").toString().replace("\"", ""));
    	
    	if (tpt == null) {
    		return "Fail: "+tpItemJsonObject.get("tptId").toString()+" Not Found in TrainingPlan Template!";
    	}
    	//System.out.println(tptEnumJsonObject);
    	//return tptEnumJsonObject.get("tpStatus").toString();
    	
    	//System.out.println(tpt.gettptId());
    	//System.out.println(tpt.getWeeks());
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
        					
        				startTime = startTime.plusDays(1);
        				entry.getValue().getAsJsonObject().addProperty("date", startTime.toString());
        				entry.getValue().getAsJsonObject().addProperty("status", "planed");
        				//System.out.println(startTime.plusDays(1).toString());
        				//tpItemJsonObject.get("tpStart").toString()
        				//System.out.println(entry.getValue().getAsJsonObject().toString());
        			}
        		
        		}
        	}
    	}
    	
    	//System.out.println("******************");
    	System.out.println(weeksjsonArray.toString());
    	boolean result;
        
    	result = edaoTp.add(new TrainingPlan(
    			tpItemJsonObject.get("tpId").toString(),
    			tpItemJsonObject.get("tpOwnerId").toString(),
    			tpItemJsonObject.get("tpPublishedAt").toString(),
    			tpItemJsonObject.get("tpUpdateAt").toString(),
    			tpItemJsonObject.get("tpOwner").toString(),
    			tpItemJsonObject.get("tpStatus").toString(),
    			tpItemJsonObject.get("tpStart").toString(),
    			tpItemJsonObject.get("tpEnd").toString(),
    			tpItemJsonObject.get("tpTargetType").toString(),
    			tpItemJsonObject.get("tpTargetMatchid").toString(),
    			tpItemJsonObject.get("tpVersionNo").getAsInt(),
    			tpt.gettptId(),
    			tpt.gettptTile(),
    			tpt.gettptType(),
    			tpt.gettptDescrition(),
    			weeksjsonArray.toString()));
    	
        if (result) {
        	return "success";
        } else {
        	return "fail";
        }
    }
    
    @RequestMapping(value="/tpstatus",method=RequestMethod.POST)
    public String updatePlanStatus(@RequestBody String tpItem) {
    	
    	JsonObject tpItemJsonObject = new JsonParser().parse(tpItem).getAsJsonObject();
    	boolean result;
    	String tpOwnerId = tpItemJsonObject.get("tpOwnerId").toString().replace("\"", "");
    	TrainingPlan tp = edaoTp.getAllPlan(tpOwnerId).get(0);
    	/*
    	LocalDate tpStartTime = LocalDate.parse(tp.gettpStart().replace("\"", ""));
    	LocalDate tpUpdateDate = LocalDate.parse(tpItemJsonObject.get("tpDate").toString().replace("\"", ""));
    	
    	int daysDelta = (int)Duration.between(tpStartTime.atStartOfDay(), tpUpdateDate.atStartOfDay()).toDays();
    	int week = daysDelta/7;
    	String days = "day"+daysDelta%7;
    	System.out.println("week = "+week+" days = "+days);
    	*/
    	
    	JsonArray weeksjsonArray = new JsonParser().parse(tp.getWeeks()).getAsJsonArray();
    	//System.out.println(weeksjsonArray.toString());
    	//System.out.println(weeksjsonArray.get(week).getAsJsonObject().get(days).toString());
    	
    	for (JsonElement pa : weeksjsonArray) {
    		JsonObject temp = pa.getAsJsonObject();
    		
        	Set<Map.Entry<String, JsonElement>> entries = temp.entrySet();
        	for (Map.Entry<String, JsonElement> entry: entries) {
        		JsonElement taskItem = entry.getValue().getAsJsonObject().get("tasks");
        		if (taskItem != null) {
        			//System.out.println(entry.getValue().getAsJsonObject().get("date").toString().replace("\"", ""));
        			//System.out.println(tpItemJsonObject.get("tpDate").toString());
        			if (entry.getValue().getAsJsonObject().get("date").toString().replace("\"", "").equals(new String(tpItemJsonObject.get("tpDate").toString().replace("\"", "")))) {
		        		float kilometers = Float.parseFloat((tpItemJsonObject.get("kilometers").toString().replace("\"", "")));
	    				float minKilometre = Float.parseFloat(entry.getValue().getAsJsonObject().get("minKilometre").toString().replace("\"", ""));
	    				float maxKilometre = Float.parseFloat(entry.getValue().getAsJsonObject().get("maxKilometre").toString().replace("\"", ""));
	    				//System.out.println("kilometers = "+kilometers+" minKilometre = "+minKilometre);
	    				if (kilometers > Float.parseFloat(entry.getValue().getAsJsonObject().get("finished").toString().replace("\"", ""))) {
	    					entry.getValue().getAsJsonObject().addProperty("finished", Float.toString(kilometers));
	    				}
		        		if (kilometers > minKilometre) {
		        			//System.out.println("change status");
		        			//System.out.println(entry.getValue().getAsJsonObject().get("status").toString());
		        			entry.getValue().getAsJsonObject().addProperty("status", "completed");
		        		} else {
		        			entry.getValue().getAsJsonObject().addProperty("status", "incompleted");
		        		}
		        		
		        			
		        	}
        		}
        	}
        }
    	
    	System.out.println(weeksjsonArray.toString());
    	result = edaoTpEnum.update(tpOwnerId, weeksjsonArray.toString());
        if (result) {
        	return "success";
        } else {
        	return "fail";
        }
    }
    
    @RequestMapping(value="/tp/{tpId}", method=RequestMethod.DELETE)
    public String deletePlanTemplate(@PathVariable String tpId) {
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
}