package com.example.AITrainer;

import java.util.concurrent.atomic.AtomicLong;

import java.util.Date;
import java.util.List;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.RandomStringUtils;
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
import com.example.db.TrainingPlanTemplateDbDeclaration;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class TrainingPlanTemplateController {
    private final AtomicLong counter = new AtomicLong();
    private final String randomTPTID = RandomStringUtils.randomAlphanumeric(4).toUpperCase();
    
    static TrainingPlanTemplateDbDeclaration edao = new TrainingPlanTemplateDbDeclaration();

    @RequestMapping(value="/tptemplates/{tptId}",method=RequestMethod.GET)
    public TrainingPlanTemplateResponse getPlanTemplate(@PathVariable String tptId) {
    	TrainingPlanTemplate tpt;
        tpt = edao.getPlanTemplate(tptId);
        if (tpt != null) {
        	return new TrainingPlanTemplateResponse("00", "success", tpt);
        } else {
        	return new TrainingPlanTemplateResponse("01", String.format("fail: %s no such tpt", tptId), tpt);
        }
    }
    
    @RequestMapping(value="/tptemplates",method=RequestMethod.GET)
    public TrainingPlanTemplateResponseArray listAllPlanTemplate(@RequestParam(name="tptCategory", required=false) String tptCategory, 
    		@RequestParam(name="tptType", required=false) String tptType) {
    	List<TrainingPlanTemplate> tplist = edao.listAllPlanTemplate(tptCategory, tptType);
    	if (tplist != null) {
    		return new TrainingPlanTemplateResponseArray("00", "success", tplist.toArray(new TrainingPlanTemplate[0]));
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
    	tptItemJsonObject.addProperty("tptId", "tpid"+randomTPTID+counter.incrementAndGet());
    	tptItemJsonObject.addProperty("publishedAt", df.format(new Date()));
    	
    	/*ObjectMapper mapper = new ObjectMapper();
    	TrainingPlanTemplate tpt = mapper.readValue(tptItemJsonObject.toString(), TrainingPlanTemplate.class);
    	edao.add(tpt);*/
    	boolean result;
    	try {
    		result = edao.add(new TrainingPlanTemplate(tptItemJsonObject.get("tptId").toString(),
        			tptItemJsonObject.get("tptTile").toString(),
        			tptItemJsonObject.get("tptType").toString(),
        			tptItemJsonObject.get("tptCategory").toString(),
        			tptItemJsonObject.get("tptDescrition").toString(),
        			tptItemJsonObject.get("publishedAt").toString(),
        			tptItemJsonObject.get("weeks").toString()));
    	} catch (Exception e) {
    		return Results.failReturnJsonObject("fail: invalid options").toString();
    	}
        if (result) {
        	return Results.successReturnJsonObject(null).toString();
        } else {
        	return Results.failReturnJsonObject("fail: create tpt").toString();
        }
    }
    
    @RequestMapping(value="/tptemplates/{tptId}",method=RequestMethod.PUT, produces="application/json;charset=UTF-8")
    public String updatePlanTemplate(@PathVariable String tptId, @RequestBody String tptItem) {
    	JsonObject tptItemJsonObject = null;
    	try {
    		tptItemJsonObject = new JsonParser().parse(tptItem).getAsJsonObject();
    	} catch (Exception e) {
    		return Results.failReturnJsonObject("fail: invalid options").toString();
    	}
    	boolean result;
    	try {
    		result = edao.update(tptId, new TrainingPlanTemplate(tptItemJsonObject.get("tptId").toString(),
        			tptItemJsonObject.get("tptTile").toString(),
        			tptItemJsonObject.get("tptType").toString(),
        			tptItemJsonObject.get("tptCategory").toString(),
        			tptItemJsonObject.get("tptDescrition").toString(),
        			tptItemJsonObject.get("publishedAt").toString(),
        			tptItemJsonObject.get("weeks").toString()));
    	} catch (Exception e) {
    		return Results.failReturnJsonObject("fail: invalid options").toString();
    	}
    	
        if (result) {
        	return Results.successReturnJsonObject(null).toString();
        } else {
        	return Results.failReturnJsonObject("fail: update tpt").toString();
        }
    }
    
    @RequestMapping(value="/tptemplates/{tptId}", method=RequestMethod.DELETE, produces="application/json;charset=UTF-8")
    public String deletePlanTemplate(@PathVariable String tptId) {
    	boolean result;
        result = edao.deletePlanTemplate(tptId);
        if (result) {
        	return Results.successReturnJsonObject(null).toString();
        } else {
        	return Results.failReturnJsonObject("fail: delete tpt").toString();
        }
    }
}