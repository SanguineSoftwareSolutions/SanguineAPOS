package com.example.apos.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import com.example.apos.App;
import com.example.apos.adapter.clsKotTableAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.fragments.clsCustomerOrderMenuFragment;
import com.example.apos.fragments.clsCustomerOrderWaiterFragment;
import com.example.apos.listeners.clsKotTableListSelectionListener;
import com.example.apos.listeners.clsMakeKotActListener;
import com.example.apos.util.clsUtility;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * }
 * Created by User on 27-04-2017.
 */

public class clsCustomerOrderScreen extends Activity implements clsKotTableListSelectionListener {
	private clsKotTableListSelectionListener tableSelectionListener;
	// private EditText edtTableSearch;
	private GridView kotTableGridview;
	public ArrayList<clsTableMaster> arrListTableMaster;
	private String keyCase = "upperCase", isRefreshScreen = "N";
	public clsMakeKotActListener objMakeKotActListener;
	private ConnectivityManager connectivityManager;
	private TextView txtTableName;
	public static Button btnRefresh;
	public static String tableNo, waiterNo, screenName = "TableScreen";
	public static TextView txtHeading, txtMenuName, txtWaiterName;
	private String paxNo = "";
	public static Activity mActivity;
	private Dialog pgDialog;
	//  public static FloatingActionButton btnCart;
	public static HashMap<String, clsKotItemsListBean> gblHmCartData = new HashMap<String, clsKotItemsListBean>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		setContentView(R.layout.customerordertablescreen);
		widget();
		try {
			tableSelectionListener = (clsKotTableListSelectionListener) clsCustomerOrderScreen.this;
		} catch (Exception ex) {
			Log.i("Tg", " Unable to initialize database." + ex.getMessage());
		}

		if (new clsUtility().isInternetConnectionAvailable(connectivityManager)) {
			//new clsTableWebService().execute();
			funGetTableList();
		} else {
			Toast.makeText(clsCustomerOrderScreen.this, "Internet Connection Not Found", Toast.LENGTH_LONG).show();
		}




