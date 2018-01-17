package com.example.apos.views;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.apos.activity.R;
import com.example.apos.config.CommonUtils;

import butterknife.ButterKnife;

/**
 * Created by Abhilash on 14/9/17.
 */

public class CustomSearchView extends RelativeLayout {

	RelativeLayout searchLabelPanel, searchProcessPanel, mSearchParentLayout;
	AppCompatImageView ivCross;
	EditText etSearch;
	TextView btnSearch;


	public CustomSearchView(Context context) {
		super(context);
		init();
	}

	public CustomSearchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public CustomSearchView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init();
	}

	private void init() {
		removeAllViews();
		final RelativeLayout searchParentLayout = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.layout_search_view, null);
		mSearchParentLayout = searchParentLayout;
		searchLabelPanel = (RelativeLayout) searchParentLayout.findViewById(R.id.searchLabelPanel);
		searchProcessPanel = (RelativeLayout) searchParentLayout.findViewById(R.id.searchPanel);
		btnSearch = ButterKnife.findById(searchParentLayout, R.id.btnSearch);
		ivCross = (AppCompatImageView) searchParentLayout.findViewById(R.id.searchCancel);
		etSearch = (EditText) searchParentLayout.findViewById(R.id.etSearch);
		searchLabelPanel.setVisibility(VISIBLE);
		searchProcessPanel.setVisibility(INVISIBLE);

//		etSearch.requestFocus() ;
//		etSearch.setCursorVisible(true);
//		CommonUtils.showKeyboard(etSearch,true);

		ivCross.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				if (etSearch.getText().toString().length() == 0) {
					searchLabelPanel.setVisibility(VISIBLE);
					searchProcessPanel.setVisibility(INVISIBLE);
				} else {
					searchLabelPanel.setVisibility(INVISIBLE);
					searchProcessPanel.setVisibility(VISIBLE);
					etSearch.setText("");
				}
			}
		});

		btnSearch.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				searchLabelPanel.setVisibility(INVISIBLE);
				searchProcessPanel.setVisibility(VISIBLE);
				etSearch.requestFocus();
				InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.toggleSoftInputFromWindow(searchParentLayout.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
			}
		});

		searchLabelPanel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
				searchLabelPanel.setVisibility(INVISIBLE);
				searchProcessPanel.setVisibility(VISIBLE);
				etSearch.requestFocus();
				InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
				inputMethodManager.toggleSoftInputFromWindow(searchParentLayout.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
			}
		});

		addView(searchParentLayout);
	}

	public boolean isSearchLabelViewVisible() {
		if (searchLabelPanel.getVisibility() == VISIBLE) {
			return true;
		}
		return false;
	}

	public void clearSearchText() {
		if (!isSearchLabelViewVisible()) {
			searchLabelPanel.setVisibility(VISIBLE);
			searchProcessPanel.setVisibility(INVISIBLE);
			etSearch.setText("");
			etSearch.requestFocus();
		}
	}

	public EditText getEditText() {
		return etSearch;
	}

	public void requestEdit() {
		if (null != mSearchParentLayout) {
			searchLabelPanel.setVisibility(INVISIBLE);
			searchProcessPanel.setVisibility(VISIBLE);
			etSearch.requestFocus();
			InputMethodManager inputMethodManager = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			inputMethodManager.toggleSoftInputFromWindow(mSearchParentLayout.getApplicationWindowToken(), InputMethodManager.SHOW_FORCED, 0);
		}
	}
}
