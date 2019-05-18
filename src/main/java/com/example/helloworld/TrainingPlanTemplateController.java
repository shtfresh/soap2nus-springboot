package com.example.helloworld;

import java.util.concurrent.atomic.AtomicLong;

import java.util.Date;
import java.sql.SQLException;
import java.text.SimpleDateFormat;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.example.TrainingPlanTemplate.TrainingPlanTemplate;
import com.example.db.DBConnectionMysql;
import com.example.db.TrainingPlanTemplateDbDeclaration;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

@RestController
public class TrainingPlanTemplateController {

    private final AtomicLong counter = new AtomicLong();
    
    TrainingPlanTemplateDbDeclaration edao = new TrainingPlanTemplateDbDeclaration();

    @RequestMapping(value="/tptemplates/{tptId}",method=RequestMethod.GET)
    public TrainingPlanTemplate getPlanTemplate(@PathVariable String tptId) {
    	checkConnection(edao);
    	TrainingPlanTemplate tpt;
        tpt = edao.getPlanTemplate(tptId);
        
        return tpt;
    }
    
    @RequestMapping(value="/tptemplates",method=RequestMethod.GET)
    public TrainingPlanTemplate[] listAllPlanTemplate() {
    	checkConnection(edao);
    	return edao.listAllPlanTemplate().toArray(new TrainingPlanTemplate[0]);
    }
    
    @RequestMapping(value="/tptemplates",method=RequestMethod.POST)
    public String createPlanTemplate(@RequestBody String tptItem) {
    	checkConnection(edao);
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
    	checkConnection(edao);
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
    	checkConnection(edao);
    	boolean result;
        result = edao.deletePlanTemplate(tptId);
        if (result) {
        	return "success";
        } else {
        	return "fail";
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