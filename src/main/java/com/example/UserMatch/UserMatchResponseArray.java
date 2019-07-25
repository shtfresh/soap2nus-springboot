package com.example.UserMatch;

public class UserMatchResponseArray {

    private final String code;
    private final String msg;
    private final UserMatch[] results;

    public UserMatchResponseArray(String code, String msg, UserMatch[] results) {
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
    
    public UserMatch[] getresults() {
        return this.results;
    }
}