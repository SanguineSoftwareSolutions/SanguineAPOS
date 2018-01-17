package com.example.apos.activity;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bewo.mach.MACHPrinter;
import com.bewo.mach.tools.MACHServices;
import com.example.apos.App;
import com.example.apos.adapter.clsButtonAdapter;
import com.example.apos.adapter.clsKotSelectedItemsCustomBaseAdapter;
import com.example.apos.adapter.clsKotViewPagerAdapter;
import com.example.apos.adapter.clsOrderDialogAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsBillSettlementDtl;
import com.example.apos.bean.clsItemModifierBean;
import com.example.apos.bean.clsKOTItemDtlBean;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.bean.clsTDHBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.dto.clsTDHWithModifierDTO;
import com.example.apos.fragments.clsKotItemListFragment;
import com.example.apos.fragments.clsKotMenuItemFragment;
import com.example.apos.fragments.clsPaxFragment;
import com.example.apos.listeners.clsKotItemListSelectionListener;
import com.example.apos.listeners.clsMakeKotActListener;
import com.example.apos.listeners.clsPaxNoSelectionListener;
import com.example.apos.util.clsPrintDemo;
import com.example.apos.util.clsSQLControllerForKOT;
import com.example.apos.util.clsSlidingTabLayout;
import com.example.apos.util.clsUtility;
import com.example.apos.util.mach.clsPrintFormatAPI;
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
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static android.text.InputType.TYPE_CLASS_NUMBER;
import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;
import static com.example.apos.activity.clsLoginActivity.ACTION_SCAN;

public class clsKotScreen extends ActionBarActivity implements View.OnClickListener, clsKotSelectedItemsCustomBaseAdapter.customButtonListener, clsMakeKotActListener, clsButtonAdapter.customItemListener {

	private TextView tvHeaderTimeStamp = null;
	private TextView tvKotHeaderTableName = null;
	private TextView tvKotHeaderWaiterName = null;

	private TextView edtKotTotalAmount = null;
	private EditText edtExternalCode = null;
	private ListView lvKotSelectedItems = null;
	public clsGlobalFunctions objGlobal;
	public clsKotSelectedItemsCustomBaseAdapter objKotItemDtl;
	private Button btnKotPrint = null;
	public static Activity mActivity;
	public static ViewPager pager;
	public int previousKOTSize = 0;
	clsPaxFragment objPax;
	clsKotViewPagerAdapter adapter;
	clsSlidingTabLayout tabs;
	Intent iData;
	CharSequence Titles[] = {"Table", "Waiter", "Item", "Menu", "Pax"};
	int Numboftabs = 5;

	private double subTotalAmt;
	Map<String, clsBillSettlementDtl> hmBillSettleAmtDtl = new HashMap<String, clsBillSettlementDtl>();
	public List<clsKOTItemDtlBean> arrListKOTItems, arrListPreviousKOTItems, arrListNewKOTItems;
	public String tableNo;
	public String debitNo;
	public String waiterNo;
	public String areaCode;
	public Context mContext;
	public String itemcode;
	public int listpos;
	private String keyCase = "upperCase";
	public String tableStatus = "Normal",billingTableStatus="Normal";

