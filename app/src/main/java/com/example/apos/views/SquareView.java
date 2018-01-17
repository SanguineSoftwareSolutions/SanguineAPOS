package com.example.apos.views;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Abhilash
 */

public class SquareView extends RelativeLayout {

	public SquareView(Context context) {
		super(context);
	}

	public SquareView(Context context, @Nullable AttributeSet attrs) {
		super(context, attrs);
	}

	public SquareView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int width = getMeasuredWidth();
		setMeasuredDimension(width, width);
	}
}
