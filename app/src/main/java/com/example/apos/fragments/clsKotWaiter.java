package com.example.apos.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.GridView;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsKotScreen;
import com.example.apos.adapter.clsKotWaiterAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsWaiterMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsKotWaiterListSelectionListener;
import com.example.apos.listeners.clsMakeKotActListener;
import com.example.apos.views.CustomSearchView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class clsKotWaiter extends Fragment implements clsKotWaiterListSelectionListener {
	@BindView(R.id.WaiterCustomSearchView)
	CustomSearchView customSearchView;
	private clsKotWaiterListSelectionListener waiterSelectionListener;
	private EditText edtKOTWaitersearch;
	private GridView kotwaiterGridview;
	private List<clsWaiterMaster> listKotWaiter;
	public ArrayList arrListWaiterMaster;
	private String keyCase = "upperCase";
	public clsMakeKotActListener objMakeKotActListener;
	private ConnectivityManager connectivityManager;
	private Dialog pgDialog;
	Activity activity;

	public static clsKotWaiter getInstance() {
		clsKotWaiter mKotWaiter = new clsKotWaiter();
		return mKotWaiter;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		activity=getActivity();
		//getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		View rootView = inflater.inflate(R.layout.kotwaiterscreen, container, false);
		getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
		ButterKnife.bind(getActivity());
		try {
			waiterSelectionListener = (clsKotWaiterListSelectionListener) clsKotWaiter.this;
			objMakeKotActListener = (clsMakeKotActListener) getActivity();
		} catch (Exception ex) {
			Log.i("Tg", " Unable to initialize database." + ex.getMessage());
		}


		kotwaiterGridview = (GridView) rootView.findViewById(R.id.kotwaiter_gridview);


		funGetWaiterList();

		customSearchView=(CustomSearchView) rootView.findViewById(R.id.WaiterCustomSearchView);
        edtKOTWaitersearch = customSearchView.getEditText();

        edtKOTWaitersearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
				if(s.toString()!=null&&arrListWaiterMaster!=null){
					ArrayList arrayListtemp = new ArrayList();
					for (int cnt = 0; cnt <arrListWaiterMaster.size(); cnt++)
					{
						clsWaiterMaster obj = (clsWaiterMaster) arrListWaiterMaster.get(cnt);
						if (obj.getStrWaiterName().toLowerCase().contains(s.toString().toLowerCase()))
						{
							arrayListtemp.add(arrListWaiterMaster.get(cnt));
						}

					}
					funFillWaiterGrid(arrayListtemp);
				}
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


		return rootView;
	}


	@Override
	public void getWaiterListSelected(String strWaiterNo, String strWaiterName) {
		// clsKotScreen.setWaiterSelectedResult(strWaiterNo, strWaiterName);
		objMakeKotActListener.setWaiterSelectedResult(strWaiterNo, strWaiterName);
		// edtKOTWaitersearch.setText(" ");
		funFillWaiterGrid(arrListWaiterMaster);


	}

	private void funFillWaiterGrid(ArrayList arrListWaiterMaster) {
		kotwaiterGridview.setAdapter(new clsKotWaiterAdapter(getActivity(), this.getActivity(), arrListWaiterMaster, waiterSelectionListener));

	}


	public void funGetWaiterList() {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				showDialog();
				App.getAPIHelper().funGetWaiterList(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gUserCode, true, new BaseAPIHelper.OnRequestComplete<ArrayList<clsWaiterMaster>>() {
					@Override
					public void onSuccess(ArrayList<clsWaiterMaster> arrListTemp) {
						dismissDialog();
						if (null != arrListTemp) {
							if (arrListTemp.size() > 0) {
								arrListWaiterMaster = arrListTemp;
								funFillWaiterGrid(arrListWaiterMaster);
							}
						}
					}

					@Override
					public void onFailure(String errorMessage, int errorCode) {
						dismissDialog();
					}
				});
			} else {
				SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.setup_your_server_settings);
		}
	}


	protected void showDialog() {
		if (null == pgDialog) {
			pgDialog = CommonUtils.getProgressDialog(activity, 0, false);
		}
		pgDialog.show();
	}

	protected void dismissDialog() {
		if (null != pgDialog) {
			pgDialog.dismiss();
		}
	}
}