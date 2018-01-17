package com.example.apos.config;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.apos.App;
import com.example.apos.dto.ConfigurationDTO;
import com.example.apos.dto.UserDTO;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.HashMap;

/**
 * Created by Abhilash on 16/9/17.
 */

public class PreferenceUtil {
	private static SharedPreferences mSharedPreferences = null;

	//Setters
	public static void putInt(String key, int value) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		mSharedPreferences.edit().putInt(key, value).commit();
	}

	public static void putBoolean(String key, boolean value) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		mSharedPreferences.edit().putBoolean(key, value).commit();
	}

	public static void putString(String key, String value) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		mSharedPreferences.edit().putString(key, value).commit();
	}

	public static void putFloat(String key, float value) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		mSharedPreferences.edit().putFloat(key, value).commit();
	}

	public static void putLong(String key, long value) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		mSharedPreferences.edit().putLong(key, value).commit();
	}

	//Getters
	public static int getInt(String key, int defValue) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		return mSharedPreferences.getInt(key, defValue);
	}

	public static boolean getBoolean(String key, boolean defValue) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		return mSharedPreferences.getBoolean(key, defValue);
	}

	public static String getString(String key, String defValue) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		return mSharedPreferences.getString(key, defValue);
	}

	public static float getFloat(String key, float defValue) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		return mSharedPreferences.getFloat(key, defValue);
	}

	public static long getLong(String key, long defValue) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		return mSharedPreferences.getLong(key, defValue);
	}

	//Setters with shared preference name
	public static void putInt(String key, int value, String preferenceName) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
		mSharedPreferences.edit().putInt(key, value).apply();
	}

	public static void putBoolean(String key, boolean value, String preferenceName) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
		mSharedPreferences.edit().putBoolean(key, value).apply();
	}

	public static void putString(String key, String value, String preferenceName) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
		mSharedPreferences.edit().putString(key, value).apply();
	}

	public static void putFloat(String key, float value, String preferenceName) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
		mSharedPreferences.edit().putFloat(key, value).apply();
	}

	public static void putLong(String key, long value, String preferenceName) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
		mSharedPreferences.edit().putLong(key, value).apply();
	}

	//Getters
	public static int getInt(String key, int defValue, String preferenceName) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
		return mSharedPreferences.getInt(key, defValue);
	}

	public static boolean getBoolean(String key, boolean defValue, String preferenceName) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
		return mSharedPreferences.getBoolean(key, defValue);
	}

	public static String getString(String key, String defValue, String preferenceName) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
		return mSharedPreferences.getString(key, defValue);
	}

	public static float getFloat(String key, float defValue, String preferenceName) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
		return mSharedPreferences.getFloat(key, defValue);
	}

	public static long getLong(String key, long defValue, String preferenceName) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
		return mSharedPreferences.getLong(key, defValue);
	}

	public static void clear() {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		mSharedPreferences.edit().clear().apply();
	}

	public static void clear(String preferenceName) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(preferenceName, Activity.MODE_PRIVATE);
		mSharedPreferences.edit().clear().apply();
	}

	public static void putMap(String key, String value) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		mSharedPreferences.edit().putString(key, value).commit();
	}


	public static String getMapValue(String key, String defValue) {
		Context context = App.getAppContext();
		mSharedPreferences = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
		return mSharedPreferences.getString(key, defValue);
	}



	/*================  User Preferences  ================*/

	private final static String KEY_USER = "USER";

	public static void setUser(UserDTO user) {
		putString(KEY_USER, new Gson().toJson(user));
	}

	public static UserDTO getUser() {
		UserDTO user = null;
		try {
			user = new Gson().fromJson(getString(KEY_USER, null), UserDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}


	/*================  Server Configuration  ================*/


	private final static String KEY_USER_CONFIG = "CONFIG";

	public static void setConfig(ConfigurationDTO config) {
		putString(KEY_USER_CONFIG, new Gson().toJson(config));
	}

	public static ConfigurationDTO getConfg() {
		ConfigurationDTO config = null;
		try {
			config = new Gson().fromJson(getString(KEY_USER_CONFIG, null), ConfigurationDTO.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return config;
	}

	private final static String KEY_USER_Multiple_CONFIG = "MultiCONFIG";
	public static void setMultipleConfiguration(HashMap<String, String> testHashMap)
	{
		putString(KEY_USER_Multiple_CONFIG, new Gson().toJson(testHashMap));

	}

	public static HashMap getMultipleConfg() {
		HashMap<String, String> testHashMap = null;
		try {
			String storedHashMapString = getString(KEY_USER_Multiple_CONFIG, "oopsDintWork");
			java.lang.reflect.Type type = new TypeToken<HashMap<String, String>>(){}.getType();
			testHashMap = new Gson().fromJson(storedHashMapString, type);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return testHashMap;
	}



	private final static String KEY_PRINTER = "Printer";
	public static void setConfig(String  printerName) {
		putString(KEY_PRINTER, new Gson().toJson(printerName));
	}

	public static String  getPrinterType() {
		String printerType = null;
		try {
			printerType = new Gson().fromJson(KEY_PRINTER,null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return printerType;
	}




}
