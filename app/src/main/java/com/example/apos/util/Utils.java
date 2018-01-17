package com.example.apos.util;

import android.util.Log;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
	
	public static String getCurrentDate() {
		String strDate = " ";
		try{
			DateFormat formater = new SimpleDateFormat("dd-MM-yy HH:mm:ss");
			Date date = new Date();
			strDate = formater.format(date);
			/*System.out.println("Simpel date format" + strDate);*/
		} catch(Exception ex){
			Log.i("Bewo_sign_on", "uable to get date." + ex.getMessage().toString());
		}
		return strDate;
	}

}
