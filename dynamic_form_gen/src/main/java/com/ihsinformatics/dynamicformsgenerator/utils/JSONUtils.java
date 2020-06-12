package com.ihsinformatics.dynamicformsgenerator.utils;

import com.ihsinformatics.dynamicformsgenerator.network.ParamNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONUtils {

	private static JSONUtils instance;
	
	public static JSONUtils getInstance() {
		if(instance == null) {
			instance = new JSONUtils();
		}
		
		return instance;
	}
	
	public JSONObject findJSONObjectInJSONArray(String havingKey, JSONArray array) throws JSONException {
		for(int i=0; i<array.length(); i++) {
			JSONObject temp = array.getJSONObject(i);
			if(temp.has(havingKey)) {
				return temp;
			}
		}
		
		return null;
	}


	public String[] findStringInJSONArray(String havingKey, JSONArray array) throws JSONException {
		for(int i=0; i<array.length(); i++) {
			String temp = array.get(i).toString();
			String[] separated = temp.split(":");
			if(separated[0].equals(havingKey)) {
				return separated;
			}
		}

		return null;
	}

	public JSONObject findJSONObjectInHYDRAJSONArray(String havingKey, JSONArray array) throws JSONException {
		for(int i=0; i<array.length(); i++) {
			JSONObject temp = array.getJSONObject(i);
			if(temp.optString(ParamNames.PARAM_QUESTION).equalsIgnoreCase(havingKey)) {
				return temp;
			}
		}

		return null;
	}
}
