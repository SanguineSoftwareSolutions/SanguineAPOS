package com.example.apos;

import android.app.Application;

import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.api.APIHelper;


public class App extends Application {

	private static App instance;
	private static APIHelper apiHelper;

	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
		apiHelper = APIHelper.init(this);
	}


	public static App getAppContext() {
		return instance;
	}

	public static synchronized APIHelper getAPIHelper()
	{
		if(clsGlobalFunctions.gAPOSWebSrviceURL!=null)
		{
			if(clsGlobalFunctions.gAPOSWebSrviceURL.contains("prjSanguineWebService/APOSIntegration"))
			{
				String[] newUrl = clsGlobalFunctions.gAPOSWebSrviceURL.toString().split("prjSanguineWebService");
				clsGlobalFunctions.gAPOSWebSrviceURL=newUrl[0];
			}
		}
		return apiHelper;
	}

	public static synchronized void refreshBaseURL() {
		APIHelper.refreshBase();
		apiHelper = APIHelper.init(getAppContext());
	}
}
