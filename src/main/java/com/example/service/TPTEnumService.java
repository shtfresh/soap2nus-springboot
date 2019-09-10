package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TrainingPlanTemplate.TrainingPlanTemplate;
import com.example.mapper.TPTEnumMapper;
import com.example.mapper.TPTMapper;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TPTEnumService {

    @Autowired
    TPTEnumMapper tptEnumMapper;
    
    public JsonObject getPlanTemplateEnum() {
    	Map<String, String> map = new HashMap<>();
    	JsonObject tptEnumJsonObject = new JsonObject();
    	map = tptEnumMapper.getEnum();
    	System.out.println("fgsfafsadadasdasdasddsadasdasdadas:"+map.toString());
    	
//    	tptEnumJsonObject.addProperty("tpStatus", map.get("tpStatus"));
//		tptEnumJsonObject.addProperty("status", map.get("status"));
//		tptEnumJsonObject.addProperty("task", map.get("task"));
//		tptEnumJsonObject.addProperty("tpTargetType", map.get("tpTargetType"));
//		tptEnumJsonObject.addProperty("tptType", map.get("tptType"));
//		tptEnumJsonObject.addProperty("run", map.get("pace_run"));
//		tptEnumJsonObject.addProperty("walk", map.get("pace_walk"));
//		tptEnumJsonObject.addProperty("easy", map.get("pace_easy"));
//		tptEnumJsonObject.addProperty("brisk", map.get("pace_brisk"));
//		tptEnumJsonObject.addProperty("jog", map.get("pace_jog"));
//		tptEnumJsonObject.addProperty("fast", map.get("pace_fast"));
//		tptEnumJsonObject.addProperty("recovery", map.get("pace_recovery"));
    	
    	
    	tptEnumJsonObject.addProperty("tpStatus", map.get("TPSTATUS"));
		tptEnumJsonObject.addProperty("status", map.get("STATUS"));
		tptEnumJsonObject.addProperty("task", map.get("TASK"));
		tptEnumJsonObject.addProperty("tpTargetType", map.get("TPTARGETTYPE"));
		tptEnumJsonObject.addProperty("tptType", map.get("TPTTYPE"));
		tptEnumJsonObject.addProperty("run", map.get("PACE_RUN"));
		tptEnumJsonObject.addProperty("walk", map.get("PACE_WALK"));
		tptEnumJsonObject.addProperty("easy", map.get("PACE_EASY"));
		tptEnumJsonObject.addProperty("brisk", map.get("PACE_BRISK"));
		tptEnumJsonObject.addProperty("jog", map.get("PACE_JOG"));
		tptEnumJsonObject.addProperty("fast", map.get("PACE_FAST"));
		tptEnumJsonObject.addProperty("recovery", map.get("PACE_RECOVERY"));
		System.out.println("fgsfafsadadasdasdasddsadasdasdadas:"+tptEnumJsonObject.toString());
    	return tptEnumJsonObject;
    }
}