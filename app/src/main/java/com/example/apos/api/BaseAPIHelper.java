package com.example.apos.api;

import android.support.annotation.StringRes;

import com.example.apos.App;
import com.example.apos.activity.BuildConfig;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.config.JsonHelper;
import com.example.apos.config.Logger;
import com.example.apos.config.PreferenceUtil;
import com.example.apos.config.StringUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class BaseAPIHelper {

	public interface OnRequestComplete<T> {
		void onSuccess(T object);

		void onFailure(String errorMessage, int errorCode);
	}

	protected App app;
	protected APIService apiService;

	// MIME type and Header Keys
	private static final String KEY_MULTIPART = "multipart";
	private static final String KEY_APPLICATION_JSON = "application/json";
	private static final String KEY_CONTENT_TYPE = "Content-Type";
	private static final String KEY_PLATFORM = "PLATFORM";
	private static final String KEY_PLATFORM_VALUE = "ANDROID_APP";

	// Response Code
	private static final int TEMP_CODE = -8;
	private static final int RESULT_INVALID_USER = 401;
	private static final int RESULT_SUCCESS = 200;
	private static final int RESULT_SUCCESS_SERVER_CONNECTION = 201;


	public void setApplication(App application) {
		this.app = application;
	}

	protected void createRestAdapter() {
		OkHttpClient.Builder httpClientBuilder = new OkHttpClient.Builder();
		httpClientBuilder.readTimeout(1, TimeUnit.MINUTES);
		httpClientBuilder.writeTimeout(1, TimeUnit.MINUTES);

		if (BuildConfig.DEBUG) {
			HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
			loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
			httpClientBuilder.addInterceptor(loggingInterceptor);
		}

		httpClientBuilder.addInterceptor(new Interceptor() {
			@Override
			public okhttp3.Response intercept(Chain chain) throws IOException {
				Request request = chain.request();
				Request.Builder requestBuilder = request.newBuilder();
				String contentType = request.header(KEY_CONTENT_TYPE);

				if (null == contentType || !contentType.contains(KEY_MULTIPART)) {
					requestBuilder.header(KEY_CONTENT_TYPE, KEY_APPLICATION_JSON);
				}

				requestBuilder.addHeader(KEY_PLATFORM, KEY_PLATFORM_VALUE);

				requestBuilder.method(request.method(), request.body());
				request = requestBuilder.build();
				if (Logger.isLogEnabled()) {
					Logger.info("Headers >> ");
					for (String name : request.headers().names()) {
						Logger.debug("\n" + name + " --> " + request.headers().get(name));
					}
				}
				return chain.proceed(request);
			}
		});

		Retrofit retrofit = new Retrofit.Builder()
				.client(httpClientBuilder.build())
				.addConverterFactory(GsonConverterFactory.create())
				.baseUrl(getBaseURL())
				.build();
		apiService = retrofit.create(APIService.class);
	}

	protected synchronized ServerError getRawServerError(Response response) {
		return new ServerError(response.code(), response.message());
	}

	protected synchronized ServerError getServerError(@StringRes int stringId) {
		return new ServerError(-1, app.getString(stringId));
	}

	protected synchronized ServerError getServerError(String message) {
		return new ServerError(-1, message);
	}

	protected synchronized ServerError getServerError() {
		return new ServerError(-1, "We apologize for the inconvenience. Please try again later");
	}

	protected abstract class ResponseHandler<T> implements Callback<T> {

		OnRequestComplete mOnRequestComplete;
		String mLogTag = "ResponseHandler";

		ResponseHandler(final OnRequestComplete onRequestComplete) {
			mOnRequestComplete = onRequestComplete;
		}

		ResponseHandler(final OnRequestComplete onRequestComplete, String logTag) {
			mOnRequestComplete = onRequestComplete;
			mLogTag = logTag;
		}

		@Override
		public void onResponse(Call<T> call, Response<T> response) {
			if (null != response) {
				try {
					switch (response.code()) {
						case RESULT_INVALID_USER:
							onFailure(response);
							break;
						case RESULT_SUCCESS:
							onSuccess(response);
							break;
						case RESULT_SUCCESS_SERVER_CONNECTION:
							onSuccess(response);
							break;
						default:
							onFailure(response);
							break;
					}
				} catch (Exception e) {
					e.printStackTrace();
					onException(e);
				}
			} else {
				onFailure();
			}
		}

		@Override
		public void onFailure(Call<T> call, Throwable t) {
			onRetrofitFailure(t);
		}

		public abstract void onSuccess(Response<T> response) throws Exception;

		protected void onException(Exception e) {
			mOnRequestComplete.onFailure(getServerError().getMessage(), TEMP_CODE);
			Logger.error("BaseAPIHelper : " + mLogTag + " : onException(): " + e.getMessage());
			e.printStackTrace();
		}

		public void onFailure(Response response) {
			String errorMessage;
			try {
				errorMessage = JsonHelper.getErrorMessage(response.errorBody());
			} catch (Exception e) {
				errorMessage = e.getMessage();
			}
			mOnRequestComplete.onFailure(errorMessage, response.code());
			Logger.error("BaseAPIHelper : " + mLogTag + " :  onFailure(): " + errorMessage);
		}

		public void onFailure() {
			mOnRequestComplete.onFailure(getServerError().getMessage(), TEMP_CODE);
			Logger.error("BaseAPIHelper : " + mLogTag + " :  onFailure(): Getting null response from the server");
		}

		public void onFailure(String errorMsg) {
			mOnRequestComplete.onFailure(errorMsg, TEMP_CODE);
			Logger.error("BaseAPIHelper : " + mLogTag + " :  onFailure(): Server custom error");
		}

		protected void onRetrofitFailure(Throwable t) {
			if (null != t) {
				Logger.error("BaseAPIHelper : " + mLogTag + " : onRetrofitFailure(): " + t.getMessage());
			} else {
				Logger.error("BaseAPIHelper : " + mLogTag + " :  onRetrofitFailure(): Throwable Object is null");
			}
			mOnRequestComplete.onFailure(getServerError().getMessage(), TEMP_CODE);
		}
	}

	private String getBaseURL() {
		if (null != PreferenceUtil.getConfg())
		{
			clsGlobalFunctions.gAPOSWebSrviceURL = PreferenceUtil.getConfg().getBaseURL();

		}
		String URL = clsGlobalFunctions.gAPOSWebSrviceURL;
		if (StringUtils.isEmpty(URL)) {
			URL = App.getAppContext().getString(R.string.base_url);
		}
		return URL;
	}
}
