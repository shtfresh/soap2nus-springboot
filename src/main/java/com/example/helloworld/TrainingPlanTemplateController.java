package com.example.helloworld;

import java.util.concurrent.atomic.AtomicLong;

import java.util.Date;
import java.util.List;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

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
import com.example.db.DBConnectionMysql;
import com.example.db.TrainingPlanTemplateDbDeclaration;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class TrainingPlanTemplateController {

    private final AtomicLong counter = new AtomicLong();
    
    TrainingPlanTemplateDbDeclaration edao = new TrainingPlanTemplateDbDeclaration();

    @RequestMapping(value="/tptemplates/{tptId}",method=RequestMethod.GET)
    public TrainingPlanTemplateResponse getPlanTemplate(@PathVariable String tptId) {
    	checkConnection(edao);
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
    	checkConnection(edao);
    	List<TrainingPlanTemplate> tplist = edao.listAllPlanTemplate(tptCategory, tptType);
    	if (tplist != null) {
    		return new TrainingPlanTemplateResponseArray("00", "success", tplist.toArray(new TrainingPlanTemplate[0]));
    	} else {
    		return new TrainingPlanTemplateResponseArray("01",  "fail: tpt not found", null);
    	}
    }
    
    @RequestMapping(value="/tptemplates",method=RequestMethod.POST)
    public String createPlanTemplate(@RequestBody String tptItem) {
    	checkConnection(edao);
    	JsonObject returnJsonObject = new JsonObject();
    	SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	
    	JsonObject tptItemJsonObject = new JsonParser().parse(tptItem).getAsJsonObject();
    	tptItemJsonObject.addProperty("tptId", "tpidXXX"+counter.incrementAndGet());
    	tptItemJsonObject.addProperty("publishedAt", df.format(new Date()));
    	
    	/*ObjectMapper mapper = new ObjectMapper();
    	TrainingPlanTemplate tpt = mapper.readValue(tptItemJsonObject.toString(), TrainingPlanTemplate.class);
    	edao.add(tpt);*/
    	boolean result;
    	result = edao.add(new TrainingPlanTemplate(tptItemJsonObject.get("tptId").toString(),
    			tptItemJsonObject.get("tptTile").toString(),
    			tptItemJsonObject.get("tptType").toString(),
    			tptItemJsonObject.get("tptCategory").toString(),
    			tptItemJsonObject.get("tptDescrition").toString(),
    			tptItemJsonObject.get("publishedAt").toString(),
    			tptItemJsonObject.get("weeks").toString()));
        if (result) {
        	return Results.successReturnJsonObject(returnJsonObject, null).toString();
        } else {
        	return Results.failReturnJsonObject(returnJsonObject, "fail: create tpt").toString();
        }
    }
    
    @RequestMapping(value="/tptemplates/{tptId}",method=RequestMethod.PUT)
    public String updatePlanTemplate(@PathVariable String tptId, @RequestBody String tptItem) {
    	JsonObject returnJsonObject = new JsonObject();
    	checkConnection(edao);
    	JsonObject tptItemJsonObject = new JsonParser().parse(tptItem).getAsJsonObject();
    	boolean result;
    	result = edao.update(tptId, new TrainingPlanTemplate(tptItemJsonObject.get("tptId").toString(),
    			tptItemJsonObject.get("tptTile").toString(),
    			tptItemJsonObject.get("tptType").toString(),
    			tptItemJsonObject.get("tptCategory").toString(),
    			tptItemJsonObject.get("tptDescrition").toString(),
    			tptItemJsonObject.get("publishedAt").toString(),
    			tptItemJsonObject.get("weeks").toString()));
    	
        if (result) {
        	return Results.successReturnJsonObject(returnJsonObject, null).toString();
        } else {
        	return Results.failReturnJsonObject(returnJsonObject, "fail: update tpt").toString();
        }
    }
    
    @RequestMapping(value="/tptemplates/{tptId}", method=RequestMethod.DELETE)
    public String deletePlanTemplate(@PathVariable String tptId) {
    	JsonObject returnJsonObject = new JsonObject();
    	checkConnection(edao);
    	boolean result;
        result = edao.deletePlanTemplate(tptId);
        if (result) {
        	return Results.successReturnJsonObject(returnJsonObject, null).toString();
        } else {
        	return Results.failReturnJsonObject(returnJsonObject, "fail: delete tpt").toString();
        }
    }
    
	public static void checkConnection(TrainingPlanTemplateDbDeclaration tpt) {
    	try {
			if(tpt.conn.isClosed()) {
				tpt.conn = DBConnectionMysql.getInstance().getConnection();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}