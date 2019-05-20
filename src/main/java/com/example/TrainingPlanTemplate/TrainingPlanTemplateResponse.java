package com.example.TrainingPlanTemplate;
 
public class TrainingPlanTemplateResponse {

    private final String code;
    private final String msg;
    private final TrainingPlanTemplate results;    
    
    public TrainingPlanTemplateResponse(String code, String msg, TrainingPlanTemplate results) {
    	this.code = code;
    	this.msg = msg;
    	this.results = results;
    }
    
    public String getcode() {
        return this.code;
    }
    
    public String getmsg() {
        return this.msg;
    }
    
    public TrainingPlanTemplate getresults() {
        return this.results;
    }
}