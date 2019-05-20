package com.example.TrainingPlan;
 
public class TrainingPlanResponseArray {

    private final String code;
    private final String msg;
    private final TrainingPlan[] results;    
    
    public TrainingPlanResponseArray(String code, String msg, TrainingPlan[] results) {
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
    
    public TrainingPlan[] getresults() {
        return this.results;
    }
}