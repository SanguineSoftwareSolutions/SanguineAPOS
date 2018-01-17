package com.example.apos.activity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.adapter.clsMainMenuAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsMainMenuBean;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.util.clsUtility;
import com.example.apos.views.CustomSearchView;
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

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;

public class clsMainMenu extends Activity {

	@BindView(R.id.customSerach)
	CustomSearchView customSearchView;

	private EditText edtMasterMenuSearch;
	ImageView imgChangePOS, imgLogout;
	private Context mContext;
	private TextView tvHeaderTitleName = null;
	ArrayList arrListMainMenuForms;
	Intent intent;
	Intent iData;
	static String moduletype;
	Thread DareTimeThread = null;
	GridView gridview;
	ArrayList arrayListTemp = new ArrayList();
	private ConnectivityManager connectivityManager;
	private Activity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.mainmenu);
		ButterKnife.bind(this);
		mActivity = this;

		iData = getIntent();
		iData.getStringExtra("PosName");

		widgetInit();
		imgChangePOS = (ImageView) findViewById(R.id.changePOS);
		imgLogout = (ImageView) findViewById(R.id.changeLogout);


		edtMasterMenuSearch = customSearchView.getEditText();
		edtMasterMenuSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count)
			{
				ArrayList arrListTemp = new ArrayList();
				for (int cnt = 0; cnt < arrListMainMenuForms.size(); cnt++)
				{

					clsMainMenuBean objMainMenu = (clsMainMenuBean) arrListMainMenuForms.get(cnt);
					String formName = objMainMenu.getModuleName_strModuleName().toString().toLowerCase();
					s = s.toString().toLowerCase();
					if (formName.contains(s))
					{
						arrListTemp.add(arrListMainMenuForms.get(cnt));
					}
				}
				arrayListTemp=arrListTemp;
				funFillMainMenuGrid(arrListTemp);
			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		gridview = (GridView) findViewById(R.id.masterMenuGridView);

		if (new clsUtility().isInternetConnectionAvailable(connectivityManager)) {

			try {
				funGetMainMenuList(moduletype);
				/*Assign pos date to global variable for header*/

				funCallWebService objWS = new funCallWebService();
				String[] newUrl = clsGlobalFunctions.gAPOSWebSrviceURL.toString().split("APOSIntegration");
				String url = newUrl[0] + "prjSanguineWebService/clsUtilityController/funGetPOSWiseDayEndData?POSCode=" + clsGlobalFunctions.gPOSCode + "&UserCode=" + clsGlobalFunctions.gUserCode;
				objWS.execute(url);
				objWS.get();
				JSONObject mJsonObj = new JSONObject();
				mJsonObj = objWS.get();

				if (null != mJsonObj.get("startDate")) {
					clsGlobalFunctions.gPOSStartDate = mJsonObj.get("startDate").toString();
					clsGlobalFunctions.gShiftEnd = mJsonObj.get("ShiftEnd").toString();
					clsGlobalFunctions.gDayEnd = mJsonObj.get("DayEnd").toString();
					clsGlobalFunctions.gShiftNo = Integer.valueOf(mJsonObj.get("ShiftNo").toString());
					boolean isShiftFound = Boolean.parseBoolean(mJsonObj.get("gShifts").toString());
					if (isShiftFound) {
						clsGlobalFunctions.gBillDateTimeType = mJsonObj.get("gBillDateTimeType").toString();
					}
				}


			} catch (Exception e) {
				e.printStackTrace();
			}


		} else {
			Toast.makeText(clsMainMenu.this, "Internet Connection Not Found", Toast.LENGTH_LONG).show();
		}


		imgLogout.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				AlertDialog.Builder builder1 = new AlertDialog.Builder(clsMainMenu.this);
				builder1.setMessage("Do You Want To Exit???");
				builder1.setCancelable(true);
				builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id)
							{
								clsGlobalFunctions.gCallingAvailable = "No";
								clsGlobalFunctions.gHashMapItemList.clear();
								clsGlobalFunctions.gArrListMenuItemMaster.clear();
								Intent loginIntent = new Intent(clsMainMenu.this, clsLoginActivity.class);
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
		});

		imgChangePOS.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				clsGlobalFunctions.gHashMapItemList.clear();
				clsGlobalFunctions.gArrListMenuItemMaster.clear();
				intent = new Intent(clsMainMenu.this, clsPOSSelection.class);
				startActivity(intent);
			}
		});

		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
				funInvokeActivity(position);
			}
		});

		gridview.requestFocus();

		System.out.println("BillPrinter Main Menu= " + clsGlobalFunctions.gBillPrinterType);

		Runnable runnable = new CountDownRunner();
		DareTimeThread = new Thread(runnable);
		//DareTimeThread.start();
	}

	private void funFillMainMenuGrid(ArrayList arrListTemp) {

		gridview.setAdapter(new clsMainMenuAdapter(mActivity, arrListTemp));
	}

	public void funInvokeActivity(int position)
	{
		clsMainMenuBean obj = (clsMainMenuBean) arrayListTemp.get(position);
		funLoadMasterForms(obj.getModuleName_strModuleName(), obj.getStrDayStartYN());

	}


	private void funLoadMasterForms(String formName, String startedPOSYN) {
		try {

			if(!clsGlobalFunctions.gAPOSWebSrviceURL.contains("prjSanguineWebService/APOSIntegration"))
			{
				clsGlobalFunctions.gAPOSWebSrviceURL=clsGlobalFunctions.gAPOSWebSrviceURL+"prjSanguineWebService/APOSIntegration";
			}

			switch (formName)
			{
				case "Direct Biller":
					if (startedPOSYN.equals("StartDay")) {
						Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
					} else {
						if (clsGlobalFunctions.gCounterWiseBilling.equals("Yes")) {
							Intent intentecounter = new Intent(clsMainMenu.this, clsCounterDtl.class);
							intentecounter.putExtra("PosName", tvHeaderTitleName.getText());
							startActivity(intentecounter);
						} else {
							Intent DirectBiller = new Intent(clsMainMenu.this, clsDirectBill.class);
							DirectBiller.putExtra("CounterCode", "");
							startActivity(DirectBiller);
						}
					}
					break;

				case "Make KOT":
					if (startedPOSYN.equals("StartDay")) {
						Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
					} else {
						Intent MakeKOT = new Intent(clsMainMenu.this, clsKotScreen.class);
						MakeKOT.putExtra("PosName", tvHeaderTitleName.getText());
						startActivity(MakeKOT);
					}
					break;

				case "VoidKot":
					if (startedPOSYN.equals("StartDay")) {
						Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
					} else {
						Intent voidKOT = new Intent(clsMainMenu.this, clsVoidKotScreen.class);
						voidKOT.putExtra("PosName", tvHeaderTitleName.getText());
						startActivity(voidKOT);
					}
					break;



				case "Make Bill":
					if (startedPOSYN.equals("StartDay")) {
						Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
					} else {
						Intent MakeBill = new Intent(clsMainMenu.this, clsMakeBillScreen.class);
						MakeBill.putExtra("PosName", tvHeaderTitleName.getText());
						startActivity(MakeBill);
					}
					break;

				case "Sales Report":
					Intent SalesReport = new Intent(clsMainMenu.this, clsSalesReportScreen.class);
					SalesReport.putExtra("PosName", tvHeaderTitleName.getText());
					startActivity(SalesReport);
					break;

				case "Reprint":
					if (startedPOSYN.equals("StartDay")) {
						Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
					} else {
						Intent reprint = new Intent(clsMainMenu.this, clsReprintDocuments.class);
						reprint.putExtra("PosName", tvHeaderTitleName.getText());
						startActivity(reprint);
					}
					break;

				case "SettleBill":
					if (startedPOSYN.equals("StartDay")) {
						Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
					} else {
						Intent settleBill = new Intent(clsMainMenu.this, clsBillList.class);
						settleBill.putExtra("PosName", tvHeaderTitleName.getText());
						startActivity(settleBill);
					}
					break;

				case "TableStatusReport":
					Intent tableStatus = new Intent(clsMainMenu.this, clsViewTableStatusScreen.class);
					tableStatus.putExtra("PosName", tvHeaderTitleName.getText());
					startActivity(tableStatus);
					break;

				case "NCKOT":
					if (startedPOSYN.equals("StartDay")) {
						Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
					} else {
						Intent NCKOT = new Intent(clsMainMenu.this, clsNCKOTScreen.class);
						NCKOT.putExtra("PosName", tvHeaderTitleName.getText());
						startActivity(NCKOT);
					}
					break;

				case "Take Away":
					if (startedPOSYN.equals("StartDay")) {
						Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
					} else {
						Intent takeAway = new Intent(clsMainMenu.this, clsTakeAwayScreen.class);
						takeAway.putExtra("PosName", tvHeaderTitleName.getText());
						startActivity(takeAway);
					}
					break;

				case "Table Reservation":
					if (startedPOSYN.equals("StartDay")) {
						Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
					} else {
						Intent tableReservation = new Intent(clsMainMenu.this, clsReservation.class);
						tableReservation.putExtra("PosName", tvHeaderTitleName.getText());
						startActivity(tableReservation);
					}
					break;

				case "POS Wise Sales":
					Intent posWiseReport = new Intent(clsMainMenu.this, clsPOSSalesReportScreen.class);
					posWiseReport.putExtra("PosName", tvHeaderTitleName.getText());
					startActivity(posWiseReport);
					break;

				case "Customer Order":
					if (startedPOSYN.equals("StartDay")) {
						Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
					} else {
						Intent customerOrder = new Intent(clsMainMenu.this, clsCustomerOrderScreen.class);
						customerOrder.putExtra("PosName", tvHeaderTitleName.getText());
						startActivity(customerOrder);
					}
					break;

				case "Non Available Items":
					if (startedPOSYN.equals("StartDay")) {
						Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
					} else {
						Intent nonAvailableScreen = new Intent(clsMainMenu.this, clsNonAvailableItemsScreen.class);
						nonAvailableScreen.putExtra("PosName", tvHeaderTitleName.getText());
						startActivity(nonAvailableScreen);
					}
					break;

				case "Mini Make KOT":
					if (startedPOSYN.equals("StartDay")) {
						Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
					} else {
						Intent miniKOT = new Intent(clsMainMenu.this, clsMiniMakeKotScreen.class);
						miniKOT.putExtra("PosName", tvHeaderTitleName.getText());
						startActivity(miniKOT);
					}
					break;

				case "Day End":
					Intent dayEnd = new Intent(clsMainMenu.this, clsDayEndScreen.class);
					dayEnd.putExtra("PosName", tvHeaderTitleName.getText());
					startActivity(dayEnd);
					break;

				case "KDSForKOTBookAndProcess":
					if (startedPOSYN.equals("StartDay")) {
						Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
					} else {
						Intent costCenterScreen = new Intent(clsMainMenu.this, clsCostCenterScreen.class);
						costCenterScreen.putExtra("PosName", tvHeaderTitleName.getText());
						startActivity(costCenterScreen);
					}
					break;

				case "Kitchen Process System":
					if (startedPOSYN.equals("StartDay")) {
						Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
					} else {
						Intent waiterScreen = new Intent(clsMainMenu.this, clsWaiterSelectionScreen.class);
						waiterScreen.putExtra("PosName", tvHeaderTitleName.getText());
						startActivity(waiterScreen);
					}
					break;


			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void widgetInit() {
		clsGlobalFunctions.gPhoneNo = "";
		clsGlobalFunctions.gPOSDateHeader="";
		intent = new Intent();
		moduletype = "";
		connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		tvHeaderTitleName = (TextView) findViewById(R.id.tv_direct_bill_header_title_name);
		tvHeaderTitleName.setText(iData.getStringExtra("PosName"));
		if(!clsGlobalFunctions.gAPOSWebSrviceURL.contains("prjSanguineWebService/APOSIntegration"))
		{
			clsGlobalFunctions.gAPOSWebSrviceURL=clsGlobalFunctions.gAPOSWebSrviceURL+"prjSanguineWebService/APOSIntegration";
		}

	}

	public void doWork() {
		runOnUiThread(new Runnable() {
			public void run() {
				try {
					if(clsGlobalFunctions.gPOSDateHeader!=""){
						TextView txtCurrentTime = (TextView) findViewById(R.id.tv_MainMenu_bill_header_timestamp);
						String formattedTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
						txtCurrentTime.setText(clsGlobalFunctions.gPOSDateHeader+" "+formattedTime);
					}else{
						TextView txtCurrentTime = (TextView) findViewById(R.id.tv_MainMenu_bill_header_timestamp);
						String formattedTime = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
						txtCurrentTime.setText("");

					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder1 = new AlertDialog.Builder(clsMainMenu.this);
		builder1.setMessage("Do You Want To Exit???");
		builder1.setCancelable(true);
		builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id)
					{
						clsGlobalFunctions.gCallingAvailable = "No";
						clsGlobalFunctions.gHashMapItemList.clear();
						clsGlobalFunctions.gArrListMenuItemMaster.clear();
						Intent loginIntent = new Intent(clsMainMenu.this, clsLoginActivity.class);
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


	class CountDownRunner implements Runnable {
		// @Override
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				try {
						doWork();
						Thread.sleep(1000);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				} catch (Exception e) {
				}
			}
		}
	}


	private class funCallWebService extends AsyncTask<String, Void, JSONObject> {
		protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
			InputStream in = entity.getContent();
			StringBuffer out = new StringBuffer();
			int n = 1;
			while (n > 0) {
				byte[] b = new byte[4096];
				n = in.read(b);
				if (n > 0) out.append(new String(b, 0, n));
			}
			return out.toString();
		}


		@Override
		protected JSONObject doInBackground(String... params) {

			HttpClient httpClient = new DefaultHttpClient();
			HttpContext localContext = new BasicHttpContext();
			HttpGet httpGet = new HttpGet(params[0]);
			String text = null;
			JSONObject jObj = new JSONObject();
			try {
				HttpResponse response = httpClient.execute(httpGet, localContext);
				HttpEntity entity = response.getEntity();
				text = getASCIIContentFromEntity(entity);
				jObj = new JSONObject(text);

			} catch (Exception e) {
			}
			return jObj;
		}

		protected void onPostExecute(JSONObject mJsonObj) {

		}
	}

	/**
	 * HTTP request for poslist.
	 */

	private void funGetMainMenuList(String moduleType) {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				boolean flgSuperUser = true;
				if (clsGlobalFunctions.gSuperUser.equals("No")) {
					flgSuperUser = false;
				}
				App.getAPIHelper().funGetMainMenuList(clsGlobalFunctions.gUserCode, moduleType, clsGlobalFunctions.gClientCode, flgSuperUser, clsGlobalFunctions.gPOSCode, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
					@Override
					public void onSuccess(JsonObject jObj) {
						if (null != jObj) {
							try {
								if (null != jObj.get("POSDate")) {
									String POSDate = jObj.get("POSDate").getAsString();
									clsGlobalFunctions.gPOSDate = POSDate;
									DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
									Date date=df.parse(clsGlobalFunctions.gPOSDate);
									clsGlobalFunctions.gPOSDateHeader=new SimpleDateFormat("dd-MM-yyyy").format(date);

									JsonArray mJsonArray = (JsonArray) jObj.get("MainMenuList");
									JsonObject mJsonObject = new JsonObject();
									for (int i = 0; i < mJsonArray.size(); i++) {
										mJsonObject = (JsonObject) mJsonArray.get(i);
										if (mJsonObject.get("ModuleName").getAsString().equals("")) {
											//memberInfo = "no data";
										} else {

											clsMainMenuBean objMainMenu = new clsMainMenuBean();
											objMainMenu.setModuleName_strModuleName(mJsonObject.get("ModuleName").getAsString());
											objMainMenu.setImageName_strImageName(mJsonObject.get("ImageName").getAsString());
											objMainMenu.setStrDayStartYN(POSDate);
											if (clsGlobalFunctions.gPOSVerion.equals("Lite")) {
												if (mJsonObject.get("ModuleName").getAsString().equals("Sales Report")) {
													arrayListTemp.add(objMainMenu);
												}
											} else {
												arrayListTemp.add(objMainMenu);
											}
										}
									}
								}

								if (arrayListTemp.size() > 0) {
									arrListMainMenuForms = arrayListTemp;
									funFillMainMenuGrid(arrayListTemp);
								} else {
									Toast.makeText(clsMainMenu.this, "Please Start Day!!!", Toast.LENGTH_SHORT).show();
								}

							} catch (Exception e) {
								e.printStackTrace();
							}

						}
						DareTimeThread.start();
					}

					@Override
					public void onFailure(String errorMessage, int errorCode) {

					}
				});
			} else {
				SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
		}
	}


}
