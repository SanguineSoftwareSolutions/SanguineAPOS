package com.example.apos.config;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.apos.App;
import com.example.apos.activity.R;

import butterknife.ButterKnife;

public class CommonUtils {

	public static void hideKeyboard(EditText editText) {
		try {
			InputMethodManager imm = (InputMethodManager) App.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
		} catch (Exception e) {
		}
	}

	public static void showKeyboard(EditText editText) {
		showKeyboard(editText, false);
	}

	public static void showKeyboard(EditText editText, boolean isForced) {
		try {
			InputMethodManager imm = (InputMethodManager) App.getAppContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			if (isForced) {
				imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
				editText.requestFocus();
			} else {
				imm.showSoftInputFromInputMethod(editText.getWindowToken(), 0);
			}
		} catch (Exception e) {
		}
	}

	public static Dialog getProgressDialog(Activity activity, int titleText, boolean cancelableFlag) {
		if (titleText == 0)
			titleText = R.string.text_loading;
		final Dialog dialog = new Dialog(activity, R.style.DialogBackground);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		dialog.setContentView(R.layout.progress_bar);
		dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationSlide;
		dialog.setCancelable(cancelableFlag);
		TextView title = ButterKnife.findById(dialog, R.id.upgrade_title_textview);
		title.setText(activity.getResources().getString(titleText));
		return dialog;
	}

	public static String getFontName(int index) {
		String font = "Regular.ttf";
		switch (index) {
			case 2:
				font = "Light.ttf";
				break;
			case 3:
				font = "Bold.ttf";
				break;
		}
		return font;
	}



}
