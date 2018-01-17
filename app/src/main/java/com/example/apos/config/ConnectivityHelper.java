package com.example.apos.config;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.apos.App;


public class ConnectivityHelper {

	/**
	 * Get the network info
	 *
	 * @return {@link NetworkInfo}
	 */
	public static NetworkInfo getNetworkInfo() {
		ConnectivityManager cm = (ConnectivityManager) App.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		return cm.getActiveNetworkInfo();
	}

	/**
	 * Check if there is any connectivity
	 *
	 * @return true when the device is connected to any data network false otherwise
	 */
	public static boolean isConnected() {
		NetworkInfo info = ConnectivityHelper.getNetworkInfo();
		return (info != null && info.isConnected());
	}

	/**
	 * Check if there is any connectivity to a Wifi network
	 *
	 * @return true when the device is connected to a wifi network false otherwise
	 */
	public static boolean isConnectedWifi() {
		NetworkInfo info = ConnectivityHelper.getNetworkInfo();
		return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_WIFI);
	}

}
