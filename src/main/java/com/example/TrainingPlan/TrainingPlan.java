package com.example.TrainingPlan;


import com.fasterxml.jackson.annotation.JsonRawValue;

 
public class TrainingPlan {

    private final String tpId;
    private final String tpOwnerId;
    private final String tpPublishedAt;
    private final String tpUpdateAt;
    private final String tpOwner;
    private final String tpStatus;
    private final String tpStart;
    private final String tpEnd;
    private final String tpTargetType;
    private final String tpTargetMatchid;
    private final int tpVersionNo ;
    private final String minKilometre;
    private final String maxKilometre;
    
    private final String tptId;
    private final String tptTile;
    private final String tptType;
    private final String tptDescrition;
    @JsonRawValue
    private String weeks;
    
    public TrainingPlan(String tpId, String tpOwnerId, String tpPublishedAt, String tpUpdateAt, String tpOwner, 
    		String tpStatus, String tpStart, String tpEnd, String tpTargetType, String tpTargetMatchid, int tpVersionNo, String minKilometre, String maxKilometre,
    		String tptId, String tptTile, String tptType, String tptDescrition, String weeks) {
    	
    	this.tpId = tpId;
    	this.tpOwnerId = tpOwnerId;
    	this.tpPublishedAt = tpPublishedAt;
    	this.tpUpdateAt = tpUpdateAt;
    	this.tpOwner = tpOwner;
    	this.tpStatus = tpStatus;
    	this.tpStart = tpStart;
    	this.tpEnd = tpEnd;
    	this.tpTargetType = tpTargetType;
    	this.tpTargetMatchid = tpTargetMatchid;
    	this.tpVersionNo = tpVersionNo;
    	this.minKilometre = minKilometre;
    	this.maxKilometre = maxKilometre;
    	
        this.tptId = tptId;
        this.tptTile = tptTile;
        this.tptType = tptType;
        this.tptDescrition = tptDescrition;
        this.weeks = weeks;
    }
    
    public String gettpId() {
        return this.tpId;
    }
    
    public String gettpOwnerId() {
        return this.tpOwnerId;
    }
    
    public String gettpPublishedAt() {
        return this.tpPublishedAt;
    }
    
    public String gettpUpdateAt() {
        return this.tpUpdateAt;
    }
    
    public String gettpOwner() {
        return this.tpOwner;
    }
    
    public String gettpStatus() {
        return this.tpStatus;
    }
    
    public String gettpStart() {
        return this.tpStart;
    }
    
    public String gettpEnd() {
        return this.tpEnd;
    }
    
    public String gettpTargetType() {
        return this.tpTargetType;
    }
    
    public String gettpTargetMatchid() {
        return this.tpTargetMatchid;
    }
    
    public int gettpVersionNo() {
        return this.tpVersionNo;
    }
    
    public String gettptId() {
        return this.tptId;
    }
    
    public String getminKilometre() {
    	return this.minKilometre;
    }
    
    public String getmaxKilometre() {
    	return this.maxKilometre;
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
    
    public String getWeeks() {
        return this.weeks;
    }
    
    public void clearWeeks() {
        this.weeks = "";
    }
}