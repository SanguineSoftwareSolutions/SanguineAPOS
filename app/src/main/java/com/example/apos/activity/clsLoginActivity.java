package com.example.apos.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.aposlicence.clsClientDetails;
import com.example.apos.bean.clsKOTItemDtlBean;
import com.example.apos.bean.clsKotMenuItemsBean;
import com.example.apos.config.AppConstants;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.PreferenceUtil;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.dialogs.ServerConfigurationFragment;
import com.example.apos.dto.ConfigurationDTO;
import com.example.apos.dto.UserDTO;
import com.example.apos.dto.clsGlobalDTO;
import com.example.apos.util.clsSQLControllerForKOT;
import com.example.apos.util.clsUtility;
import com.google.gson.JsonObject;

import java.net.URLEncoder;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class clsLoginActivity extends AppCompatActivity {

	EditText txtUserName;
	EditText txtPassword;
	Button btnLogin;
	TextView edtTxt;
	Thread DateTimeThread = null;
	private TextView tvHeaderClassname = null, tvHeaderTitleName = null, tvHeaderTimeStamp = null;

	private clsSQLController dbcon;
	private clsSQLControllerForKOT dbKOTCon;
	String urlData, kotPrintrData, billPrinterData;
	private String keyCase = "upperCase";
	private String edtData = "";
	private String android_id = "";
	private ConnectivityManager connectivityManager;
	private boolean hasTerminalLicence = false;
	private TreeMap<String, String> urlMap = new TreeMap<String, String>();
	//private ImageView btnDialog;


	static final String ACTION_SCAN = "com.google.zxing.client.android.SCAN";

	private Activity mActivity;
	private Dialog pgDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//Full screen is set for the Window
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.loginscreen);
		mActivity = this;
		ButterKnife.bind(mActivity);

		widgetInit();


		Runnable runnable = new CountDownRunner();
		DateTimeThread = new Thread(runnable);
		DateTimeThread.start();

		dbcon = new clsSQLController(this);
		try {
			dbcon.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}

		edtTxt = (TextView) findViewById(R.id.edtText);

		android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
				Settings.Secure.ANDROID_ID);
		edtTxt.setText(android_id);


		String data = funSetData();
		if (!data.isEmpty()) {
			//System.out.println("Data= "+data);
			String[] spData = data.split("#");
			clsGlobalFunctions.gAPOSWebSrviceURL = (spData[0]);
			clsGlobalFunctions.gBillPrinterType = (spData[2]);

			System.out.println(spData[0]);
			System.out.println("BillPrinter Login= " + clsGlobalFunctions.gBillPrinterType);





		}

		List<clsKotMenuItemsBean> list = dbcon.getWebServieURLList();
		if (list.size() > 0) {
			String result = "";
			for (int cnt = 0; cnt < list.size(); cnt++) {
				clsKotMenuItemsBean objBean = list.get(0);
				clsGlobalFunctions.gAPOSWebSrviceURL = objBean.getStrMenuItemName();
				clsGlobalFunctions.gBillPrinterType = objBean.getStrItemImage();

				System.out.println(objBean.getStrMenuItemName());
				System.out.println("BillPrinter Login= " + clsGlobalFunctions.gBillPrinterType);
			}
		}


		btnLogin.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(txtUserName.getWindowToken(), 0);
				imm.hideSoftInputFromWindow(txtPassword.getWindowToken(), 0);
				String strUserName = txtUserName.getText().toString();
				String strPassword = txtPassword.getText().toString();
				if (strUserName.length() > 0 && strPassword.length() > 0) {
					try {
						if (new clsUtility().isInternetConnectionAvailable(connectivityManager)) {

							requestLogin(strUserName, strPassword);

							/*clsGlobalFunctions.gPendingKOTItem = funGetPendingKOTData();
							if (!clsGlobalFunctions.gPendingKOTItem.isEmpty()) {
								Toast.makeText(clsLoginActivity.this, "data" + clsGlobalFunctions.gPendingKOTItem.size(), Toast.LENGTH_LONG).show();
							}*/

						} else {
							Toast.makeText(clsLoginActivity.this, "Internet Connection Not Found", Toast.LENGTH_LONG).show();
						}

					} catch (Exception e) {
						Toast.makeText(clsLoginActivity.this, "Some problem occurred", Toast.LENGTH_LONG).show();
					}
				} else {
					Toast.makeText(clsLoginActivity.this, "Username or Password is empty", Toast.LENGTH_LONG).show();
				}
			}
		});
		setupUser();
	}

	private void setupUser() {
		if (null != PreferenceUtil.getUser())
		{
			UserDTO userDto=PreferenceUtil.getUser();
			txtUserName.setText(userDto.getUserName());
			txtUserName.setSelection(txtUserName.getText().length());
			txtPassword.setText("");

			if (null != PreferenceUtil.getConfg())
			{
				final ConfigurationDTO objConfig = PreferenceUtil.getConfg();
				clsGlobalFunctions.gBillPrinterType=objConfig.getBILLPrinter();
			}
			else
			{
				clsGlobalFunctions.gBillPrinterType="External Printer";
			}
		}

	}


	private String funSetData() {
		String data = dbcon.funFetchData();
		return data;
	}

	public void funInitSetupWebService(String POSCode) {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {

				App.getAPIHelper().funGetSetupValues(POSCode, new BaseAPIHelper.OnRequestComplete<clsGlobalDTO>() {
					@Override
					public void onSuccess(clsGlobalDTO object) {
						if (null != object) {
							String setupStatus = "";
							if (object.getStatus().equals("Error")) {
								setupStatus = "Error";
								Toast.makeText(clsLoginActivity.this, "Required Stucture Update", Toast.LENGTH_LONG).show();
							} else {
								object.savePreference();
								setupStatus = "Success";
								funCheckAPOSLicence();
							}
						}
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


	public void getLastBillDate() {
		if (ConnectivityHelper.isConnected()) {
			App.getAPIHelper().getLastBillDate(new BaseAPIHelper.OnRequestComplete<JsonObject>() {
				@Override
				public void onSuccess(JsonObject jObj) {
					try {
						if (null != jObj) {
							//JSONObject jObj = new JSONObject(object.toString());

							if (!jObj.get("lastBilldate").toString().equals("Not Found")) {
								try {
									clsGlobalFunctions.gLastBillDate = jObj.get("lastBilldate").getAsString();
									SimpleDateFormat dFormat = new SimpleDateFormat("yyyy-MM-dd");
									Date systemDate = dFormat.parse(dFormat.format(new Date()));
									Date POSExpiryDate = dFormat.parse(dFormat.format(clsClientDetails.hmClientDtl.get(clsGlobalFunctions.gClientCode).expiryDate));
									long ExpiryDateTime = POSExpiryDate.getTime();
									long TimeDifference = 0;
									if (!clsGlobalFunctions.gLastBillDate.equals("0")) {
										Date gMaxBillDate = dFormat.parse(clsGlobalFunctions.gLastBillDate);
										TimeDifference = ExpiryDateTime - gMaxBillDate.getTime();
										long diffDays = TimeDifference / (24 * 60 * 60 * 1000);
										if (diffDays <= 15) {
											Toast.makeText(clsLoginActivity.this, diffDays + " Days Remaining For Licence to Expire", Toast.LENGTH_LONG).show();
										}
									} else {
										TimeDifference = ExpiryDateTime - systemDate.getTime();
									}


									if (TimeDifference >= 0) {
										getTerminalRegistrationDetails();
									} else {
										Toast.makeText(clsLoginActivity.this, "Invalid POS Please Contact Technical Support, Licence is exceeded", Toast.LENGTH_LONG).show();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							} else {
								Toast.makeText(clsLoginActivity.this, "Last Bill Date Not Found!!", Toast.LENGTH_LONG).show();
								return;
							}
						} else {
							Toast.makeText(clsLoginActivity.this, "Error!!", Toast.LENGTH_LONG).show();
							return;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				@Override
				public void onFailure(String errorMessage, int errorCode) {
				}
			});
		} else {
			SnackBarUtils.showSnackBar(mActivity, "No internet available or not connected to any network");
		}
	}


	public void getTerminalRegistrationDetails() {
		if (ConnectivityHelper.isConnected()) {
			final clsUtility objUtility = new clsUtility();
			final String hostName = objUtility.funGetHostName();
			App.getAPIHelper().getTerminalRegistrationDetails(objUtility.funGetCurrentMACAddress(clsLoginActivity.this), "APOS", clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
				@Override
				public void onSuccess(JsonObject jObj) {

					if (null != jObj) {

						try {
							//JSONObject jObj = new JSONObject(object.toString());
							String[] arrTerminalData = jObj.get("MaxTerminal").toString().split("\\.");
							int intMAXTerminalFromDB = Integer.parseInt(arrTerminalData[0]);
							String isRegistered = "No";
							int intMAXTerminalFromLicence = funGetMAXTerminalFromLicence(clsGlobalFunctions.gClientCode);
							if (intMAXTerminalFromDB == intMAXTerminalFromLicence) {
								if (jObj.get("RegisterTerminal").getAsString().equalsIgnoreCase("True")) {
									hasTerminalLicence = true;
								} else {
									hasTerminalLicence = false;
								}

							} else if (intMAXTerminalFromDB < intMAXTerminalFromLicence) {
								if (jObj.get("RegisterTerminal").getAsString().equalsIgnoreCase("True")) {
									hasTerminalLicence = true;
								} else {
									App.getAPIHelper().funRegisterTerminal(hostName, objUtility.funGetCurrentMACAddress(clsLoginActivity.this), clsGlobalFunctions.gClientCode, clsGlobalFunctions.gUserCode, "APOS", new BaseAPIHelper.OnRequestComplete<JsonObject>() {
										@Override
										public void onSuccess(JsonObject object) {
											hasTerminalLicence = true;
										}

										@Override
										public void onFailure(String errorMessage, int errorCode) {
											hasTerminalLicence = false;
										}
									});
								}
							} else {
								hasTerminalLicence = false;
							}

							if (hasTerminalLicence) {
								funSetPOSVerion(clsGlobalFunctions.gClientCode);
								Intent intent = new Intent(clsLoginActivity.this, clsPOSSelection.class);
								startActivity(intent);
							}
							else
							{
								Toast.makeText(clsLoginActivity.this, "Please Contact Technical Support, Licence is exceeded", Toast.LENGTH_LONG).show();
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						Toast.makeText(clsLoginActivity.this, "Please Contact Technical Support!!", Toast.LENGTH_LONG).show();
					}
				}

				@Override
				public void onFailure(String errorMessage, int errorCode) {
				}
			});
		} else {
			SnackBarUtils.showSnackBar(mActivity, "No internet available or not connected to any network");
		}
	}


	public void doWork() {
		runOnUiThread(new Runnable() {
			public void run() {
				try {
					TextView txtCurrentTime = (TextView) findViewById(R.id.tv_direct_bill_header_timestamp);
					String formattedDate = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss").format(Calendar.getInstance().getTime());
					txtCurrentTime.setText(formattedDate);
				} catch (Exception e) {
					//e.printStackTrace();
				}
			}
		});
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

	private void widgetInit()
	{
		clsGlobalFunctions.gHashMapItemList = new HashMap<>();
		clsGlobalFunctions.gArrListMenuItemMaster=new ArrayList();
		tvHeaderClassname = (TextView) findViewById(R.id.tv_direct_bill_header_class_name);
		tvHeaderTitleName = (TextView) findViewById(R.id.tv_direct_bill_header_title_name);
		tvHeaderTimeStamp = (TextView) findViewById(R.id.tv_direct_bill_header_timestamp);
		txtUserName = (EditText) findViewById(R.id.userid);
		txtPassword = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btnLogin);
		//btnReset = (Button) findViewById(R.id.btnReset);
		//btnExit = (Button) findViewById(R.id.btnExit);
		billPrinterData = "";
		connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		clsClientDetails.funAddClientCodeAndName();
		setupUser();

	}


	//for scan QR and get authenticated user by card string
	@OnClick(R.id.btnScanner)
	public void onClickScan() {
		try {
			Intent intent = new Intent(ACTION_SCAN);
			intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
			startActivityForResult(intent, 0);
		} catch (ActivityNotFoundException anfe) {
			showDialog(clsLoginActivity.this, "No Scanner Found", "Download a scanner code activity?", "Yes", "No").show();
		}
	}

	private static AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
		AlertDialog.Builder downloadDialog = new AlertDialog.Builder(act);
		downloadDialog.setTitle(title);
		downloadDialog.setMessage(message);

		downloadDialog.setPositiveButton(buttonYes, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
				Uri uri = Uri.parse("market://search?q=pname:" + "com.google.zxing.client.android");
				Intent intent = new Intent(Intent.ACTION_VIEW, uri);
				try {
					act.startActivity(intent);
				} catch (ActivityNotFoundException anfe) {

				}
			}
		});
		downloadDialog.setNegativeButton(buttonNo, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialogInterface, int i) {
			}
		});

		return downloadDialog.show();

	}

	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (requestCode == 0) {
			if (resultCode == RESULT_OK) {
				String debitCardString = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				try {
					if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL))
					{
						if (ConnectivityHelper.isConnected()) {
							App.getAPIHelper().serverAuthByQRScan(URLEncoder.encode(debitCardString, "UTF-8"), new BaseAPIHelper.OnRequestComplete<UserDTO>() {
								@Override
								public void onSuccess(UserDTO object) {
									if (null != object) {
										switch (object.getStatus()) {
											case AppConstants.STATUS_EXPIRED:
												Toast.makeText(clsLoginActivity.this, "User Has Expired", Toast.LENGTH_LONG).show();
												break;

											case AppConstants.STATUS_INVALID:
												Toast.makeText(clsLoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
												break;

											case AppConstants.STATUS_OK:
												clsGlobalFunctions.gUserCode = object.getUserCode();
												clsGlobalFunctions.gUserName = object.getUserName();

												if (object.getUserType().equals("op"))
													clsGlobalFunctions.gSuperUser = "No";
												else
													clsGlobalFunctions.gSuperUser = "Yes";

												funInitSetupWebService("All");
										}
									}
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
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	private int funGetMAXTerminalFromLicence(String clientCode) {
		int intMAXTerminal = 0;
		try {
			clsClientDetails objClientDetails = clsClientDetails.hmClientDtl.get(clientCode);
			intMAXTerminal = objClientDetails.getIntMAXTerminal();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			return intMAXTerminal;
		}
	}


	private void funSetPOSVerion(String posVersion) {
		clsGlobalFunctions.gPOSVerion = posVersion;
	}




	private List<clsKOTItemDtlBean> funGetPendingKOTData() {
		dbKOTCon = new clsSQLControllerForKOT(this);
		try {
			dbKOTCon.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		List<clsKOTItemDtlBean> list = dbKOTCon.funFetchData();
		return list;
	}


	/**
	 * Fragment dialog for server configuration.
	 */
	@OnClick(R.id.btnDialog)
	public void onClickConfiguration() {
		ServerConfigurationFragment configurationFragment = new ServerConfigurationFragment();
		configurationFragment.setCallback(new ServerConfigurationFragment.Callback() {
			@Override
			public void onSuccess() {
				SnackBarUtils.showSnackBar(mActivity, "Server configuration successfully created");
			}
		});
		configurationFragment.show(getSupportFragmentManager(), null);
		configurationFragment.setCancelable(false);
	}

	/**
	 * HTTP request for login.
	 */

	private void requestLogin(String userName, String password) {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				showDialog();
				App.getAPIHelper().serverAuth(userName, password, new BaseAPIHelper.OnRequestComplete<UserDTO>() {
					@Override
					public void onSuccess(UserDTO object) {
						dismissDialog();
						if (null != object) {
							switch (object.getStatus()) {

								case AppConstants.STATUS_EXPIRED:
									Toast.makeText(clsLoginActivity.this, "User Has Expired", Toast.LENGTH_LONG).show();
									break;

								case AppConstants.STATUS_INVALID:
									Toast.makeText(clsLoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
									break;

								case AppConstants.STATUS_OK:
									PreferenceUtil.setUser(object);
									clsGlobalFunctions.gUserCode = txtUserName.getText().toString();
									clsGlobalFunctions.gUserName = object.getUserName();
									if (object.getUserType().equals("op"))
										clsGlobalFunctions.gSuperUser = "No";
									else
										clsGlobalFunctions.gSuperUser = "Yes";

									// Initialize setup values
									funInitSetupWebService("All");
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


	public void funCheckAPOSLicence() {
		if (ConnectivityHelper.isConnected()) {
			clsUtility objUtility = new clsUtility();
			final String hostName = objUtility.funGetHostName();
			App.getAPIHelper().funCheckAPOSLicence(clsGlobalFunctions.gClientCode,objUtility.funGetCurrentMACAddress(clsLoginActivity.this), hostName, clsGlobalFunctions.gUserCode, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
				@Override
				public void onSuccess(JsonObject jObj) {

					if (null != jObj) {

						try
						{

						    if(jObj.get("Status").getAsString().equals("success"))
							{
								funSetPOSVerion(jObj.get("POSVersion").getAsString());
								Intent intent = new Intent(clsLoginActivity.this, clsPOSSelection.class);
								startActivity(intent);
							}
							else if(jObj.get("Status").getAsString().equals("Terminal Exceeded"))
							{
								Toast.makeText(mActivity, "Please Contact Technical Support, Licence is exceeded", Toast.LENGTH_LONG).show();
							}
							else if(jObj.get("Status").getAsString().equals("Invalid"))
							{
								Toast.makeText(mActivity, "Please Contact Technical Support, Licence is exceeded", Toast.LENGTH_LONG).show();
							}
							else
							{
								Toast.makeText(mActivity, "Please Contact Technical Support!!", Toast.LENGTH_LONG).show();
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
					} else {
						Toast.makeText(mActivity, "Please Contact Technical Support!!", Toast.LENGTH_LONG).show();
					}
				}

				@Override
				public void onFailure(String errorMessage, int errorCode) {
				}
			});
		} else {
			SnackBarUtils.showSnackBar(mActivity, "No internet available or not connected to any network");
		}
	}



	/*@Override
	public void onBackPressed()
	{
		*//*android.os.Process.killProcess(android.os.Process.myPid());
		//System.exit(0);*//*


	}
*/
    @Override
    public void onBackPressed()
    {
		txtPassword.setText("");
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }


	/**
	 * Progressbar methods for show and dismiss.
	 */
	private void showDialog() {
		if (null == pgDialog) {
			pgDialog = CommonUtils.getProgressDialog(mActivity, 0, false);
		}
		pgDialog.show();
	}

	private void dismissDialog() {
		if (null != pgDialog) {
			pgDialog.dismiss();
		}
	}
}

