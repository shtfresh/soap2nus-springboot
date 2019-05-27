package com.example.Results;

import com.google.gson.JsonObject;

public class Results {
	
	private static JsonObject returnJsonObject = new JsonObject();

	public static JsonObject successReturnJsonObject(JsonObject results) {
		returnJsonObject.addProperty("code", "00");
		returnJsonObject.addProperty("msg", "success");
		returnJsonObject.add("results", results);
		return returnJsonObject;
	}
	
	public static JsonObject failReturnJsonObject(String msg) {
		returnJsonObject.addProperty("code", "01");
		returnJsonObject.addProperty("msg", msg);
		returnJsonObject.add("results", null);
		return returnJsonObject;
	}
}