		btnRefresh.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {

				//Creating the instance of PopupMenu
				PopupMenu popup = new PopupMenu(clsCustomerOrderScreen.this, btnRefresh);
				//Inflating the Popup using xml file
				popup.getMenuInflater()
						.inflate(R.menu.cust_order_menulist, popup.getMenu());

				//registering popup with OnMenuItemClickListener
				popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
					public boolean onMenuItemClick(MenuItem item)
					{
						FragmentManager fragmentManager=null;
						FragmentTransaction fragmentTransaction=null;
						Bundle bundle1=null;
						Fragment newFragment=null;
						FragmentTransaction transaction=null;
						switch (item.getTitle().toString())
						{
							case "Menu":
								txtHeading.setText("Menu");
								fragmentManager = getFragmentManager();
								fragmentTransaction = fragmentManager.beginTransaction();
								//add a fragment
								bundle1 = new Bundle();
								bundle1.putString("FromDate", " ");
								bundle1.putString("ToDate", " ");
								bundle1.putString("ReportType", " ");
								newFragment = new clsCustomerOrderMenuFragment();
								newFragment.setArguments(bundle1);

								// Create new transaction
								transaction = getFragmentManager().beginTransaction();
								transaction.replace(R.id.linearCustomerOrder, newFragment);
								transaction.addToBackStack(null);
								// Commit the transaction
								transaction.commit();
							  break;

							case "Waiter":
								clsCustomerOrderScreen.screenName = "WaiterScreen";
								txtHeading.setText("Waiter");
								clsCustomerOrderScreen.txtMenuName.setText("");
								clsCustomerOrderScreen.txtWaiterName.setText("");
								clsCustomerOrderScreen.waiterNo = "";
								gblHmCartData.clear();
								fragmentManager = getFragmentManager();
								fragmentTransaction = fragmentManager.beginTransaction();

								//add a fragment
								bundle1 = new Bundle();
								bundle1.putString("FromDate", " ");
								bundle1.putString("ToDate", " ");
								bundle1.putString("ReportType", " ");
								newFragment = new clsCustomerOrderWaiterFragment();
								newFragment.setArguments(bundle1);

								// Create new transaction
								transaction = getFragmentManager().beginTransaction();
								transaction.replace(R.id.linearCustomerOrder, newFragment);
								transaction.addToBackStack(null);

								// Commit the transaction
								transaction.commit();
                              break;

							case "Table":
								clsCustomerOrderScreen.screenName = "TableScreen";
								tableNo = "";
								txtTableName.setText("");
								clsCustomerOrderScreen.txtMenuName.setText("");
								clsCustomerOrderScreen.txtWaiterName.setText("");
								clsCustomerOrderScreen.waiterNo = "";
								gblHmCartData.clear();
								Intent intent = new Intent();
								intent.setClass(clsCustomerOrderScreen.mActivity, clsCustomerOrderScreen.class);
								startActivity(intent);
                              break;

						}
						return true;
					}
				});

				popup.show(); //showing popup menu
			}
		});





	}


	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		if (screenName.equals("TableScreen")) {
			//  isRefreshScreen="Y";
			// arrListTableMaster.clear();
			// finish();

			Intent intent = new Intent(clsCustomerOrderScreen.this, clsMainMenu.class);
			intent.putExtra("PosName", clsGlobalFunctions.gPOSName);
			intent.putExtra("PosCode", clsGlobalFunctions.gPOSCode);
			startActivity(intent);
			finish();

		} else if (screenName.equals("WaiterScreen")) {
			clsCustomerOrderScreen.screenName = "TableScreen";
			tableNo = "";
			txtTableName.setText("");
			Intent intent = new Intent();
			intent.setClass(clsCustomerOrderScreen.mActivity, clsCustomerOrderScreen.class);
			startActivity(intent);
		} else if (screenName.equals("MenuScreen")) {
			clsCustomerOrderScreen.screenName = "WaiterScreen";
			txtHeading.setText("Waiter");
			clsCustomerOrderScreen.txtWaiterName.setText("");
			clsCustomerOrderScreen.waiterNo = "";
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

			//add a fragment
			Bundle bundle1 = new Bundle();
			bundle1.putString("FromDate", " ");
			bundle1.putString("ToDate", " ");
			bundle1.putString("ReportType", " ");
			Fragment newFragment = new clsCustomerOrderWaiterFragment();
			newFragment.setArguments(bundle1);

			// Create new transaction
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.linearCustomerOrder, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();

		} else if (screenName.equals("ItemScreen")) {
			clsCustomerOrderScreen.screenName = "MenuScreen";
			txtHeading.setText("Menu");
			clsCustomerOrderScreen.txtMenuName.setText("");
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

			//add a fragment
			Bundle bundle1 = new Bundle();
			bundle1.putString("FromDate", " ");
			bundle1.putString("ToDate", " ");
			bundle1.putString("ReportType", " ");
			Fragment newFragment = new clsCustomerOrderMenuFragment();
			newFragment.setArguments(bundle1);

			// Create new transaction
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.linearCustomerOrder, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}

	}

	private void widget() {
		mActivity = clsCustomerOrderScreen.this;
		txtTableName = (TextView) findViewById(R.id.txtTableName);
		txtHeading = (TextView) findViewById(R.id.txtheading);
		btnRefresh = (Button) findViewById(R.id.btnRefresh);
		txtMenuName = (TextView) findViewById(R.id.txtMenuName);
		txtWaiterName = (TextView) findViewById(R.id.txtWaiterName);
		btnRefresh.setVisibility(View.INVISIBLE);
		kotTableGridview = (GridView) findViewById(R.id.kottable_gridview);
		connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		// btnCart=(FloatingActionButton) findViewById(R.id.btnCart);
		// btnCart.setVisibility(View.INVISIBLE);
	}

	@Override
	public void getTableListSelected(String strTableNo, String strTableName) {
		if (isRefreshScreen.equals("Y")) {

		} else {
			String status = "";
			for (clsTableMaster objTable : arrListTableMaster) {
				if (objTable.getStrTableNo().equals(strTableNo)) {
					status = objTable.getStrTableStatus();
					break;
				}
			}
			tableNo = strTableNo;
			txtTableName.setText(Html.fromHtml( "<b>"+"TABLE"+ "</b>" +
					"<br>"+strTableName+"<br/>"));
			clsCustomerOrderScreen.screenName = "WaiterScreen";
			//funOpenDialogForPaxSelection();

			txtHeading.setText("WAITER");
			FragmentManager fragmentManager = getFragmentManager();
			FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

			//add a fragment
			Bundle bundle1 = new Bundle();
			bundle1.putString("FromDate", " ");
			bundle1.putString("ToDate", " ");
			bundle1.putString("ReportType", " ");
			Fragment newFragment = new clsCustomerOrderWaiterFragment();
			newFragment.setArguments(bundle1);

			// Create new transaction
			FragmentTransaction transaction = getFragmentManager().beginTransaction();
			transaction.replace(R.id.linearCustomerOrder, newFragment);
			transaction.addToBackStack(null);

			// Commit the transaction
			transaction.commit();
		}


	}

	@Override
	public void getDirectKotItemListSelected(String strTableNo, String strTableName, String strWaiterNo, String strWaiterName) {
		String status = "", cardType = "", cardNo = "";
		double kotAmt = 0, cardBalance = 0;
		int paxNo = 1;
		for (clsTableMaster objTable : arrListTableMaster) {
			if (objTable.getStrTableNo().equals(strTableNo)) {
				status = objTable.getStrTableStatus();
				kotAmt = objTable.getDblKOTAmt();
				cardBalance = objTable.getDblCardBalanace();
				cardType = objTable.getStrCardType();
				paxNo = objTable.getIntPaxNo();
				cardNo = objTable.getStrCardNo();
				break;
			}
		}
	}


	private void funFillTableGrid(ArrayList arrListTableMaster) {
		kotTableGridview.setAdapter(new clsKotTableAdapter(clsCustomerOrderScreen.this, arrListTableMaster, tableSelectionListener));
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
								funFillTableGrid(arrListTableMaster);
							}
						}
					}

					@Override
					public void onFailure(String errorMessage, int errorCode) {
						dismissDialog();

					}

				});
			} else {
				SnackBarUtils.showSnackBar(clsCustomerOrderScreen.this, R.string.no_internet_connection);

			}
		} else {
			SnackBarUtils.showSnackBar(clsCustomerOrderScreen.this, R.string.setup_your_server_settings);
		}
	}


	protected void showDialog() {
		if (null == pgDialog) {
			pgDialog = CommonUtils.getProgressDialog(clsCustomerOrderScreen.this, 0, false);
		}
		pgDialog.show();
	}

	protected void dismissDialog() {
		if (null != pgDialog) {
			pgDialog.dismiss();
		}
	}
}
