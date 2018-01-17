package com.example.apos.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.adapter.clsPOSSelectionAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsCounterBeen;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.bean.clsKotMenuItemsBean;
import com.example.apos.bean.clsPosSelectionMaster;
import com.example.apos.bean.clsSettlementDtl;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.dto.clsGlobalDTO;
import com.example.apos.util.clsUtility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class clsPOSSelection extends Activity implements clsPOSSelectionAdapter.customButtonListener {

	private TextView tvHeaderClassname;
	private ArrayList<String> posName;
	private GridView gridview;
	ArrayList arrListPosMaster = new ArrayList();
	private ConnectivityManager connectivityManager;
	private Dialog pgDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.posselectionscreen);
		widgetInit();
		gridview = (GridView) findViewById(R.id.grid_view);
		if(!clsGlobalFunctions.gAPOSWebSrviceURL.contains("prjSanguineWebService/APOSIntegration"))
		{
			clsGlobalFunctions.gAPOSWebSrviceURL=clsGlobalFunctions.gAPOSWebSrviceURL+"prjSanguineWebService/APOSIntegration";
		}

		clsGlobalFunctions.gHashMapSettlementDtl = new HashMap<String, List<clsSettlementDtl>>();
		clsGlobalFunctions.gHashMapCounterDtl = new HashMap<String, List<clsCounterBeen>>();
		if (new clsUtility().isInternetConnectionAvailable(connectivityManager)) {
			// new POSWebService().execute();
			funGetPOSList();

		} else {
			Toast.makeText(clsPOSSelection.this, "Internet Connection Not Found", Toast.LENGTH_LONG).show();
		}
	}

	private void widgetInit() {
		connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		tvHeaderClassname = (TextView) findViewById(R.id.tv_direct_bill_header_class_name);
	}

	public void onStart() {
		super.onStart();
	}

	public void doWork() {
		runOnUiThread(new Runnable() {
			public void run() {
				try {
					TextView txtCurrentTime = (TextView) findViewById(R.id.tv_direct_bill_header_timestamp);
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd  HH:mm").format(Calendar.getInstance().getTime());
					txtCurrentTime.setText(formattedDate);
				} catch (Exception e) {
				}
			}
		});
	}

	@Override
	public void onBackPressed()
	{
		AlertDialog.Builder builder1 = new AlertDialog.Builder(clsPOSSelection.this);
		builder1.setMessage("Do You Want To Exit???");
		builder1.setCancelable(true);
		builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id)
					{
						clsGlobalFunctions.gCallingAvailable = "No";
						clsGlobalFunctions.gHashMapItemList.clear();
						clsGlobalFunctions.gArrListMenuItemMaster.clear();
						Intent loginIntent = new Intent(clsPOSSelection.this, clsLoginActivity.class);
						startActivity(loginIntent);
					}
				}
		);
		builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id)
					{
						dialog.cancel();
					}
				}
		);
		AlertDialog alert11 = builder1.create();
		alert11.show();
	}

	@Override
	public void onButtonClickListner(int position, String value) {
		Toast.makeText(clsPOSSelection.this, "client code is" + clsGlobalFunctions.gClientCode, Toast.LENGTH_LONG).show();
		clsPosSelectionMaster objPOSMaster = (clsPosSelectionMaster) arrListPosMaster.get(position);
		clsGlobalFunctions.gPOSCode = objPOSMaster.getStrPosCode();
		clsGlobalFunctions.gPOSName = objPOSMaster.getStrPosName();
		clsGlobalFunctions.gCounterWiseBilling = objPOSMaster.getStrCounterWiseBilling();
		clsGlobalFunctions.gPrintPOSVatNo = objPOSMaster.getStrPrintVatNo();
		clsGlobalFunctions.gPrintPOSServiceTaxNo = objPOSMaster.getStrPrintServiceTaxNo();
		clsGlobalFunctions.gPOSVatNo = objPOSMaster.getStrVatNo();
		clsGlobalFunctions.gPOSServiceTaxNo = objPOSMaster.getStrServiceTaxNo();
		//new InitSetupWebService().execute(objPOSMaster.getStrPosCode());
		funInitSetupWebService(objPOSMaster.getStrPosCode());
		funGetItemPriceDtlList();
		funGetMenuHeadDetailList();
		Intent intent = new Intent(clsPOSSelection.this, clsMainMenu.class);
		intent.putExtra("PosName", objPOSMaster.getStrPosName());
		intent.putExtra("PosCode", objPOSMaster.getStrPosCode());
		startActivity(intent);
		finish();
	}

	private void funGrid(ArrayList arrList) {
		clsPOSSelectionAdapter posAdapter = new clsPOSSelectionAdapter(this, arrListPosMaster);
		posAdapter.setCustomButtonListner(this);
		gridview.setAdapter(posAdapter);
	}

	public void funInitSetupWebService(String POSCode) {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				showDialog();
				App.getAPIHelper().funGetSetupValues(POSCode, new BaseAPIHelper.OnRequestComplete<clsGlobalDTO>() {
					@Override
					public void onSuccess(clsGlobalDTO object) {
						dismissDialog();
						if (null != object) {
							String setupStatus = "";
							if (object.getStatus().equals("Error")) {
								setupStatus = "Error";
								Toast.makeText(clsPOSSelection.this, "Required Stucture Update", Toast.LENGTH_LONG).show();
							} else {
								object.savePreference();
								setupStatus = "Success";
							}
						}
					}

					@Override
					public void onFailure(String errorMessage, int errorCode) {
						dismissDialog();
					}
				});
			} else {
				SnackBarUtils.showSnackBar(this, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(this, R.string.setup_your_server_settings);
		}
	}

	/**
	 * HTTP request for pos-list.
	 */

	private void funGetPOSList() {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				showDialog();
				App.getAPIHelper().funGetPOSList(clsGlobalFunctions.gUserCode, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsPosSelectionMaster>>() {
					@Override
					public void onSuccess(ArrayList<clsPosSelectionMaster> obj) {
						dismissDialog();
						if (null != obj) {
							try {
								arrListPosMaster = obj;

								clsGlobalFunctions.gHashMapSettlementDtl.clear();
								clsGlobalFunctions.gHashMapCounterDtl.clear();

								ArrayList<String> poslist = new ArrayList<String>();
								for (int cnt = 0; cnt < arrListPosMaster.size(); cnt++) {
									clsPosSelectionMaster objPosMaster = (clsPosSelectionMaster) arrListPosMaster.get(cnt);
									poslist.add(objPosMaster.getStrPosName());
									clsGlobalFunctions.gHashMapSettlementDtl.put(objPosMaster.getStrPosCode(), objPosMaster.getArrListSettleMentDtl());
									clsGlobalFunctions.gHashMapCounterDtl.put(objPosMaster.getStrPosCode(), objPosMaster.getArrListCounterDtl());
								}
								funGrid(poslist);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
					@Override
					public void onFailure(String errorMessage, int errorCode) {
						dismissDialog();
					}
				});
			} else {
				SnackBarUtils.showSnackBar(clsPOSSelection.this, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(clsPOSSelection.this, R.string.setup_your_server_settings);
		}
	}


	public void funGetItemPriceDtlList() {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				clsGlobalFunctions objGlobal = new clsGlobalFunctions();
				showDialog();
				App.getAPIHelper().funGetItemPriceDtlList(clsGlobalFunctions.gPOSCode,objGlobal.funGetCurrentDate(), objGlobal.funGetCurrentDate(), new BaseAPIHelper.OnRequestComplete<ArrayList<clsKotItemsListBean>>() {
					@Override
					public void onSuccess(ArrayList<clsKotItemsListBean> arrListTemp) {
						dismissDialog();
						String pricingDay = new clsUtility().funGetDayForPricing();
						ArrayList<clsKotItemsListBean> arrListItemMastertemp = new ArrayList();
						if (null != arrListTemp)
						{
							clsKotItemsListBean obj = new clsKotItemsListBean();
							if (arrListTemp.size() > 0)
							{
								for (int i = 0; i < arrListTemp.size(); i++)
								{
									obj = arrListTemp.get(i);
									obj.setDblSalePrice(new clsUtility().funGetitemPriceOnDay(pricingDay, obj));

									if(clsGlobalFunctions.gHashMapItemList.size()>0)
									{
										if(clsGlobalFunctions.gHashMapItemList.containsKey(obj.getAreaCode()))
										{
											arrListItemMastertemp=clsGlobalFunctions.gHashMapItemList.get(obj.getAreaCode());
											arrListItemMastertemp.add(obj);
										}
										else
										{
											arrListItemMastertemp = new ArrayList();
											arrListItemMastertemp.add(obj);
										}
									}
									else
									{
										arrListItemMastertemp = new ArrayList();
										arrListItemMastertemp.add(obj);
									}
									clsGlobalFunctions.gHashMapItemList.put(obj.getAreaCode(),arrListItemMastertemp);
								}
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


	public void funGetMenuHeadDetailList() {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				showDialog();
				App.getAPIHelper().funGetMenuHeadList(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
					@Override
					public void onSuccess(JsonObject jObj) {
						dismissDialog();
						if (null != jObj)
						{
							funFillList(jObj);
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


	public void funFillList(JsonObject jObj) {
		if (null != jObj) {
			try {
				JsonArray mJsonArray = (JsonArray) jObj.get("MenuHeadList");
				JsonObject mJsonObject = new JsonObject();
				for (int i = 0; i < mJsonArray.size(); i++) {
					mJsonObject = (JsonObject) mJsonArray.get(i);
					if (mJsonObject.get("MenuName").getAsString().equals("")) {
						//memberInfo = "no data";
					} else {
						clsKotMenuItemsBean obj = new clsKotMenuItemsBean();
						obj.setStrMenuItemName(mJsonObject.get("MenuName").getAsString());
						obj.setStrMenuItemCode(mJsonObject.get("MenuCode").getAsString());
						obj.setStrMenuType("MenuHead");
						clsGlobalFunctions.gArrListMenuItemMaster.add(obj);
					}
				}


			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}


	/**
	 * Progressbar methods for show and dismiss.
	 */
	protected void showDialog() {
		if (null == pgDialog) {
			pgDialog = CommonUtils.getProgressDialog(this, 0, false);
		}
		pgDialog.show();
	}

	protected void dismissDialog() {
		if (null != pgDialog) {
			pgDialog.dismiss();
		}
	}

}

