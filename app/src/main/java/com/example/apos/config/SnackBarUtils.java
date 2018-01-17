package com.example.apos.config;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.view.View;


public class SnackBarUtils {

	public static synchronized void showSnackBar(@NonNull Activity activity, String msg) {
		if (null != msg && null != activity) {
			Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
		}
	}

	public static synchronized void showSnackBar(@NonNull Activity activity, int stringResId) {
		if (null != activity) {
			String msg = activity.getString(stringResId);
			Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG).show();
		}
	}

	public static synchronized void showSnackBar(@NonNull View view, String msg) {
		if (null != msg && null != view) {
			Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
		}
	}

	public static synchronized void showSnackBar(@NonNull View view, int stringResId) {
		if (null != view) {
			String msg = view.getContext().getString(stringResId);
			Snackbar.make(view, msg, Snackbar.LENGTH_LONG).show();
		}
	}

	public static synchronized Snackbar getSnackBarWithTryAgainButton(@NonNull Activity activity, int stringResId) {
		Snackbar mSnackbar = null;
		if (null != activity) {
			String msg = activity.getString(stringResId);
			mSnackbar = Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_LONG);
		}
		return mSnackbar;
	}

	public static synchronized Snackbar getSnackBarWithTryAgainButton(@NonNull Activity activity, String msg) {
		Snackbar mSnackbar = null;
		if (null != activity) {
			mSnackbar = Snackbar.make(activity.findViewById(android.R.id.content), msg, Snackbar.LENGTH_INDEFINITE);
		}
		return mSnackbar;
	}
}
