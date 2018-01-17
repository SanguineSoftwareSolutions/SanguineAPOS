package com.example.apos.activity;

import android.app.Activity;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.Selection;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
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
import com.example.apos.adapter.clsKotSelectedItemsCustomBaseAdapter;
import com.example.apos.adapter.clsNCKOTViewPageAdapter;
import com.example.apos.adapter.clsOrderDialogAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsBillSettlementDtl;
import com.example.apos.bean.clsItemModifierBean;
import com.example.apos.bean.clsKOTItemDtlBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.fragments.clsKotOtherOptionsFragment;
import com.example.apos.fragments.clsNCKOTItemListFragment;
import com.example.apos.fragments.clsNCKOTPaxFragment;
import com.example.apos.listeners.clsKotItemListSelectionListener;
import com.example.apos.listeners.clsNCKOTActListener;
import com.example.apos.listeners.clsPaxNoSelectionListener;
import com.example.apos.util.Utils;
import com.example.apos.util.clsPrintDemo;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class clsNCKOTScreen extends ActionBarActivity implements View.OnClickListener,clsKotSelectedItemsCustomBaseAdapter.customButtonListener,clsNCKOTActListener {

    private TextView tvHeaderTimeStamp = null;
    private TextView tvKotHeaderTableName = null;
    private TextView tvKotHeaderWaiterName = null;
    private TextView edtNCKOTTotalAmount = null;
    private ListView lvKotSelectedItems = null;
    public clsGlobalFunctions objGlobal;
    public clsKotSelectedItemsCustomBaseAdapter objKotItemDtl;
    private Button btnNCKOTCashCard = null, btnNCKOTPrint = null;
    public static Activity mActivity;
    public static ViewPager pager;
    clsNCKOTViewPageAdapter adapter;
    clsSlidingTabLayout tabs;
    Intent iData;
    private Dialog pgDialog;


    public int previousKOTSize = 0;
    //CharSequence Titles[]={"Table","Waiter","Item","Menu","Pax","Options"};
    //int Numboftabs =6;
    CharSequence Titles[] = {"Table", "Waiter", "Item", "Pax", "Options"};
    int Numboftabs = 5;

    private double subTotalAmt;
    Map<String, clsBillSettlementDtl> hmBillSettleAmtDtl = new HashMap<String, clsBillSettlementDtl>();
    public List<clsKOTItemDtlBean> arrListKOTItems, arrListPreviousKOTItems, arrListNewKOTItems;
    public String tableNo;
    public String debitNo;
    public String waiterNo;
    public Context mContext;
    public String itemcode;
    public int listpos;
    private String keyCase = "upperCase";
    public String tableStatus = "Normal";

    MACHServices machService;
    private String debitCardMemberName;
    private String debitCardMemberCode;
    private double totalMemberBalance;
    public clsKotItemListSelectionListener objMakeKotItemListSelectionListener;
    private clsPaxNoSelectionListener objPaxNoSelection;
    // private Button btnNCKOTMember;
    private String cardType;
    private double KOTAmt;
    private String NCKOT;
    private String reasonCode;
    private String kotRemark;
    private int paxNo;
    private ConnectivityManager connectivityManager;
    private BluetoothAdapter mBluetoothAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        mActivity = clsNCKOTScreen.this;
        getSupportActionBar().hide();
        setContentView(R.layout.nckotscreen);

        iData = getIntent();
        Runnable runnable = new CountDownRunner();
        arrListKOTItems = new ArrayList<clsKOTItemDtlBean>();
        arrListPreviousKOTItems = new ArrayList<clsKOTItemDtlBean>();
        arrListNewKOTItems = new ArrayList<clsKOTItemDtlBean>();

        widgetInit();
        tvHeaderTimeStamp.setText(iData.getStringExtra("PosName"));
        objMakeKotItemListSelectionListener = (clsKotItemListSelectionListener) clsNCKOTItemListFragment.getInstance();
        objPaxNoSelection = (clsPaxNoSelectionListener) clsNCKOTPaxFragment.getInstance();
        adapter = new clsNCKOTViewPageAdapter(this.getSupportFragmentManager(), Titles, Numboftabs, pager);
        pager = (ViewPager) findViewById(R.id.pager_NCKOT);
        pager.setAdapter(adapter);
        tabs = (clsSlidingTabLayout) findViewById(R.id.tabs_NCKOT);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new clsSlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);
    }

    private void widgetInit() {
        if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer")) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled()) {
                Toast.makeText(clsNCKOTScreen.this, "Bluetooth is disable", Toast.LENGTH_LONG).show();

            } else {
                new clsPrintDemo().funPrintViaBluetoothPrinter(mBluetoothAdapter);
            }
        }

        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NCKOT = "Y";
        reasonCode = "";
        kotRemark = "";
        totalMemberBalance = 0;
        KOTAmt = 0;
        paxNo = 1;
        debitNo = "";
        cardType = "";
        debitCardMemberName = "";
        debitCardMemberCode = "";
        subTotalAmt = 0;
        waiterNo = "";
        tableNo = "";
        tvHeaderTimeStamp = (TextView) findViewById(R.id.tvPosName);
        btnNCKOTCashCard = (Button) findViewById(R.id.btnNCKOTCashCard);
        btnNCKOTCashCard.setOnClickListener(this);
        btnNCKOTPrint = (Button) findViewById(R.id.btnNCKOTSettle);
        btnNCKOTPrint.setOnClickListener(this);
        lvKotSelectedItems = (ListView) findViewById(R.id.listNCKOTSelecteditems);
        edtNCKOTTotalAmount = (TextView) findViewById(R.id.edt_NCKOT_Total_Order_Amount);
        tvHeaderTimeStamp.setText(Utils.getCurrentDate());
        tvKotHeaderTableName = (TextView) findViewById(R.id.tv_kot_header_table_name);
        tvKotHeaderWaiterName = (TextView) findViewById(R.id.tv_kot_header_waiter_name);
        // btnNCKOTMember = (Button) findViewById(R.id.btnNCKOTMember);
        // btnNCKOTMember.setOnClickListener(this);
        edtNCKOTTotalAmount.setText("0.00");


    }


    @Override
    public void setTableSelectedResult(String strTableNo, String strTableName, String status) {
        // Check if waiter selection is disable for table. If Y then open item tab else open waiter tab
        if (clsGlobalFunctions.gSkipWaiter.equals("Y")) {
            pager.setCurrentItem(2);
        } else {
            pager.setCurrentItem(1);
        }
        tvKotHeaderTableName.setText(strTableName);
        tableNo = strTableNo;
        tableStatus = status;
    }


    @Override
    public void setDirectKotItemListSelectedResult1(String strTableNo, String strTableName, String strWaiterNo, String strWaiterName, String status, double kotAmt, double cardBalance, String cardType, int paxNo) {
        // Check if waiter selection is compulsory for each kot for same table. If Y then open waiter tab else open item tab
        if (clsGlobalFunctions.gMultipleWaiterSelection.equals("Y")) {
            pager.setCurrentItem(1);
        } else {
            pager.setCurrentItem(2);
            waiterNo = strWaiterNo;
            tvKotHeaderWaiterName.setText(strWaiterName);
        }
        tvKotHeaderTableName.setText(strTableName);
        tableNo = strTableNo;
        tableStatus = status;
        KOTAmt = kotAmt;
        totalMemberBalance = cardBalance;
        this.cardType = cardType;
        this.paxNo = paxNo;

        funLoadPrevoiusKotItemList();

    }

    @Override
    public void setWaiterSelectedResult(String strWaiterNo, String strWaiterName) {
        //Toast.makeText(this, "Select Table ", Toast.LENGTH_SHORT).show();

        if (tableNo.isEmpty()) {
        } else {
            if (clsGlobalFunctions.gSkipPax.equals("Y")) {
                pager.setCurrentItem(2);
                waiterNo = strWaiterNo;
                tvKotHeaderWaiterName.setText(strWaiterName);
            } else {
                clsNCKOTPaxFragment.tableNo = tableNo;
                waiterNo = strWaiterNo;
                objPaxNoSelection.getPaxSelectedNo(tableNo);
                tvKotHeaderWaiterName.setText(strWaiterName);
                //Toast.makeText(this, getWindowManager().getDefaultDisplay().getOrientation(), Toast.LENGTH_SHORT).show();
                System.out.println("Orientation= " + getWindowManager().getDefaultDisplay().getOrientation());
                pager.setCurrentItem(3);
            }
        }
    }

    @Override
    public void setSelectedPaxResult(int strPaxNo) {
        if (tableNo.isEmpty()) {

        } else {
            pager.setCurrentItem(2);
            paxNo = strPaxNo;
            Toast.makeText(clsNCKOTScreen.this, "PaxNo=" + paxNo, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void setSelectedOtherOptionsResult(String strNcKot, String strRemark, String strReasonCode) {
        NCKOT = strNcKot;
        kotRemark = strRemark;
        reasonCode = strReasonCode;
        Toast.makeText(clsNCKOTScreen.this, "NCKOT=" + NCKOT, Toast.LENGTH_LONG).show();

    }

    @Override
    public void funRefreshItemDtl(String strItemCode, String strItemName, String strSubGroupCode, double dblSalePrice)
    {
        if (!tableNo.isEmpty()) {
            clsGlobalFunctions objGlobal = new clsGlobalFunctions();
            String dateTime = clsGlobalFunctions.funGetPOSDateTime();

            // dateTime=dateTime.replaceAll(" ","%20");
            double qty = 1;
            clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean("1", tableNo, "", totalMemberBalance, "", "", clsGlobalFunctions.gPOSCode
                    , strItemCode, strItemName, qty, dblSalePrice, waiterNo, "K01", paxNo, "Y", "", clsGlobalFunctions.gUserCode,
                    clsGlobalFunctions.gUserCode, dateTime, dateTime, "No"
                    , "N", "N", "N", NCKOT, "", "", "", "", "", "", false, dblSalePrice, "");
            boolean flgAlreadyPresent = false;
            int iListViewScrollPosition = -1;

            for (int k = 0; k < arrListKOTItems.size(); k++) {
                if (arrListKOTItems.get(k).getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase())) {
                    boolean flgFound = false;
                    for (int cnt = 0; cnt < arrListPreviousKOTItems.size(); cnt++) {
                        if (arrListPreviousKOTItems.get(cnt).getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase())) {
                            flgAlreadyPresent = false;
                            flgFound = true;
                        }
                    }
                    for (int cnt1 = 0; cnt1 < arrListNewKOTItems.size(); cnt1++) {
                        if (arrListNewKOTItems.get(cnt1).getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase())) {
                            clsKOTItemDtlBean kotItemDtl = (clsKOTItemDtlBean) arrListNewKOTItems.get(cnt1);

                            if (kotItemDtl.getStrItemCode().toLowerCase().contains(strItemCode.toLowerCase())) {
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
            if (!flgAlreadyPresent) {
                arrListKOTItems.add(objItemDtl);
                arrListNewKOTItems.add(objItemDtl);
            }


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
                edtNCKOTTotalAmount.setText(String.valueOf(subTotalAmt));
            }
        } else {
            if (!tableNo.isEmpty()) {
                clsGlobalFunctions objGlobal = new clsGlobalFunctions();
                String dateTime = clsGlobalFunctions.funGetPOSDateTime();

                // dateTime=dateTime.replaceAll(" ","%20");
                double qty = 1;
                clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean("1", tableNo, "", totalMemberBalance, "", "", clsGlobalFunctions.gPOSCode
                        , strItemCode, strItemName, qty, dblSalePrice, waiterNo, "K01", paxNo, "Y", "", clsGlobalFunctions.gUserCode,
                        clsGlobalFunctions.gUserCode, dateTime, dateTime, "No"
                        , "N", "N", "N", NCKOT, "", "", "", "", "", "", false, dblSalePrice, "");
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
                    edtNCKOTTotalAmount.setText(String.valueOf(subTotalAmt));
                }
            }

        }
    }


    private void funRefreshItemGrid()
    {
        int iListViewScrollPosition = -1;
        objKotItemDtl = new clsKotSelectedItemsCustomBaseAdapter(mActivity, arrListKOTItems,0);
        lvKotSelectedItems.setItemsCanFocus(true);
        lvKotSelectedItems.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        objKotItemDtl.setCustomButtonListner((clsKotSelectedItemsCustomBaseAdapter.customButtonListener) mActivity);
        lvKotSelectedItems.setAdapter(objKotItemDtl);
        lvKotSelectedItems.setSelection(objKotItemDtl.getCount() - 1);
        if(iListViewScrollPosition != -1){
            lvKotSelectedItems.setSelection(iListViewScrollPosition);
        }
        subTotalAmt=0;
        for(int l = 0; l < arrListKOTItems.size(); l++){
            subTotalAmt = subTotalAmt + arrListKOTItems.get(l).getDblAmount();
            edtNCKOTTotalAmount.setText(String.valueOf(subTotalAmt));
        }
    }


    private void funClearObjects()
    {
        hmBillSettleAmtDtl=null;
        arrListKOTItems=null;
        clsGlobalFunctions.gCounterCode="";
        //clsGlobalFunctions.gCounterWiseBilling="No";
    }

    @Override
    public void onBackPressed() {
        //Toast.makeText(clsOrderActivity.this, "Back ", Toast.LENGTH_SHORT).show();

        if(clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer")) {
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
        switch(view.getId()){
            case R.id.btnNCKOTCashCard:
                //funCheckMember("CashCard");
                break;

            case R.id.btnNCKOTSettle:
               // btnNCKOTPrint.setEnabled(false);
                if(tableStatus.equalsIgnoreCase("Normal") && clsGlobalFunctions.gMemberCodeForKotInMposByCardSwipe.equals("Y"))
                {
                    if(tableNo.isEmpty())
                    {
                        Toast.makeText(getApplicationContext(), "Please Select Table!!!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (debitCardMemberCode.isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Please Select Member!!!", Toast.LENGTH_LONG).show();
                        return;
                    }

                    funSetMemberDetail(debitCardMemberCode, debitCardMemberName, cardType, totalMemberBalance);

                }
                else
                {
                    funSetMemberDetail(debitCardMemberCode, debitCardMemberName,cardType,totalMemberBalance);
                }
                break;

           /* case R.id.btnNCKOTMember:
                funCheckMember("Member");
                break;*/

            default:
                break;
        }
    }


    private void funSelectReason()
    {

        final Dialog dialog = new Dialog(mActivity);
        dialog.setContentView(R.layout.reasondialog);
        dialog.setTitle("Reason Dialog");
        Button btnOK = (Button) dialog.findViewById(R.id.btnReasonOk);
        Button btnCancle= (Button) dialog.findViewById(R.id.btnReasonCancle);
        btnCancle.setOnClickListener(new View.OnClickListener() {
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



    private int funCheckNcKOT()
    {
        int flgNcKOT=0;

        if(NCKOT.equals("Y"))
        {
            flgNcKOT=1;
            funSelectReason();
        }
        else
        {
            flgNcKOT=0;
        }


        return flgNcKOT;

    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
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



    private void funSetMemberDetail(String memberCode,String memberName,String attCardType, double attCardBal)
    {
        for(int cnt=0;cnt<arrListKOTItems.size();cnt++) {
            clsKOTItemDtlBean objKOTItemDtl = (clsKOTItemDtlBean) arrListKOTItems.get(cnt);
            objKOTItemDtl.setStrCustomerCode(memberCode);
            objKOTItemDtl.setStrCustomerName(memberName);
            objKOTItemDtl.setStrCardNo(memberCode);
            objKOTItemDtl.setStrCardType(attCardType);
            objKOTItemDtl.setDblRedeemAmt(attCardBal);
        }



        if(cardType.equals("CashCard") && (tableStatus.equalsIgnoreCase("Normal") || tableStatus.equalsIgnoreCase("Billed")))
        {   // For Cash Card with New KOT.

            // Web Service to insert debit card temp dtls
            // new DCTempTableWS().execute(memberCode);
            double totalAmt= Double.parseDouble(edtNCKOTTotalAmount.getText().toString())+KOTAmt;

            if(totalAmt > totalMemberBalance)
            {
                Toast.makeText(clsNCKOTScreen.this,"Insufficient Balance!!!",Toast.LENGTH_LONG).show();
            }
            else
            {
                debitCardMemberCode="";
                debitCardMemberName="";
                // funCheckNcKOT();
                if (new clsUtility().isWifiAvailable(connectivityManager))
                {
                    if((kotRemark.isEmpty()) || (reasonCode.isEmpty()) )
                    {
                        Toast.makeText(clsNCKOTScreen.this,"Enter remark and reason code for NonChargable KOT",Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        // new FireKOTWS().execute();
                        funFireKOTWS();
                    }
                }
                else
                {
                    Toast.makeText(clsNCKOTScreen.this,"Wifi Not Available",Toast.LENGTH_LONG).show();
                }

            }
        }
        else if(cardType.equals("CashCard") && (tableStatus.equalsIgnoreCase("Occupied"))) // For Cash Card with Busy Table
        {
            double totalAmt= Double.parseDouble(edtNCKOTTotalAmount.getText().toString())+KOTAmt;

            if(totalAmt > totalMemberBalance)
            {
                Toast.makeText(clsNCKOTScreen.this,"Insufficient Balance!!!"+totalAmt,Toast.LENGTH_LONG).show();
            }
            else
            {
                debitCardMemberCode="";
                debitCardMemberName="";
                if (new clsUtility().isWifiAvailable(connectivityManager))
                {
                    if((kotRemark.isEmpty()) || (reasonCode.isEmpty()) )
                    {
                        Toast.makeText(clsNCKOTScreen.this,"Enter remark and reason code for NonChargable KOT",Toast.LENGTH_LONG).show();

                    }
                    else
                    {
                        // new FireKOTWS().execute();
                        funFireKOTWS();
                    }
                }
                else
                {
                    Toast.makeText(clsNCKOTScreen.this,"Wifi Not Available",Toast.LENGTH_LONG).show();
                }

            }
        }
        else {   // For Member Card

            debitCardMemberCode="";
            debitCardMemberName="";
            if (new clsUtility().isWifiAvailable(connectivityManager))
            {
                if((kotRemark.isEmpty()) || (reasonCode.isEmpty()) )
                {
                    Toast.makeText(clsNCKOTScreen.this,"Enter remark and reason code for NonChargable KOT",Toast.LENGTH_LONG).show();

                }
                else
                {
                    //new FireKOTWS().execute();
                    funFireKOTWS();
                }
            }
            else
            {
                Toast.makeText(clsNCKOTScreen.this,"Wifi Not Available",Toast.LENGTH_LONG).show();
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
        if(value.split("#")[1].equals("selectedrow"))
        {
            listpos=position;
            itemcode=value.split("#")[2];
            final Dialog dialog = new Dialog(clsNCKOTScreen.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.edititemqty);
            final int pos=position;
            clsKOTItemDtlBean objKotItem=arrListKOTItems.get(position);

            ImageButton btnMinus = (ImageButton) dialog.findViewById(R.id.btnRemoveQty);
            ImageButton btnAdd = (ImageButton) dialog.findViewById(R.id.btnAddQty);
            Button btnOk = (Button) dialog.findViewById(R.id.btnUpdate);
            ImageView btnDeleteItem = (ImageView) dialog.findViewById(R.id.btnDeleteItem);
            ImageView btnCloseDialog = (ImageView) dialog.findViewById(R.id.btnClose);
            final EditText edtFFName = (EditText) dialog.findViewById(R.id.edtKotFFItemName);
            final EditText edtFFRate = (EditText) dialog.findViewById(R.id.edtKotFFItemRate);
            Button btnPredefinedModifier = (Button) dialog.findViewById(R.id.btnKotModifier);
            TextView txtItemName = (TextView) dialog.findViewById(R.id.txtItemName);
            final TextView txtQty = (TextView) dialog.findViewById(R.id.txtQty);


            txtItemName.setText(objKotItem.getStrItemName());
            txtQty.setText(""+objKotItem.getDblItemQuantity());

            btnMinus.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(clsNCKOTScreen.this, "Item Button click " + pos, Toast.LENGTH_SHORT).show();
                    clsKOTItemDtlBean objKotItem = arrListKOTItems.get(pos);
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
                        String strDeleteItemCode = arrListKOTItems.get(pos).getStrItemCode();
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
                    Toast.makeText(clsNCKOTScreen.this, "Item Button click " + pos, Toast.LENGTH_SHORT).show();
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
                    if (!v.isSelected()) {
                        v.setSelected(true);
                        Toast.makeText(clsNCKOTScreen.this, "Item Button click " + pos, Toast.LENGTH_SHORT).show();
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
                    String dateTime = clsGlobalFunctions.funGetPOSDateTime();
                    String freeFlowModName="";
                    try{
                        freeFlowModName =new clsUtility().funCheckSpecialCharacters(edtFFName.getText().toString());
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                    if (edtFFName.getText().toString().isEmpty()) {
                        Toast.makeText(mActivity, "Enter Free Flow Modifier Name", Toast.LENGTH_SHORT).show();
                    } else {
                        double freeFlowRate = 0;
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(edtFFName, InputMethodManager.SHOW_IMPLICIT);


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


            dialog.show();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.TOP);
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.getWindowManager().getDefaultDisplay();
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




    class CountDownRunner implements Runnable{
        // @Override
        public void run() {
            while(!Thread.currentThread().isInterrupted()){
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }catch(Exception e){
                }
            }
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
                            objRows.addProperty("macAddress", objUtility.funGetCurrentMACAddress(clsNCKOTScreen.this));

                            arrKOTClass.add(objRows);
                        }

                    } else {
                        if(arrListKOTItems.size()==0){
                            Toast.makeText(mActivity, "First Select Item " , Toast.LENGTH_SHORT).show();
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
                            objRows.addProperty("macAddress", objUtility.funGetCurrentMACAddress(clsNCKOTScreen.this));


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


                                Toast.makeText(clsNCKOTScreen.this, "KOT No- " + kotNO,Toast.LENGTH_SHORT).show();
                                // funPrintKot(res);
                                //new GetKOTDataWS().execute(res);
                                funGetKOTDataFromWS(kotNO);
                                arrListKOTItems.clear();
                                funRefreshItemGrid();
                                tvKotHeaderTableName.setText("");
                                tvKotHeaderWaiterName.setText("");
                                edtNCKOTTotalAmount.setText("");
                                tableNo="";
                                waiterNo="";
                                paxNo=1;
                                debitCardMemberCode="";
                                debitCardMemberName="";
                                // btnNCKOTMember.setText("Select Member");
                                /*clsKotOtherOptionsFragment.chkNcKOT.setChecked(false);
                                clsKotOtherOptionsFragment.edtRemark.setText("");*/
                                new clsKotOtherOptionsFragment().widgetInit();
                                pager.setCurrentItem(0);
                                totalMemberBalance=0;
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
                            ArrayList arrListKotItemMaster = new ArrayList();
                            JsonArray mJsonArray = (JsonArray) jMainObj.get("PreviousKOTDtls");
                            JsonObject mJsonObject = new JsonObject();


                            for (int i = 0; i < mJsonArray.size(); i++) {
                                mJsonObject = (JsonObject) mJsonArray.get(i);
                                if (mJsonObject.get("TableNo").getAsString().toString().equals("")) {

                                } else {
                                    double reedemAmt = Double.parseDouble(mJsonObject.get("RedeemAmt").getAsString().toString());
                                    clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean("1", mJsonObject.get("TableNo").getAsString().toString(), mJsonObject.get("CardNo").getAsString().toString(), reedemAmt, "", mJsonObject.get("CustomerCode").getAsString().toString(), clsGlobalFunctions.gPOSCode
                                            , mJsonObject.get("ItemCode").getAsString().toString(), mJsonObject.get("ItemName").getAsString().toString()
                                            , Double.parseDouble(mJsonObject.get("ItemQty").getAsString().toString()), Double.parseDouble(mJsonObject.get("Amount").getAsString().toString())
                                            , mJsonObject.get("WaiterNo").getAsString().toString(), mJsonObject.get("KOTNo").getAsString().toString(), 1, "Y", "", clsGlobalFunctions.gUserCode,
                                            clsGlobalFunctions.gUserCode, objGlobal.funGetCurrentDateTime(), objGlobal.funGetCurrentDateTime(), "No"
                                            , "N", "N", "N", "N", "", "", "", "", "", "", false, Double.parseDouble(mJsonObject.get("Rate").getAsString().toString()), mJsonObject.get("CardType").getAsString().toString());
                                    arrListKotItemMaster.add(objItemDtl);
                                }

                            }

                            if (arrListKotItemMaster.size()>0)
                            {
                                arrListKOTItems=arrListKotItemMaster;
                                arrListPreviousKOTItems=arrListKotItemMaster;
                                previousKOTSize=arrListKOTItems.size();
                                funRefreshItemGrid();

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
                                    final Dialog dialog = new Dialog(clsNCKOTScreen.this);
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

    private void funPrintKot(JsonArray jArrItemDtl, String costCenterName, String tableName, String KOTNo,String PaxNo)
    {
        try {

            StringBuilder sbPrintKot = new StringBuilder();
            clsGlobalFunctions objGlobal = new clsGlobalFunctions();
            clsPrintFormatAPI objPrint=new clsPrintFormatAPI();
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

            for(int i=0;i<jArrItemDtl.size();i++) {
                JsonObject objRows = (JsonObject)jArrItemDtl.get(i);
                sbPrintKot.append(objPrint.funGetStringWithAlignment(objRows.get("ItemQty").getAsString(), "Left", 8));
                sbPrintKot.append(objPrint.funGetStringWithAlignment(objRows.get("ItemName").getAsString(), "Left", 22));
                sbPrintKot.append("\n");
            }
            sbPrintKot.append("--------------------------------");

            sbPrintKot.append("\n");
            sbPrintKot.append("\n");
            sbPrintKot.append("\n");
            sbPrintKot.append("\n");




            // Function to print bill on mach device.
            // clsMakeBillScreen.funPrintBillForMachDevice(sbPrintBill.toString());

            if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer"))
            {
                new clsPrintDemo().sendData(sbPrintKot.toString(),clsPrintDemo.mmOutputStream,clsPrintDemo.mmInputStream);
            }
            else
            {
                sbPrintKot.append("\n");
                sbPrintKot.append("\n");
                sbPrintKot.append("\n");
                sbPrintKot.append("\n");
                sbPrintKot.append("\n");
                sbPrintKot.append("\n");
                //funPrintBillForMachDevice(sbPrintKot.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
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

