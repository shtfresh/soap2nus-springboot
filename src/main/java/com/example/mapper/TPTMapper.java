package com.example.mapper;

import java.util.List;
import java.util.Map;

import com.example.TrainingPlanTemplate.TrainingPlanTemplate;

public interface TPTMapper {

    //Map<String , Object> getUserData(Map map);
    List<TrainingPlanTemplate> listAllTPT(Map map);
    
    TrainingPlanTemplate getTPT(Map map);
    //List<Map<String , Object>> getMatchList(Map map);
    
    void addTPT(TrainingPlanTemplate tptItem);
    
    void updateTPT(TrainingPlanTemplate tptItem);
    
    void deleteTPT(Map map);

    //Map<String , Object> getMatchData(Map map);
}
