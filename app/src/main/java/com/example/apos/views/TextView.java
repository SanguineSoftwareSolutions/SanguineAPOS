package com.example.apos.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

import com.example.apos.activity.R;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.TypefaceLoader;

public class TextView extends AppCompatTextView {

	private Context context;
	private String font = "Regular.ttf";

	public TextView(Context context) {
		super(context);
		this.context = context;
		init();
	}

	public TextView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
		init(context, attrs);
	}

	public TextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		init(context, attrs);
	}

	private void init() {
		if (!isInEditMode()) {
			Typeface typeface = TypefaceLoader.get(context, "fonts/" + font);
			setTypeface(typeface);
		}
	}

	private void init(Context context, AttributeSet attrs) {
		if (!isInEditMode()) {
			TypedArray styledAttrs = context.obtainStyledAttributes(attrs, R.styleable.TextView);
			int index = styledAttrs.getInt(R.styleable.TextView_customTypeface, 1);
			styledAttrs.recycle();
			Typeface typeface = TypefaceLoader.get(context, "fonts/" + CommonUtils.getFontName(index));
			setTypeface(typeface);
		}
	}
}