	MACHServices machService;
	private String debitCardMemberName;
	private String debitCardMemberCode;
	private double totalMemberBalance;
	public clsKotItemListSelectionListener objMakeKotItemListSelectionListener;
	private clsPaxNoSelectionListener objPaxNoSelection;
	private Button btnCheckKOT;
	private String cardType;
	private String cardNo;
	private double KOTAmt;
	private String NCKOT;
	private String reasonCode;
	private String kotRemark;
	private int paxNo;
	public double taxTotalAmt, userEnteredRate = 0;
	private ConnectivityManager connectivityManager;
	private String menuSelection;
	private BluetoothAdapter mBluetoothAdapter;
	private clsSQLControllerForKOT dbCon;
	public Map<String, List<clsTDHBean>> hm_ComboItemDtl = new HashMap<>();
	public Dialog tdhDialog;
	private Map<String, Map<String, String>> mapSelectedMenuAndItemList;
	private Map<String, Integer> mapSelectedMenu;
	private Map<String, Double> mapModifierItemAndQty;
	private HashMap<String, clsTDHBean> hm_ModifierGroupDetail = null;
	private HashMap<String, clsTDHBean> hm_ModifierItemDetail = null;
	private String customerCode = "";
	private Dialog pgDialog;
	List<clsTDHBean> arrListModifierGroup ;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		super.onCreate(savedInstanceState);
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));

		getSupportActionBar().hide();
		setContentView(R.layout.makekotscreen);

		iData = getIntent();
		Runnable runnable = new CountDownRunner();
		arrListKOTItems = new ArrayList<clsKOTItemDtlBean>();
		arrListPreviousKOTItems = new ArrayList<clsKOTItemDtlBean>();
		arrListNewKOTItems = new ArrayList<clsKOTItemDtlBean>();
		widgetInit();
		// tvHeaderTimeStamp.setText(iData.getStringExtra("PosName"));
		mActivity = clsKotScreen.this;
		objPax = new clsPaxFragment();
		objMakeKotItemListSelectionListener = (clsKotItemListSelectionListener) clsKotItemListFragment.getInstance();
		objPaxNoSelection = (clsPaxNoSelectionListener) clsPaxFragment.getInstance();
		adapter = new clsKotViewPagerAdapter(this.getSupportFragmentManager(), Titles, Numboftabs, pager);
		pager = (ViewPager) findViewById(R.id.pager_kot);
		pager.setAdapter(adapter);
		tabs = (clsSlidingTabLayout) findViewById(R.id.tabs_kot);
		tabs.setDistributeEvenly(true);
		tabs.setCustomTabColorizer(new clsSlidingTabLayout.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}
		});
		tabs.setViewPager(pager);

		edtExternalCode.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				if ((actionId == EditorInfo.IME_ACTION_DONE) || (actionId == 0)) {
					String externalCode = edtExternalCode.getText().toString();
					if (!externalCode.isEmpty()) {
						if (new clsUtility().isInternetConnectionAvailable(connectivityManager)) {
							try {
								funGetItemDetailsByExternalCode(externalCode);
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							Toast.makeText(clsKotScreen.mActivity, "Internet Connection Not Found", Toast.LENGTH_LONG).show();
						}
					}
				}
				return false;
				}
			});
	}

	private void widgetInit() {
		if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer")) {
			mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
			if (!mBluetoothAdapter.isEnabled()) {
				Toast.makeText(clsKotScreen.this, "Bluetooth is disable", Toast.LENGTH_LONG).show();

			} else {
				new clsPrintDemo().funPrintViaBluetoothPrinter(mBluetoothAdapter);
			}
		}

		dbCon = new clsSQLControllerForKOT(this);
		try {
			dbCon.open();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NCKOT = "N";
		reasonCode = "";
		kotRemark = "";
		totalMemberBalance = 0;
		KOTAmt = 0;
		paxNo = 1;
		debitNo = "";
		cardType = "";
		cardNo = "";
		areaCode="";
		debitCardMemberName = "";
		debitCardMemberCode = "";
		subTotalAmt = 0;
		waiterNo = "";
		tableNo = "";
		btnKotPrint = (Button) findViewById(R.id.btnkotSettle);
		btnKotPrint.setOnClickListener(this);
		lvKotSelectedItems = (ListView) findViewById(R.id.listkotselecteditems);
		edtKotTotalAmount = (TextView) findViewById(R.id.edt_kot_total_order_amount);
		//  tvHeaderTimeStamp.setText(Utils.getCurrentDate());
		tvKotHeaderTableName = (TextView) findViewById(R.id.tv_kot_header_table_name);
		tvKotHeaderWaiterName = (TextView) findViewById(R.id.tv_kot_header_waiter_name);
		edtExternalCode = (EditText) findViewById(R.id.edtExternalCode);
		edtExternalCode.setVisibility(View.INVISIBLE);
		//btnMember.setOnClickListener(this);

		edtKotTotalAmount.setText("0.00");
		menuSelection = "N";
		btnCheckKOT = (Button) findViewById(R.id.btnCheckKOT);
		btnCheckKOT.setOnClickListener(this);
		mapSelectedMenuAndItemList = new HashMap<>();
		mapSelectedMenu = new HashMap<>();
		mapModifierItemAndQty = new HashMap<>();
		hm_ModifierGroupDetail = new HashMap<>();
		hm_ModifierItemDetail = new HashMap<>();


		try {
			funCheckAndGetModifierListForTDH();
		} catch (Exception e) {
			e.printStackTrace();
		}
		funGetTDHListWS();

	}


	@Override
	public void setTableSelectedResult(String strTableNo, String strTableName, String status,String strAreaCode) {
		// Check if waiter selection is disable for table. If Y then open item tab else open waiter tab

		funResetFields();

		if (clsGlobalFunctions.gSkipWaiter.equals("Y")) {
			if (clsGlobalFunctions.gSkipPax.equals("Y")) {
				edtExternalCode.setVisibility(View.VISIBLE);
				pager.setCurrentItem(2);
			} else {
				clsPaxFragment.tableNo = strTableNo;
				objPaxNoSelection.getPaxSelectedNo(strTableNo);
				System.out.println("Orientation= " + getWindowManager().getDefaultDisplay().getOrientation());
				pager.setCurrentItem(4);
			}

		} else {
			pager.setCurrentItem(1);
		}
		tvKotHeaderTableName.setText(strTableName);
		tableNo = strTableNo;
		tableStatus = status;
		areaCode=strAreaCode;

      //System.out.println(areaCode);
	}


	@Override
	public void setDirectKotItemListSelectedResult1(String strTableNo, String strTableName, String strWaiterNo, String strWaiterName, String status, double kotAmt, double cardBalance, String cardType, int paxNo, String cardNo,String linkedWaiterNo,String strAreaCode) {

		if(!linkedWaiterNo.toString().isEmpty())
		{
			edtExternalCode.setVisibility(View.VISIBLE);
			pager.setCurrentItem(2);
			waiterNo = strWaiterNo;
			tvKotHeaderWaiterName.setText(strWaiterName);
		}
		else
		{
			// Check if waiter selection is compulsory for each kot for same table. If Y then open waiter tab else open item tab
			if(clsGlobalFunctions.gMultipleWaiterSelection.equals("Y"))
			{
				pager.setCurrentItem(1);
			}
			else
			{
				edtExternalCode.setVisibility(View.VISIBLE);
				pager.setCurrentItem(2);
				waiterNo=strWaiterNo;
				tvKotHeaderWaiterName.setText(strWaiterName);
			}
		}
		tvKotHeaderTableName.setText(strTableName);
		tableNo = strTableNo;
		tableStatus = status;
		KOTAmt = kotAmt;
		totalMemberBalance = cardBalance;
		this.cardType = cardType;
		this.cardNo = cardNo;
		this.paxNo = paxNo;
		areaCode=strAreaCode;

		//System.out.println(areaCode);

		// new LoadPrevoiusKotItemList().execute();
		funLoadPrevoiusKotItemList();


	}

	@Override
	public void setWaiterSelectedResult(String strWaiterNo, String strWaiterName) {
		//Toast.makeText(this, "Select Table ", Toast.LENGTH_SHORT).show();

		if (tableNo.isEmpty()) {
		} else {
			if (clsGlobalFunctions.gSkipPax.equals("Y")) {
				edtExternalCode.setVisibility(View.VISIBLE);
				pager.setCurrentItem(2);
				waiterNo = strWaiterNo;
				tvKotHeaderWaiterName.setText(strWaiterName);
			} else {
				clsPaxFragment.tableNo = tableNo;
				waiterNo = strWaiterNo;
				objPaxNoSelection.getPaxSelectedNo(tableNo);
				tvKotHeaderWaiterName.setText(strWaiterName);
				System.out.println("Orientation= " + getWindowManager().getDefaultDisplay().getOrientation());
				pager.setCurrentItem(4);
			}
		}
	}

	@Override
	public void setSelectedPaxResult(int strPaxNo) {
		if (tableNo.isEmpty()) {

		} else {
			if (clsGlobalFunctions.gClientCode.equals("154.001")) {
				pager.setCurrentItem(3);
			} else {
				edtExternalCode.setVisibility(View.VISIBLE);
				pager.setCurrentItem(2);
			}
			paxNo = strPaxNo;
			Toast.makeText(clsKotScreen.this, "PaxNo=" + paxNo, Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public void setSelectedOtherOptionsResult(String strNcKot, String strRemark, String strReasonCode) {
		NCKOT = strNcKot;
		kotRemark = strRemark;
		reasonCode = strReasonCode;
		Toast.makeText(clsKotScreen.this, "NCKOT=" + NCKOT, Toast.LENGTH_LONG).show();

	}

	@Override
	public void funRefreshItemDtl(String strItemCode, String strItemName, String strSubGroupCode, double dblSalePrice) {
		userEnteredRate = 0;
		if(previousKOTSize>0)
		{
			if(!tableNo.isEmpty())
			{
				if(clsGlobalFunctions.gSkipWaiter.equals("N") )
				{
					if (!waiterNo.isEmpty())
					{
						funSelectAndAddItemToGridWithPreviousKOT(strItemCode,strItemName,strSubGroupCode,dblSalePrice);
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Please Select Waiter!!!", Toast.LENGTH_LONG).show();
						return;
					}
				}
				else
				{
					funSelectAndAddItemToGridWithPreviousKOT(strItemCode,strItemName,strSubGroupCode,dblSalePrice);
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Please Select Table!!!", Toast.LENGTH_LONG).show();
				return;
			}
		}
		else
		{
			if(!tableNo.isEmpty())
			{
				if(clsGlobalFunctions.gSkipWaiter.equals("N") )
				{
					if(!waiterNo.isEmpty())
					{
						funSelectAndAddItemToGridWithoutPreviousKOT(strItemCode,strItemName,strSubGroupCode,dblSalePrice);
					}
					else
					{
						Toast.makeText(getApplicationContext(), "Please Select Waiter!!!", Toast.LENGTH_LONG).show();
						return;
					}
				}
				else
				{
					funSelectAndAddItemToGridWithoutPreviousKOT(strItemCode,strItemName,strSubGroupCode,dblSalePrice);
				}
			}
			else
			{
				Toast.makeText(getApplicationContext(), "Please Select Table!!!", Toast.LENGTH_LONG).show();
				return;
			}

		}
		if (clsGlobalFunctions.gCalculateTaxOnMakeKOT.equals("Y")) {
			// Toast.makeText(clsKotScreen.this,"Calculating Tax...",Toast.LENGTH_LONG).show();
			// new TaxCalculateWS().execute();
			funTaxCalculate();
		}

		if (mapModifierItemAndQty.containsKey(strItemCode)) {
			String modifierGroupCode = "";
			int maxLimit = 0, minLimit = 0;
			for (Map.Entry<String, clsTDHBean> entry : hm_ModifierGroupDetail.entrySet()) {
				modifierGroupCode = entry.getKey();
				clsTDHBean objTdh = entry.getValue();
				maxLimit = objTdh.getIntItemMaxLimit();
				minLimit = objTdh.getIntItemMaxLimit();
				break;
			}
			mapSelectedMenu.put(modifierGroupCode, maxLimit);
			funOpenTDHDialogForModifier(strItemCode, strItemName, modifierGroupCode, minLimit, maxLimit);
		} else {
			if (hm_ComboItemDtl.size() > 0) {
				try {
					List<clsTDHBean> arrListOfModifierGroup = new ArrayList();
					/*clsTDHMenuListWS objTDH = new clsTDHMenuListWS();
					objTDH.execute(strItemCode);
					objTDH.get();*/

					arrListOfModifierGroup = funGetTDHMenuList(strItemCode);
					String menuCode = "";
					int minLimit = 0, maxLimit = 0;
					List<clsTDHBean> arrList = funGetTDHMenuList(strItemCode);
					for (int cnt = 0; cnt < arrList.size(); cnt++) {
						clsTDHBean objBean = arrList.get(0);
						menuCode = objBean.getStrTDHItemCode();
						double qty = objBean.getDblQty();
						maxLimit = (int) Math.round(qty);
						;
						break;
					}
					mapSelectedMenu.put(menuCode, maxLimit);
					funOpenTDHDialogForItem(strItemCode, strItemName, arrListOfModifierGroup, menuCode, minLimit, maxLimit);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

	}


	@Override
	public void setMenuItemSelectionCodeResult(String strMenuItemCode, String strMenuItemName, String strMenuType)
	{
		if(!tableNo.isEmpty())
		{
			if(clsGlobalFunctions.gSkipWaiter.equals("N") )
			{
				if (!waiterNo.isEmpty())
				{
					if (strMenuType.equals("MenuHead")) {
						if (clsGlobalFunctions.gMenuItemSortingOn.equals("subMenuHeadWise")) {
							clsKotMenuItemFragment obj = new clsKotMenuItemFragment();
							obj.populateSelectedSubMenuItems(strMenuItemCode, strMenuItemName);
							pager.setCurrentItem(3);
						} else {
							new clsKotItemListFragment().populateSelectedMenuItems(strMenuItemCode, strMenuItemName, strMenuType,areaCode);
							pager.setCurrentItem(2);
						}
					} else {
						new clsKotItemListFragment().populateSelectedMenuItems(strMenuItemCode, strMenuItemName, strMenuType,areaCode);
						pager.setCurrentItem(2);
					}
				}
				else
				{
					Toast.makeText(getApplicationContext(), "Please Select Waiter!!!", Toast.LENGTH_LONG).show();
					return;
				}
			}
			else
			{
				if (strMenuType.equals("MenuHead")) {
					if (clsGlobalFunctions.gMenuItemSortingOn.equals("subMenuHeadWise")) {
						clsKotMenuItemFragment obj = new clsKotMenuItemFragment();
						obj.populateSelectedSubMenuItems(strMenuItemCode, strMenuItemName);
						pager.setCurrentItem(3);
					} else {
						new clsKotItemListFragment().populateSelectedMenuItems(strMenuItemCode, strMenuItemName, strMenuType,areaCode);
						pager.setCurrentItem(2);
					}
				} else {
					new clsKotItemListFragment().populateSelectedMenuItems(strMenuItemCode, strMenuItemName, strMenuType,areaCode);
					pager.setCurrentItem(2);
				}
			}
		}
		else
		{
			Toast.makeText(getApplicationContext(), "Please Select Table!!!", Toast.LENGTH_LONG).show();
			return;
		}


	}


	private void funRefreshItemGrid() {

		int iListViewScrollPosition = -1;
		objKotItemDtl = new clsKotSelectedItemsCustomBaseAdapter(mActivity, arrListKOTItems, previousKOTSize);
		lvKotSelectedItems.setItemsCanFocus(true);
		lvKotSelectedItems.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		objKotItemDtl.setCustomButtonListner((clsKotSelectedItemsCustomBaseAdapter.customButtonListener) mActivity);
		lvKotSelectedItems.setAdapter(objKotItemDtl);
		lvKotSelectedItems.setSelection(objKotItemDtl.getCount() - 1);
		if (iListViewScrollPosition != -1) {
			lvKotSelectedItems.setSelection(iListViewScrollPosition);
		}
		subTotalAmt = 0;
		for (int l = 0; l < arrListKOTItems.size(); l++) {
			subTotalAmt = subTotalAmt + arrListKOTItems.get(l).getDblAmount();
			edtKotTotalAmount.setText(String.valueOf(Math.rint(subTotalAmt)));
		}

	}


	private void funClearObjects() {
		hmBillSettleAmtDtl = null;
		arrListKOTItems = null;
		clsGlobalFunctions.gCounterCode = "";

	}

	private void funResetFields() {
		arrListKOTItems.clear();
		arrListNewKOTItems.clear();
		arrListPreviousKOTItems.clear();
		funRefreshItemGrid();
		tvKotHeaderTableName.setText("");
		tvKotHeaderWaiterName.setText("");
		edtKotTotalAmount.setText("");
		tableNo = "";
		waiterNo = "";
		paxNo = 1;
		debitCardMemberCode = "";
		debitCardMemberName = "";
		//btnMember.setText("Select Member");
		pager.setCurrentItem(0);
		totalMemberBalance = 0;
		NCKOT = "N";
		btnKotPrint.setEnabled(true);

	}

	@Override
	public void onBackPressed() {
		//Toast.makeText(clsOrderActivity.this, "Back ", Toast.LENGTH_SHORT).show();

		if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer")) {
			try {
				if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer")) {
					new clsPrintDemo().closeBT(clsPrintDemo.mmOutputStream, clsPrintDemo.mmInputStream, clsPrintDemo.socket);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		funClearObjects();
		finish();
	}




	@Override
	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.btnkotSettle:

				btnKotPrint.setEnabled(false);
				if(billingTableStatus.equalsIgnoreCase("BillingInProgress"))
				{
					btnKotPrint.setEnabled(true);
					Toast.makeText(getApplicationContext(), "Billing is in process on this table!!!", Toast.LENGTH_LONG).show();
					return;
				}
				else
				{
					if (tableStatus.equalsIgnoreCase("Normal") && clsGlobalFunctions.gMemberCodeForKotInMposByCardSwipe.equals("Y"))
					{
						if (tableNo.isEmpty()) {
							Toast.makeText(getApplicationContext(), "Please Select Table!!!", Toast.LENGTH_LONG).show();
							return;
						}
						if (debitCardMemberCode.isEmpty()) {
							Toast.makeText(getApplicationContext(), "Please Select Member!!!", Toast.LENGTH_LONG).show();
							return;
						}
						funSetMemberDetail(debitCardMemberCode, debitCardMemberName, cardType, totalMemberBalance);


					} else if ((tableStatus.equalsIgnoreCase("Normal") || tableStatus.equalsIgnoreCase("Billed")) && clsGlobalFunctions.gCMSMemberForKOTMPOS.equals("Y"))
					{
				      funSetMemberDetail(debitCardMemberCode, debitCardMemberName, cardType, totalMemberBalance);
					}
					else
					{
						funSetMemberDetail(debitCardMemberCode, debitCardMemberName, cardType, totalMemberBalance);
					}
				}
				break;


			case R.id.btnCheckKOT:
				if (!tableNo.isEmpty()) {
					if (tableStatus.equalsIgnoreCase("Occupied")) {
						//new funCheckKOT().execute();
						funCheckKOT();
					} else {
						Toast.makeText(getApplicationContext(), "Please Save KOt First", Toast.LENGTH_LONG).show();
						return;
					}
				} else {
					Toast.makeText(getApplicationContext(), "Please Select Table", Toast.LENGTH_LONG).show();
					return;
				}
				break;

			default:
				break;
		}
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();

		machService = new MACHServices(getApplicationContext());
		machService.mach_initialize(getApplicationContext());

		try {

			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int connectStatus = machService.mach_connect();
		Toast.makeText(getApplicationContext(), "Connection Status : " + connectStatus, Toast.LENGTH_LONG).show();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		machService.mach_release(getApplicationContext());
	}


	private static android.support.v7.app.AlertDialog showDialog(final Activity act, CharSequence title, CharSequence message, CharSequence buttonYes, CharSequence buttonNo) {
		android.support.v7.app.AlertDialog.Builder downloadDialog = new android.support.v7.app.AlertDialog.Builder(act);
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
				String contents = intent.getStringExtra("SCAN_RESULT");
				String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
				//new DebitCardMemberWS().execute(contents, cardType);
			}
		}
	}


	private void funSetMemberDetail(String memberCode, String memberName, String attCardType, double attCardBal) {
		if (memberCode.isEmpty()) {
			memberCode = cardNo;
		}
		for (int cnt = 0; cnt < arrListKOTItems.size(); cnt++) {
			clsKOTItemDtlBean objKOTItemDtl = (clsKOTItemDtlBean) arrListKOTItems.get(cnt);
			objKOTItemDtl.setStrCustomerCode(customerCode);
			objKOTItemDtl.setStrCustomerName(memberName);
			objKOTItemDtl.setStrCardNo(memberCode);
			objKOTItemDtl.setStrCardType(attCardType);
			objKOTItemDtl.setDblRedeemAmt(attCardBal);
		}


		// double totalAmount= Double.parseDouble(edtKotTotalAmount.getText().toString())+taxTotalAmt;
		//  edtKotTotalAmount.setText(String.valueOf(totalAmount));


		if (cardType.equals("CashCard") && (tableStatus.equalsIgnoreCase("Normal") || tableStatus.equalsIgnoreCase("Billed"))) {   // For Cash Card with New KOT.

			// Web Service to insert debit card temp dtls
			//new DCTempTableWS().execute(memberCode);
			funInsertDebitCartTmpDtl(memberCode);
			double totalAmt = Double.parseDouble(edtKotTotalAmount.getText().toString()) + KOTAmt + taxTotalAmt;

			if (totalAmt > totalMemberBalance) {
				Toast.makeText(clsKotScreen.this, "Insufficient Balance!!!" + totalAmt, Toast.LENGTH_LONG).show();
			} else {
				debitCardMemberCode = "";
				debitCardMemberName = "";

				if (new clsUtility().isInternetConnectionAvailable(connectivityManager)) {
					dbCon.funDeleteQuery("delete from tblkottemp");
					// new FireKOTWS().execute();
					funFireKOTWS();

				} else {
					Toast.makeText(clsKotScreen.this, "Internet Connection Not Found", Toast.LENGTH_LONG).show();
					funSaveFailedKOTToLocalDB();
				}


			}
		} else if (cardType.equals("CashCard") && (tableStatus.equalsIgnoreCase("Occupied"))) // For Cash Card with Busy Table
		{
			double totalAmt = Double.parseDouble(edtKotTotalAmount.getText().toString()) + KOTAmt;

			if (totalAmt > totalMemberBalance) {
				Toast.makeText(clsKotScreen.this, "Insufficient Balance!!!" + totalAmt, Toast.LENGTH_LONG).show();
			} else {
				debitCardMemberCode = "";
				debitCardMemberName = "";
				if (new clsUtility().isInternetConnectionAvailable(connectivityManager)) {
					dbCon.funDeleteQuery("delete from tblkottemp");
					// new FireKOTWS().execute();
					funFireKOTWS();

				} else {
					Toast.makeText(clsKotScreen.this, "Internet Connection Not Found", Toast.LENGTH_LONG).show();
					funSaveFailedKOTToLocalDB();
				}

			}
		} else {   // For Member Card

			debitCardMemberCode = "";
			debitCardMemberName = "";
			if (new clsUtility().isInternetConnectionAvailable(connectivityManager)) {
				try {
					dbCon.funDeleteQuery("delete from tblkottemp");
					funFireKOTWS();
					//new FireKOTWS().execute();
					System.out.println("Update table");
				} catch (Exception e) {
					e.printStackTrace();
				}

			} else {
				Toast.makeText(clsKotScreen.this, "Internet Connection Not Found", Toast.LENGTH_LONG).show();
				funSaveFailedKOTToLocalDB();
			}
		}
	}


	public void doWork() {
		runOnUiThread(new Runnable() {
			public void run() {
				try {
					TextView txtCurrentTime = (TextView) findViewById(R.id.tv_direct_bill_header_timestamp);
					String formattedDate = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
					txtCurrentTime.setText(clsGlobalFunctions.gPOSDateHeader+" "+formattedDate);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	//for delete purpose
	@Override
	public void onButtonClickListner(int position, String value)
	{
		if (value.split("#")[1].equals("selectedrow"))
		{
			if(position>=previousKOTSize)
				{
					listpos = position;
					itemcode = value.split("#")[2];
					final Dialog dialog = new Dialog(clsKotScreen.this);
					dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
					dialog.setContentView(R.layout.edititemqty);
					final int pos = position;
					clsKOTItemDtlBean objKotItem = arrListKOTItems.get(position);


					ImageButton btnMinus = (ImageButton) dialog.findViewById(R.id.btnRemoveQty);
					ImageButton btnAdd = (ImageButton) dialog.findViewById(R.id.btnAddQty);
					Button btnOk = (Button) dialog.findViewById(R.id.btnUpdate);
					ImageView btnDeleteItem = (ImageView) dialog.findViewById(R.id.btnDeleteItem);
					ImageView btnCloseDialog = (ImageView) dialog.findViewById(R.id.btnClose);
					final EditText edtFFName = (EditText) dialog.findViewById(R.id.edtKotFFItemName);
					final EditText edtFFRate = (EditText) dialog.findViewById(R.id.edtKotFFItemRate);
					//Button btnApplyFreeFlowMod = (Button) dialog.findViewById(R.id.btnApplyFreeFlow);
					Button btnPredefinedModifier = (Button) dialog.findViewById(R.id.btnKotModifier);
					TextView txtItemName = (TextView) dialog.findViewById(R.id.txtItemName);
					final TextView txtQty = (TextView) dialog.findViewById(R.id.txtQty);

					txtItemName.setText(objKotItem.getStrItemName());
					txtQty.setText("" + objKotItem.getDblItemQuantity());

					btnMinus.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							List<clsKOTItemDtlBean> lstTemp = new ArrayList<clsKOTItemDtlBean>();
							lstTemp = arrListKOTItems;
							Toast.makeText(clsKotScreen.this, "Item Button click " + pos, Toast.LENGTH_SHORT).show();
							//clsKOTItemDtlBean objKotItem = arrListKOTItems.get(pos);
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
							} else if (qty == 1)
							{
								//lstTemp.remove(pos);
								String strDeleteItemCode = arrListKOTItems.get(pos).getStrItemCode();
								arrListKOTItems.remove(pos);
								funDeleteItemFromGrid(strDeleteItemCode);
								funRefreshItemGrid();
								dialog.dismiss();
							}
							txtQty.setText("" + qty1);
							// arrListKOTItems
							//arrListKOTItems=lstTemp;
						}
					});


					btnAdd.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							Toast.makeText(clsKotScreen.this, "Item Button click " + pos, Toast.LENGTH_SHORT).show();
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
							txtQty.setText(String.valueOf(qty1));
						}
					});


					btnDeleteItem.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (!v.isSelected()) {
								v.setSelected(true);
								Toast.makeText(clsKotScreen.this, "Item Button click " + pos, Toast.LENGTH_SHORT).show();
								clsKOTItemDtlBean objKotItem = arrListKOTItems.get(pos);
								String strDeleteItemCode = arrListKOTItems.get(pos).getStrItemCode();
								arrListKOTItems.remove(pos);
								funDeleteItemFromGrid(strDeleteItemCode);
							}

							funRefreshItemGrid();
							dialog.dismiss();

						}
					});

					btnOk.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v)
						{
							clsGlobalFunctions objGlobal = new clsGlobalFunctions();
							String dateTime = clsGlobalFunctions.funGetPOSDateTime();
							// dateTime=dateTime.replaceAll(" ","%20");
							String freeFlowModName="";
							try{
								freeFlowModName=new clsUtility().funCheckSpecialCharacters(edtFFName.getText().toString());
								if (edtFFName.getText().toString().isEmpty()) {
									Toast.makeText(clsKotScreen.this, "Enter Free Flow Modifier Name", Toast.LENGTH_SHORT).show();
								} else {
									double freeFlowRate = 0;
									InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
									imm.showSoftInput(edtFFName, InputMethodManager.SHOW_IMPLICIT);

									//freeFlowModName = edtFFName.getText().toString();
									if (!edtFFRate.getText().toString().isEmpty()) {
										freeFlowRate = Double.parseDouble(edtFFRate.getText().toString());
									}

									int sequenceNo = 1;
									String seq = "0";
									clsKOTItemDtlBean objItemDtl = null;
									for (int i = 0; i < arrListKOTItems.size(); i++) {
										clsKOTItemDtlBean objBean = arrListKOTItems.get(i);
										if (objBean.getStrItemCode().contains(itemcode)) {
											seq = objBean.getStrSerialNo();
											if (seq.contains(".")) {
												String[] spSeq = seq.split("\\.");
												sequenceNo = Integer.parseInt(spSeq[1]);
												sequenceNo++;
												objItemDtl = new clsKOTItemDtlBean(spSeq[0] + "." + String.valueOf(sequenceNo), tableNo, "", 0.00, "", "", clsGlobalFunctions.gPOSCode
														, itemcode + "M999", "-->" + freeFlowModName
														, 1, freeFlowRate, waiterNo, "K01", paxNo, "Y", ""
														, clsGlobalFunctions.gUserCode, clsGlobalFunctions.gUserCode
														, dateTime, dateTime, "No", "N", "N", "N", NCKOT, "", "", "", "", "", "", true, freeFlowRate, "");
											} else {
												objItemDtl = new clsKOTItemDtlBean(seq + "." + String.valueOf(sequenceNo), tableNo, "", 0.00, "", "", clsGlobalFunctions.gPOSCode
														, itemcode + "M999", "-->" + freeFlowModName
														, 1, freeFlowRate, waiterNo, "K01", paxNo, "Y", ""
														, clsGlobalFunctions.gUserCode, clsGlobalFunctions.gUserCode
														, dateTime, dateTime, "No", "N", "N", "N", NCKOT, "", "", "", "", "", "", true, freeFlowRate, "");
											}

										}
									}

									if (previousKOTSize > 0) {
										arrListNewKOTItems.add(objItemDtl);
										arrListKOTItems.add(listpos + 1, objItemDtl);
									} else {
										arrListKOTItems.add(listpos + 1, objItemDtl);
									}

								}
							}catch(Exception e){
								e.printStackTrace();
							}


							funRefreshItemGrid();
							dialog.dismiss();
						}
					});

					btnPredefinedModifier.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {

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
				else
				{
					Toast.makeText(clsKotScreen.this, "You can not edit item now!!!!", Toast.LENGTH_SHORT).show();
				}


		}

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

	private void hideDefaultKeyboard() {
		getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
	}

	// Web service call for CMS Member Validation

	//  private void funPrintKot(JSONArray jArrItemDtl,JSONArray jArrModItemDtl,String costCenterName, String tableName, String KOTNo,String PaxNo)
	private void funPrintKot(JsonArray jArrItemDtl, String costCenterName, String tableName, String KOTNo, String PaxNo) {
		try {

			StringBuilder sbPrintKot = new StringBuilder();
			clsGlobalFunctions objGlobal = new clsGlobalFunctions();
			clsPrintFormatAPI objPrint = new clsPrintFormatAPI();
			// System.out.println(clsGlobalFunctions.gPOSName+costCenterName+KOTNo+tableName);
			sbPrintKot.append(objPrint.funGetStringWithAlignment("KOT", "Left", 40));
			sbPrintKot.append("\n");
			sbPrintKot.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gPOSName, "Left", 40));
			sbPrintKot.append("\n");
			sbPrintKot.append(objPrint.funGetStringWithAlignment(costCenterName, "Left", 40));
			sbPrintKot.append("\n");
			sbPrintKot.append(objPrint.funGetStringWithAlignment("DINE", "Left", 40));
			sbPrintKot.append("\n");
			sbPrintKot.append("----------------------------------------");
			sbPrintKot.append("\n");
			sbPrintKot.append(objPrint.funGetStringWithAlignment("KOT NO: ", "Left", 13));
			sbPrintKot.append(objPrint.funGetStringWithAlignment(KOTNo, "Left", 27));
			sbPrintKot.append("\n");
			sbPrintKot.append(objPrint.funGetStringWithAlignment("Table Name: ", "Left", 13));
			sbPrintKot.append(objPrint.funGetStringWithAlignment(tableName, "Left", 10));
			sbPrintKot.append(objPrint.funGetStringWithAlignment("PAX: ", "Left", 10));
			sbPrintKot.append(objPrint.funGetStringWithAlignment(PaxNo, "Left", 7));
			sbPrintKot.append("\n");
			sbPrintKot.append(objPrint.funGetStringWithAlignment("DATE:", "Left", 13));
			sbPrintKot.append(objPrint.funGetStringWithAlignment(objGlobal.funGetCurrentDate(), "Left", 27));
			sbPrintKot.append("\n");
			sbPrintKot.append("----------------------------------------");
			sbPrintKot.append("\n");
			sbPrintKot.append(objPrint.funGetStringWithAlignment("QTY ", "Left", 10));
			sbPrintKot.append(objPrint.funGetStringWithAlignment("ITEMNAME ", "Left", 30));
			sbPrintKot.append("\n");
			sbPrintKot.append("----------------------------------------");
			sbPrintKot.append("\n");

			for (int i = 0; i < jArrItemDtl.size(); i++) {
				JsonObject objRows = (JsonObject) jArrItemDtl.get(i);
				String[] arrData = objRows.get("ItemQty").getAsString().split("\\.");
				int qty = Integer.parseInt(arrData[0]);
				sbPrintKot.append(objPrint.funGetStringWithAlignment(String.valueOf(qty), "Left", 10));
				sbPrintKot.append(objPrint.funGetStringWithAlignment(objRows.get("ItemName").getAsString(), "Left", 30));
				sbPrintKot.append("\n");
			}
			  /* if(jArrModItemDtl.length()>0)
			   {
                   for(int i=0;i<jArrModItemDtl.length();i++)
                   {
                       JSONObject objRows = jArrModItemDtl.getJSONObject(i);
                       sbPrintKot.append(objPrint.funGetStringWithAlignment(objRows.getString("ModItemQty"), "Left", 8));
                       sbPrintKot.append(objPrint.funGetStringWithAlignment(objRows.getString("ModItemName"), "Left", 22));
                       sbPrintKot.append("\n");
                   }
               }
               */
			sbPrintKot.append("----------------------------------------");

			sbPrintKot.append("\n");
			sbPrintKot.append("\n");


			// Function to print bill on mach device.
			// clsMakeBillScreen.funPrintBillForMachDevice(sbPrintBill.toString());

			if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer")) {
				Toast.makeText(clsKotScreen.this, "Kot Printed from Bluetooth Printer" + sbPrintKot.toString(), Toast.LENGTH_LONG).show();
				System.out.println("Kot Print"+sbPrintKot.toString());
				new clsPrintDemo().sendData(sbPrintKot.toString(), clsPrintDemo.mmOutputStream, clsPrintDemo.mmInputStream);
			} else {
				sbPrintKot.append("\n");
				sbPrintKot.append("\n");
				sbPrintKot.append("\n");
				sbPrintKot.append("\n");
				sbPrintKot.append("\n");
				sbPrintKot.append("\n");
				System.out.println(sbPrintKot.toString());
				//  funPrintBillForMachDevice(sbPrintKot.toString());
			}


		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	public void funPrintBillForMachDevice(String billFormat) {
		if (machService.isMachActive()) {
			byte print_return = machService.print_text(billFormat, (byte) 0x00, (byte) 0x02, false, (byte) 0x00, false, false, (byte) 0x00);
			//For Magnetic Swipe
			//byte[] track = machService.getMSRTrackData(20);

			switch (print_return) {
				case MACHPrinter.BAT_LOW_ERROR:
					Toast.makeText(clsKotScreen.this, "Printer Status : Battery Low ", Toast.LENGTH_LONG).show();
					break;
				case MACHPrinter.THERMALPRINTER_NO_PLATEN:
					Toast.makeText(clsKotScreen.this, "Printer Status : No Paper ", Toast.LENGTH_LONG).show();
					break;
				case MACHPrinter.THERMALPRINTER_NO_PAPER:
					Toast.makeText(clsKotScreen.this, "Printer Status : No Platen ", Toast.LENGTH_LONG).show();
					break;
				case MACHPrinter.THERMALPRINTER_NO_PRINTER:
					Toast.makeText(clsKotScreen.this, "Printer Status : No Printer Module ", Toast.LENGTH_LONG).show();
					break;
				case MACHPrinter.THERMALPRINTER_STATUS_OK:
					Toast.makeText(clsKotScreen.this, "Printer Status : OK ", Toast.LENGTH_LONG).show();
					break;
				case MACHPrinter.THERMALPRINTER_TIMEOUT_APP:
					Toast.makeText(clsKotScreen.this, "Printer Status : Timeout from the Application ", Toast.LENGTH_LONG).show();
					break;
				default:
					Toast.makeText(clsKotScreen.this, "Printer Status : " + print_return, Toast.LENGTH_LONG).show();
					break;
			}
		} else
			Toast.makeText(clsKotScreen.this, "Printer Status : MACH is not active", Toast.LENGTH_LONG).show();
	}


	//For check KOT

	private void funSaveFailedKOTToLocalDB() {
		funClearObjects();
		finish();
	}

	@Override
	public void onItemClickListner(int position, String value) {
		//  tdhDialog = new Dialog(clsKotScreen.this);

		int minLimit = 0, maxLimit = 0;
		String itemData = value.split("#")[3];
		String name = value.split("#")[1];
		String dataType = value.split("#")[2];
		String itemCode = itemData.split("!")[0];
		String itemName = itemData.split("!")[1];
		tdhDialog.dismiss();
		if (mapModifierItemAndQty.containsKey(itemCode)) {
			if (dataType.equals("TDHItems")) {
				String modifierCode = itemData.split("!")[2];
				maxLimit = Integer.parseInt(itemData.split("!")[3]);
				Map<String, String> mapItem = new HashMap<String, String>();

				if (mapSelectedMenuAndItemList.size() > 0 && mapSelectedMenuAndItemList.containsKey(modifierCode)) {
					mapItem = mapSelectedMenuAndItemList.get(modifierCode);
					if (mapItem.containsKey(value.split("#")[0])) {
						mapItem.remove(value.split("#")[0]);
						if (mapItem.size() == 0) {
							mapSelectedMenuAndItemList.remove(modifierCode);
						} else {
							mapSelectedMenuAndItemList.remove(modifierCode);
							mapSelectedMenuAndItemList.put(modifierCode, mapItem);
						}

					} else {
						if (mapItem.size() == maxLimit) {
							Toast.makeText(clsKotScreen.this, "You Have Selected Max Item Qty!!!!", Toast.LENGTH_LONG).show();
						} else {
							mapItem.put(value.split("#")[0], value.split("#")[1]);
							mapSelectedMenuAndItemList.put(modifierCode, mapItem);
						}
					}

				} else {
					mapItem.put(value.split("#")[0], value.split("#")[1]);
					mapSelectedMenuAndItemList.put(modifierCode, mapItem);
				}


				if (!modifierCode.isEmpty()) {
					funOpenTDHDialogForModifier(itemCode, itemName, modifierCode, minLimit, maxLimit);
				}
			} else {
				String modifierGroupCode = value.split("#")[0].toString();

				if (hm_ModifierGroupDetail.size() > 0 && hm_ModifierGroupDetail.containsKey(modifierGroupCode)) {
					clsTDHBean objBean = hm_ModifierGroupDetail.get(modifierGroupCode);
					maxLimit = objBean.getIntItemMaxLimit();
				}


				if (!modifierGroupCode.isEmpty()) {
					if (mapSelectedMenu.size() > 0 && mapSelectedMenu.containsKey(modifierGroupCode)) {
						mapSelectedMenu.remove(modifierGroupCode);
						mapSelectedMenu.put(modifierGroupCode, maxLimit);
					} else {
						mapSelectedMenu.put(modifierGroupCode, maxLimit);
					}
					funOpenTDHDialogForModifier(itemCode, itemName, modifierGroupCode, minLimit, maxLimit);
				}
			}

		} else {
			try {
				List<clsTDHBean> arrListOfModifierGroup = new ArrayList();
				/*clsTDHMenuListWS objTDH = new clsTDHMenuListWS();
				objTDH.execute(itemCode);
				objTDH.get();*/

				arrListOfModifierGroup = funGetTDHMenuList(itemCode);
				if (dataType.equals("TDHItems")) {
					String menuCode = itemData.split("!")[2];
					maxLimit = Integer.parseInt(itemData.split("!")[3]);
					Map<String, String> mapItem = new HashMap<String, String>();

					if (mapSelectedMenuAndItemList.size() > 0 && mapSelectedMenuAndItemList.containsKey(menuCode)) {
						mapItem = mapSelectedMenuAndItemList.get(menuCode);
						if (mapItem.containsKey(value.split("#")[0])) {
							mapItem.remove(value.split("#")[0]);
							if (mapItem.size() == 0) {
								mapSelectedMenuAndItemList.remove(menuCode);
							} else {
								mapSelectedMenuAndItemList.remove(menuCode);
								mapSelectedMenuAndItemList.put(menuCode, mapItem);
							}
						} else {
							if (mapItem.size() == maxLimit) {
								Toast.makeText(clsKotScreen.this, "You Have Selected Max Item Qty!!!!", Toast.LENGTH_LONG).show();
							} else {
								mapItem.put(value.split("#")[0], value.split("#")[1]);
								mapSelectedMenuAndItemList.put(menuCode, mapItem);
							}
						}

					} else {
						mapItem.put(value.split("#")[0], value.split("#")[1]);
						mapSelectedMenuAndItemList.put(menuCode, mapItem);
					}


					if (!menuCode.isEmpty()) {
						funOpenTDHDialogForItem(itemCode, itemName, arrListOfModifierGroup, menuCode, minLimit, maxLimit);
					}
				} else {

					String menuCode = "";
					List<clsTDHBean> arrList = funGetTDHMenuList();
					for (int cnt = 0; cnt < arrList.size(); cnt++) {
						clsTDHBean objBean = arrList.get(cnt);
						if (value.split("#")[0].toString().equals(objBean.getStrTDHItemCode())) {
							menuCode = objBean.getStrTDHItemCode();
							double qty = objBean.getDblQty();
							maxLimit = (int) Math.round(qty);
							break;
						}
					}

					if (!menuCode.isEmpty()) {
						if (mapSelectedMenu.size() > 0 && mapSelectedMenu.containsKey(menuCode)) {
							mapSelectedMenu.remove(menuCode);
							mapSelectedMenu.put(menuCode, maxLimit);
						} else {
							mapSelectedMenu.put(menuCode, maxLimit);
						}
						funOpenTDHDialogForItem(itemCode, itemName, arrListOfModifierGroup, menuCode, minLimit, maxLimit);
					}

				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


	}


	private void funOpenTDHDialogForItem(final String strItemCode, String strItemName, List<clsTDHBean> arrListOfModifierGroup, String menuCode, int minLimit, int maxLimit) {
		if (hm_ComboItemDtl.size() > 0) {
			if (hm_ComboItemDtl.containsKey(strItemCode)) {
				tdhDialog = new Dialog(clsKotScreen.this);
				tdhDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
				tdhDialog.setContentView(R.layout.tdhdialogform);
				final TextView txtItemName = (TextView) tdhDialog.findViewById(R.id.txtItemName);
				final TextView txtMinLimit = (TextView) tdhDialog.findViewById(R.id.txtMinLimit);
				final TextView txtMaxLimit = (TextView) tdhDialog.findViewById(R.id.txtMaxLimit);
				final Button btnDone = (Button) tdhDialog.findViewById(R.id.btnDone);
				final Button btnClose = (Button) tdhDialog.findViewById(R.id.btnClose);
				GridView gridviewModifierGroup, gridviewTDHItem;
				gridviewModifierGroup = (GridView) tdhDialog.findViewById(R.id.gridviewModifierGroup);
				gridviewTDHItem = (GridView) tdhDialog.findViewById(R.id.gridviewTDHItem);
				txtMinLimit.setText(String.valueOf(minLimit));
				txtMaxLimit.setText(String.valueOf(maxLimit));


				txtItemName.setText(strItemName);

				try {
					if (arrListOfModifierGroup.size() > 0) {
						ArrayList arrListOfMenu = new ArrayList();
						for (int cnt = 0; cnt < arrListOfModifierGroup.size(); cnt++) {
							clsTDHBean objBean = arrListOfModifierGroup.get(cnt);

							if (mapSelectedMenu.size() > 0 && mapSelectedMenu.containsKey(menuCode)) {
								if (objBean.getStrTDHItemCode().equals(menuCode)) {
									objBean.setIsSelected("Y");
								} else {
									objBean.setIsSelected("N");
								}

							} else {
								objBean.setIsSelected("N");
							}
							arrListOfMenu.add(objBean);
						}

						clsButtonAdapter objBtnAdapter = new clsButtonAdapter(clsKotScreen.mActivity, clsKotScreen.mActivity, arrListOfMenu, "Menu", strItemCode + "!" + strItemName + "!" + menuCode + "!" + maxLimit);
						objBtnAdapter.setCustomItemListner((clsButtonAdapter.customItemListener) mActivity);
						gridviewModifierGroup.setAdapter(objBtnAdapter);
					}


					List<clsTDHBean> arrListOfTDHItem = new ArrayList();
					arrListOfTDHItem = (ArrayList) hm_ComboItemDtl.get(strItemCode);


					ArrayList arrListOfSelectedTDHMenuItems = new ArrayList();
					Map<String, String> mapItem = new HashMap<String, String>();
					for (int cnt = 0; cnt < arrListOfTDHItem.size(); cnt++) {
						clsTDHBean objBean = arrListOfTDHItem.get(cnt);
						if (objBean.getStrModifierGroupCode().equals(menuCode)) {
							if (mapSelectedMenuAndItemList.size() > 0 && mapSelectedMenuAndItemList.containsKey(menuCode)) {
								mapItem = mapSelectedMenuAndItemList.get(menuCode);
								if (mapItem.containsKey(objBean.getStrTDHItemCode())) {
									objBean.setIsSelected("Y");
								} else {
									objBean.setIsSelected("N");
								}
							} else {
								objBean.setIsSelected("N");
							}

							arrListOfSelectedTDHMenuItems.add(objBean);
						}
					}
					clsButtonAdapter objBtnAdapter = new clsButtonAdapter(clsKotScreen.mActivity, clsKotScreen.mActivity, arrListOfSelectedTDHMenuItems, "TDHItems", strItemCode + "!" + strItemName + "!" + menuCode + "!" + maxLimit);
					objBtnAdapter.setCustomItemListner((clsButtonAdapter.customItemListener) mActivity);
					gridviewTDHItem.setAdapter(objBtnAdapter);


					btnDone.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							if (mapSelectedMenuAndItemList.size() > 0) {
								int pos = 0, seq = 0;
								for (int i = 0; i < arrListKOTItems.size(); i++) {
									clsKOTItemDtlBean objBean = arrListKOTItems.get(i);
									if (objBean.getStrItemCode().equals(strItemCode)) {
										pos = i;
										seq = Integer.parseInt(objBean.getStrSerialNo());
										break;
									}
								}
								clsGlobalFunctions objGlobal = new clsGlobalFunctions();
								String dateTime = clsGlobalFunctions.funGetPOSDateTime();
								int sequenceNo = 0;
								Iterator<Map.Entry<String, Map<String, String>>> itMenu = mapSelectedMenuAndItemList.entrySet().iterator();
								while (itMenu.hasNext()) {
									Map.Entry<String, Map<String, String>> menuEntry = itMenu.next();
									String menuCode = menuEntry.getKey();
									Iterator<Map.Entry<String, String>> itItems = menuEntry.getValue().entrySet().iterator();
									while (itItems.hasNext()) {
										Map.Entry<String, String> entry = itItems.next();
										String itemCode = entry.getKey();
										String itemName = entry.getValue();
										boolean flgAlreadyPresentMod = false;
										sequenceNo++;
										if (!flgAlreadyPresentMod) {
											clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean(String.valueOf(seq) + "." + String.valueOf(sequenceNo), tableNo, "", 0.00, "", "", clsGlobalFunctions.gPOSCode
													, itemCode, "=>" + itemName, mapSelectedMenu.get(menuCode), 0.00, waiterNo, "K01", paxNo, "Y", ""
													, clsGlobalFunctions.gUserCode, clsGlobalFunctions.gUserCode
													, dateTime, dateTime, "No", "N", "N", "N", NCKOT, "", "", "", "", "", "", true, 0.00, "");
											arrListKOTItems.add(pos + 1, objItemDtl);
											arrListNewKOTItems.add(objItemDtl);
											pos++;

										}

										funRefreshItemGrid();
									}
								}

								mapSelectedMenuAndItemList.clear();
								mapSelectedMenu.clear();
								onResume();
								tdhDialog.dismiss();
							}
						}
					});

					btnClose.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View v) {
							mapSelectedMenuAndItemList.clear();
							mapSelectedMenu.clear();
							tdhDialog.dismiss();

						}
					});

				} catch (Exception e) {
					e.printStackTrace();
				}


				tdhDialog.show();
				Window window = tdhDialog.getWindow();
				window.setGravity(Gravity.CENTER);
				window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
				window.getWindowManager().getDefaultDisplay();
			}
		}

	}


	private void funOpenTDHDialogForModifier(final String strItemCode, String strItemName, String modifierGroupCode, int minLimit, int maxLimit) {
		tdhDialog = new Dialog(clsKotScreen.this);
		tdhDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
		tdhDialog.setContentView(R.layout.tdhdialogform);
		final TextView txtItemName = (TextView) tdhDialog.findViewById(R.id.txtItemName);
		final TextView txtMinLimit = (TextView) tdhDialog.findViewById(R.id.txtMinLimit);
		final TextView txtMaxLimit = (TextView) tdhDialog.findViewById(R.id.txtMaxLimit);
		final Button btnDone = (Button) tdhDialog.findViewById(R.id.btnDone);
		final Button btnClose = (Button) tdhDialog.findViewById(R.id.btnClose);
		GridView gridviewModifierGroup, gridviewTDHItem;
		gridviewModifierGroup = (GridView) tdhDialog.findViewById(R.id.gridviewModifierGroup);
		gridviewTDHItem = (GridView) tdhDialog.findViewById(R.id.gridviewTDHItem);
		txtMinLimit.setText(String.valueOf(minLimit));
		txtMaxLimit.setText(String.valueOf(maxLimit));


		txtItemName.setText(strItemName);

		try {
			if (hm_ModifierGroupDetail.size() > 0) {
				ArrayList arrListOfModifierGroup = new ArrayList();

				for (Map.Entry<String, clsTDHBean> entry : hm_ModifierGroupDetail.entrySet()) {
					clsTDHBean objTdh = entry.getValue();
					if (mapSelectedMenu.size() > 0 && mapSelectedMenu.containsKey(modifierGroupCode)) {
						if (objTdh.getStrTDHItemCode().equals(modifierGroupCode)) {
							objTdh.setIsSelected("Y");
						} else {
							objTdh.setIsSelected("N");
						}
					} else {
						objTdh.setIsSelected("N");
					}
					arrListOfModifierGroup.add(objTdh);
				}

				clsButtonAdapter objBtnAdapter = new clsButtonAdapter(clsKotScreen.mActivity, clsKotScreen.mActivity, arrListOfModifierGroup, "Menu", strItemCode + "!" + strItemName + "!" + modifierGroupCode + "!" + maxLimit);
				objBtnAdapter.setCustomItemListner((clsButtonAdapter.customItemListener) mActivity);
				gridviewModifierGroup.setAdapter(objBtnAdapter);
			}


			if (hm_ModifierItemDetail.size() > 0) {
				ArrayList arrListModifierItem = new ArrayList();

				Map<String, String> mapItem = new HashMap<String, String>();
				for (Map.Entry<String, clsTDHBean> entry : hm_ModifierItemDetail.entrySet()) {
					clsTDHBean objTdh = entry.getValue();
					if (objTdh.getStrModifierGroupCode().equals(modifierGroupCode)) {
						if (mapSelectedMenuAndItemList.size() > 0 && mapSelectedMenuAndItemList.containsKey(modifierGroupCode)) {
							mapItem = mapSelectedMenuAndItemList.get(modifierGroupCode);
							if (mapItem.containsKey(objTdh.getStrTDHItemCode())) {
								objTdh.setIsSelected("Y");
							} else {
								objTdh.setIsSelected("N");
							}
						} else {
							objTdh.setIsSelected("N");
						}

						arrListModifierItem.add(objTdh);
					}

				}

				clsButtonAdapter objBtnAdapter = new clsButtonAdapter(clsKotScreen.mActivity, clsKotScreen.mActivity, arrListModifierItem, "TDHItems", strItemCode + "!" + strItemName + "!" + modifierGroupCode + "!" + maxLimit);
				objBtnAdapter.setCustomItemListner((clsButtonAdapter.customItemListener) mActivity);
				gridviewTDHItem.setAdapter(objBtnAdapter);
			}


			btnDone.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					if (mapSelectedMenuAndItemList.size() > 0) {
						int pos = 0, seq = 0;
						for (int i = 0; i < arrListKOTItems.size(); i++) {
							clsKOTItemDtlBean objBean = arrListKOTItems.get(i);
							if (objBean.getStrItemCode().equals(strItemCode)) {
								pos = i;
								seq = Integer.parseInt(objBean.getStrSerialNo());
								break;
							}
						}
						clsGlobalFunctions objGlobal = new clsGlobalFunctions();
						String dateTime = clsGlobalFunctions.funGetPOSDateTime();
						int sequenceNo = 0;
						Iterator<Map.Entry<String, Map<String, String>>> itMenu = mapSelectedMenuAndItemList.entrySet().iterator();
						while (itMenu.hasNext()) {
							Map.Entry<String, Map<String, String>> menuEntry = itMenu.next();
							String menuCode = menuEntry.getKey();
							Iterator<Map.Entry<String, String>> itItems = menuEntry.getValue().entrySet().iterator();
							while (itItems.hasNext()) {
								Map.Entry<String, String> entry = itItems.next();
								String itemCode = entry.getKey();
								String itemName = entry.getValue();
								boolean flgAlreadyPresentMod = false;
								sequenceNo++;

								double rate = 0;
								if (hm_ModifierItemDetail.containsKey(itemCode)) {
									clsTDHBean objBean = hm_ModifierItemDetail.get(itemCode);
									rate = objBean.getDblRate();
								}


								if (!flgAlreadyPresentMod) {
									clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean(String.valueOf(seq) + "." + String.valueOf(sequenceNo), tableNo, "", 0.00, "", "", clsGlobalFunctions.gPOSCode
											, strItemCode + itemCode, itemName
											, 1, rate, waiterNo, "K01", paxNo, "Y", ""
											, clsGlobalFunctions.gUserCode, clsGlobalFunctions.gUserCode
											, dateTime, dateTime, "No", "N", "N", "N", NCKOT, "", "", "", "", "", "", true, rate, "");


									arrListKOTItems.add(pos + 1, objItemDtl);
									arrListNewKOTItems.add(objItemDtl);
									pos++;

								}

								funRefreshItemGrid();
							}
						}

						mapSelectedMenuAndItemList.clear();
						mapSelectedMenu.clear();
						onResume();
						tdhDialog.dismiss();
					}
				}
			});

			btnClose.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					mapSelectedMenuAndItemList.clear();
					mapSelectedMenu.clear();
					tdhDialog.dismiss();

				}
			});

		} catch (Exception e) {
			e.printStackTrace();
		}


		tdhDialog.show();
		Window window = tdhDialog.getWindow();
		window.setGravity(Gravity.CENTER);
		window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
		window.getWindowManager().getDefaultDisplay();

	}

	public void funCheckAndGetModifierListForTDH() {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				boolean flgSuperUser = true;
				if (clsGlobalFunctions.gSuperUser.equals("No")) {
					flgSuperUser = false;
				}
				showDialog();
				App.getAPIHelper().funCheckAndGetModifierListForTDH(clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsTDHWithModifierDTO>>() {
					@Override
					public void onSuccess(ArrayList<clsTDHWithModifierDTO> listTDHWithModifierDTO) {
						dismissDialog();
						if (null != listTDHWithModifierDTO) {
							try {
								if (listTDHWithModifierDTO.size() > 0) {
									clsTDHWithModifierDTO objTDHWithModifierDTO;
									for (int i = 0; i < listTDHWithModifierDTO.size(); i++) {
										objTDHWithModifierDTO = listTDHWithModifierDTO.get(i);
										mapModifierItemAndQty.put(objTDHWithModifierDTO.getStrTDHMainItemCode(), Double.valueOf(objTDHWithModifierDTO.getTDHMainItemMaxQty()));

										ArrayList<clsTDHBean> listModifierGroupList = objTDHWithModifierDTO.getListModifierGroupList();
										if (listModifierGroupList.size() > 0) {
											//JSONObject mJsonGroupObject = new JSONObject();
											clsTDHBean obTDHBean = new clsTDHBean();
											for (int j = 0; j < listModifierGroupList.size(); j++) {
												obTDHBean = listModifierGroupList.get(j);
												hm_ModifierGroupDetail.put(obTDHBean.getStrTDHItemCode(), obTDHBean);

												ArrayList<clsTDHBean> listModifierItemsArray = obTDHBean.getListModifierItemList();
												if (listModifierItemsArray.size() > 0) {
													clsTDHBean objTdhItem = new clsTDHBean();
													for (int k = 0; k < listModifierItemsArray.size(); k++) {
														objTdhItem = listModifierItemsArray.get(k);
														hm_ModifierItemDetail.put(objTdhItem.getStrTDHItemCode(), objTdhItem);
													}
												}
											}
										}

									}
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
				SnackBarUtils.showSnackBar(this, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(this, R.string.setup_your_server_settings);
		}
	}

	public void funGetTDHListWS() {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {

				boolean flgSuperUser = true;
				if (clsGlobalFunctions.gSuperUser.equals("No")) {
					flgSuperUser = false;
				}
				showDialog();
				App.getAPIHelper().funGetTDHListWS(clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<JsonArray>() {
					@Override
					public void onSuccess(JsonArray mJsonMainArray) {
						dismissDialog();
						Map<String, List<clsTDHBean>> hmapComboItemDtl = new HashMap<>();
						if (null != mJsonMainArray) {
							try {
								//  JsonArray mJsonMainArray = new JsonArray(jsObj.getAsString());
								JsonObject mJsonMainObject = new JsonObject();
								for (int i = 0; i < mJsonMainArray.size(); i++) {
									List<clsTDHBean> arrListTDHItem = new ArrayList<clsTDHBean>();
									mJsonMainObject = (JsonObject) mJsonMainArray.get(i);
									if (mJsonMainObject.get("TDHCombo " + (i + 1)).getAsString().toString().equals("")) {
										//memberInfo = "no data";
									} else {
										JsonArray mJsonArray = (JsonArray) mJsonMainObject.get("TDHCombo " + (i + 1));
										JsonObject mJsonObject = new JsonObject();
										for (int cnt = 0; cnt < mJsonArray.size(); cnt++) {
											mJsonObject = (JsonObject) mJsonArray.get(cnt);
											clsTDHBean objItem = new clsTDHBean();
											objItem.setStrTDHItemCode(mJsonObject.get("TDHItemCode").getAsString().toString());
											objItem.setStrTDHItemName(mJsonObject.get("TDHItemName").getAsString().toString());
											objItem.setDblQty(Double.parseDouble(mJsonObject.get("Quantity").getAsString()));
											objItem.setStrDefaultYN(mJsonObject.get("DefaultYN").getAsString().toString());
											objItem.setStrModifierGroupCode(mJsonObject.get("SubMenuCode").getAsString().toString());
											arrListTDHItem.add(objItem);
										}

									}
									hmapComboItemDtl.put(mJsonMainObject.get("TDHMainItemCode").getAsString().toString(), arrListTDHItem);
								}
								if (hm_ComboItemDtl.size() > 0) {
									hm_ComboItemDtl.clear();
									hm_ComboItemDtl = hmapComboItemDtl;

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
				SnackBarUtils.showSnackBar(this, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(this, R.string.setup_your_server_settings);
		}
	}

	private void funInsertDebitCartTmpDtl(String memberCode) {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				JSONObject objDebitDtl = new JSONObject();
				JSONArray arrDebitClass = new JSONArray();
				JSONObject objDebitRows = new JSONObject();

				try {

					objDebitRows.put("strTableNo", tableNo);
					objDebitRows.put("strCardNo", cardNo);
					objDebitRows.put("dblRedeemAmt", totalMemberBalance);
					objDebitRows.put("strPrintYN", "Y");

					arrDebitClass.put(objDebitRows);
					objDebitDtl.put("DebitDtl", arrDebitClass);

				} catch (Exception e) {
					e.printStackTrace();
				}
				showDialog();
				App.getAPIHelper().funInsertDebitCartTmpDtl(objDebitDtl, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
					@Override
					public void onSuccess(JsonObject jObj) {
						dismissDialog();
						if (null != jObj) {
							try {

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
				String dateTime = clsGlobalFunctions.gPOSDate;
				String posDate = dateTime.replaceAll(" ", "%20");
				try {
					if (previousKOTSize > 0) {
						// if(arrListNewKOTItems.size()>0) {
						for (int cnt = 0; cnt < arrListNewKOTItems.size(); cnt++) {
							String itemName = "";
							clsKOTItemDtlBean objKOTItemDtl = (clsKOTItemDtlBean) arrListNewKOTItems.get(cnt);
							if (objKOTItemDtl.getStrItemName().contains("#")) {
								itemName = objKOTItemDtl.getStrItemName().split("#")[0];
							} else {
								itemName = objKOTItemDtl.getStrItemName();
							}
							JsonObject objRows = new JsonObject();
							objRows.addProperty("strSerialNo", objKOTItemDtl.getStrSerialNo());
							objRows.addProperty("strTableNo", tableNo);
							objRows.addProperty("strCardNo", objKOTItemDtl.getStrCardNo());
							objRows.addProperty("dblRedeemAmt", objKOTItemDtl.getDblRedeemAmt());
							objRows.addProperty("strHomeDelivery", objKOTItemDtl.getStrHomeDelivery());
							objRows.addProperty("strCustomerCode", objKOTItemDtl.getStrCustomerCode());
							objRows.addProperty("strPOSCode", objKOTItemDtl.getStrPOSCode());
							objRows.addProperty("strPOSName", clsGlobalFunctions.gPOSName);
							objRows.addProperty("strItemCode", objKOTItemDtl.getStrItemCode());
							objRows.addProperty("strItemName", itemName);
							objRows.addProperty("dblItemQuantity", objKOTItemDtl.getDblItemQuantity());
							objRows.addProperty("dblAmount", objKOTItemDtl.getDblAmount());
							objRows.addProperty("strWaiterNo", waiterNo);
							objRows.addProperty("strKOTNo", objKOTItemDtl.getStrKOTNo());
							objRows.addProperty("intPaxNo", paxNo);

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
							objRows.addProperty("strNCKotYN", NCKOT);
							objRows.addProperty("strCustomerName", objKOTItemDtl.getStrCustomerName());
							objRows.addProperty("strActiveYN", objKOTItemDtl.getStrActiveYN());

							objRows.addProperty("dblBalance", objKOTItemDtl.getDblBalance());
							objRows.addProperty("dblCreditLimit", objKOTItemDtl.getDblCreditLimit());
							objRows.addProperty("strCounterCode", objKOTItemDtl.getStrCounterCode());
							objRows.addProperty("strPromoCode", objKOTItemDtl.getStrPromoCode());
							objRows.addProperty("dblRate", objKOTItemDtl.getDblRate());
							objRows.addProperty("strRemark", kotRemark);
							objRows.addProperty("strReasonCode", reasonCode);
							objRows.addProperty("strClientCode", clsGlobalFunctions.gClientCode);
							objRows.addProperty("strCardType", objKOTItemDtl.getStrCardType());
							objRows.addProperty("POSDate", posDate);

							clsUtility objUtility = new clsUtility();
							objRows.addProperty("deviceName", objUtility.funGetHostName());
							objRows.addProperty("macAddress", objUtility.funGetCurrentMACAddress(clsKotScreen.this));
							arrKOTClass.add(objRows);

						}

					} else {
						if(arrListKOTItems.size()==0){
							Toast.makeText(clsKotScreen.this, "First Select Item " , Toast.LENGTH_SHORT).show();
							return;
						}
						for (int cnt = 0; cnt < arrListKOTItems.size(); cnt++) {
							clsKOTItemDtlBean objKOTItemDtl = (clsKOTItemDtlBean) arrListKOTItems.get(cnt);
							JsonObject objRows = new JsonObject();
							objRows.addProperty("strSerialNo", objKOTItemDtl.getStrSerialNo());
							objRows.addProperty("strTableNo", tableNo);
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
							objRows.addProperty("strWaiterNo", waiterNo);
							objRows.addProperty("strKOTNo", objKOTItemDtl.getStrKOTNo());
							objRows.addProperty("intPaxNo", paxNo);

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
							objRows.addProperty("strNCKotYN", NCKOT);
							objRows.addProperty("strCustomerName", objKOTItemDtl.getStrCustomerName());
							objRows.addProperty("strActiveYN", objKOTItemDtl.getStrActiveYN());

							objRows.addProperty("dblBalance", objKOTItemDtl.getDblBalance());
							objRows.addProperty("dblCreditLimit", objKOTItemDtl.getDblCreditLimit());
							objRows.addProperty("strCounterCode", objKOTItemDtl.getStrCounterCode());
							objRows.addProperty("strPromoCode", objKOTItemDtl.getStrPromoCode());
							objRows.addProperty("dblRate", objKOTItemDtl.getDblRate());
							objRows.addProperty("strRemark", kotRemark);
							objRows.addProperty("strReasonCode", reasonCode);
							objRows.addProperty("strClientCode", clsGlobalFunctions.gClientCode);
							objRows.addProperty("strCardType", objKOTItemDtl.getStrCardType());
							objRows.addProperty("POSDate", posDate);

							clsUtility objUtility = new clsUtility();
							objRows.addProperty("deviceName", objUtility.funGetHostName());
							objRows.addProperty("macAddress", objUtility.funGetCurrentMACAddress(clsKotScreen.this));
							arrKOTClass.add(objRows);
						}
					}
					objKOTDtl.add("KOTDtl", arrKOTClass);
					//  JsonObject jObj=objKOTDtl.t;
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

								Toast.makeText(clsKotScreen.this, "KOT No- " + kotNO, Toast.LENGTH_SHORT).show();
								// funPrintKot(res);
								// new GetKOTDataWS().execute(kotNO);
								funGetKOTDataFromWS(kotNO);
								funResetFields();
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
									JsonArray jArrItemDtl = (JsonArray) jObjHeaderInfo.getAsJsonArray("ItemDtl");
									// JSONArray jArrModItemDtl=(JSONArray) jObjHeaderInfo.getJSONArray("ModItemDtl");
									// funPrintKot(jArrItemDtl,jArrModItemDtl,costCenterName,tableName,KOTNo,PaxNo);
									funPrintKot(jArrItemDtl, costCenterName, tableName, KOTNo, PaxNo);
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

	public void funLoadPrevoiusKotItemList() {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				boolean flgSuperUser = true;
				if (clsGlobalFunctions.gSuperUser.equals("No")) {
					flgSuperUser = false;
				}
				showDialog();
				App.getAPIHelper().funLoadPrevoiusKotItemList(tableNo, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
					@Override
					public void onSuccess(JsonObject jMainObj) {
						dismissDialog();
						if (null != jMainObj) {
							HashMap<String, String> hmPreviousKoTDtl = new HashMap<>();
							arrListKOTItems = new ArrayList<clsKOTItemDtlBean>();
							arrListPreviousKOTItems = new ArrayList<clsKOTItemDtlBean>();
							arrListNewKOTItems = new ArrayList<clsKOTItemDtlBean>();
							JsonArray mJsonArray = (JsonArray) jMainObj.get("PreviousKOTDtls");
							JsonObject mJsonObject = new JsonObject();

							for (int i = 0; i < mJsonArray.size(); i++) {
								mJsonObject = (JsonObject) mJsonArray.get(i);
								if (mJsonObject.get("TableNo").getAsString().equals("")) {

								} else {
									if (hmPreviousKoTDtl.size() > 0) {
										if (hmPreviousKoTDtl.containsKey(mJsonObject.get("KOTNo").getAsString())) {
											double reedemAmt = Double.parseDouble(mJsonObject.get("RedeemAmt").getAsString());
											clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean("1", mJsonObject.get("TableNo").getAsString(), mJsonObject.get("CardNo").getAsString(), reedemAmt, "", mJsonObject.get("CustomerCode").getAsString(), clsGlobalFunctions.gPOSCode
													, mJsonObject.get("ItemCode").getAsString(), mJsonObject.get("ItemName").getAsString()
													, Double.parseDouble(mJsonObject.get("ItemQty").getAsString()), Double.parseDouble(mJsonObject.get("Amount").getAsString())
													, mJsonObject.get("WaiterNo").getAsString(), mJsonObject.get("KOTNo").getAsString(), 1, "Y", "", clsGlobalFunctions.gUserCode,
													clsGlobalFunctions.gUserCode, objGlobal.funGetCurrentDateTime(), objGlobal.funGetCurrentDateTime(), "No"
													, "N", "N", "N", "N", "", "", "", "", "", "", false, Double.parseDouble(mJsonObject.get("Rate").getAsString()), mJsonObject.get("CardType").getAsString());
											arrListKOTItems.add(objItemDtl);
										} else {
											double reedemAmt = Double.parseDouble(mJsonObject.get("RedeemAmt").getAsString());
											clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean("1", mJsonObject.get("TableNo").getAsString(), mJsonObject.get("CardNo").getAsString(), reedemAmt, "", mJsonObject.get("CustomerCode").getAsString(), clsGlobalFunctions.gPOSCode
													, mJsonObject.get("ItemCode").getAsString(), mJsonObject.get("ItemName").getAsString() + "#" + mJsonObject.get("KOTNo").getAsString() + "#" + mJsonObject.get("KOTTime").getAsString()
													, Double.parseDouble(mJsonObject.get("ItemQty").getAsString()), Double.parseDouble(mJsonObject.get("Amount").getAsString())
													, mJsonObject.get("WaiterNo").getAsString(), mJsonObject.get("KOTNo").getAsString(), 1, "Y", "", clsGlobalFunctions.gUserCode,
													clsGlobalFunctions.gUserCode, objGlobal.funGetCurrentDateTime(), objGlobal.funGetCurrentDateTime(), "No"
													, "N", "N", "N", "N", "", "", "", "", "", "", false, Double.parseDouble(mJsonObject.get("Rate").getAsString()), mJsonObject.get("CardType").getAsString());
											arrListKOTItems.add(objItemDtl);

											hmPreviousKoTDtl.put(mJsonObject.get("KOTNo").getAsString(), mJsonObject.get("KOTTime").getAsString());
										}
									} else {
										double reedemAmt = Double.parseDouble(mJsonObject.get("RedeemAmt").getAsString());
										clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean("1", mJsonObject.get("TableNo").getAsString(), mJsonObject.get("CardNo").getAsString(), reedemAmt, "", mJsonObject.get("CustomerCode").getAsString(), clsGlobalFunctions.gPOSCode
												, mJsonObject.get("ItemCode").getAsString(), mJsonObject.get("ItemName").getAsString() + "#" + mJsonObject.get("KOTNo").getAsString() + "#" + mJsonObject.get("KOTTime").getAsString()
												, Double.parseDouble(mJsonObject.get("ItemQty").getAsString()), Double.parseDouble(mJsonObject.get("Amount").getAsString())
												, mJsonObject.get("WaiterNo").getAsString(), mJsonObject.get("KOTNo").getAsString(), 1, "Y", "", clsGlobalFunctions.gUserCode,
												clsGlobalFunctions.gUserCode, objGlobal.funGetCurrentDateTime(), objGlobal.funGetCurrentDateTime(), "No"
												, "N", "N", "N", "N", "", "", "", "", "", "", false, Double.parseDouble(mJsonObject.get("Rate").getAsString()), mJsonObject.get("CardType").getAsString());
										arrListKOTItems.add(objItemDtl);

										hmPreviousKoTDtl.put(mJsonObject.get("KOTNo").getAsString(), mJsonObject.get("KOTTime").getAsString());
									}

									billingTableStatus=mJsonObject.get("BillingTableStatus").getAsString();
								}
							}
						}

						if (arrListKOTItems.size() > 0) {
							arrListPreviousKOTItems = arrListKOTItems;
							previousKOTSize = arrListKOTItems.size();
							funRefreshItemGrid();
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

									final ArrayList arrListItemModifier = (ArrayList) arrListTemp;
									final Dialog dialog = new Dialog(clsKotScreen.this);
									dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
									dialog.setContentView(R.layout.kotmodifierdialog);

									//  dialog.setTitle("Freeflow Modifier");

									final ArrayList arrList = new ArrayList<String>();
									Button btnClose = (Button) dialog.findViewById(R.id.btnFFClose);
									final GridView dialoglist = (GridView) dialog.findViewById(R.id.dialoglist);
									for (int cnt = 0; cnt < arrListTemp.size(); cnt++) {
										clsItemModifierBean objItem = (clsItemModifierBean) arrListTemp.get(cnt);
										arrList.add(objItem.getStrModifierName());
									}
									clsOrderDialogAdapter adapter = new clsOrderDialogAdapter(getBaseContext(), arrList);
									dialoglist.setAdapter(adapter);
									dialoglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
										@Override
										public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

											clsGlobalFunctions objGlobal = new clsGlobalFunctions();
											String dateTime = clsGlobalFunctions.funGetPOSDateTime();
											// dateTime=dateTime.replaceAll(" ","%20");
											clsItemModifierBean objItemModifier = (clsItemModifierBean) arrListItemModifier.get(position);

											boolean flgAlreadyPresentMod = false;

											for (int cnt = 0; cnt < arrListKOTItems.size(); cnt++) {
												if (arrListKOTItems.get(cnt).isModifier()) {
													clsKOTItemDtlBean objTemp = arrListKOTItems.get(cnt);
													String itemWithModifier = arrListKOTItems.get(cnt).getStrItemCode();
													String itemCode1 = itemWithModifier.substring(0, 7);
													String modifierCode1 = itemWithModifier.substring(7, 11);
													if (itemCode1.equals(objItemModifier.getStrItemCode()) && modifierCode1.equals(objItemModifier.getStrModifierCode())) {
														double modQty = objTemp.getDblItemQuantity();
														modQty = modQty + 1;
														double amt = objItemModifier.getRate() * modQty;
														objTemp.setDblAmount(amt);
														objTemp.setDblItemQuantity(modQty);

														arrListKOTItems.set(cnt, objTemp);
														flgAlreadyPresentMod = true;
														break;
													}
												}
											}

											int sequenceNo = 1;

											if (!flgAlreadyPresentMod) {
												String seq = "0";
												clsKOTItemDtlBean objItemDtl = null;
												for (int i = 0; i < arrListKOTItems.size(); i++) {
													clsKOTItemDtlBean objBean = arrListKOTItems.get(i);
													if (objBean.getStrItemCode().contains(objItemModifier.getStrItemCode())) {
														seq = objBean.getStrSerialNo();
														if (seq.contains(".")) {
															String[] spSeq = seq.split("\\.");
															sequenceNo = Integer.parseInt(spSeq[1]);
															sequenceNo++;
															objItemDtl = new clsKOTItemDtlBean(spSeq[0] + "." + String.valueOf(sequenceNo), tableNo, "", 0.00, "", "", clsGlobalFunctions.gPOSCode
																	, objItemModifier.getStrItemCode() + objItemModifier.getStrModifierCode(), objItemModifier.getStrModifierName()
																	, 1, objItemModifier.getRate(), waiterNo, "K01", paxNo, "Y", ""
																	, clsGlobalFunctions.gUserCode, clsGlobalFunctions.gUserCode
																	, dateTime, dateTime, "No", "N", "N", "N", NCKOT, "", "", "", "", "", "", true, objItemModifier.getRate(), "");
														} else {
															objItemDtl = new clsKOTItemDtlBean(String.valueOf(seq) + "." + String.valueOf(sequenceNo), tableNo, "", 0.00, "", "", clsGlobalFunctions.gPOSCode
																	, objItemModifier.getStrItemCode() + objItemModifier.getStrModifierCode(), objItemModifier.getStrModifierName()
																	, 1, objItemModifier.getRate(), waiterNo, "K01", paxNo, "Y", ""
																	, clsGlobalFunctions.gUserCode, clsGlobalFunctions.gUserCode
																	, dateTime, dateTime, "No", "N", "N", "N", NCKOT, "", "", "", "", "", "", true, objItemModifier.getRate(), "");
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

	private void funTaxCalculate() {
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				JsonObject objTaxDtl = new JsonObject();
				JsonArray arrKOTClass = new JsonArray();
				try {
					for (int cnt = 0; cnt < arrListKOTItems.size(); cnt++) {
						clsKOTItemDtlBean objKOTItemDtl = (clsKOTItemDtlBean) arrListKOTItems.get(cnt);
						JsonObject objRows = new JsonObject();

						//objRows.put("strTableNo",objKOTItemDtl.getStrTableNo());
						objRows.addProperty("strPOSCode", objKOTItemDtl.getStrPOSCode());
						objRows.addProperty("strItemCode", objKOTItemDtl.getStrItemCode());
						objRows.addProperty("strItemName", objKOTItemDtl.getStrItemName());
						objRows.addProperty("dblItemQuantity", objKOTItemDtl.getDblItemQuantity());
						objRows.addProperty("dblAmount", objKOTItemDtl.getDblAmount());
						//  objRows.put("dblRate",objKOTItemDtl.getDblRate());
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

				App.getAPIHelper().funTaxCalculate(objTaxDtl, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
					@Override
					public void onSuccess(JsonObject jObj) {

						if (null != jObj) {
							try {

								String taxAmount = jObj.get("totalTaxAmt").getAsString().toString();
								double totalTaxAmt = 0;
								if (taxAmount.equalsIgnoreCase("")) {
									totalTaxAmt = 0;
								} else {
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
					public void onFailure(String errorMessage, int errorCode) {

					}
				});


			} else {
				SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.no_internet_connection);
			}
		} else {
			SnackBarUtils.showSnackBar(clsKotScreen.mActivity, R.string.setup_your_server_settings);
		}
	}

	private List<clsTDHBean> funGetTDHMenuList(String... params)
	{
		arrListModifierGroup = new ArrayList();
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				showDialog();
				App.getAPIHelper().funGetTDHMenuList(clsGlobalFunctions.gClientCode,params[0] , new BaseAPIHelper.OnRequestComplete<ArrayList<clsTDHBean>>() {
					@Override
					public void onSuccess(ArrayList<clsTDHBean> arrTDH) {
						dismissDialog();
						if (null != arrTDH) {
							try{
								arrListModifierGroup=arrTDH;
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
		return  arrListModifierGroup;
	}

	private void funCheckKOT()
	{
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				showDialog();
				String posName = clsGlobalFunctions.gPOSName.replaceAll(" ", "%20");

				App.getAPIHelper().funCheckKOT(tableNo,clsGlobalFunctions.gPOSCode,posName , new BaseAPIHelper.OnRequestComplete<JsonObject>() {
					@Override
					public void onSuccess(JsonObject jObj) {
						dismissDialog();
						if (null != jObj) {
							try{
								String result=jObj.get("result").getAsString().toString();

								if (result.isEmpty()) {
									Toast.makeText(clsKotScreen.this, "Please select bill printer", Toast.LENGTH_LONG).show();
								} else {
									Toast.makeText(clsKotScreen.this, result, Toast.LENGTH_LONG).show();
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

	private void funGetItemDetailsByExternalCode(String externalCode)
	{
		if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
			if (ConnectivityHelper.isConnected()) {
				showDialog();
				String areaCode="";
				App.getAPIHelper().funGetItemDetailsByExternalCode(clsGlobalFunctions.gPOSCode,externalCode,clsGlobalFunctions.gAreaWisePricing,tableNo,areaCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsKotItemsListBean>>() {
					@Override
					public void onSuccess(ArrayList<clsKotItemsListBean> arrListTemp) {
						dismissDialog();
						if (null != arrListTemp) {
							try
							{
								String pricingDay = new clsUtility().funGetDayForPricing();
								final ArrayList<clsKotItemsListBean> arrListItemMastertemp = new ArrayList();
								if (null != arrListTemp) {
									clsKotItemsListBean obj = new clsKotItemsListBean();
									if (arrListTemp.size() > 0) {
										for (int i = 0; i < arrListTemp.size(); i++) {
											obj = arrListTemp.get(i);
											obj.setDblSalePrice(new clsUtility().funGetitemPriceOnDay(pricingDay, obj));
											arrListItemMastertemp.add(obj);
										}
									}
									if (arrListItemMastertemp.size() > 0) {
										clsKotItemsListBean obItem = arrListItemMastertemp.get(0);
										if(obItem.getDblSalePrice()>0){
											funRefreshItemDtl(obItem.getStrItemCode(), obItem.getStrItemName(), "", obItem.getDblSalePrice());
											edtExternalCode.setText("");
											CommonUtils.hideKeyboard(edtExternalCode);
										}
										else{

											final Dialog dialog = new Dialog(mActivity);
											dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
											dialog.setContentView(R.layout.dialogforinput);

											final EditText edtInputTypeValue = (EditText) dialog.findViewById(R.id.edtInputTypeValue);
											edtInputTypeValue.setInputType(TYPE_CLASS_NUMBER);

											final Button btnOK = (Button) dialog.findViewById(R.id.btnOk);
											TextView txtInputName = (TextView) dialog.findViewById(R.id.txtInputName);
											TextView txtInputType=(TextView)dialog.findViewById(R.id.textInputName);
											txtInputName.setText("Enter Rate");
											txtInputType.setText("Enter Rate");

											btnOK.setOnClickListener(new View.OnClickListener() {
												@Override
												public void onClick(View view) {
													if (edtInputTypeValue.getText().toString().isEmpty()) {
														Toast.makeText(mActivity, "Enter rate", Toast.LENGTH_SHORT).show();
														return;
													} else {
														clsKotItemsListBean obItem = arrListItemMastertemp.get(0);
														if (obItem.getDblSalePrice() == 0) {
															funRefreshItemDtl(obItem.getStrItemCode(), obItem.getStrItemName(), "", Double.valueOf(edtInputTypeValue.getText().toString()));

															new CommonUtils().hideKeyboard(edtInputTypeValue);
															dialog.dismiss();
														}


													}
												}
											});
											dialog.show();
											Window window = dialog.getWindow();
											window.setGravity(Gravity.CENTER);
											window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
											window.getWindowManager().getDefaultDisplay();
											}
										edtExternalCode.setText("");
										CommonUtils.hideKeyboard(edtExternalCode);
									} else {
										Toast.makeText(clsKotScreen.mActivity, "Item not found!!!!!", Toast.LENGTH_LONG).show();
										edtExternalCode.setText("");
										CommonUtils.hideKeyboard(edtExternalCode);
									}
								}
								else {
									Toast.makeText(clsKotScreen.mActivity, "Item not found!!!!!", Toast.LENGTH_LONG).show();
									edtExternalCode.setText("");
									CommonUtils.hideKeyboard(edtExternalCode);
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



	public void funSelectAndAddItemToGridWithoutPreviousKOT(String strItemCode, String strItemName, String strSubGroupCode, double dblSalePrice)
	{
		int seq=0;
		if(arrListKOTItems.size()>0)
		{
			for(int j=0;j<arrListKOTItems.size();j++)
			{
				clsKOTItemDtlBean objBean=arrListKOTItems.get(j);
				if(objBean.getStrItemName().contains("-->") || objBean.getStrItemName().contains("=>"))
				{
				}
				else
				{
					seq++;
				}
			}
		}

		clsGlobalFunctions objGlobal=new clsGlobalFunctions();
		String dateTime=clsGlobalFunctions.funGetPOSDateTime();

		// dateTime=dateTime.replaceAll(" ","%20");
		double qty=1;
		clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean(String.valueOf(seq), tableNo, "", totalMemberBalance, "", "", clsGlobalFunctions.gPOSCode
				,strItemCode ,strItemName,qty,dblSalePrice,waiterNo, "K01", paxNo, "Y", "", clsGlobalFunctions.gUserCode,
				clsGlobalFunctions.gUserCode, dateTime, dateTime, "No"
				, "N", "N", "N", NCKOT, "", "","", "", "","",false,dblSalePrice,"");
		boolean flgAlreadyPresent = false;
		int iListViewScrollPosition = -1;

		for (int k = 0; k < arrListKOTItems.size(); k++)
		{
			if (arrListKOTItems.get(k).getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase()))
			{
				clsKOTItemDtlBean kotItemDtl =(clsKOTItemDtlBean) arrListKOTItems.get(k);

				if(kotItemDtl.getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase()))
				{
					kotItemDtl.setDblItemQuantity(kotItemDtl.getDblItemQuantity() + 1);
					kotItemDtl.setDblAmount(kotItemDtl.getDblAmount() + dblSalePrice);
					flgAlreadyPresent = true;
					iListViewScrollPosition = k;
					arrListKOTItems.set(k, kotItemDtl);

					break;
				}
			}
		}
		if(!flgAlreadyPresent)
		{
			arrListKOTItems.add(objItemDtl);
		}

		objKotItemDtl = new clsKotSelectedItemsCustomBaseAdapter(mActivity, arrListKOTItems,previousKOTSize);
		lvKotSelectedItems.setItemsCanFocus(true);
		lvKotSelectedItems.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		objKotItemDtl.setCustomButtonListner((clsKotSelectedItemsCustomBaseAdapter.customButtonListener) mActivity);
		lvKotSelectedItems.setAdapter(objKotItemDtl);
		lvKotSelectedItems.setSelection(objKotItemDtl.getCount() - 1);
		if(iListViewScrollPosition != -1){
			lvKotSelectedItems.setSelection(iListViewScrollPosition);
		}

		subTotalAmt=0;
		for(int l = 0; l < arrListKOTItems.size(); l++)
		{
			subTotalAmt = subTotalAmt + arrListKOTItems.get(l).getDblAmount();
			edtKotTotalAmount.setText(String.valueOf(subTotalAmt));
		}
	}


	public void funSelectAndAddItemToGridWithPreviousKOT(String strItemCode, String strItemName, String strSubGroupCode, double dblSalePrice)
	{
		int seq=0;
		if(arrListKOTItems.size()>0)
		{
			for(int j=0;j<arrListKOTItems.size();j++)
			{
				clsKOTItemDtlBean objBean=arrListKOTItems.get(j);
				if(objBean.getStrItemName().contains("-->") || objBean.getStrItemName().contains("=>"))
				{

				}
				else
				{
					seq++;
				}
			}
		}

		clsGlobalFunctions objGlobal=new clsGlobalFunctions();
		String dateTime=clsGlobalFunctions.funGetPOSDateTime();

		// dateTime=dateTime.replaceAll(" ","%20");
		double qty=1;
		clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean(String.valueOf(seq), tableNo, "", totalMemberBalance, "", "", clsGlobalFunctions.gPOSCode
				,strItemCode ,strItemName,qty,dblSalePrice,waiterNo, "K01", paxNo, "Y", "", clsGlobalFunctions.gUserCode,
				clsGlobalFunctions.gUserCode, dateTime, dateTime, "No"
				, "N", "N", "N", NCKOT, "", "","", "", "","",false,dblSalePrice,"");
		boolean flgAlreadyPresent = false;
		int iListViewScrollPosition = -1;

		for (int k = 0; k < arrListKOTItems.size(); k++)
		{
			if (arrListKOTItems.get(k).getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase()))
			{
				boolean flgFound=false;
				for (int cnt = 0; cnt < arrListPreviousKOTItems.size(); cnt++)
				{
					if (arrListPreviousKOTItems.get(cnt).getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase()))
					{
						flgAlreadyPresent=false;
						flgFound=true;
					}
				}
				for (int cnt1 = 0; cnt1 < arrListNewKOTItems.size(); cnt1++)
				{
					if (arrListNewKOTItems.get(cnt1).getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase()))
					{
						clsKOTItemDtlBean kotItemDtl =(clsKOTItemDtlBean) arrListNewKOTItems.get(cnt1);

						if(kotItemDtl.getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase()))
						{
							kotItemDtl.setDblItemQuantity(kotItemDtl.getDblItemQuantity() + 1);
							kotItemDtl.setDblAmount(kotItemDtl.getDblAmount() + dblSalePrice);
							flgAlreadyPresent = true;
							iListViewScrollPosition = cnt1;
							arrListNewKOTItems.set(cnt1, kotItemDtl);

							break;
						}
					}
				}

				break;
			}
		}
		if(!flgAlreadyPresent)
		{
			arrListKOTItems.add(objItemDtl);
			arrListNewKOTItems.add(objItemDtl);
		}


		objKotItemDtl = new clsKotSelectedItemsCustomBaseAdapter(mActivity, arrListKOTItems,previousKOTSize);
		lvKotSelectedItems.setItemsCanFocus(true);
		lvKotSelectedItems.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
		objKotItemDtl.setCustomButtonListner((clsKotSelectedItemsCustomBaseAdapter.customButtonListener) mActivity);

		lvKotSelectedItems.setAdapter(objKotItemDtl);
		lvKotSelectedItems.setSelection(objKotItemDtl.getCount() - 1);
		if(iListViewScrollPosition != -1){
			lvKotSelectedItems.setSelection(iListViewScrollPosition);
		}

		subTotalAmt=0;
		for(int l = 0; l < arrListKOTItems.size(); l++)
		{
			subTotalAmt = subTotalAmt + arrListKOTItems.get(l).getDblAmount();
			edtKotTotalAmount.setText(String.valueOf(subTotalAmt));
		}
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
