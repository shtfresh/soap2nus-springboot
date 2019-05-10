package com.example.helloworld;

import java.util.concurrent.atomic.AtomicLong;
import java.util.Date;
import java.util.Map;
import java.util.Set;
import java.text.SimpleDateFormat;
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
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.fasterxml.jackson.databind.util.StdDateFormat;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class TrainingPlanController {

    private final AtomicLong counter = new AtomicLong();
    
    TrainingPlanTemplateDbDeclaration edaoTpt = new TrainingPlanTemplateDbDeclaration();
    
    TrainingPlanDbDeclaration edaoTp = new TrainingPlanDbDeclaration();

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
    	SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd");
    	
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
        		if (entry.getValue().getAsJsonObject().get("tasks") != null) {
        			startTime = startTime.plusDays(1);
        			entry.getValue().getAsJsonObject().addProperty("date", startTime.toString());
        			entry.getValue().getAsJsonObject().addProperty("status", "planed");
        			//System.out.println(startTime.plusDays(1).toString());
        			//tpItemJsonObject.get("tpStart").toString()
        			
        			//System.out.println(entry.getValue().getAsJsonObject().toString());
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
    
    /*
    @RequestMapping(value="/tptemplates/{tptId}",method=RequestMethod.PUT)
    public String updatePlanTemplate(@PathVariable String tptId, @RequestBody String tptItem) {
    	
    	JsonObject tptItemJsonObject = new JsonParser().parse(tptItem).getAsJsonObject();
    	
    	edao.update(tptId, new TrainingPlanTemplate(tptItemJsonObject.get("tptId").toString(),
    			tptItemJsonObject.get("tptTile").toString(),
    			tptItemJsonObject.get("tptType").toString(),
    			tptItemJsonObject.get("tptDescrition").toString(),
    			tptItemJsonObject.get("publishedAt").toString(),
    			tptItemJsonObject.get("weeks").toString()));

        return "Success";
    }*/
    
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
}