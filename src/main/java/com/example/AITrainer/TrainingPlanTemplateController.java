package com.example.AITrainer;

import java.util.concurrent.atomic.AtomicLong;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Results.Results;
import com.example.TrainingPlanTemplate.TrainingPlanTemplate;
import com.example.TrainingPlanTemplate.TrainingPlanTemplateResponse;
import com.example.TrainingPlanTemplate.TrainingPlanTemplateResponseArray;
import com.example.service.TPTService;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class TrainingPlanTemplateController {
    private final AtomicLong counter = new AtomicLong();
    private final String randomTPTID = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
    
    @Autowired
    TPTService tptService;

    @RequestMapping(value="/tptemplates/{tptId}",method=RequestMethod.GET)
    public TrainingPlanTemplateResponse getPlanTemplate(@PathVariable String tptId) {
    	TrainingPlanTemplate tpt;
        tpt = tptService.getPlanTemplate(tptId);
        if (tpt != null) {
        	return new TrainingPlanTemplateResponse("00", "success", tpt);
        } else {
        	return new TrainingPlanTemplateResponse("01", String.format("fail: %s no such tpt", tptId), tpt);
        }
    }
    
    @RequestMapping(value="/tptemplates",method=RequestMethod.GET)
    public TrainingPlanTemplateResponseArray listAllPlanTemplate(@RequestParam(name="tptCategory", required=false) String tptCategory, 
    		@RequestParam(name="tptType", required=false) String tptType) {
    	List<TrainingPlanTemplate> tptlist = tptService.listAllPlanTemplate(tptCategory, tptType);
    	if (tptlist.size() > 0) {
    		return new TrainingPlanTemplateResponseArray("00", "success", tptlist.toArray(new TrainingPlanTemplate[0]));
    	} else {
    		return new TrainingPlanTemplateResponseArray("01",  "fail: tpt not found", null);
    	}
    }
    
    @RequestMapping(value="/tptemplates",method=RequestMethod.POST, produces="application/json;charset=UTF-8")
    public String createPlanTemplate(@RequestBody String tptItem) {
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	JsonObject tptItemJsonObject = null;
    	try {
    		tptItemJsonObject = new JsonParser().parse(tptItem).getAsJsonObject();
    	} catch (Exception e) {
    		return Results.failReturnJsonObject("fail: invalid options").toString();
    	}
    	try {
	    	String tptTile = tptItemJsonObject.get("tptTile").toString();
	    	String tptType = tptItemJsonObject.get("tptType").toString();
	    	String tptCategory = tptItemJsonObject.get("tptCategory").toString();
	    	String tptDescrition = tptItemJsonObject.get("tptDescrition").toString();

    		tptService.addTPT(new TrainingPlanTemplate(
    				"tpid"+randomTPTID+counter.incrementAndGet(),
    				tptTile.substring(1, tptTile.length()-1),
    				tptType.substring(1, tptType.length()-1),
    				tptCategory.substring(1, tptCategory.length()-1),
    				tptDescrition.substring(1, tptDescrition.length()-1),
        			df.format(new Date()),
        			tptItemJsonObject.get("weeks").toString()));
    	} catch (Exception e) {
    		return Results.failReturnJsonObject("fail: create tpt").toString();
    	}
    	return Results.successReturnJsonObject(null).toString();
    }
    
    @RequestMapping(value="/tptemplates/{tptId}",method=RequestMethod.PUT, produces="application/json;charset=UTF-8")
    public String updatePlanTemplate(@PathVariable String tptId, @RequestBody String tptItem) {
    	JsonObject tptItemJsonObject = null;
    	try {
    		tptItemJsonObject = new JsonParser().parse(tptItem).getAsJsonObject();
    	} catch (Exception e) {
    		return Results.failReturnJsonObject("fail: invalid options").toString();
    	}
    	
    	try {
	    	String tptTile = tptItemJsonObject.get("tptTile").toString();
	    	String tptType = tptItemJsonObject.get("tptType").toString();
	    	String tptCategory = tptItemJsonObject.get("tptCategory").toString();
	    	String tptDescrition = tptItemJsonObject.get("tptDescrition").toString();
	    	String publishedAt = tptItemJsonObject.get("publishedAt").toString();

    		tptService.updateTPT(new TrainingPlanTemplate(
    				tptId,
    				tptTile.substring(1, tptTile.length()-1),
    				tptType.substring(1, tptType.length()-1),
    				tptCategory.substring(1, tptCategory.length()-1),
    				tptDescrition.substring(1, tptDescrition.length()-1),
    				publishedAt.substring(1, publishedAt.length()-1),
        			tptItemJsonObject.get("weeks").toString()));
    	} catch (Exception e) {
    		e.printStackTrace();
    		return Results.failReturnJsonObject("fail: update tpt").toString();
    	}
    	return Results.successReturnJsonObject(null).toString();
    }
    
    @RequestMapping(value="/tptemplates/{tptId}", method=RequestMethod.DELETE, produces="application/json;charset=UTF-8")
    public String deletePlanTemplate(@PathVariable String tptId) {
    	try {
    		tptService.deleteTPT(tptId);
    	} catch (Exception e) {
    		return Results.failReturnJsonObject("fail: delete tpt").toString();
    	}
        return Results.successReturnJsonObject(null).toString();
        /*
        if (result == 1) {
        	return Results.successReturnJsonObject(null).toString();
        } else {
        	return Results.failReturnJsonObject("fail: delete tpt").toString();
        }*/
    }
}