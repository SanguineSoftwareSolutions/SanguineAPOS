package com.example.apos.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.adapter.clsKotSelectedItemsCustomBaseAdapter;
import com.example.apos.adapter.clsOrderDialogAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsItemModifierBean;
import com.example.apos.bean.clsKOTItemDtlBean;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.bean.clsWaiterMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsKotItemListSelectionListener;
import com.example.apos.util.clsUtility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;


public class clsMiniMakeKotScreen extends Activity implements clsKotItemListSelectionListener, clsKotSelectedItemsCustomBaseAdapter.customButtonListener, View.OnClickListener {

	Intent intent;
	Intent iData;
	private String moduletype;
	private ConnectivityManager connectivityManager;
	private AutoCompleteTextView autoCompleteTextView;
	private clsKotItemListSelectionListener itemListSelectionListener = null;
	ArrayList arrListItemMaster = new ArrayList();
	ArrayList arrListTableMaster = new ArrayList();
	ArrayList arrListWaiterMaster = new ArrayList();
	private Spinner spinnerTable, spinnerWaiter;
	private List<clsKOTItemDtlBean> arrListKOTItems, arrListNewKOTItems;
	private clsKotSelectedItemsCustomBaseAdapter objKotItemDtl;
	private ListView lvKotSelectedItems = null;
	private double subTotalAmt = 0;
	private TextView edtKotTotalAmount = null;
	private EditText edtPax = null, edtQty = null,edtExternalCode = null;
	private Button btnExecute;
	public int listpos = 0;
	private TextWatcher textWatcher;
	boolean changedValue = false;
	private String tableNo = "", waiterNo = "";
	private Dialog pgDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.minimakekotscreen);
		itemListSelectionListener = (clsKotItemListSelectionListener) clsMiniMakeKotScreen.this;
		widgetInit();

		try {
			funGetItemPriceDtlForCustomerOrder();


			autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
					boolean itemFound = false;
					String itemName = autoCompleteTextView.getText().toString();
					clsKotItemsListBean objKOT = new clsKotItemsListBean();
					for (int cnt = 0; cnt < arrListItemMaster.size(); cnt++) {
						objKOT = (clsKotItemsListBean) arrListItemMaster.get(cnt);
						if (objKOT.getStrItemName().equals(itemName)) {
							edtExternalCode.setText(objKOT.getStrExternalCode());
							itemFound = true;
							break;
						}

					}

					if (itemFound)
					{
						for (int cnt = 0; cnt < arrListTableMaster.size(); cnt++) {
							clsTableMaster objTable = (clsTableMaster) arrListTableMaster.get(cnt);
							if (objTable.getStrTableName().equals(spinnerTable.getSelectedItem().toString())) {
								tableNo = objTable.getStrTableNo();
								break;
							}
						}

						for (int cnt = 0; cnt < arrListWaiterMaster.size(); cnt++) {
							clsWaiterMaster objWaiter = (clsWaiterMaster) arrListWaiterMaster.get(cnt);
							if (objWaiter.getStrWaiterName().equals(spinnerWaiter.getSelectedItem().toString())) {
								waiterNo = objWaiter.getStrWaterNo();
								break;
							}
						}

						funRefreshItemDtl(objKOT.getStrItemCode(), objKOT.getStrItemName(), objKOT.getStrSubGroupCode(), objKOT.getDblSalePrice(), tableNo, waiterNo);
						edtExternalCode.setText("");
						autoCompleteTextView.setText("");
						edtQty.setText("1");
						changedValue = false;
					}

				}
			});


			btnExecute.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (arrListKOTItems.size() > 0) {
						funFireKOTWS();
					} else {
						Toast.makeText(getApplicationContext(), "Please Select Item!!!", Toast.LENGTH_LONG).show();
						return;
					}

				}
			});



			edtExternalCode.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					String externalCode = edtExternalCode.getText().toString();
					if (!externalCode.isEmpty())
					{
							try
							{
								//funGetItemDetailsByExternalCode(externalCode,v);

							} catch (Exception e) {
								e.printStackTrace();
							}
					}
				}
			});


		} catch (Exception e) {
			e.printStackTrace();
		}


	}


	private void widgetInit() {
		arrListKOTItems = new ArrayList<clsKOTItemDtlBean>();
		arrListNewKOTItems = new ArrayList<clsKOTItemDtlBean>();
		clsGlobalFunctions.gPhoneNo = "";
		intent = new Intent();
		moduletype = "";
		connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.autoCompleteItemNameTextview);
		spinnerTable = (Spinner) findViewById(R.id.spinnerTable);
		spinnerWaiter = (Spinner) findViewById(R.id.spinnerWaiter);
		lvKotSelectedItems = (ListView) findViewById(R.id.listMiniMakeKotItemList);
		edtKotTotalAmount = (TextView) findViewById(R.id.edtMiniKotTotalAmount);
		edtExternalCode = (EditText) findViewById(R.id.edtExternalCode);
		edtQty = (EditText) findViewById(R.id.edtQty);
		edtPax = (EditText) findViewById(R.id.edtPax);
		btnExecute = (Button) findViewById(R.id.btnExecute);
