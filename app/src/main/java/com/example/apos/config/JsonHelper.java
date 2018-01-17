package com.example.apos.config;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import okhttp3.ResponseBody;

public class JsonHelper {

	private static final String MESSAGE = "msg";

	private static JsonHelper instance;
	private static Gson gson;

	private JsonHelper() {
		super();
	}

	public static synchronized JsonHelper init() {
		if (null == instance) {
			instance = new JsonHelper();
			gson = new GsonBuilder().serializeNulls().create();
		}
		return instance;
	}

	//====================================================================

	public static String getString(JSONObject jsonObject, String key) {
		String value = null;
		try {
			value = jsonObject.getString(key);
		} catch (JSONException e) {
			Logger.error("JSON: " + key + "->" + value + "\n" + e.getMessage());
		}
		return value;
	}

	public static String getErrorMessage(ResponseBody errorBody) {
		String message = null;
		try {
			Map map = gson.fromJson(errorBody.string(), Map.class);
			message = String.valueOf(map.get(MESSAGE));
		} catch (Exception e) {
			Logger.error("Unable to parse error message" + "\n" + e.getMessage());
		}
		return message;
	}

	public static String getMessage(Map map) {
		String message = null;
		try {
			message = String.valueOf(map.get(MESSAGE));
		} catch (Exception e) {
			Logger.error("Unable to parse error message" + "\n" + e.getMessage());
		}
		return message;
	}


}
