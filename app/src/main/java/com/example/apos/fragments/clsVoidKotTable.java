package com.example.apos.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsMakeBillScreen;
import com.example.apos.activity.clsVoidKotScreen;
import com.example.apos.adapter.clsVoidKotTableAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsVoidKotActListener;
import com.example.apos.listeners.clsVoidKotTableListener;
import com.example.apos.util.clsUtility;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class clsVoidKotTable extends Fragment implements clsVoidKotTableListener {
	private clsVoidKotTableListener tableSelectionListener;
	private Spinner spinnerTableList;
	private GridView kotTableGridview;
	ArrayList arrListTableMaster;
	ArrayList arrListKotMaster;
	private static String tableNo;
	public clsVoidKotActListener objVoidKotActListener;
	private ConnectivityManager connectivityManager;
	private Dialog pgDialog;
	Activity mActivity;

	public static clsVoidKotTable getInstance() {
		clsVoidKotTable mKotTable = new clsVoidKotTable();
		return mKotTable;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.voidkottable, container, false);
		mActivity= clsVoidKotScreen.mActivity;
		connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		try {
			tableSelectionListener = (clsVoidKotTableListener) clsVoidKotTable.this;
			objVoidKotActListener = (clsVoidKotActListener) getActivity();
		} catch (Exception ex) {
			Log.i("Tg", " Unable to initialize database." + ex.getMessage());
		}

		kotTableGridview = (GridView) rootView.findViewById(R.id.voidkottable_gridview);

		funGetBusyTableList();


		return rootView;
	}


	@Override
	public void getTableListSelected(String tableNo, String tableName) {
		objVoidKotActListener.funSetSelectedKotTableData(tableNo, tableName);
	}



	private void funFillTableGrid(ArrayList arrListTableMaster)
	{
		kotTableGridview.setAdapter(new clsVoidKotTableAdapter(getActivity(), this.getActivity(), arrListTableMaster, tableSelectionListener));

	}

	public void funGetBusyTableList()
	{
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected())
			{
				showDialog();
				App.getAPIHelper().funGetBusyTableList(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gCMSIntegrationYN, clsGlobalFunctions.gTreatMemberAsTable, new BaseAPIHelper.OnRequestComplete<ArrayList<clsTableMaster>>() {
					@Override
					public void onSuccess(ArrayList<clsTableMaster> arrListTemp) {
						dismissDialog();
						if (null != arrListTemp) {
							if (arrListTemp.size() > 0) {
								arrListTableMaster = arrListTemp;
								ArrayList<String> tablelist = new ArrayList<String>();
								for (int cnt = 0; cnt < arrListTemp.size(); cnt++) {
									clsTableMaster objTable = (clsTableMaster) arrListTemp.get(cnt);
									tablelist.add(objTable.getStrTableName());
									tableNo = objTable.getStrTableNo();
								}
                                dismissDialog();
								funFillTableGrid(arrListTableMaster);
							}else{
								dismissDialog();
							}
						}
					}

					@Override
					public void onFailure(String errorMessage, int errorCode) {
						dismissDialog();
					}
				});
			} else {
				SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
		}
	}


	/**
	 * Progressbar methods for show and dismiss.
	 */
	protected void showDialog() {
		if (null == pgDialog) {
			pgDialog = CommonUtils.getProgressDialog(mActivity, 0, false);
		}
		pgDialog.show();
	}

	protected void dismissDialog() {
		if (null != pgDialog) {
			pgDialog.dismiss();
		}
	}

}
