package com.example.apos.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bewo.mach.tools.MACHServices;
import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsReprintDocuments;
import com.example.apos.adapter.clsReprintKotItemListAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsBillListDtl;
import com.example.apos.bean.clsItemDtl;
import com.example.apos.bean.clsKOTItemDtlBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsReprintKotItemListSelectionListener;
import com.example.apos.util.clsPrintDemo;
import com.example.apos.util.clsUtility;
import com.example.apos.util.mach.clsPrintFormatAPI;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.IOException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class clsLoadItemListForReprintKot extends Fragment implements clsReprintKotItemListSelectionListener, View.OnClickListener {
	private static ListView listKotItems;
	ArrayList arrListTableMaster;
	private static ArrayList<clsKOTItemDtlBean> arrListKOT;
	public static clsReprintKotItemListSelectionListener kotItemSelectionListener;
	public static clsGlobalFunctions objGlobal;
	static clsReprintKotItemListAdapter kotItemListAdapter;
	//public static List<clsKOTItemDtlBean> arrListKOTItems;
	private static TextView edtKotTotalAmount = null;
	public static String billNo;
	public static String tableNo;
	public static String posCode;
	private static double subTotalAmt;
	private Button  btnVoidKotPrint = null;
	private ConnectivityManager connectivityManager;
	MACHServices machService;
	private BluetoothAdapter mBluetoothAdapter;
	private Dialog pgDialog;
	Activity mActivity;


	public static clsLoadItemListForReprintKot getInstance() {
		clsLoadItemListForReprintKot mLoadKot = new clsLoadItemListForReprintKot();
		return mLoadKot;
	}


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

	}


	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.loadkotitemlistforreprint, container, false);
		mActivity= clsReprintDocuments.mActivity;
		if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer")) {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if (!mBluetoothAdapter.isEnabled()) {
				Toast.makeText(getActivity(), "Bluetooth is disable", Toast.LENGTH_LONG).show();

			} else {
				new clsPrintDemo().funPrintViaBluetoothPrinter(mBluetoothAdapter);
			}
		}
		connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
		btnVoidKotPrint = (Button) rootView.findViewById(R.id.btnReprintKotPrint);

		btnVoidKotPrint.setOnClickListener(this);

		edtKotTotalAmount = (TextView) rootView.findViewById(R.id.edt_reprint_kot_total_order_amount);

		try {
			kotItemSelectionListener = (clsReprintKotItemListSelectionListener) clsLoadItemListForReprintKot.this;
		} catch (Exception ex) {
			Log.i("Tg", " Unable to initialize database." + ex.getMessage());
		}
		subTotalAmt = 0;
		listKotItems = (ListView) rootView.findViewById(R.id.listReprintKotSelectedItems);

		if (new clsUtility().isInternetConnectionAvailable(connectivityManager)) {
			funLoadKotItemList("All", "All", "All","KOT");
		} else {
			Toast.makeText(getActivity(), "Internet Connection Not Found", Toast.LENGTH_LONG).show();
		}

		return rootView;
	}


	public static void funLoadKotItemList(String billNo1, String TableNo, String POSCode,String reprintOperation) {
		billNo = billNo1;
		tableNo = TableNo;
		posCode = POSCode;
		clsLoadItemListForReprintKot ob=new clsLoadItemListForReprintKot();
		if(reprintOperation.equals("Bill")){
			ob.funGetSelectedBillDetails(billNo);
		}
		else if(reprintOperation.equals("DirectBiller")){
			ob.funGetSelectedBillDetails(billNo);
		}
		else if(reprintOperation.equals("KOT")){
			ob.funLoadKotItemListWS(billNo, POSCode);
		}


	}


	private static void funFillListView(ArrayList arrlist) {
	kotItemListAdapter = new clsReprintKotItemListAdapter(clsReprintDocuments.mActivity, clsReprintDocuments.mActivity, arrlist, kotItemSelectionListener);
		listKotItems.setAdapter(kotItemListAdapter);
	}
	@Override
	public void getVoidKotItemList() {
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {

			case R.id.btnReprintKotPrint:
				if(!billNo.equals("")) {
					if (clsReprintDocuments.reprintOperation.equals("Bill")) {
						funGenerateBillTextFileWebService(billNo);
					} else if (clsReprintDocuments.reprintOperation.equals("DirectBiller")) {
						funGenerateDirectBillerKOTTextFileWebService(billNo);
					} else if (clsReprintDocuments.reprintOperation.equals("KOT")) {
						funGetKOTDataFromWS(billNo);
						funReprintKOT(posCode, tableNo, billNo);
					}
				}
				break;

			default:
				break;
		}


	}

	private void funClearObjects() {
		arrListKOT.clear();
		tableNo = "";
		posCode = "";
		subTotalAmt = 0;
		billNo = "";
		edtKotTotalAmount.setText("0.0");
		funFillListView(new ArrayList());
	}

	private void funReprintKOT(String... params)
	{
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected())
			{
				String posCode = params[0];
				String tableNo = params[1];
				String kotNo = params[2];
                clsUtility objUtility = new clsUtility();
                String deviceName=objUtility.funGetHostName();

				showDialog();
				App.getAPIHelper().funReprintKOT(posCode,tableNo, kotNo,deviceName, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
					@Override
					public void onSuccess(JsonObject jObj) {
						if (null != jObj)
						{
							System.out.print(jObj.get("msg").getAsString());
							funClearObjects();
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


	private void funPrintKot(JsonArray jArrItemDtl, JsonArray jArrModItemDtl, String costCenterName, String tableName, String KOTNo, String PaxNo) {
		try {

			StringBuilder sbPrintKot = new StringBuilder();
			clsGlobalFunctions objGlobal = new clsGlobalFunctions();
			clsPrintFormatAPI objPrint = new clsPrintFormatAPI();
			// System.out.println(clsGlobalFunctions.gPOSName+costCenterName+KOTNo+tableName);
			sbPrintKot.append(objPrint.funGetStringWithAlignment("KOT", "Left", 32));
			sbPrintKot.append("\n");
			sbPrintKot.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gPOSName, "Left", 32));
			sbPrintKot.append("\n");
			sbPrintKot.append(objPrint.funGetStringWithAlignment(costCenterName, "Left", 32));
			sbPrintKot.append("\n");
			sbPrintKot.append(objPrint.funGetStringWithAlignment("DINE", "Left", 32));
			sbPrintKot.append("\n");
			sbPrintKot.append("--------------------------------");
			sbPrintKot.append(objPrint.funGetStringWithAlignment("KOT NO: ", "Left", 9));
			sbPrintKot.append(objPrint.funGetStringWithAlignment(KOTNo, "Left", 23));
			sbPrintKot.append("\n");
			sbPrintKot.append(objPrint.funGetStringWithAlignment("Table Name: ", "Left", 15));
			sbPrintKot.append(objPrint.funGetStringWithAlignment(tableName, "Left", 8));
			sbPrintKot.append(objPrint.funGetStringWithAlignment("PAX: ", "Left", 5));
			sbPrintKot.append(objPrint.funGetStringWithAlignment(PaxNo, "Left", 4));
			sbPrintKot.append("\n");
			sbPrintKot.append(objPrint.funGetStringWithAlignment("DATE & TIME: ", "Left", 13));
			sbPrintKot.append(objPrint.funGetStringWithAlignment(objGlobal.funGetCurrentDateTime(), "Left", 19));
			sbPrintKot.append("\n");
			sbPrintKot.append("--------------------------------");
			sbPrintKot.append(objPrint.funGetStringWithAlignment("QTY ", "Left", 8));
			sbPrintKot.append(objPrint.funGetStringWithAlignment("ITEMNAME ", "Left", 22));
			sbPrintKot.append("\n");
			sbPrintKot.append("--------------------------------");

			for (int i = 0; i < jArrItemDtl.size(); i++) {
				JsonObject objRows = (JsonObject)jArrItemDtl.get(i);
				sbPrintKot.append(objPrint.funGetStringWithAlignment(objRows.get("ItemQty").getAsString(), "Left", 8));
				sbPrintKot.append(objPrint.funGetStringWithAlignment(objRows.get("ItemName").getAsString(), "Left", 22));
				sbPrintKot.append("\n");
			}
			if (jArrModItemDtl.size() > 0) {
				for (int i = 0; i < jArrModItemDtl.size(); i++) {
					JsonObject objRows = (JsonObject)jArrModItemDtl.get(i);
					sbPrintKot.append(objPrint.funGetStringWithAlignment(objRows.get("ModItemQty").getAsString(), "Left", 8));
					sbPrintKot.append(objPrint.funGetStringWithAlignment(objRows.get("ModItemName").getAsString(), "Left", 22));
					sbPrintKot.append("\n");
				}
			}
			sbPrintKot.append("--------------------------------");

			sbPrintKot.append("\n");
			sbPrintKot.append("\n");
			sbPrintKot.append("\n");
			sbPrintKot.append("\n");


			if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer")) {

				try {
					new clsPrintDemo().sendData(sbPrintKot.toString(), clsPrintDemo.mmOutputStream, clsPrintDemo.mmInputStream);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				sbPrintKot.append("\n");
				sbPrintKot.append("\n");
				sbPrintKot.append("\n");
				sbPrintKot.append("\n");
				sbPrintKot.append("\n");
				sbPrintKot.append("\n");
				System.out.println("print kot:" + sbPrintKot.toString());
				//new clsReprintKotScreen().funPrintBillForMachDevice(sbPrintKot.toString());
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public  void funLoadKotItemListWS(String... params)
	{
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected())
			{
				String kotNo = params[0];
				String POSCode = params[1];
				showDialog();
				App.getAPIHelper().funGetKOTData(POSCode, kotNo, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
					@Override
					public void onSuccess(JsonObject jObj) {
						if (null != jObj)
						{

							dismissDialog();
							ArrayList arrListKotItemMaster = new ArrayList();
							arrListKOT=new ArrayList<clsKOTItemDtlBean>();
							JsonArray mJsonArray = (JsonArray) jObj.get("KOTData");
							JsonObject mJsonObject = new JsonObject();

							for (int i = 0; i < mJsonArray.size(); i++) {
								mJsonObject = (JsonObject) mJsonArray.get(i);
								if (mJsonObject.get("KOTNo").getAsString().toString().equals(""))
								{
									//memberInfo = "no data";
								} else
								{
									double rate = Double.parseDouble(mJsonObject.get("Amount").getAsString()) / Double.parseDouble(mJsonObject.get("ItemQty").getAsString());
									clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean("1", mJsonObject.get("TableNo").getAsString(), "", 0.00, "", "", clsGlobalFunctions.gPOSCode
											, mJsonObject.get("ItemCode").getAsString(), mJsonObject.get("ItemName").getAsString()
											, Double.parseDouble(mJsonObject.get("ItemQty").getAsString()), Double.parseDouble(mJsonObject.get("Amount").getAsString())
											, mJsonObject.get("WaiterNo").getAsString(), mJsonObject.get("KOTNo").getAsString(), 1, "Y", "", clsGlobalFunctions.gUserCode,
											clsGlobalFunctions.gUserCode, objGlobal.funGetCurrentDateTime(), objGlobal.funGetCurrentDateTime(), "No"
											, "N", "N", "N", "N", "", "", "", "", "", "", false, rate, "");
									arrListKotItemMaster.add(objItemDtl);
								}
							}
							if (arrListKotItemMaster.size() > 0) {
								arrListKOT = arrListKotItemMaster;
								double amount = 0;
								for (int cnt = 0; cnt < arrListKotItemMaster.size(); cnt++) {
									clsKOTItemDtlBean objItemDtl = (clsKOTItemDtlBean) arrListKotItemMaster.get(cnt);

									amount = amount + Double.parseDouble(String.valueOf(objItemDtl.getDblAmount()));
									String finalresult = new Double(amount).toString();
									edtKotTotalAmount.setText(finalresult);
								}
								funFillListView(arrListKotItemMaster);
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


	private void funGetKOTDataFromWS(String kotNO) {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				showDialog();
				App.getAPIHelper().funGetKOTDataFromWS(kotNO, clsGlobalFunctions.gPOSCode, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
					@Override
					public void onSuccess(JsonObject jObj) {
						dismissDialog();
						if (null != jObj) {
							try {
								JsonArray jArrKOTData = (JsonArray) jObj.get("KOTData");
								for (int cnt = 0; cnt < jArrKOTData.size(); cnt++) {


									JsonObject jObjHeaderInfo = (JsonObject) jArrKOTData.get(cnt);
									String costCenterName = jObjHeaderInfo.get("CostCenterName").getAsString();
									String tableName = jObjHeaderInfo.get("TableName").getAsString();
									String KOTNo = jObjHeaderInfo.get("KOTNo").getAsString();
									String PaxNo = jObjHeaderInfo.get("PaxNo").getAsString();
									JsonArray jArrItemDtl = (JsonArray) jObjHeaderInfo.get("ItemDtl");
									JsonArray jArrModItemDtl = (JsonArray) jObjHeaderInfo.get("ModItemDtl");
									funPrintKot(jArrItemDtl, jArrModItemDtl, costCenterName, tableName, KOTNo, PaxNo);
								}
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
				SnackBarUtils.showSnackBar(getActivity(), R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(getActivity(), R.string.setup_your_server_settings);
		}
	}


	private void funGetSelectedBillDetails(String billNo)
	{
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				showDialog();
				App.getAPIHelper().funGetBillDtl(clsGlobalFunctions.gClientCode, billNo, new BaseAPIHelper.OnRequestComplete<ArrayList<clsBillListDtl>>() {
					@Override
					public void onSuccess(ArrayList<clsBillListDtl> arrListTemp) {
						dismissDialog();
						if (null != arrListTemp)
						{
							ArrayList arrListKotItemMaster = new ArrayList();
							List<clsItemDtl> arrListItemtDtl = new ArrayList<clsItemDtl>();
							try {
								clsBillListDtl objBill=arrListTemp.get(0);
								arrListItemtDtl=objBill.getArrListItemtDtl();

								clsItemDtl obItemDtltmp=new clsItemDtl();
								for(int cnt=0;cnt<arrListItemtDtl.size();cnt++)
								{		/*here converted bill list into kot list for printing purpose */
									obItemDtltmp=arrListItemtDtl.get(cnt);
									double rate = Double.parseDouble(obItemDtltmp.getStrAmount()) / Double.parseDouble(obItemDtltmp.getStrQty());
									clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean("1", "", "", 0.00, "", "", clsGlobalFunctions.gPOSCode
											, "", obItemDtltmp.getStrItemName()
											, Double.parseDouble(obItemDtltmp.getStrQty()), Double.parseDouble(obItemDtltmp.getStrAmount())
											, "",objBill.getStrBillNo(), 1, "Y", "", clsGlobalFunctions.gUserCode,
											clsGlobalFunctions.gUserCode, objGlobal.funGetCurrentDateTime(), objGlobal.funGetCurrentDateTime(), "No"
											, "N", "N", "N", "N", "", "", "", "", "", "", false,rate, "");
									arrListKotItemMaster.add(objItemDtl);
									edtKotTotalAmount.setText(objBill.getStrGrandTotal());
								}
								if(arrListKotItemMaster.size()>0){
									for(int j=0;j<3;j++){
										clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean("", "", "", 0.00, "", "", clsGlobalFunctions.gPOSCode
												, "", ""
												, 0,0
												, "",objBill.getStrBillNo(), 1, "Y", "","",
												"", "", "", "No"
												, "N", "N", "N", "N", "", "", "", "", "", "", false,0, "");
										arrListKotItemMaster.add(objItemDtl);
									}

									//Add Sub Total amount in list
									clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean("1", "", "", 0.00, "", "", clsGlobalFunctions.gPOSCode
											, "", "Sub Total "
											, 0, Double.parseDouble(objBill.getStrSubTotal())
											, "",objBill.getStrBillNo(), 1, "Y", "", clsGlobalFunctions.gUserCode,
											clsGlobalFunctions.gUserCode, objGlobal.funGetCurrentDateTime(), objGlobal.funGetCurrentDateTime(), "No"
											, "N", "N", "N", "N", "", "", "", "", "", "", false,0, "");
									arrListKotItemMaster.add(objItemDtl);

									//Add Tax amount in list
									 objItemDtl = new clsKOTItemDtlBean("1", "", "", 0.00, "", "", clsGlobalFunctions.gPOSCode
											, "", "Tax Amount "
											, 0, Double.parseDouble(objBill.getStrTaxAmt())
											, "",objBill.getStrBillNo(), 1, "Y", "", clsGlobalFunctions.gUserCode,
											clsGlobalFunctions.gUserCode, objGlobal.funGetCurrentDateTime(), objGlobal.funGetCurrentDateTime(), "No"
											, "N", "N", "N", "N", "", "", "", "", "", "", false,0, "");
									arrListKotItemMaster.add(objItemDtl);

									//Add Grand Total amount in list
									objItemDtl = new clsKOTItemDtlBean("1", "", "", 0.00, "", "", clsGlobalFunctions.gPOSCode
											, "", "Grand Total "
											, 0, Double.parseDouble(objBill.getStrGrandTotal())
											, "",objBill.getStrBillNo(), 1, "Y", "", clsGlobalFunctions.gUserCode,
											clsGlobalFunctions.gUserCode, objGlobal.funGetCurrentDateTime(), objGlobal.funGetCurrentDateTime(), "No"
											, "N", "N", "N", "N", "", "", "", "", "", "", false,0, "");
									arrListKotItemMaster.add(objItemDtl);

								}
								funFillListView(arrListKotItemMaster);
								//funSetList(objBill);
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
				SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
		}
	}

	public void funGenerateBillTextFileWebService(String billNumber) {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected())
			{
				String reprint="Y";
				showDialog();
				App.getAPIHelper().funGenerateBillTextFile(billNumber,clsGlobalFunctions.gPOSCode,clsGlobalFunctions.gClientCode,reprint, new BaseAPIHelper.OnRequestComplete<HashMap>() {
					@Override
					public void onSuccess(HashMap map) {
						dismissDialog();
						if (null != map)
						{
							try
							{
								String response="";
								HashMap<String,String> hmBill= map;
								String cardNo ="";
								for (Map.Entry<String, String> entry : hmBill.entrySet())
								{
									response=entry.getValue();
								}

							}
							catch (Exception e)
							{
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
				SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
		}
	}





	public void funGenerateDirectBillerKOTTextFileWebService(String billNumber) {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected())
			{
				String reprint="Reprint";
				showDialog();
				App.getAPIHelper().funGenerateDirectBillerKOTTextFile(clsGlobalFunctions.gPOSCode,billNumber,clsGlobalFunctions.gDirectBillerAreaCode,reprint, new BaseAPIHelper.OnRequestComplete<HashMap>() {
					@Override
					public void onSuccess(HashMap map) {
						dismissDialog();
						if (null != map)
						{
							try
							{
								String response="";
								HashMap<String,String> hmBill= map;
								String cardNo ="";
								for (Map.Entry<String, String> entry : hmBill.entrySet())
								{
									response=entry.getValue();
								}

							}
							catch (Exception e)
							{
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
				SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
		}
	}




	protected void showDialog() {
		if (null == pgDialog) {
			if(mActivity==null)
			{mActivity=clsReprintDocuments.mActivity;}

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