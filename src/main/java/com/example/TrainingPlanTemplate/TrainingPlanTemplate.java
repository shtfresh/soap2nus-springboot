package com.example.TrainingPlanTemplate;

import com.fasterxml.jackson.annotation.JsonRawValue;

public class TrainingPlanTemplate {

    private final String tptId;
    private final String tptTile;
    private final String tptType;
    private final String tptDescrition;
    private final String publishedAt;
    @JsonRawValue
    private final String weeks;
    
    public TrainingPlanTemplate(String tptId, String tptTile, String tptType, String tptDescrition, String publishedAt, String weeks) {
        this.tptId = tptId;
        this.tptTile = tptTile;
        this.tptType = tptType;
        this.tptDescrition = tptDescrition;
        this.publishedAt = publishedAt;
        this.weeks = weeks;
    }

    public String gettptId() {
        return this.tptId;
    }
    
    public String gettptTile() {
        return this.tptTile;
    }
    
    
    public String gettptType() {
        return this.tptType;
    }
    
    public String gettptDescrition() {
        return this.tptDescrition;
    }
    
    public String getPublishedAt() {
        return this.publishedAt;
    }
    
    public String getWeeks() {
    	//System.out.println(this.weeks);
        return this.weeks;
    }
}