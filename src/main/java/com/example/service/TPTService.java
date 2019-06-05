package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TrainingPlanTemplate.TrainingPlanTemplate;
import com.example.mapper.TPTMapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TPTService {

    @Autowired
    TPTMapper tptMapper;
    
    public void addTPT(TrainingPlanTemplate tptItem) {
    	tptMapper.addTPT(tptItem);
    }
    
    public void updateTPT(TrainingPlanTemplate tptItem) {
    	tptMapper.updateTPT(tptItem);
    }
    
    public void deleteTPT(String tptId) {
    	Map<String , Object> map = new HashMap<>();
    	map.put("tptId" , tptId);
    	tptMapper.deleteTPT(map);
    }
    
    public TrainingPlanTemplate getPlanTemplate(String tptId) {
    	Map<String , Object> map = new HashMap<>();
    	map.put("tptId" , tptId);
    	return tptMapper.getTPT(map);
    }

    public List<TrainingPlanTemplate> listAllPlanTemplate(String tptCategory, String tptType){
        Map<String , Object> map = new HashMap<>();
        if (tptCategory != null) {
        	map.put("tptCategory" , tptCategory);
        }
        if (tptCategory != null) {
        	map.put("tptType" , tptType);
        }
        return tptMapper.listAllTPT(map);
    }
}