//        edtQty.setOnClickListener(this);


	}


	@Override
	public void onBackPressed() {
		clsGlobalFunctions.gCallingAvailable = "No";
		finish();
	}

	@Override
	public void getItemsListSelectedForOder(String strItemCode, String strItemName, String strSubGroupCode, double dblSalePrice) {


	}


	private void funRefreshItemDtl(String strItemCode, String strItemName, String strSubGroupCode, double dblSalePrice, String tableNo, String waiterNo) {
		int seq = 0;
		if (arrListKOTItems.size() > 0) {
			for (int j = 0; j < arrListKOTItems.size(); j++) {
				clsKOTItemDtlBean objBean = arrListKOTItems.get(j);
				if (objBean.getStrItemName().contains("-->") || objBean.getStrItemName().contains("=>")) {

				} else {
					seq++;
				}
			}
		}

		clsGlobalFunctions objGlobal = new clsGlobalFunctions();
		String dateTime = clsGlobalFunctions.funGetPOSDateTime();

		// dateTime=dateTime.replaceAll(" ","%20");
		double qty = Double.parseDouble(edtQty.getText().toString());
		int pax = Integer.parseInt(edtPax.getText().toString());
		clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean(String.valueOf(seq), tableNo, "", 0, "", "", clsGlobalFunctions.gPOSCode
				, strItemCode, strItemName, qty, dblSalePrice, waiterNo, "K01", pax, "Y", "", clsGlobalFunctions.gUserCode,
				clsGlobalFunctions.gUserCode, dateTime, dateTime, "No"
				, "N", "N", "N", "N", "", "", "", "", "", "", false, dblSalePrice, "");
		boolean flgAlreadyPresent = false;
		int iListViewScrollPosition = -1;

		for (int k = 0; k < arrListKOTItems.size(); k++) {
			if (arrListKOTItems.get(k).getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase())) {
				clsKOTItemDtlBean kotItemDtl = (clsKOTItemDtlBean) arrListKOTItems.get(k);

				if (kotItemDtl.getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase())) {
					kotItemDtl.setDblItemQuantity(kotItemDtl.getDblItemQuantity() + 1);
					kotItemDtl.setDblAmount(kotItemDtl.getDblAmount() + dblSalePrice);
					flgAlreadyPresent = true;
					iListViewScrollPosition = k;
					arrListKOTItems.set(k, kotItemDtl);

					break;
				}
			}
		}
		if (!flgAlreadyPresent) {
			arrListKOTItems.add(objItemDtl);
		}

		objKotItemDtl = new clsKotSelectedItemsCustomBaseAdapter(clsMiniMakeKotScreen.this, arrListKOTItems, 0);
		lvKotSelectedItems.setItemsCanFocus(true);
		lvKotSelectedItems.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		objKotItemDtl.setCustomButtonListner((clsKotSelectedItemsCustomBaseAdapter.customButtonListener) clsMiniMakeKotScreen.this);
		lvKotSelectedItems.setAdapter(objKotItemDtl);
		lvKotSelectedItems.setSelection(objKotItemDtl.getCount() - 1);
		if (iListViewScrollPosition != -1) {
			lvKotSelectedItems.setSelection(iListViewScrollPosition);
		}

		subTotalAmt = 0;
		for (int l = 0; l < arrListKOTItems.size(); l++) {
			subTotalAmt = subTotalAmt + arrListKOTItems.get(l).getDblAmount();
			edtKotTotalAmount.setText(String.valueOf(subTotalAmt));
		}


		if (clsGlobalFunctions.gCalculateTaxOnMakeKOT.equals("Y")) {
			funTaxCalculate();
		}
	}


	private void funRefreshItemGrid() {
		int iListViewScrollPosition = -1;
		objKotItemDtl = new clsKotSelectedItemsCustomBaseAdapter(clsMiniMakeKotScreen.this, arrListKOTItems, 0);
		lvKotSelectedItems.setItemsCanFocus(true);
		lvKotSelectedItems.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		objKotItemDtl.setCustomButtonListner((clsKotSelectedItemsCustomBaseAdapter.customButtonListener) clsMiniMakeKotScreen.this);
		lvKotSelectedItems.setAdapter(objKotItemDtl);
		lvKotSelectedItems.setSelection(objKotItemDtl.getCount() - 1);
		if (iListViewScrollPosition != -1) {
			lvKotSelectedItems.setSelection(iListViewScrollPosition);
		}
		subTotalAmt = 0;
		for (int l = 0; l < arrListKOTItems.size(); l++) {
			subTotalAmt = subTotalAmt + arrListKOTItems.get(l).getDblAmount();
			edtKotTotalAmount.setText(String.valueOf(subTotalAmt));
		}

	}


	@Override
	public void onButtonClickListner(int position, String value) {
		if (value.split("#")[1].equals("selectedrow")) {
			listpos = position;
			final String itemcode = value.split("#")[2];
			final Dialog dialog = new Dialog(clsMiniMakeKotScreen.this);
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(R.layout.edititemqty);
			final int pos = position;
			clsKOTItemDtlBean objKotItem = arrListKOTItems.get(position);

			ImageButton btnMinus = (ImageButton) dialog.findViewById(R.id.btnRemoveQty);
			ImageButton btnAdd = (ImageButton) dialog.findViewById(R.id.btnAddQty);
			Button btnOk = (Button) dialog.findViewById(R.id.btnUpdate);
			ImageView btnDeleteItem = (ImageView) dialog.findViewById(R.id.btnDeleteItem);
			final EditText edtFFName = (EditText) dialog.findViewById(R.id.edtKotFFItemName);
			final EditText edtFFRate = (EditText) dialog.findViewById(R.id.edtKotFFItemRate);
			//Button btnApplyFreeFlowMod = (Button) dialog.findViewById(R.id.btnApplyFreeFlow);
			ImageView btnCloseDialog = (ImageView) dialog.findViewById(R.id.btnClose);
			Button btnPredefinedModifier = (Button) dialog.findViewById(R.id.btnKotModifier);
			TextView txtItemName = (TextView) dialog.findViewById(R.id.txtItemName);
			final TextView txtQty = (TextView) dialog.findViewById(R.id.txtQty);


			txtItemName.setText(objKotItem.getStrItemName());
			txtQty.setText("" + objKotItem.getDblItemQuantity());


			btnMinus.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					List <clsKOTItemDtlBean> lstTemp=new ArrayList<clsKOTItemDtlBean>();
					lstTemp=arrListKOTItems;
					clsKOTItemDtlBean objKotItem = lstTemp.get(pos);
					double qty1 = 0;
					double qty = objKotItem.getDblItemQuantity();
					double amt = objKotItem.getDblAmount();
					double rate = ((objKotItem.getDblAmount()) / (objKotItem.getDblItemQuantity()));
					if (qty > 1) {
						qty = qty - 1;
						amt = qty * rate;
						objKotItem.setDblItemQuantity(qty);
						objKotItem.setDblAmount(amt);
						qty1 = objKotItem.getDblItemQuantity();
						arrListKOTItems.set(pos, objKotItem);
					} else if (qty == 1) {
						//lstTemp.remove(pos);
						String strDeleteItemCode=arrListKOTItems.get(pos).getStrItemCode();
						arrListKOTItems.remove(pos);
						funDeleteItemFromGrid(strDeleteItemCode);
						funRefreshItemGrid();
						dialog.dismiss();
					}
					txtQty.setText("" + qty1);
				}
			});


			btnAdd.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clsKOTItemDtlBean objKotItem = arrListKOTItems.get(pos);
					double qty1 = 0;
					double qty = objKotItem.getDblItemQuantity();
					double rate = ((objKotItem.getDblAmount()) / (objKotItem.getDblItemQuantity()));
					qty = qty + 1;
					double amt = qty * rate;
					objKotItem.setDblItemQuantity(qty);
					objKotItem.setDblAmount(amt);
					qty1 = objKotItem.getDblItemQuantity();
					arrListKOTItems.set(pos, objKotItem);
					txtQty.setText("" + qty1);
				}
			});


			btnDeleteItem.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					clsKOTItemDtlBean objKotItem = arrListKOTItems.get(pos);
					String strDeleteItemCode=arrListKOTItems.get(pos).getStrItemCode();
					arrListKOTItems.remove(pos);
					funDeleteItemFromGrid(strDeleteItemCode);
					funRefreshItemGrid();
					dialog.dismiss();
				}
			});

			btnOk.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v)
				{
					int previousKOTSize=0;
					clsGlobalFunctions objGlobal = new clsGlobalFunctions();
					String dateTime=clsGlobalFunctions.funGetPOSDateTime();
					// dateTime=dateTime.replaceAll(" ","%20");

					if(!edtFFName.getText().toString().isEmpty())
					{
						double freeFlowRate=0;
						InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
						imm.showSoftInput(edtFFName , InputMethodManager.SHOW_IMPLICIT);

						String freeFlowModName=edtFFName.getText().toString();
						if(!edtFFRate.getText().toString().isEmpty())
						{
							freeFlowRate=Double.parseDouble(edtFFRate.getText().toString());
						}

						int sequenceNo=1;
						String seq="0";
						int paxNo=Integer.parseInt(edtPax.getText().toString());
						clsKOTItemDtlBean objItemDtl=null;
						for(int i=0;i<arrListKOTItems.size();i++)
						{
							clsKOTItemDtlBean objBean=arrListKOTItems.get(i);
							if(objBean.getStrItemCode().contains(itemcode))
							{
								seq= objBean.getStrSerialNo();
								if(seq.contains("."))
								{
									String []spSeq=seq.split("\\.");
									sequenceNo=Integer.parseInt(spSeq[1]);
									sequenceNo++;
									objItemDtl = new clsKOTItemDtlBean(spSeq[0]+"."+String.valueOf(sequenceNo), tableNo, "", 0.00, "", "", clsGlobalFunctions.gPOSCode
											, itemcode+"M999", "-->"+freeFlowModName
											, 1, freeFlowRate, waiterNo, "K01",paxNo, "Y", ""
											, clsGlobalFunctions.gUserCode, clsGlobalFunctions.gUserCode
											, dateTime, dateTime , "No", "N", "N", "N", "N", "", "", "", "", "", "", true,freeFlowRate,"");
								}
								else
								{
									objItemDtl = new clsKOTItemDtlBean(seq+"."+String.valueOf(sequenceNo), tableNo, "", 0.00, "", "", clsGlobalFunctions.gPOSCode
											, itemcode+"M999", "-->"+freeFlowModName
											, 1, freeFlowRate, waiterNo, "K01",paxNo, "Y", ""
											, clsGlobalFunctions.gUserCode, clsGlobalFunctions.gUserCode
											, dateTime, dateTime , "No", "N", "N", "N", "N", "", "", "", "", "", "", true,freeFlowRate,"");
								}
							}
						}
						if(previousKOTSize>0)
						{
							arrListNewKOTItems.add(objItemDtl);
							arrListKOTItems.add(listpos + 1, objItemDtl);
						}
						else
						{
							arrListKOTItems.add(listpos + 1, objItemDtl);
						}
					}
					funRefreshItemGrid();
					dialog.dismiss();
				}
			});



			btnPredefinedModifier.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					funGetModifierList(itemcode);
					dialog.dismiss();
				}
			});

			btnCloseDialog.setOnClickListener(new View.OnClickListener()
			{
				@Override
				public void onClick(View v)
				{
					dialog.dismiss();
				}
			});

			dialog.show();
			Window window = dialog.getWindow();
			window.setGravity(Gravity.CENTER);
			window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
			window.getWindowManager().getDefaultDisplay();

		}


	}

	@Override
	public void onClick(View view) {

	}


	private void funSetList(ArrayList arrList)
	{
		ArrayList<String> arrItemList = new ArrayList<>();
		for (int cnt = 0; cnt < arrList.size(); cnt++) {
			clsKotItemsListBean objKOT = (clsKotItemsListBean) arrList.get(cnt);
			arrItemList.add(objKOT.getStrItemName());
		}
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(clsMiniMakeKotScreen.this, android.R.layout.simple_list_item_1, arrItemList);
		autoCompleteTextView.setAdapter(adapter);
		autoCompleteTextView.setThreshold(1);

		try
		{
			funGetTableList();
			funGetWaiterList();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}


	public void funGetItemPriceDtlForCustomerOrder() {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				clsGlobalFunctions objGlobal = new clsGlobalFunctions();
				showDialog();
				App.getAPIHelper().funGetItemPriceDtlForCustomerOrder(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gDirectBillerAreaCode, "All", clsGlobalFunctions.gAreaWisePricing, objGlobal.funGetCurrentDate(), objGlobal.funGetCurrentDate(), true, "", new BaseAPIHelper.OnRequestComplete<ArrayList<clsKotItemsListBean>>() {
					@Override
					public void onSuccess(ArrayList<clsKotItemsListBean> arrListTemp)
					{
						dismissDialog();
						String pricingDay = new clsUtility().funGetDayForPricing();
						ArrayList<clsKotItemsListBean> arrListItemMastertemp = new ArrayList();
						if (null != arrListTemp) {
							clsKotItemsListBean obj = new clsKotItemsListBean();
							if (arrListTemp.size() > 0) {
								for (int i = 0; i < arrListTemp.size(); i++) {
									obj = arrListTemp.get(i);
									obj.setDblSalePrice(funGetitemPriceOnDay(pricingDay, obj));
									arrListItemMastertemp.add(obj);
								}
							}
							if (arrListItemMastertemp.size() > 0)
							{
								arrListItemMaster = arrListItemMastertemp;
								funSetList(arrListItemMaster);
							} else {
								funSetList(new ArrayList());

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


	public void funGetTableList() {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				showDialog();
				App.getAPIHelper().funGetTableList(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gCMSIntegrationYN, clsGlobalFunctions.gTreatMemberAsTable, new BaseAPIHelper.OnRequestComplete<ArrayList<clsTableMaster>>() {
					@Override
					public void onSuccess(ArrayList<clsTableMaster> arrListTemp) {
						dismissDialog();
						if (null != arrListTemp) {
							if (arrListTemp.size() > 0) {
								arrListTableMaster = arrListTemp;
								ArrayList arrListTable = new ArrayList();
								if (arrListTableMaster.size() > 0) {
									for (int tbl = 0; tbl < arrListTableMaster.size(); tbl++) {
										clsTableMaster objTable = (clsTableMaster) arrListTableMaster.get(tbl);
										arrListTable.add(objTable.getStrTableName());
									}

									ArrayAdapter<String> tableAdapter = new ArrayAdapter<String>(clsMiniMakeKotScreen.this, R.layout.spinneritemtextview, arrListTable);
									tableAdapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
									spinnerTable.setAdapter(tableAdapter);
									dismissDialog();
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
								ArrayList arrListWaiter = new ArrayList();
								if (arrListWaiterMaster.size() > 0) {
									for (int wtr = 0; wtr < arrListWaiterMaster.size(); wtr++) {
										clsWaiterMaster objWaiter = (clsWaiterMaster) arrListWaiterMaster.get(wtr);
										arrListWaiter.add(objWaiter.getStrWaiterName());
									}

									ArrayAdapter<String> waiterAdapter = new ArrayAdapter<String>(clsMiniMakeKotScreen.this, R.layout.spinneritemtextview, arrListWaiter);
									waiterAdapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
									spinnerWaiter.setAdapter(waiterAdapter);
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



	private void funTaxCalculate() {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				JsonObject objTaxDtl = new JsonObject();
				JsonArray arrKOTClass = new JsonArray();
				try
				{

					for (int cnt = 0; cnt < arrListKOTItems.size(); cnt++) {
						clsKOTItemDtlBean objKOTItemDtl = (clsKOTItemDtlBean) arrListKOTItems.get(cnt);
						JsonObject objRows = new JsonObject();

						objRows.addProperty("strPOSCode", objKOTItemDtl.getStrPOSCode());
						objRows.addProperty("strItemCode", objKOTItemDtl.getStrItemCode());
						objRows.addProperty("strItemName", objKOTItemDtl.getStrItemName());
						objRows.addProperty("dblItemQuantity", objKOTItemDtl.getDblItemQuantity());
						objRows.addProperty("dblAmount", objKOTItemDtl.getDblAmount());
						objRows.addProperty("strClientCode", clsGlobalFunctions.gClientCode);
						objRows.addProperty("OperationType", "Dine In");
						objRows.addProperty("AreaCode", "A001");
						String dateTime = clsGlobalFunctions.gPOSDate;
						String posDate = dateTime.replaceAll(" ", "%20");
						objRows.addProperty("POSDate", posDate);
						arrKOTClass.add(objRows);
					}
					objTaxDtl.add("TaxDtl", arrKOTClass);

				} catch (Exception e) {
					e.printStackTrace();
				}

				App.getAPIHelper().funTaxCalculate(objTaxDtl, new BaseAPIHelper.OnRequestComplete<JsonObject>()
				{
					@Override
					public void onSuccess(JsonObject jObj)
					{

						if (null != jObj) {
							try {

								String taxAmount = jObj.get("totalTaxAmt").getAsString().toString();
								double totalTaxAmt = 0;
								if (taxAmount.equalsIgnoreCase("")) {
									totalTaxAmt = 0;
								}
								else
								{
									totalTaxAmt = Double.parseDouble(taxAmount);
									double totalAmt = Double.parseDouble(edtKotTotalAmount.getText().toString());
									double amt = totalAmt + totalTaxAmt;
									String amount = String.valueOf(Math.rint(amt));
									edtKotTotalAmount.setText(amount);
									System.out.println(totalTaxAmt);
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}

					@Override
					public void onFailure(String errorMessage, int errorCode)
					{

					}
				});


			} else {
				SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.setup_your_server_settings);
		}
	}




	/*private void funGetItemDetailsByExternalCode(String externalCode, final View v)
	{
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				showDialog();
				App.getAPIHelper().funGetItemDetailsByExternalCode(clsGlobalFunctions.gPOSCode,externalCode,clsGlobalFunctions.gAreaWisePricing, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
					@Override
					public void onSuccess(JsonObject jObj) {
						dismissDialog();
						if (null != jObj) {
							try
							{
								JsonArray mJsonMainArray = (JsonArray) jObj.get("externalCodeDetails");
								if (mJsonMainArray.size() > 0) {
									JsonObject mJsonMainObject = new JsonObject();
									for (int i = 0; i < mJsonMainArray.size(); i++)
									{
										mJsonMainObject = (JsonObject) mJsonMainArray.get(i);

										String tableNo = "";
										String waiterNo = "";
										for (int cnt = 0; cnt < arrListTableMaster.size(); cnt++) {
											clsTableMaster objTable = (clsTableMaster) arrListTableMaster.get(cnt);
											if (objTable.getStrTableName().equals(spinnerTable.getSelectedItem().toString())) {
												tableNo = objTable.getStrTableNo();
												break;
											}
										}

										for (int cnt = 0; cnt < arrListWaiterMaster.size(); cnt++) {
											clsWaiterMaster objWaiter = (clsWaiterMaster) arrListWaiterMaster.get(cnt);
											if (objWaiter.getStrWaiterName().equals(spinnerWaiter.getSelectedItem().toString())) {
												waiterNo = objWaiter.getStrWaterNo();
												break;
											}
										}
										funRefreshItemDtl(mJsonMainObject.get("ItemCode").toString(), mJsonMainObject.get("ItemName").toString(), "", Double.parseDouble(mJsonMainObject.get("PriceMonday").toString()), tableNo, waiterNo);
										edtExternalCode.setText("");
										autoCompleteTextView.setText("");
										InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
										imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
									}
								} else {
									Toast.makeText(clsKotScreen.mActivity, "Item not found!!!!!", Toast.LENGTH_LONG).show();
									edtExternalCode.setText("");
								}
							}
							catch (Exception e) {
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
				SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.setup_your_server_settings);
		}

	}

*/


	private void funGetModifierList(String itemcode) {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				showDialog();
				App.getAPIHelper().funGetModifierList(itemcode, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsItemModifierBean>>() {
					@Override
					public void onSuccess(ArrayList<clsItemModifierBean> arrListTemp) {
						dismissDialog();
						if (null != arrListTemp) {
							try {
								if (arrListTemp.size() > 0) {

									final ArrayList arrListItemModifier= (ArrayList) arrListTemp;
									final Dialog dialog = new Dialog(clsMiniMakeKotScreen.this);
									dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
									dialog.setContentView(R.layout.kotmodifierdialog);

									//  dialog.setTitle("Freeflow Modifier");

									final ArrayList arrList=new ArrayList<String>();
									Button btnClose = (Button)  dialog.findViewById(R.id.btnFFClose);
									final GridView dialoglist= (GridView) dialog.findViewById(R.id.dialoglist);
									for(int cnt=0;cnt<arrListTemp.size();cnt++) {
										clsItemModifierBean objItem = (clsItemModifierBean) arrListTemp.get(cnt);
										arrList.add(objItem.getStrModifierName());
									}
									clsOrderDialogAdapter adapter = new clsOrderDialogAdapter(getBaseContext(),arrList);
									dialoglist.setAdapter(adapter);
									dialoglist.setOnItemClickListener(new AdapterView.OnItemClickListener()
									{
										@Override
										public void onItemClick(AdapterView<?> parent, View view, int position, long id)
										{

											clsGlobalFunctions objGlobal = new clsGlobalFunctions();
											String dateTime=clsGlobalFunctions.funGetPOSDateTime();
											clsItemModifierBean objItemModifier = (clsItemModifierBean) arrListItemModifier.get(position);

											boolean flgAlreadyPresentMod=false;

											for(int cnt=0;cnt<arrListKOTItems.size();cnt++)
											{
												if(arrListKOTItems.get(cnt).isModifier())
												{
													clsKOTItemDtlBean objTemp=arrListKOTItems.get(cnt);
													String itemWithModifier = arrListKOTItems.get(cnt).getStrItemCode();
													String itemCode1=itemWithModifier.substring(0, 7);
													String modifierCode1=itemWithModifier.substring(7,11);
													if(itemCode1.equals(objItemModifier.getStrItemCode()) && modifierCode1.equals(objItemModifier.getStrModifierCode()))
													{
														double modQty=objTemp.getDblItemQuantity();
														modQty=modQty+1;
														double amt=objItemModifier.getRate()*modQty;
														objTemp.setDblAmount(amt);
														objTemp.setDblItemQuantity(modQty);

														arrListKOTItems.set(cnt,objTemp);
														flgAlreadyPresentMod=true;
														break;
													}
												}
											}

											int sequenceNo=1;

											if(!flgAlreadyPresentMod)
											{
												String seq="0";
												int paxNo=Integer.parseInt(edtPax.getText().toString());
												clsKOTItemDtlBean objItemDtl=null;
												for(int i=0;i<arrListKOTItems.size();i++)
												{
													clsKOTItemDtlBean objBean=arrListKOTItems.get(i);
													if(objBean.getStrItemCode().contains(objItemModifier.getStrItemCode()))
													{
														seq= objBean.getStrSerialNo();
														if(seq.contains("."))
														{
															String []spSeq=seq.split("\\.");
															sequenceNo=Integer.parseInt(spSeq[1]);
															sequenceNo++;
															objItemDtl = new clsKOTItemDtlBean(spSeq[0]+"."+String.valueOf(sequenceNo), tableNo, "", 0.00, "", "", clsGlobalFunctions.gPOSCode
																	, objItemModifier.getStrItemCode() + objItemModifier.getStrModifierCode(), objItemModifier.getStrModifierName()
																	, 1, objItemModifier.getRate(), waiterNo, "K01",paxNo, "Y", ""
																	, clsGlobalFunctions.gUserCode, clsGlobalFunctions.gUserCode
																	, dateTime, dateTime , "No", "N", "N", "N", "N", "", "", "", "", "", "", true,objItemModifier.getRate(),"");
														}
														else
														{
															objItemDtl = new clsKOTItemDtlBean(String.valueOf(seq)+"."+String.valueOf(sequenceNo), tableNo, "", 0.00, "", "", clsGlobalFunctions.gPOSCode
																	, objItemModifier.getStrItemCode() + objItemModifier.getStrModifierCode(), objItemModifier.getStrModifierName()
																	, 1, objItemModifier.getRate(), waiterNo, "K01",paxNo, "Y", ""
																	, clsGlobalFunctions.gUserCode, clsGlobalFunctions.gUserCode
																	, dateTime, dateTime , "No", "N", "N", "N", "N", "", "", "", "", "", "", true,objItemModifier.getRate(),"");
														}

													}
												}

												arrListKOTItems.add(listpos + 1, objItemDtl);
												arrListNewKOTItems.add(objItemDtl);

											}

											funRefreshItemGrid();

											onResume();
											dialog.dismiss();
										}
									});

									btnClose.setOnClickListener(new View.OnClickListener() {
										@Override
										public void onClick(View v) {
											dialog.dismiss();
										}
									});


									dialog.show();
									Window window = dialog.getWindow();
									window.setGravity(Gravity.CENTER);
									window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
									window.getWindowManager().getDefaultDisplay();

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
				SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.setup_your_server_settings);
		}
	}


	private void funFireKOTWS() {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				JsonObject objKOTDtl = new JsonObject();
				JsonArray arrKOTClass = new JsonArray();
				try {

					for (int cnt = 0; cnt < arrListKOTItems.size(); cnt++) {
						clsKOTItemDtlBean objKOTItemDtl = (clsKOTItemDtlBean) arrListKOTItems.get(cnt);
						JsonObject objRows = new JsonObject();
						objRows.addProperty("strSerialNo", objKOTItemDtl.getStrSerialNo());
						objRows.addProperty("strTableNo", objKOTItemDtl.getStrTableNo());
						objRows.addProperty("strCardNo", objKOTItemDtl.getStrCardNo());
						objRows.addProperty("dblRedeemAmt", objKOTItemDtl.getDblRedeemAmt());
						objRows.addProperty("strHomeDelivery", objKOTItemDtl.getStrHomeDelivery());
						objRows.addProperty("strCustomerCode", objKOTItemDtl.getStrCustomerCode());
						objRows.addProperty("strPOSCode", objKOTItemDtl.getStrPOSCode());
						objRows.addProperty("strPOSName", clsGlobalFunctions.gPOSName);
						objRows.addProperty("strItemCode", objKOTItemDtl.getStrItemCode());
						objRows.addProperty("strItemName", objKOTItemDtl.getStrItemName());
						objRows.addProperty("dblItemQuantity", objKOTItemDtl.getDblItemQuantity());
						objRows.addProperty("dblAmount", objKOTItemDtl.getDblAmount());
						objRows.addProperty("strWaiterNo", objKOTItemDtl.getStrWaiterNo());
						objRows.addProperty("strKOTNo", objKOTItemDtl.getStrKOTNo());
						objRows.addProperty("intPaxNo", objKOTItemDtl.getIntPaxNo());
						objRows.addProperty("strPrintYN", objKOTItemDtl.getStrPrintYN());
						objRows.addProperty("strManualKOTNo", objKOTItemDtl.getStrPromoCode());
						objRows.addProperty("strUserCreated", objKOTItemDtl.getStrUserCreated());
						objRows.addProperty("strUserEdited", objKOTItemDtl.getStrUserEdited());
						objRows.addProperty("dteDateCreated", objKOTItemDtl.getDteDateCreated());
						objRows.addProperty("dteDateEdited", objKOTItemDtl.getDteDateEdited());
						objRows.addProperty("strOrderBefore", objKOTItemDtl.getStrOrderBefore());
						objRows.addProperty("strTakeAwayYesNo", objKOTItemDtl.getStrTakeAwayYesNo());
						objRows.addProperty("tdhComboItemYN", objKOTItemDtl.getTdhComboItemYN());
						objRows.addProperty("strDelBoyCode", objKOTItemDtl.getStrDelBoyCode());
						objRows.addProperty("strNCKotYN", "N");
						objRows.addProperty("strCustomerName", objKOTItemDtl.getStrCustomerName());
						objRows.addProperty("strActiveYN", objKOTItemDtl.getStrActiveYN());
						objRows.addProperty("dblBalance", objKOTItemDtl.getDblBalance());
						objRows.addProperty("dblCreditLimit", objKOTItemDtl.getDblCreditLimit());
						objRows.addProperty("strCounterCode", objKOTItemDtl.getStrCounterCode());
						objRows.addProperty("strPromoCode", objKOTItemDtl.getStrPromoCode());
						objRows.addProperty("dblRate", objKOTItemDtl.getDblRate());
						objRows.addProperty("strRemark", "");
						objRows.addProperty("strReasonCode", "");
						objRows.addProperty("strClientCode", clsGlobalFunctions.gClientCode);
						objRows.addProperty("strCardType", objKOTItemDtl.getStrCardType());

						clsUtility objUtility = new clsUtility();
						objRows.addProperty("deviceName", objUtility.funGetHostName());
						arrKOTClass.add(objRows);
					}

					objKOTDtl.add("KOTDtl", arrKOTClass);
				} catch (Exception e) {
					e.printStackTrace();
				}
				showDialog();
				App.getAPIHelper().funSaveAndPrintKOT(objKOTDtl, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
					@Override
					public void onSuccess(JsonObject jObj) {
						dismissDialog();
						if (null != jObj) {
							try {
								String kotNO = jObj.get("kotNO").getAsString();
								String printingResult = jObj.get("printingResult").getAsString();
								Toast.makeText(clsMiniMakeKotScreen.this, "KOT No- " + kotNO, Toast.LENGTH_SHORT).show();
								String[] arrSplData = kotNO.split("#");
								arrListKOTItems.clear();
								edtKotTotalAmount.setText("");
								edtPax.setText("0");
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
				SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.setup_your_server_settings);
		}
	}





	private double funGetitemPriceOnDay(String day, clsKotItemsListBean obBean) {

		double itemPrice = 0;
		switch (day) {
			case "PriceSunday":
				itemPrice = obBean.getDblSalePriceSun();
				break;

			case "PriceMonday":
				itemPrice = obBean.getDblSalePriceMon();
				break;

			case "PriceTuesday":
				itemPrice = obBean.getDblSalePriceTues();
				break;

			case "PriceWenesday":
				itemPrice = obBean.getDblSalePriceWend();
				break;

			case "PriceThursday":
				itemPrice = obBean.getDblSalePriceThu();
				break;

			case "PriceFriday":
				itemPrice = obBean.getDblSalePriceFri();
				break;

			case "PriceSaturday":
				itemPrice = obBean.getDblSalePriceSat();
				break;

			case "strPriceSunday":
				itemPrice = obBean.getDblSalePriceSun();
				break;

			default:
				itemPrice = obBean.getDblSalePrice();
		}
		return itemPrice;
	}



	private void funDeleteItemFromGrid(String strDeleteItemCode)
	{
		if(arrListKOTItems.size()>0 )
		{
			for (int cnt = 0; cnt <= arrListKOTItems.size(); cnt++)
			{
				clsKOTItemDtlBean obj=null;
				if(cnt==arrListKOTItems.size())
				{
					obj= (clsKOTItemDtlBean) arrListKOTItems.get(cnt-1);
					if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
					{
						arrListKOTItems.remove(obj);
					}
					else
					{
						if(cnt>=2)
						{
							obj = (clsKOTItemDtlBean) arrListKOTItems.get(cnt - 2);
							if (obj.getStrItemCode().contains(strDeleteItemCode + "M")) {
								arrListKOTItems.remove(obj);
							}
						}
					}
				}
				else
				{
					obj = (clsKOTItemDtlBean) arrListKOTItems.get(cnt);
					if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
					{
						arrListKOTItems.remove(obj);
					}
					else
					{
						if(cnt!=0)
						{
							obj= (clsKOTItemDtlBean) arrListKOTItems.get(cnt-1);
							if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
							{
								arrListKOTItems.remove(obj);
							}
						}
					}
				}
			}
		}
		if (arrListNewKOTItems.size() > 0)
		{
			for (int i = 0; i <= arrListNewKOTItems.size(); i++)
			{
				clsKOTItemDtlBean obj=null;
				if(i==arrListNewKOTItems.size())
				{
					obj = (clsKOTItemDtlBean) arrListNewKOTItems.get(i-1);
					if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
					{
						arrListNewKOTItems.remove(obj);
					}
					else
					{
						if(i>=2)
						{
							obj = (clsKOTItemDtlBean) arrListNewKOTItems.get(i - 2);
							if (obj.getStrItemCode().contains(strDeleteItemCode + "M")) {
								arrListNewKOTItems.remove(obj);
							}
						}
					}
				}
				else
				{
					obj = (clsKOTItemDtlBean) arrListNewKOTItems.get(i);
					if (obj.getStrItemCode().contains(strDeleteItemCode))
					{
						arrListNewKOTItems.remove(obj);
					}
					else
					{
						if(i!=0)
						{
							obj= (clsKOTItemDtlBean) arrListNewKOTItems.get(i-1);
							if (obj.getStrItemCode().contains(strDeleteItemCode))
							{
								arrListNewKOTItems.remove(obj);
							}
						}
					}
				}

			}
		}
	}





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
