package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.TrainingPlan.TrainingPlan;
import com.example.TrainingPlanTemplate.TrainingPlanTemplate;
import com.example.mapper.TPMapper;
import com.example.mapper.TPTMapper;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class TPService {

    @Autowired
    TPMapper tpMapper;

    public void addTP(TrainingPlan tpItem) throws SQLException {
    	tpMapper.addTP(tpItem);
    }
    
    public void updateTP(String tpOwnerId, Map<String , String> map) throws SQLException {
    	map.put("tpOwnerId" , tpOwnerId);
    	tpMapper.updateTP(map);
    }
    
    public void deleteTP(String tpId) {
    	Map<String , Object> map = new HashMap<>();
    	map.put("tpId" , tpId);
    	tpMapper.deleteTP(map);
    }
    
    public void updateTPStatus(String tpStatus, String tpId) throws SQLException {
    	Map<String , Object> map = new HashMap<>();
    	map.put("tpId" , tpId);
    	map.put("tpStatus" , tpStatus);
    	tpMapper.updateTPStatus(map);
    }
    
    public TrainingPlan getActivePlan(String tpOwnerId) {
    	Map<String , Object> map = new HashMap<>();
    	map.put("tpOwnerId" , tpOwnerId);
    	return tpMapper.getActiveTP(map);
    }

    public List<TrainingPlan> getAllPlan(String tpOwnerId){
        Map<String , Object> map = new HashMap<>();
        map.put("tpOwnerId" , tpOwnerId);
        return tpMapper.getAllTP(map);
    }
    
    public List<TrainingPlan> getAllActivePlan(String date){
        Map<String , Object> map = new HashMap<>();
        map.put("date" , date);
        return tpMapper.getAllActiveTP(map);
    }
    
    public TrainingPlan getPlanOnTP(String tpId){
        Map<String , Object> map = new HashMap<>();
        map.put("tpId" , tpId);
        List<TrainingPlan> resultList = tpMapper.getTP(map);
        if (resultList.size() > 0) {
			return resultList.get(0);
		} else {
            return null;
        }
    }
}