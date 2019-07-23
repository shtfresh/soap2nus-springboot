package com.example.UserMatch;

public class UserMatch {

    private final String matchId;
    private final String matchName;
    private final String matchStartTime;
    private final String matchAddress;
    private final String matchType;
    private final String matchDesp;
    private final String CompetitionRule;
    private final String EnrollGuide;
    private final String OtherInfo;
    private final String Banner;

    public UserMatch(String matchId, String matchName, String matchStartTime,
                     String matchAddress, String matchType, String matchDesp,
                     String CompetitionRule, String EnrollGuide, String OtherInfo, String Banner) {
        this.matchId = matchId;
        this.matchName = matchName;
        this.matchStartTime = matchStartTime;
        this.matchAddress = matchAddress;
        this.matchType = matchType;
        this.matchDesp = matchDesp;
        this.CompetitionRule = CompetitionRule;
        this.EnrollGuide = EnrollGuide;
        this.OtherInfo = OtherInfo;
        this.Banner = Banner;
    }
/*
    public Integer getmatchId() {
        return this.matchId;
    }

    public String getmatchName() {
        return this.matchName;
    }

    public Integer getmatchType() {
        return this.matchType;
    }

    public String getmatchAddress() {
        return this.matchAddress;
    }

    public String getmatchStartTime() {
        return this.matchStartTime;
    }

    public String getmatchEndTime() {
        return this.matchEndTime;
    }

    public String getplace() {
        return this.place;
    }

    public String provinc() {
        return this.provinc;
    }

    public String city() {
        return this.city;
    }

    public String area() {
        return this.area;
    }

    public Integer isDomestic() {
        return this.isDomestic;
    }*/
}