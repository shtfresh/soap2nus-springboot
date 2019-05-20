package com.example.Results;

import com.google.gson.JsonObject;

public class Results {
	
	public static JsonObject successReturnJsonObject(JsonObject returnJsonObject, JsonObject results) {
		returnJsonObject.addProperty("code", "00");
		returnJsonObject.addProperty("msg", "success");
		returnJsonObject.add("results", results);
		return returnJsonObject;
	}
	
	public static JsonObject failReturnJsonObject(JsonObject returnJsonObject, String msg) {
		returnJsonObject.addProperty("code", "01");
		returnJsonObject.addProperty("msg", msg);
		returnJsonObject.add("results", null);
		return returnJsonObject;
	}
}