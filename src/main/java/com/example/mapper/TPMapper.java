package com.example.mapper;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import com.example.TrainingPlan.TrainingPlan;

public interface TPMapper {
    
    TrainingPlan getActiveTP(Map map);

    List<TrainingPlan> getAllTP(Map map);
    
    List<TrainingPlan> getAllActiveTP(Map map);
    
    List<TrainingPlan> getTP(Map map);

    void addTP(TrainingPlan tpItem) throws SQLException;
    
    void updateTPStatus(Map map) throws SQLException;
    
    void updateTP(Map<String, String> map) throws SQLException;
    
    void deleteTP(Map map);
}
