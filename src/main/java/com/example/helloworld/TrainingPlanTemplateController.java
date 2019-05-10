package com.example.helloworld;

import java.util.concurrent.atomic.AtomicLong;

import java.util.Date;
import java.text.SimpleDateFormat;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.TrainingPlanTemplate.TrainingPlanTemplate;
import com.example.db.TrainingPlanTemplateDbDeclaration;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class TrainingPlanTemplateController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();
    
    TrainingPlanTemplateDbDeclaration edao = new TrainingPlanTemplateDbDeclaration();

    //@RequestMapping(value="/tptemplates",method=RequestMethod.GET)
    @RequestMapping(value="/tptemplates/{tptId}",method=RequestMethod.GET)
    public TrainingPlanTemplate getPlanTemplate(@PathVariable String tptId) {
    	TrainingPlanTemplate tpt;
        tpt = edao.getPlanTemplate(tptId);
        //System.out.println(tpt.getWeeks());
        
        return tpt;
    }
    
    @RequestMapping(value="/tptemplates",method=RequestMethod.GET)
    public TrainingPlanTemplate[] listAllPlanTemplate() {
    	return edao.listAllPlanTemplate().toArray(new TrainingPlanTemplate[0]);
    }
    
    @RequestMapping(value="/tptemplates",method=RequestMethod.POST)
    public String createPlanTemplate(@RequestBody String tptItem) {
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
    			tptItemJsonObject.get("tptDescrition").toString(),
    			tptItemJsonObject.get("publishedAt").toString(),
    			tptItemJsonObject.get("weeks").toString()));
    	//System.out.println(tptItemJsonObject.get("tptId").toString());
    	//System.out.println(tptItemJsonObject.get("weeks").toString());
        if (result) {
        	return "success";
        } else {
        	return "fail";
        }
    }
    
    @RequestMapping(value="/tptemplates/{tptId}",method=RequestMethod.PUT)
    public String updatePlanTemplate(@PathVariable String tptId, @RequestBody String tptItem) {
    	
    	JsonObject tptItemJsonObject = new JsonParser().parse(tptItem).getAsJsonObject();
    	boolean result;
    	result = edao.update(tptId, new TrainingPlanTemplate(tptItemJsonObject.get("tptId").toString(),
    			tptItemJsonObject.get("tptTile").toString(),
    			tptItemJsonObject.get("tptType").toString(),
    			tptItemJsonObject.get("tptDescrition").toString(),
    			tptItemJsonObject.get("publishedAt").toString(),
    			tptItemJsonObject.get("weeks").toString()));
    	
        if (result) {
        	return "success";
        } else {
        	return "fail";
        }
    }
    
    @RequestMapping(value="/tptemplates/{tptId}", method=RequestMethod.DELETE)
    public String deletePlanTemplate(@PathVariable String tptId) {
    	boolean result;
        result = edao.deletePlanTemplate(tptId);
        if (result) {
        	return "success";
        } else {
        	return "fail";
        }
       
    }
}