package com.example.apos.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.bewo.mach.tools.MACHServices;
import com.example.apos.App;
import com.example.apos.adapter.clsBillListSettleAdapter;
import com.example.apos.adapter.clsGuestDialogAdapter;
import com.example.apos.adapter.clsPOSSettlementAdapter;
import com.example.apos.adapter.clsSetSettleModeAdapter;
import com.example.apos.adapter.clsUnReservedTableListDialogAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsBillDtl;
import com.example.apos.bean.clsBillHd;
import com.example.apos.bean.clsBillListDtl;
import com.example.apos.bean.clsBillSeriesItemDtl;
import com.example.apos.bean.clsCustomerMaster;
import com.example.apos.bean.clsDirectBillSelectedListItemBean;
import com.example.apos.bean.clsGuestRoomDtl;
import com.example.apos.bean.clsItemDtl;
import com.example.apos.bean.clsPromotionItems;
import com.example.apos.bean.clsReasonMaster;
import com.example.apos.bean.clsSettlementDtl;
import com.example.apos.bean.clsSettlementOption;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.util.clsPrintDemo;
import com.example.apos.util.clsUtility;
import com.example.apos.util.mach.clsPrintFormatAPI;
import com.example.apos.views.CustomSearchView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

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
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import static android.text.InputType.TYPE_NUMBER_FLAG_DECIMAL;

public class clsBillSettlement extends Activity implements clsPOSSettlementAdapter.customButtonListener  {
    MACHServices machService;
    clsBillListSettleAdapter objBillListSettleAdapter;
    Button btnSettle,btnPrint;
    ListView objSettleBillList, objSelectedPayModeList;
    Spinner spinnerPayMode;
    double paidAmt = 0.0, bal = 0.0, totalAmt = 0.0,balance=0.0;
    String billNo, settleType;
    String tableNo = "", waiterNo;
    String cardBalance;
    String memberFound;
    String debitCardNo;
    String cardNo;
    String debitCardString;
    public Activity mActivity;
    LinearLayout layoutBalanceScreen;
    EditText edtPaidAmount, edtBalanceRemark;
    ArrayList<clsSettlementDtl> listSettleDtl;
    clsSetSettleModeAdapter objSettleAdapter;
    clsSettlementDtl objSettleDtl;
    List<clsSettlementOption> listBillSettleObject;
    private String customerCode;
    private String debitCardMemberName;
    private String debitCardMemberCode;
    private double totalMemberBalance,totalDisAmt=0;
    TextView textBillNo, textBillDate, textSubTotal, textGrandTotal,
            textTaxAmt, textDisAmt, textPayMode, txtBillAmt, txtBalance,
            txtMemberBalance, txtShowMemberBalanace,txtBalnceAmt,textViewBillAmt,
            textViewPaidAmt,textViewBalRmk,tvHeaderDate,tvBillNoHeader;
    Map<String, clsSettlementDtl> hmSettleDtl = new HashMap<String, clsSettlementDtl>();
    Map<String, clsSettlementOption> hmBillSettlementDtl = new HashMap<String, clsSettlementOption>();
    private ConnectivityManager connectivityManager;
    Intent intenBillNo,intentObj;
    clsDirectBillSelectedListItemBean obj;
    List<clsDirectBillSelectedListItemBean> listDirectBillSelectedItems;
    List<clsPromotionItems> listPromotionItems=new ArrayList<clsPromotionItems>();
    private String operationType,operationTypeForTax;
    private String customerName;
    private String custTyp;
    ArrayList<clsItemDtl> arrListItemDtl;
    private String formName="",type="",reasonName="",reasonCode="",remark="",customerDtlsForCredit="",discReasonRemark="";
    private BluetoothAdapter mBluetoothAdapter;
    private String alreadyCardAssigned="N";
    List<clsGuestRoomDtl> listOfGuestRoomDtl = new ArrayList<>();
    GridView dialoglist = null;
    Dialog dialog = null,childDialog=null;
    private  String keyCase="upperCase";
    private Dialog pgDialog;
    Thread DareTimeThread = null;
    List<clsCustomerMaster> arrListCustomerMaster =new ArrayList<clsCustomerMaster>();
    private TreeMap hmReason;
    private ArrayList<String> reasonList;
    GridView settleGridView;
    int intNoOfBills=0;
    //HashMap<String,List<clsBillSeriesItemDtl>> mapBillSeriesData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.billsettlementscreen);
        mActivity = clsBillSettlement.this;
        widgetInit();

        Runnable runnable = new CountDownRunner();
        DareTimeThread = new Thread(runnable);
        DareTimeThread.start();
        intenBillNo = getIntent();
        billNo = intenBillNo.getStringExtra("BillNO");
        intentObj = getIntent();
        listDirectBillSelectedItems = (List<clsDirectBillSelectedListItemBean>) intentObj.getSerializableExtra("LIST");

        if(null != intentObj.getSerializableExtra("LIST"))
        {
            formName="Direct Biller";
            textBillDate.setText(clsGlobalFunctions.gPOSDateHeader);
            tvBillNoHeader.setVisibility(View.INVISIBLE);
            funGetdataFromDirectBiller();
        }
        else
        {
            formName="Make Bill";
            btnPrint.setVisibility(View.INVISIBLE);
            funGetSelectedBillDetails();
        }


        final ArrayList<String> arrPayMode = new ArrayList<String>();

        listSettleDtl = (ArrayList) clsGlobalFunctions.gHashMapSettlementDtl.get(clsGlobalFunctions.gPOSCode);
        for (int cnt = 0; cnt < listSettleDtl.size(); cnt++) {
            clsSettlementDtl objSettle = listSettleDtl.get(cnt);
            arrPayMode.add(objSettle.getStrSettlementName());
            hmSettleDtl.put(objSettle.getStrSettlementName(), objSettle);
        }

        clsPOSSettlementAdapter settleAdapter = new clsPOSSettlementAdapter(this, listSettleDtl);
        settleAdapter.setCustomButtonListner(this);
        settleGridView.setAdapter(settleAdapter);

        btnSettle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                btnSettle.setEnabled(false);
                String txtAmt=txtBalance.getText().toString();
                String balText=txtBalnceAmt.getText().toString();

                if ((!txtAmt.equals("0.0")) && (balText.equals("Balance")) )
                {
                    Toast.makeText(clsBillSettlement.this, "Balance is Non Zero", Toast.LENGTH_LONG).show();

                }
                else
                {
                    if(formName.equalsIgnoreCase("Make Bill"))
                    {
                        funSaveBill();
                        /*funInsertBillHd();
                        funInsertBillSettlementDtl();
                        if(hmBillSettlementDtl.size()>0)
                        {
                            if(hmBillSettlementDtl.containsKey("Complementary"))
                            {
                                funInsertComplementoryDtl();
                            }
                        }*/

                    }
                    else
                    {
                        type="Settle";
                        if(clsGlobalFunctions.gEnableBillSeries.equals("Y")){
                           funGetBillSeriesList();
                        }else{
                            funGenerateBillNo(clsGlobalFunctions.gPOSCode,customerCode);
                        }
                    }
                }

                btnSettle.setEnabled(true);

            }
        });

        btnPrint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                btnPrint.setEnabled(false);
                type="Print";
                if(clsGlobalFunctions.gEnableBillSeries.equals("Y")){
                    funGetBillSeriesList();
                }else{
                    funGenerateBillNo(clsGlobalFunctions.gPOSCode,customerCode);
                }

                btnPrint.setEnabled(true);


            }
        });

    }

    private void funGetdataFromDirectBiller()
    {
        if(listDirectBillSelectedItems.size()>0)
        {
            if(clsGlobalFunctions.gActivePromotions.equals("Y"))
            {
                if(listDirectBillSelectedItems.size()>0)
                {
                    obj = (clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(0);
                    customerCode = obj.getStrCustomerCode();
                    customerName = obj.getStrCustomerName();
                    operationType = obj.getStrOperationType();
                    if(operationType.equals("TakeAway"))
                    {
                        operationTypeForTax="TakeAway";
                    }
                    else if(operationType.equals("DirectBiller"))
                    {
                        operationTypeForTax="DineIn";
                    }
                    else
                    {
                        operationTypeForTax="HomeDelivery";
                    }
                    custTyp=obj.getStrCustomerType();
                }

                funCalculatePromotion();
            }
            else
            {
                for (int cnt = 0; cnt < listDirectBillSelectedItems.size(); cnt++)
                {
                    obj = (clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(cnt);
                    customerCode = obj.getStrCustomerCode();
                    customerName = obj.getStrCustomerName();
                    operationType = obj.getStrOperationType();
                    if(operationType.equals("TakeAway"))
                    {
                        operationTypeForTax="TakeAway";
                    }
                    else if(operationType.equals("DirectBiller"))
                    {
                        operationTypeForTax="DineIn";
                    }
                    else
                    {
                        operationTypeForTax="HomeDelivery";
                    }
                    clsItemDtl obItemDtl = new clsItemDtl();
                    obItemDtl.setStrItemName(obj.getStrDesc());
                    obItemDtl.setStrQty(String.valueOf(obj.getQty()));
                    obItemDtl.setStrAmount(String.valueOf(obj.getAmount()));
                    arrListItemDtl.add(obItemDtl);
                }

                funSetListFromDirectBiller(arrListItemDtl);
            }
        }

    }

    private void widgetInit()
    {
        machService = new MACHServices(getApplicationContext());
        memberFound = "N";

        type="";
        formName="Make Bill";
        intentObj=new Intent();
        intenBillNo=new Intent();
        billNo="";
        listDirectBillSelectedItems = new ArrayList<clsDirectBillSelectedListItemBean>();
        arrListItemDtl = new ArrayList<clsItemDtl>();
        operationType="";
        operationTypeForTax="";
        customerCode="";
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        settleType = "Cash";
        cardBalance = "";
        debitCardMemberName = "";
        debitCardMemberCode = "";
        debitCardNo = "";
        debitCardString="";
        cardNo="";
        btnSettle = (Button) findViewById(R.id.billSettle);
        btnPrint= (Button) findViewById(R.id.billPrint);
        textBillNo = (TextView) findViewById(R.id.txtBillNo);
        textBillDate = (TextView) findViewById(R.id.txtBillDate);
        textSubTotal = (TextView) findViewById(R.id.txtBillSubTotal);
        textGrandTotal = (TextView) findViewById(R.id.txtBillGrandTotal);
        textTaxAmt = (TextView) findViewById(R.id.txtBillTaxAmt);
        textDisAmt = (TextView) findViewById(R.id.txtBillDisAmt);
        txtBalance = (TextView) findViewById(R.id.txtBalanceAmt);
        txtBalnceAmt=(TextView) findViewById(R.id.textBalanceAmt);
        tvHeaderDate=(TextView) findViewById(R.id.tv_Settle_bill_header_timestamp);
        objSettleBillList = (ListView) findViewById(R.id.listSelectedBill);
        objSelectedPayModeList = (ListView) findViewById(R.id.listPayMode);
        settleGridView= (GridView) findViewById(R.id.settleGridView);
        tvBillNoHeader=(TextView) findViewById(R.id.tvBillNoHeader);
        if(clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer")) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            new clsPrintDemo().funPrintViaBluetoothPrinter(mBluetoothAdapter);
        }

    }

    private void funClearObjects() {
        tableNo = "";
        waiterNo = "";
        billNo = "";
        textGrandTotal.setText("0.0");
        textSubTotal.setText("0.0");
        textTaxAmt.setText("0.0");
        textDisAmt.setText("0.0");
        textPayMode.setText("");//
        txtBillAmt.setText("0.0");//
        txtBalance.setText("0.0");
        edtPaidAmount.setText("0.0");//
        edtPaidAmount.setSelection(edtPaidAmount.getText().length());
        edtBalanceRemark.setText("");//
        arrListItemDtl.clear();
        textViewBillAmt.setText("BillAmt:");//
        textViewPaidAmt.setText("PaidAmt:");//
        textViewBalRmk.setText("Bal Remark:");//
       // txtShowMemberBalanace.setVisibility(View.INVISIBLE);
       // txtShowMemberBalanace.setText("");
      //  txtMemberBalance.setText("");
       // txtMemberBalance.setVisibility(View.INVISIBLE);



    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    TextView txtCurrentTime = (TextView) findViewById(R.id.tv_Settle_bill_header_timestamp);
                    String formattedDate = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                    txtCurrentTime.setText(clsGlobalFunctions.gPOSDateHeader+" "+formattedDate);
                } catch (Exception e) {
                }
            }
        });
    }

    @Override
    public void onButtonClickListner(int position, String value)
    {
        settleType = hmSettleDtl.get(value).getStrSettlementType();
        // funSetValue(settleType);
        funOpenDialogForSelectedSettleMode(settleType,value);


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


    private void updateField(double balanceAmt)
    {
        if(balanceAmt>0)
        {
            txtBalnceAmt.setText("Refund");
            txtBalance.setText(String.valueOf(balanceAmt));

        }
        else if(balanceAmt==0)
        {
            txtBalnceAmt.setText("Balance");
            txtBalance.setText(String.valueOf(balanceAmt));
        }
        else
        {
            double balAmt=0;
            balAmt=-(balanceAmt);
            txtBalnceAmt.setText("Balance");
            txtBalance.setText(String.valueOf(balAmt));
        }

    }

    private void funGetSettleListData()
    {
        final ArrayList<String> arrPayselectedMode = new ArrayList<String>();
        for (Map.Entry<String, clsSettlementOption> entry : hmBillSettlementDtl.entrySet())
        {
            if(entry.getKey().equals("Complementary"))
            {
                clsSettlementOption obj = entry.getValue();
                arrPayselectedMode.add(obj.getStrSettelmentDesc() + "#" + obj.getDblActualAmt());
            }
            else
            {
                clsSettlementOption obj = entry.getValue();
                arrPayselectedMode.add(obj.getStrSettelmentDesc() + "#" + obj.getDblPaidAmt());
            }
        }
        objSettleAdapter = new clsSetSettleModeAdapter(getApplicationContext(), arrPayselectedMode);
        objSelectedPayModeList.setAdapter(objSettleAdapter);
    }



    private JsonArray funInsertBillSettlementDtl() {
        JsonArray mJsonArray = new JsonArray();
        try {
            JsonObject jObj = new JsonObject();


            for (Map.Entry<String, clsSettlementOption> entry : hmBillSettlementDtl.entrySet()) {
                JsonObject mJsonObjectSettlementDtl = new JsonObject();
                clsSettlementOption objSettle = entry.getValue();

                mJsonObjectSettlementDtl.addProperty("BillNo", billNo);
                mJsonObjectSettlementDtl.addProperty("SettlementCode", objSettle.getStrSettelmentCode());
                mJsonObjectSettlementDtl.addProperty("SettlementAmt", objSettle.getDblSettlementAmt());
                mJsonObjectSettlementDtl.addProperty("SettlementDesc", objSettle.getStrSettelmentDesc());

                mJsonObjectSettlementDtl.addProperty("PaidAmt", objSettle.getDblPaidAmt());
                mJsonObjectSettlementDtl.addProperty("ExpiryDate", objSettle.getStrExpiryDate());
                mJsonObjectSettlementDtl.addProperty("CardName", objSettle.getStrCardName());
                mJsonObjectSettlementDtl.addProperty("Remark", objSettle.getStrRemark());
                mJsonObjectSettlementDtl.addProperty("ClientCode", objSettle.getStrClientCode());
                mJsonObjectSettlementDtl.addProperty("ActualAmt", objSettle.getDblActualAmt());
                mJsonObjectSettlementDtl.addProperty("RefundAmt", objSettle.getDblRefundAmt());
                mJsonObjectSettlementDtl.addProperty("GiftVoucherCode", objSettle.getStrGiftVoucherCode());
                mJsonObjectSettlementDtl.addProperty("DataPostFlag", objSettle.getStrDataPostFlag());
                mJsonObjectSettlementDtl.addProperty("DebitCardNo",debitCardNo );
                mJsonObjectSettlementDtl.addProperty("DebitCardString", debitCardNo);
                mJsonObjectSettlementDtl.addProperty("POSCode", clsGlobalFunctions.gPOSCode);
                mJsonObjectSettlementDtl.addProperty("POSDate", clsGlobalFunctions.funGetPOSDateTime());
                mJsonObjectSettlementDtl.addProperty("SettlementMode", objSettle.getStrSettelmentType());
                if (clsGlobalFunctions.gEnablePMSIntegration.equals("Y") && settleType.equals("Room"))
                {
                    mJsonObjectSettlementDtl.addProperty("FolioNo", objSettle.getStrFolioNo());
                    mJsonObjectSettlementDtl.addProperty("RoomNo", objSettle.getStrRoomNo());
                    mJsonObjectSettlementDtl.addProperty("CustomerCode", objSettle.getStrGuestCode());
                }
                else
                {
                    mJsonObjectSettlementDtl.addProperty("FolioNo", "");
                    mJsonObjectSettlementDtl.addProperty("RoomNo", "");
                    mJsonObjectSettlementDtl.addProperty("CustomerCode", objSettle.getStrCustomerCode());

                }
                mJsonObjectSettlementDtl.addProperty("BillDate", clsGlobalFunctions.funGetPOSDateTime());
                mJsonArray.add(mJsonObjectSettlementDtl);

            }

            jObj.add("BillSettlementDtl", mJsonArray);
           // funSaveBill(jObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
    return mJsonArray;

    }

    private JsonArray funInsertBillHd() {
        JsonArray mJsonArray = new JsonArray();
        try {
            clsGlobalFunctions objGlobal = new clsGlobalFunctions();
            JsonObject jObj = new JsonObject();

            JsonObject mJsonObject = new JsonObject();

            mJsonObject.addProperty("BillNo", billNo);
            mJsonObject.addProperty("AdvBookingNo", "");
            mJsonObject.addProperty("BillDate", clsGlobalFunctions.funGetPOSDateTime());
            mJsonObject.addProperty("POSCode", clsGlobalFunctions.gPOSCode);
            if (hmBillSettlementDtl.size() == 1) {
                Object[] arrObj = hmBillSettlementDtl.values().toArray();
                clsSettlementOption objTemp = (clsSettlementOption) arrObj[0];
                mJsonObject.addProperty("SettelmentMode", objTemp.getStrSettelmentType());
                if (clsGlobalFunctions.gEnablePMSIntegration.equals("Y") && settleType.equals("Room"))
                {
                    String settleAmt=String.valueOf(objTemp.getDblSettlementAmt());
                    String pmsData=settleAmt+"#"+objTemp.getStrFolioNo()+"#"+objTemp.getStrRoomNo()+"#"+objTemp.getStrGuestCode()+"#"+billNo;
                    new funPostPMSSettleData().execute(pmsData);
                }


            }
            else
            {
                mJsonObject.addProperty("SettelmentMode", "MultiSettle");

            }

            if (hmBillSettlementDtl.containsKey("Complementary"))
            {
                mJsonObject.addProperty("TaxAmount", 0.0);
                mJsonObject.addProperty("SubTotal", 0.0);
                mJsonObject.addProperty("GrandTotal", 0.0);
            }
            else
            {
                mJsonObject.addProperty("TaxAmount", textTaxAmt.getText().toString());
                mJsonObject.addProperty("SubTotal", textSubTotal.getText().toString());
                mJsonObject.addProperty("GrandTotal", textGrandTotal.getText().toString());
            }


            mJsonObject.addProperty("DiscountAmt", textDisAmt.getText().toString());
            double finalDiscPer = (Double.valueOf(textDisAmt.getText().toString()) /Double.valueOf(textSubTotal.getText().toString())) * 100;
            mJsonObject.addProperty("DiscountPer", finalDiscPer);
            if(!discReasonRemark.isEmpty())
            {
                mJsonObject.addProperty("ReasonCode", discReasonRemark.split("#")[0]);
                mJsonObject.addProperty("Remarks", "Promotion discount");
                mJsonObject.addProperty("DiscountRemark", "Promotion discount");
            }
            else
            {
                mJsonObject.addProperty("ReasonCode", "");
                mJsonObject.addProperty("Remarks", "");
                mJsonObject.addProperty("DiscountRemark", "");
            }

            mJsonObject.addProperty("TakeAway", "No");
            mJsonObject.addProperty("OperationType", operationType);
            mJsonObject.addProperty("UserCreated", clsGlobalFunctions.gUserCode);
            mJsonObject.addProperty("UserEdited", clsGlobalFunctions.gUserCode);
            mJsonObject.addProperty("DateCreated", objGlobal.funGetCurrentDateTime());
            mJsonObject.addProperty("DateEdited", objGlobal.funGetCurrentDateTime());
            mJsonObject.addProperty("ClientCode", clsGlobalFunctions.gClientCode);
            mJsonObject.addProperty("CustomerCode", customerCode);
            mJsonObject.addProperty("TableNo", tableNo);
            mJsonObject.addProperty("WaiterNo", waiterNo);
            mJsonObject.addProperty("ManualBillNo", "");
            mJsonObject.addProperty("ShiftCode", 1);
            mJsonObject.addProperty("PaxNo", 0);
            mJsonObject.addProperty("DataPostFlag", "N");
            mJsonObject.addProperty("TipAmount", 0.0);
            mJsonObject.addProperty("SettleDate", objGlobal.funGetCurrentDateTime());
            mJsonObject.addProperty("CounterCode", "");
            mJsonObject.addProperty("DeliveryCharges", 0.0);
            mJsonObject.addProperty("CouponCode", "");
            mJsonObject.addProperty("AreaCode", "A001");
            mJsonObject.addProperty("TakeAwayRemark", "");
            mJsonObject.addProperty("CardNo", debitCardMemberCode);
            String dateTime=clsGlobalFunctions.gPOSDate;
            String posDate=dateTime.replaceAll(" ","%20");
            mJsonObject.addProperty("BillDt", posDate);
            mJsonArray.add(mJsonObject);

            //jObj.add("BillHdInfo", mJsonArray);
            //funSaveBill(jObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mJsonArray;
    }



    private void funSetList(clsBillListDtl objBillListDtl) {
        ArrayList<clsItemDtl> arrList = (ArrayList<clsItemDtl>) objBillListDtl.getArrListItemtDtl();
        customerCode=objBillListDtl.getCustomerCode();
        customerName=objBillListDtl.getStrCustName();
        operationType=objBillListDtl.getOperationType();
        objBillListSettleAdapter = new clsBillListSettleAdapter(clsBillSettlement.this, arrList);
        objSettleBillList.setAdapter(objBillListSettleAdapter);

        textBillNo.setText(objBillListDtl.getStrBillNo());
        textSubTotal.setText(objBillListDtl.getStrSubTotal());
        textGrandTotal.setText(objBillListDtl.getStrGrandTotal());
        txtBalance.setText(objBillListDtl.getStrGrandTotal());
        textDisAmt.setText(objBillListDtl.getStrDiscountAmt());
        textTaxAmt.setText(objBillListDtl.getStrTaxAmt());
        tableNo = objBillListDtl.getStrTableNo();
        waiterNo = objBillListDtl.getStrWaiterNo();
        String billDate="";
        try{
            DateFormat df= new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date=df.parse(objBillListDtl.getDteBillDate());
            billDate=new SimpleDateFormat("dd-MM-yyyy").format(date);//hh:mm:
        }catch (Exception e){
            e.printStackTrace();
        }
        textBillDate.setText(billDate);
    }

    private void funSetListFromDirectBiller(ArrayList<clsItemDtl> arrListItemDtl)
    {
        totalAmt=0;
        double taxTotalAmt=0;
        for(int cnt=0;cnt<arrListItemDtl.size();cnt++)
        {
            clsItemDtl objItem=(clsItemDtl) arrListItemDtl.get(cnt);
            totalAmt+=Double.parseDouble(objItem.getStrAmount());
        }
        textSubTotal.setText(String.valueOf(totalAmt));
        textDisAmt.setText(String.valueOf(totalDisAmt));
        //new TaxCalculateWS().execute();
        funTaxCalculateWs();
        objBillListSettleAdapter = new clsBillListSettleAdapter(clsBillSettlement.this, arrListItemDtl);
        objSettleBillList.setAdapter(objBillListSettleAdapter);
    }



    private void funGenerateBillTextFile(final String bill)
    {
        if(formName.equals("Make Bill"))
        {
            if(hmBillSettlementDtl.size()>1)
            {
                funGenerateAndPrintTextfile(bill);
            }
            else
            {
                for (Map.Entry<String, clsSettlementOption> entry : hmBillSettlementDtl.entrySet())
                {
                    clsSettlementOption obj = entry.getValue();
                    String printBillOnAfterSettleYN=hmSettleDtl.get(obj.getStrSettelmentDesc()).getStrBillPrintOnSettlementYN();
                    if(printBillOnAfterSettleYN.equals("Y"))
                    {
                        funGenerateAndPrintTextfile(bill);
                    }else{
                        intNoOfBills--;
                        if(intNoOfBills==0){
                            finish();
                        }
                    }
                }
            }
        }
        else
        {
            if(type.equals("Print"))
            {
                if(clsGlobalFunctions.gEnableKOTForDirectBiller.equalsIgnoreCase("Y"))
                {
                    if(clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("External Printer"))// External Printer Type
                    {
                        funGenerateDirectBillerKOTTextFileWebService(bill);
                    }
                    else
                    {
//                        if (mBluetoothAdapter.isEnabled())
//                        {
//                            funPrintDirectBillerKOTFromWS(bill);
//                        }
                        funPrintDirectBillerKOTFromWS(bill);
                    }
                }

                funGenerateAndPrintTextfile(bill);

            }
            else
            {
//                AlertDialog.Builder builder1 = new AlertDialog.Builder(clsBillSettlement.this);
//                builder1.setMessage("Do You Want To Print Bill???");
//                builder1.setCancelable(true);
//                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id)
//                            {
//                                funGenerateAndPrintTextfile(bill);
//                                intNoOfBills--;
//                                if(intNoOfBills==0){
//                                    finish();
//                                }
//
//                            }
//                        }
//                );
//                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id)
//                            {
//                                dialog.cancel();
//                                finish();
//                            }
//                        }
//                );
//                AlertDialog alert11 = builder1.create();
//                alert11.show();

                funGenerateAndPrintTextfile(bill);
            }
        }

        if(!(type.equals("Print"))){
            //funClearObjects();
        }

    }


    private JsonArray funInsertBillHd(String billNo,String customerCode)
    {
        JsonArray mJsonArray = new JsonArray();
        try {
            List<clsBillHd> listBillHdObject = new ArrayList<clsBillHd>();
            clsGlobalFunctions objGlobal = new clsGlobalFunctions();

            JsonObject jObj = new JsonObject();

            JsonObject mJsonObject = new JsonObject();

            mJsonObject.addProperty("BillNo", billNo);
            mJsonObject.addProperty("AdvBookingNo", "");
            mJsonObject.addProperty("BillDate", clsGlobalFunctions.funGetPOSDateTime());
            mJsonObject.addProperty("POSCode", clsGlobalFunctions.gPOSCode);
            if (hmBillSettlementDtl.size()>0)
            {
                if (hmBillSettlementDtl.size() == 1) {
                    Object[] arrObj = hmBillSettlementDtl.values().toArray();
                    clsSettlementOption objTemp = (clsSettlementOption) arrObj[0];
                    mJsonObject.addProperty("SettelmentMode", objTemp.getStrSettelmentType());
                    if (clsGlobalFunctions.gEnablePMSIntegration.equals("Y") && settleType.equals("Room"))
                    {
                        String settleAmt=String.valueOf(objTemp.getDblSettlementAmt());
                        String pmsData=settleAmt+"#"+objTemp.getStrFolioNo()+"#"+objTemp.getStrRoomNo()+"#"+objTemp.getStrGuestCode()+"#"+billNo;
                        new funPostPMSSettleData().execute(pmsData);
                    }

                } else
                {
                    mJsonObject.addProperty("SettelmentMode", "MultiSettle");

                }
            }
            else
            {
                mJsonObject.addProperty("SettelmentMode", "");
            }
            mJsonObject.addProperty("DiscountAmt", textDisAmt.getText().toString());
            double finalDiscPer = (Double.valueOf(textDisAmt.getText().toString()) /Double.valueOf(textSubTotal.getText().toString())) * 100;
            mJsonObject.addProperty("DiscountPer", finalDiscPer);
            mJsonObject.addProperty("TaxAmount", textTaxAmt.getText().toString());
            mJsonObject.addProperty("SubTotal", textSubTotal.getText().toString());
            mJsonObject.addProperty("GrandTotal",textGrandTotal.getText().toString());
            if(operationType.equalsIgnoreCase("TakeAway"))
            {
                mJsonObject.addProperty("TakeAway", "Yes");
            }
            else
            {
                mJsonObject.addProperty("TakeAway", "No");
            }
            mJsonObject.addProperty("OperationType",operationType);
            mJsonObject.addProperty("UserCreated", clsGlobalFunctions.gUserCode);
            mJsonObject.addProperty("UserEdited", clsGlobalFunctions.gUserCode);
            mJsonObject.addProperty("DateCreated", objGlobal.funGetCurrentDateTime());
            mJsonObject.addProperty("DateEdited", objGlobal.funGetCurrentDateTime());
            mJsonObject.addProperty("ClientCode", clsGlobalFunctions.gClientCode);

            mJsonObject.addProperty("TableNo","");
            mJsonObject.addProperty("WaiterNo", "");
            mJsonObject.addProperty("CustomerCode", customerCode);
            mJsonObject.addProperty("ManualBillNo", "");
            mJsonObject.addProperty("ShiftCode", 1);
            mJsonObject.addProperty("PaxNo", 0);
            mJsonObject.addProperty("DataPostFlag","N");
            if(!discReasonRemark.isEmpty())
            {
                mJsonObject.addProperty("ReasonCode", discReasonRemark.split("#")[0]);
                mJsonObject.addProperty("Remarks", "Promotion discount");
                mJsonObject.addProperty("DiscountRemark", "Promotion discount");
            }
            else
            {
                mJsonObject.addProperty("ReasonCode", "");
                mJsonObject.addProperty("Remarks", "");
                mJsonObject.addProperty("DiscountRemark", "");
            }
            mJsonObject.addProperty("TipAmount", 0.0);
            mJsonObject.addProperty("SettleDate", objGlobal.funGetCurrentDateTime());
            mJsonObject.addProperty("CounterCode", "");
            mJsonObject.addProperty("DeliveryCharges", 0.0);
            mJsonObject.addProperty("CouponCode", "");
            mJsonObject.addProperty("AreaCode", clsGlobalFunctions.gDirectBillerAreaCode);
            mJsonObject.addProperty("TakeAwayRemark", "");
            mJsonObject.addProperty("CardNo", debitCardNo);
            String dateTime=clsGlobalFunctions.gPOSDate;
            String posDate=dateTime.replaceAll(" ","%20");
            mJsonObject.addProperty("BillDt", posDate);
            mJsonArray.add(mJsonObject);

           // jObj.add("BillHdInfo", mJsonArray);
           // funSaveBill(jObj);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mJsonArray;
    }


    private JsonArray funInsertBillDtl(String billNo,String customerCode) {
        JsonArray mJsonArrBillDtl = new JsonArray();
        try {
            List<clsBillDtl> listBillDtlObject = new ArrayList<clsBillDtl>();
            clsGlobalFunctions objGlobal = new clsGlobalFunctions();
            JsonObject jObjBillDtl = new JsonObject();


            for (int l = 0; l < listDirectBillSelectedItems.size(); l++) {
                clsDirectBillSelectedListItemBean objDirectBillList = (clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(l);

                //I001321M99
                String itemCode=objDirectBillList.getStrItemCode();
                if(itemCode.contains("M") && itemCode.length()>=10)
                {
                }
                else
                {
                    JsonObject mJsonObjectBillDtl = new JsonObject();
                    String discountAmt="0.0";
                    if(listPromotionItems.size()>0) {
                        for (int k = 0; k < listPromotionItems.size(); k++) {
                            clsPromotionItems objPromoBillList = (clsPromotionItems) listPromotionItems.get(k);
                            if (objPromoBillList.getPromoType().equalsIgnoreCase("Discount"))
                            {
                              if(objPromoBillList.getItemCode().equalsIgnoreCase(itemCode))
                              {
                                  discountAmt=objPromoBillList.getDiscAmt()+"#"+objPromoBillList.getDiscPer();
                                  break;
                              }
                            }
                        }
                    }

                    itemCode=itemCode.substring(0,7);
                    mJsonObjectBillDtl.addProperty("ItemCode", itemCode);
                    mJsonObjectBillDtl.addProperty("ItemName", objDirectBillList.getStrDesc());

                    mJsonObjectBillDtl.addProperty("BillNo", billNo);
                    mJsonObjectBillDtl.addProperty("BillDate", clsGlobalFunctions.funGetPOSDateTime());
                    mJsonObjectBillDtl.addProperty("AdvBookingNo", "");
                    mJsonObjectBillDtl.addProperty("Quantity", objDirectBillList.getQty());
                    mJsonObjectBillDtl.addProperty("Rate", objDirectBillList.getItemSalePrice());
                    mJsonObjectBillDtl.addProperty("Amount", objDirectBillList.getAmount());
                    mJsonObjectBillDtl.addProperty("TaxAmount", 0.00);
                    mJsonObjectBillDtl.addProperty("KOTNo", "");
                    mJsonObjectBillDtl.addProperty("ClientCode", clsGlobalFunctions.gClientCode);
                    mJsonObjectBillDtl.addProperty("CustomerCode",customerCode);
                    mJsonObjectBillDtl.addProperty("OrderProcessedTime", "00:00:00");
                    mJsonObjectBillDtl.addProperty("DataPostFlag", "N");
                    mJsonObjectBillDtl.addProperty("MMSDataPostFlag", "N");
                    mJsonObjectBillDtl.addProperty("ManualKOTNo", "");
                    mJsonObjectBillDtl.addProperty("tdhYN", "N");
                    mJsonObjectBillDtl.addProperty("PromoCode", "");
                    mJsonObjectBillDtl.addProperty("CounterCode", "");
                    mJsonObjectBillDtl.addProperty("WaiterNo", "");
                    String dateTime=clsGlobalFunctions.gPOSDate;
                    mJsonObjectBillDtl.addProperty("DiscountAmt", discountAmt);
                    String posDate=dateTime.replaceAll(" ","%20");
                    mJsonObjectBillDtl.addProperty("BillDt", posDate);
                    mJsonObjectBillDtl.addProperty("OrderPickedUpdTime", "00:00:00");
                    mJsonObjectBillDtl.addProperty("AreaCode",clsGlobalFunctions.gDirectBillerAreaCode);
                    mJsonObjectBillDtl.addProperty("OperationType",operationTypeForTax);
                    mJsonObjectBillDtl.addProperty("POSCode",clsGlobalFunctions.gPOSCode);
                    //mJsonObject.put("DiscountPer", 0.00);
                    mJsonObjectBillDtl.addProperty("UserCode", clsGlobalFunctions.gUserCode);
                    mJsonArrBillDtl.add( mJsonObjectBillDtl);
                }
            }

           // jObjBillDtl.add("BillDtlInfo", mJsonArrBillDtl);
           // funSaveBill(jObjBillDtl);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mJsonArrBillDtl;

    }



    private JsonArray funInsertBillModifierDtl(String billNo) {
        JsonArray mJsonArrBillModDtl = new JsonArray();
        try {
            clsGlobalFunctions objGlobal = new clsGlobalFunctions();

            JsonObject jObjBillModDtl = new JsonObject();


            for (int l = 0; l < listDirectBillSelectedItems.size(); l++) {
                clsDirectBillSelectedListItemBean objDirectBillList = (clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(l);

                //I001321M99
                String itemCode=objDirectBillList.getStrItemCode();
                if(itemCode.contains("M") && itemCode.length()>=10)
                {

                    JsonObject mJsonObjectBillModDtl = new JsonObject();

                    String modifierCode=itemCode.substring(7, 11);
                    itemCode=itemCode.substring(0,7);

                    mJsonObjectBillModDtl.addProperty("BillNo", billNo);
                    mJsonObjectBillModDtl.addProperty("ClientCode", clsGlobalFunctions.gClientCode);
                    mJsonObjectBillModDtl.addProperty("ItemCode",itemCode);
                    mJsonObjectBillModDtl.addProperty("ModifierCode",modifierCode);
                    mJsonObjectBillModDtl.addProperty("ModifierName",objDirectBillList.getStrDesc());
                    mJsonObjectBillModDtl.addProperty("Rate",objDirectBillList.getItemSalePrice());
                    mJsonObjectBillModDtl.addProperty("Quantity",objDirectBillList.getQty());
                    mJsonObjectBillModDtl.addProperty("Amount",objDirectBillList.getAmount());
                    mJsonObjectBillModDtl.addProperty("CustomerCode","");
                    mJsonObjectBillModDtl.addProperty("DataPostFlag","N");
                    mJsonObjectBillModDtl.addProperty("MMSDataPostFlag","N");
                    mJsonObjectBillModDtl.addProperty("DataPostFlag","N");
                    mJsonObjectBillModDtl.addProperty("MMSDataPostFlag","N");
                    mJsonObjectBillModDtl.addProperty("DataPostFlag","N");
                    mJsonObjectBillModDtl.addProperty("MMSDataPostFlag","N");
                    mJsonObjectBillModDtl.addProperty("DefaultModifierSelection","N");
                    mJsonObjectBillModDtl.addProperty("DiscPer","0.00");
                    mJsonObjectBillModDtl.addProperty("DiscAmt","0.00");
                    mJsonObjectBillModDtl.addProperty("BillDate", clsGlobalFunctions.funGetPOSDateTime());

                    mJsonArrBillModDtl.add( mJsonObjectBillModDtl);
                }
            }

            jObjBillModDtl.add("BillModifierDtl", mJsonArrBillModDtl);
            //funSaveBill(jObjBillModDtl);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mJsonArrBillModDtl;
    }


    private JsonObject funInsertPromotionDtl(String billNo) {
        JsonObject jObjBillDtl = new JsonObject();
        try
        {
            clsGlobalFunctions objGlobal = new clsGlobalFunctions();

            JsonArray mJsonPromoArrBillDtl = new JsonArray();
            JsonArray mJsonDiscArrBillDtl = new JsonArray();

            if(listPromotionItems.size()>0)
            {
                for (int l = 0; l < listPromotionItems.size(); l++)
                {
                    clsPromotionItems objPromoBillList = (clsPromotionItems) listPromotionItems.get(l);
                    if(objPromoBillList.getPromoType().equalsIgnoreCase("Discount"))
                    {
                        JsonObject mJsonObjectDiscDtl = new JsonObject();

                        mJsonObjectDiscDtl.addProperty("BillNo", billNo);
                        mJsonObjectDiscDtl.addProperty("ClientCode", clsGlobalFunctions.gClientCode);
                        mJsonObjectDiscDtl.addProperty("UserCode", clsGlobalFunctions.gUserCode);
                        mJsonObjectDiscDtl.addProperty("POSCode", clsGlobalFunctions.gPOSCode);
                        mJsonObjectDiscDtl.addProperty("CurrentDateTime",objGlobal.funGetCurrentDateTime());
                        mJsonObjectDiscDtl.addProperty("POSDate", clsGlobalFunctions.funGetPOSDateTime());
                        mJsonObjectDiscDtl.addProperty("DiscAmt", objPromoBillList.getDiscAmt());
                        mJsonObjectDiscDtl.addProperty("DiscPer",objPromoBillList.getDiscPer());
                        mJsonObjectDiscDtl.addProperty("DiscOnAmt", objPromoBillList.getDiscOnAmt());
                        mJsonObjectDiscDtl.addProperty("DiscOnType", "ItemWise");
                        mJsonObjectDiscDtl.addProperty("DiscOnValue", objPromoBillList.getItemName());
                        mJsonObjectDiscDtl.addProperty("Reason", discReasonRemark.split("#")[0]);
                        mJsonObjectDiscDtl.addProperty("Remark", discReasonRemark.split("#")[1]);

                        mJsonDiscArrBillDtl.add( mJsonObjectDiscDtl);
                    }
                    else
                    {
                        JsonObject mJsonObjectPromotionDtl = new JsonObject();
                        mJsonObjectPromotionDtl.addProperty("ItemCode", objPromoBillList.getItemCode());
                        mJsonObjectPromotionDtl.addProperty("PromoCode", objPromoBillList.getPromoCode());
                        mJsonObjectPromotionDtl.addProperty("BillNo", billNo);
                        mJsonObjectPromotionDtl.addProperty("PromoType", objPromoBillList.getPromoType());
                        Double rate=Double.valueOf(objPromoBillList.getBillAmt())/objPromoBillList.getFreeItemQty();
                        mJsonObjectPromotionDtl.addProperty("Quantity", objPromoBillList.getFreeItemQty());
                        mJsonObjectPromotionDtl.addProperty("Rate",rate);
                        mJsonObjectPromotionDtl.addProperty("Amount", objPromoBillList.getBillAmt());
                        mJsonObjectPromotionDtl.addProperty("ClientCode", clsGlobalFunctions.gClientCode);
                        mJsonObjectPromotionDtl.addProperty("DataPostFlag", "N");
                        mJsonObjectPromotionDtl.addProperty("BillDate", clsGlobalFunctions.funGetPOSDateTime());
                        mJsonPromoArrBillDtl.add( mJsonObjectPromotionDtl);
                    }
                }
                if(mJsonDiscArrBillDtl.size()>0)
                {
                    jObjBillDtl.add("BillDiscountDtl", mJsonDiscArrBillDtl);
                    //funSaveBill(jObjBillDtl);
                }
                if(mJsonPromoArrBillDtl.size()>0)
                {
                    jObjBillDtl = new JsonObject();
                    jObjBillDtl.add("BillPromotionDtl", mJsonPromoArrBillDtl);
                    //funSaveBill(jObjBillDtl);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return jObjBillDtl;

    }


    private JsonArray funInsertComplementoryDtl() {
        JsonArray mJsonArrBillDtl = new JsonArray();
        try
        {
            clsGlobalFunctions objGlobal = new clsGlobalFunctions();
            JsonObject jObjBillDtl = new JsonObject();


            JsonObject mJsonObjectComplementoryDtl = new JsonObject();
            mJsonObjectComplementoryDtl.addProperty("BillNo", billNo);
            mJsonObjectComplementoryDtl.addProperty("reasonCode", reasonCode);
            mJsonObjectComplementoryDtl.addProperty("remark", remark);
            mJsonObjectComplementoryDtl.addProperty("couponCode", "");
            mJsonObjectComplementoryDtl.addProperty("clientCode", clsGlobalFunctions.gClientCode);
            mJsonObjectComplementoryDtl.addProperty("posCode", clsGlobalFunctions.gPOSCode);
            mJsonObjectComplementoryDtl.addProperty("posDate", clsGlobalFunctions.funGetPOSDateTime());
            mJsonArrBillDtl.add( mJsonObjectComplementoryDtl);
            jObjBillDtl.add("BillComplementoryDtl", mJsonArrBillDtl);

           // funSaveBill(jObjBillDtl);



        } catch (Exception e) {
            e.printStackTrace();
        }
        return mJsonArrBillDtl;
    }



    private void funCalculateTax()
    {
        for (int cnt = 0; cnt < listDirectBillSelectedItems.size(); cnt++)
        {
            obj = (clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(cnt);
            customerCode = obj.getStrCustomerCode();
            customerName = obj.getStrCustomerName();
            operationType = obj.getStrOperationType();
            if(operationType.equals("TakeAway"))
            {
                operationTypeForTax="TakeAway";
            }
            else if(operationType.equals("DirectBiller"))
            {
                operationTypeForTax="DineIn";
            }
            else
            {
                operationTypeForTax="HomeDelivery";
            }
            custTyp=obj.getStrCustomerType();
            clsItemDtl obItemDtl = new clsItemDtl();
            obItemDtl.setStrItemName(obj.getStrDesc());
            obItemDtl.setStrQty(String.valueOf(obj.getQty()));
            obItemDtl.setStrAmount(String.valueOf(obj.getAmount()));
            arrListItemDtl.add(obItemDtl);
        }

        funSetListFromDirectBiller(arrListItemDtl);
    }


    private class funPostPMSSettleData extends AsyncTask<String, Void, JSONObject>
    {

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
        protected JSONObject doInBackground(String... params)
        {
            if(!clsGlobalFunctions.gAPOSWebSrviceURL.contains("prjSanguineWebService/APOSIntegration"))
            {
                clsGlobalFunctions.gAPOSWebSrviceURL=clsGlobalFunctions.gAPOSWebSrviceURL+"prjSanguineWebService/APOSIntegration";
            }

            String []pmsData=params[0].split("#");
            JSONObject jObj = null;
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(clsGlobalFunctions.gAPOSWebSrviceURL + "/funPostPMSSettleData?ClientCode=" + clsGlobalFunctions.gClientCode+ "&BillNo=" + pmsData[4]+ "&POSCode=" + clsGlobalFunctions.gPOSCode
                    + "&POSDate=" + clsGlobalFunctions.gPOSDate+ "&SettleAmt=" + pmsData[0]+ "&FolioNo=" + pmsData[1]+ "&RoomNo=" + pmsData[2]+ "&GuestNo=" + pmsData[3]);
            System.out.println(clsGlobalFunctions.gAPOSWebSrviceURL + "/funPostPMSSettleData?ClientCode=" + clsGlobalFunctions.gClientCode+ "&BillNo=" + pmsData[4]+ "&POSCode=" + clsGlobalFunctions.gPOSCode
                    + "&POSDate=" + clsGlobalFunctions.gPOSDate+ "&SettleAmt=" + pmsData[0]+ "&FolioNo=" + pmsData[1]+ "&RoomNo=" + pmsData[2]+ "&GuestNo=" + pmsData[3]);
            String text = null;

            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
                jObj = new JSONObject(text);

            } catch (Exception e) {
                e.printStackTrace();
            }
            return jObj;
        }


        protected void onPostExecute(JSONObject jObj)
        {


        }
    }


    private  class funLoadFolioMaster extends AsyncTask<Void, Void, List>
    {
        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException
        {
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
        protected List doInBackground(Void... params)
        {

            List<clsGuestRoomDtl> listOfGuestRoomDtl = new ArrayList<>();
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();

            if(!clsGlobalFunctions.gAPOSWebSrviceURL.contains("prjSanguineWebService/APOSIntegration"))
            {
                clsGlobalFunctions.gAPOSWebSrviceURL=clsGlobalFunctions.gAPOSWebSrviceURL+"prjSanguineWebService/APOSIntegration";
            }

            String []sanguineURL=clsGlobalFunctions.gAPOSWebSrviceURL.split("APOSIntegration");

            System.out.println(sanguineURL[0]+"PMSIntegration/funGetFolioDetails?ClientCode="+clsGlobalFunctions.gClientCode);
            HttpGet httpGet = new HttpGet(sanguineURL[0]+"PMSIntegration/funGetFolioDetails?ClientCode="+clsGlobalFunctions.gClientCode);
            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
                JSONObject jObj = new JSONObject(text);
                JSONArray mJsonArray = (JSONArray) jObj.get("FolioDtls");
                JSONObject mJsonObject = new JSONObject();

                for (int i = 0; i < mJsonArray.length(); i++) {
                    mJsonObject = (JSONObject) mJsonArray.get(i);
                    if (mJsonObject.get("FolioNo").toString().equals("")) {
                        //memberInfo = "no data";
                    } else
                    {
                        clsGuestRoomDtl objGuestRoomDtl = new clsGuestRoomDtl();
                        mJsonObject = (JSONObject) mJsonArray.get(i);
                        objGuestRoomDtl.setStrFolioNo(mJsonObject.get("FolioNo").toString());
                        objGuestRoomDtl.setStrRoomNo(mJsonObject.get("RoomNo").toString());
                        objGuestRoomDtl.setStrRoomDesc(mJsonObject.get("RoomDesc").toString());
                        objGuestRoomDtl.setStrGuestName(mJsonObject.get("GuestName").toString());
                        objGuestRoomDtl.setStrGuestCode(mJsonObject.get("GuestCode").toString());
                        listOfGuestRoomDtl.add(objGuestRoomDtl);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return listOfGuestRoomDtl;
        }

        protected void onPostExecute(List arrListTemp) {

            if (arrListTemp.size()>0)
            {
                listOfGuestRoomDtl.clear();
                for(int cnt=0;cnt<arrListTemp.size();cnt++)
                {
                    clsGuestRoomDtl objGuestMaster=(clsGuestRoomDtl)arrListTemp.get(cnt);
                    listOfGuestRoomDtl.add(objGuestMaster);
                }
                funFillGuestSearchDailog(arrListTemp);
            }
        }
    }




    private void funFillGuestSearchDailog(List<clsGuestRoomDtl> arrList)
    {
        dialog = new Dialog(clsBillSettlement.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.pmsguestlistdialog);
        final EditText searchMember=(EditText)dialog.findViewById(R.id.edtSearchGuest);
        dialoglist = (GridView) dialog.findViewById(R.id.gvGuestListview);
        funFillGuestDialog(arrList);

        searchMember.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.toString()!=null&&listOfGuestRoomDtl!=null){
                    ArrayList arrayListtemp = new ArrayList();
                    for (int cnt = 0; cnt <listOfGuestRoomDtl.size(); cnt++)
                    {
                        clsGuestRoomDtl obj = (clsGuestRoomDtl) listOfGuestRoomDtl.get(cnt);
                        if (obj.getStrGuestName().toLowerCase().contains(s.toString().toLowerCase()))
                        {
                            arrayListtemp.add(listOfGuestRoomDtl.get(cnt));
                        }

                    }
                    funFillGuestDialog(arrayListtemp);
                }

            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(800, 750);

    }



    private void funFillGuestDialog(List<clsGuestRoomDtl> arrList)
    {
        final List<clsGuestRoomDtl> arrListTempGuestInfo=arrList;
        ArrayList<String> arrListGuestInfo=new ArrayList<String>();
        for(int cnt=0;cnt<arrList.size();cnt++)
        {
            clsGuestRoomDtl objGuestMaster=arrList.get(cnt);
            arrListGuestInfo.add(objGuestMaster.getStrGuestCode()+"#"+objGuestMaster.getStrGuestName()
                    +"#"+objGuestMaster.getStrRoomNo()+"#"+objGuestMaster.getStrRoomDesc());
        }

        clsGuestDialogAdapter objGusetAdapter = new clsGuestDialogAdapter(clsBillSettlement.this, arrListGuestInfo);
        dialoglist.setAdapter(objGusetAdapter);
        dialoglist.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                clsGuestRoomDtl objGuestDtl = (clsGuestRoomDtl) arrListTempGuestInfo.get(position);
                textViewBillAmt.setText("Guest Name");
                txtBillAmt.setText(objGuestDtl.getStrGuestName());
                textViewPaidAmt.setText("Folio No");
                edtPaidAmount.setText(objGuestDtl.getStrFolioNo());
                edtPaidAmount.setSelection(edtPaidAmount.getText().length());
                edtPaidAmount.setGravity(Gravity.CENTER);
                edtBalanceRemark.setGravity(Gravity.CENTER);
                txtMemberBalance.setText("Room No");
                txtShowMemberBalanace.setText(objGuestDtl.getStrRoomNo());
                textViewBalRmk.setText("Guest Code");
                edtBalanceRemark.setText(objGuestDtl.getStrGuestCode());


                onResume();
                dialog.dismiss();
            }
        });
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

  /*  public void onActivityResult(int requestCode, int resultCode, Intent intent)
    {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = intent.getStringExtra("SCAN_RESULT");
                String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
                if(alreadyCardAssigned.equals("Y"))
                {
                    new ValidateCardForSettleWS().execute(cardNo, "CashCard");
                }
                else
                {
                    debitCardString=contents;
                    new DebitCardMemberWS().execute(contents, "CashCard");
                }

            }
        }
    }
    */





    private void funGetSelectedBillDetails()
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
                            try {
                                clsBillListDtl objBill=null;
                                for(int cnt=0;cnt<arrListTemp.size();cnt++)
                                {
                                    objBill= arrListTemp.get(cnt);
                                }
                                if(objBill.getCardNo()!=null)
                                {
                                    if(!objBill.getCardNo().isEmpty())
                                    {
                                        alreadyCardAssigned="Y";
                                    }
                                }
                                funSetList(objBill);
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
                SnackBarUtils.showSnackBar(clsBillSettlement.this, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsBillSettlement.this, R.string.setup_your_server_settings);
        }
    }



    private void funCalculatePromotion() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                showDialog();
                JsonObject objPromoItemDtl=new JsonObject();
                JsonArray arrPromoItems = new JsonArray();
                for(int cnt=0;cnt<listDirectBillSelectedItems.size();cnt++)
                {
                    clsDirectBillSelectedListItemBean objDirectBillItem=(clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(cnt);
                    customerCode = objDirectBillItem.getStrCustomerCode();
                    customerName = objDirectBillItem.getStrCustomerName();
                    operationType = objDirectBillItem.getStrOperationType();
                    if(operationType.equals("TakeAway"))
                    {
                        operationTypeForTax="TakeAway";
                    }
                    else if(operationType.equals("DirectBiller"))
                    {
                        operationTypeForTax="DineIn";
                    }
                    else
                    {
                        operationTypeForTax="HomeDelivery";
                    }
                    JsonObject objRows=new JsonObject();

                    objRows.addProperty("itemCode",objDirectBillItem.getStrItemCode());
                    objRows.addProperty("itemName",objDirectBillItem.getStrDesc());
                    objRows.addProperty("quantity",objDirectBillItem.getQty());
                    objRows.addProperty("amount",objDirectBillItem.getAmount());
                    arrPromoItems.add(objRows);
                }


                objPromoItemDtl.add("PromotionDtl", arrPromoItems);
                objPromoItemDtl.addProperty("POSCode",clsGlobalFunctions.gPOSCode);
                objPromoItemDtl.addProperty("TableNo", "");
                objPromoItemDtl.addProperty("POSDate", clsGlobalFunctions.gPOSDate);


                App.getAPIHelper().funCalculatePromotion(objPromoItemDtl, new BaseAPIHelper.OnRequestComplete<HashMap>() {
                    @Override
                    public void onSuccess(HashMap map) {
                        dismissDialog();
                        if (null != map)
                        {
                            try
                            {
                                String result ="";
                                HashMap<String,String> hmBill= map;
                                for (Map.Entry<String, String> entry : hmBill.entrySet())
                                {
                                    result=entry.getValue();
                                }

                                String[]items=result.split("!");
                                for(int cnt=0;cnt<items.length;cnt++)
                                {
                                    System.out.println("Items:-" + items[cnt]);
                                    String[]subItems=items[cnt].split("#");
                                    if(!subItems[4].isEmpty())
                                    {
                                        clsPromotionItems objPromoItemDtls=new clsPromotionItems();
                                        objPromoItemDtls.setItemCode(subItems[0]);
                                        objPromoItemDtls.setPromoCode(subItems[4]);
                                        objPromoItemDtls.setPromoType(subItems[5]);
                                        objPromoItemDtls.setBillAmt(subItems[2]);
                                        objPromoItemDtls.setFreeItemQty(Double.valueOf(subItems[7]));
                                        objPromoItemDtls.setDiscAmt(Double.valueOf(subItems[8]));
                                        objPromoItemDtls.setDiscPer(Double.valueOf(subItems[9]));
                                        objPromoItemDtls.setDiscOnAmt(Double.valueOf(subItems[2]));
                                        objPromoItemDtls.setItemName(subItems[1]);
                                        listPromotionItems.add(objPromoItemDtls);
                                    }

                                }
                                if(listPromotionItems.size()>0)
                                {
                                    final String finalResult = result;
                                    if(clsGlobalFunctions.gPopUpToApplyPromotionsOnBill.equals("Y"))
                                    {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(clsBillSettlement.this);
                                        builder1.setMessage("Do want to Calculate Promotions for this Bill??");
                                        builder1.setCancelable(true);

                                        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                                                {
                                                    public void onClick(DialogInterface dialog, int id)
                                                    {
                                                        funGetReasonList();
                                                        listDirectBillSelectedItems.clear();
                                                        String[]items= finalResult.split("!");
                                                        for(int cnt=0;cnt<items.length;cnt++)
                                                        {
                                                            System.out.println("Items:-" + items[cnt]);
                                                            String[]subItems=items[cnt].split("#");
                                                            String[] arr=subItems[3].split("\\.");
                                                            double amt=0,disAmt=0;
                                                            amt =Double.parseDouble(subItems[2]);
                                                            if(subItems[5].equalsIgnoreCase("Discount"))
                                                            {
                                                                totalDisAmt+=Double.parseDouble(subItems[8]);
                                                            }
                                                            clsDirectBillSelectedListItemBean objDb= new clsDirectBillSelectedListItemBean(subItems[0], subItems[1],Integer.parseInt(arr[0]), amt, amt, true,customerCode,custTyp,operationType,"N");
                                                            listDirectBillSelectedItems.add(objDb);
                                                        }


                                                        for (int cnt = 0; cnt < listDirectBillSelectedItems.size(); cnt++)
                                                        {
                                                            obj = (clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(0);
                                                            customerCode = obj.getStrCustomerCode();
                                                            customerName = obj.getStrCustomerName();
                                                            operationType = obj.getStrOperationType();
                                                            if(operationType.equals("TakeAway"))
                                                            {
                                                                operationTypeForTax="TakeAway";
                                                            }
                                                            else if(operationType.equals("DirectBiller"))
                                                            {
                                                                operationTypeForTax="DineIn";
                                                            }
                                                            else
                                                            {
                                                                operationTypeForTax="HomeDelivery";
                                                            }
                                                            custTyp=obj.getStrCustomerType();
                                                        }

                                                        funCalculateTax();

                                                    }
                                                }
                                        );

                                        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id)
                                                    {
                                                        dialog.cancel();
                                                        funCalculateTax();
                                                    }
                                                }
                                        );
                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();
                                    }
                                    else
                                    {
                                        funGetReasonList();
                                        listDirectBillSelectedItems.clear();
                                        items= finalResult.split("!");
                                        for(int cnt=0;cnt<items.length;cnt++)
                                        {
                                            System.out.println("Items:-" + items[cnt]);
                                            String[]subItems=items[cnt].split("#");
                                            String[] arr=subItems[3].split("\\.");
                                            double amt=0,disAmt=0;
                                            amt =Double.parseDouble(subItems[2]);
                                            if(subItems[5].equalsIgnoreCase("Discount"))
                                            {
                                                totalDisAmt+=Double.parseDouble(subItems[8]);
                                            }
                                            clsDirectBillSelectedListItemBean objDb= new clsDirectBillSelectedListItemBean(subItems[0], subItems[1],Integer.parseInt(arr[0]), amt, amt, true,customerCode,custTyp,operationType,"N");
                                            listDirectBillSelectedItems.add(objDb);
                                        }


                                        for (int cnt = 0; cnt < listDirectBillSelectedItems.size(); cnt++)
                                        {
                                            obj = (clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(0);
                                            customerCode = obj.getStrCustomerCode();
                                            customerName = obj.getStrCustomerName();
                                            operationType = obj.getStrOperationType();
                                            if(operationType.equals("TakeAway"))
                                            {
                                                operationTypeForTax="TakeAway";
                                            }
                                            else if(operationType.equals("DirectBiller"))
                                            {
                                                operationTypeForTax="DineIn";
                                            }
                                            else
                                            {
                                                operationTypeForTax="HomeDelivery";
                                            }
                                            custTyp=obj.getStrCustomerType();
                                        }

                                        funCalculateTax();
                                    }
                                }
                                else
                                {

                                    for(int cnt=0;cnt<items.length;cnt++)
                                    {
                                        System.out.println("Items:-" + items[cnt]);
                                        String[]subItems=items[cnt].split("#");
                                        clsItemDtl obItemDtl = new clsItemDtl();
                                        obItemDtl.setStrItemName(subItems[1]);
                                        obItemDtl.setStrAmount(subItems[2]);
                                        obItemDtl.setStrQty(subItems[3]);
                                        arrListItemDtl.add(obItemDtl);

                                        if(arrListItemDtl.size()>0)
                                        {
                                            funSetListFromDirectBiller(arrListItemDtl);
                                        }

                                    }

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



    private void funTaxCalculateWs() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                JsonObject objTaxDtl = new JsonObject();
                JsonArray arrTaxDtl = new JsonArray();
                try {
                    for(int cnt=0;cnt<listDirectBillSelectedItems.size();cnt++)
                    {
                        clsDirectBillSelectedListItemBean objDirectBillItem=(clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(cnt);
                        JsonObject objRows=new JsonObject();

                        objRows.addProperty("strPOSCode",clsGlobalFunctions.gPOSCode);
                        objRows.addProperty("strItemCode",objDirectBillItem.getStrItemCode());
                        objRows.addProperty("strItemName",objDirectBillItem.getStrDesc());
                        objRows.addProperty("dblItemQuantity",objDirectBillItem.getQty());
                        objRows.addProperty("dblAmount",objDirectBillItem.getAmount());
                        objRows.addProperty("strClientCode",clsGlobalFunctions.gClientCode);
                        objRows.addProperty("OperationType",operationTypeForTax);//operationTypeFor Tax
                        objRows.addProperty("AreaCode",clsGlobalFunctions.gDirectBillerAreaCode);
                        String dateTime=clsGlobalFunctions.gPOSDate;
                        String posDate=dateTime.replaceAll(" ","%20");
                        objRows.addProperty("POSDate",posDate);

                        arrTaxDtl.add(objRows);
                    }

                    objTaxDtl.add("TaxDtl", arrTaxDtl);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                showDialog();
                App.getAPIHelper().funTaxCalculate(objTaxDtl, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        dismissDialog();
                        if (null != jObj) {
                            try {

                                String taxAmount = jObj.get("totalTaxAmt").getAsString().toString();
                                double totalTaxAmt=0;
                                if(taxAmount.equalsIgnoreCase(""))
                                {
                                    textTaxAmt.setText("0.0");
                                }
                                else
                                {
                                    totalTaxAmt= Double.parseDouble(taxAmount);
                                    String amount= String.valueOf(Math.rint(totalTaxAmt));
                                    textTaxAmt.setText(amount);
                                }

                                double subtotal=Double.parseDouble(textSubTotal.getText().toString());
                                double disAmt=Double.parseDouble(textDisAmt.getText().toString());
                                double gTotal=Math.rint(subtotal + totalTaxAmt-disAmt);
                                textGrandTotal.setText(String.valueOf(gTotal));
                                txtBalance.setText(String.valueOf(gTotal));
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

/*

    private void funTaxCalculateForBillSeriesItem(final List<clsDirectBillSelectedListItemBean> listBillSeriesWiseItem) {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                JsonObject objTaxDtl = new JsonObject();
                JsonArray arrTaxDtl = new JsonArray();
                try {
                    for(int cnt=0;cnt<listBillSeriesWiseItem.size();cnt++)
                    {
                        clsDirectBillSelectedListItemBean objDirectBillItem=(clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(cnt);
                        JsonObject objRows=new JsonObject();

                        objRows.addProperty("strPOSCode",clsGlobalFunctions.gPOSCode);
                        objRows.addProperty("strItemCode",objDirectBillItem.getStrItemCode());
                        objRows.addProperty("strItemName",objDirectBillItem.getStrDesc());
                        objRows.addProperty("dblItemQuantity",objDirectBillItem.getQty());
                        objRows.addProperty("dblAmount",objDirectBillItem.getAmount());
                        objRows.addProperty("strClientCode",clsGlobalFunctions.gClientCode);
                        objRows.addProperty("OperationType",operationTypeForTax);//operationTypeFor Tax
                        objRows.addProperty("AreaCode",clsGlobalFunctions.gDirectBillerAreaCode);
                        String dateTime=clsGlobalFunctions.gPOSDate;
                        String posDate=dateTime.replaceAll(" ","%20");
                        objRows.addProperty("POSDate",posDate);

                        arrTaxDtl.add(objRows);
                    }

                    objTaxDtl.add("TaxDtl", arrTaxDtl);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                showDialog();
                App.getAPIHelper().funTaxCalculate(objTaxDtl, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        dismissDialog();
                        if (null != jObj) {
                            try {

                                String taxAmount = jObj.get("totalTaxAmt").getAsString().toString();
                                double totalTaxAmt=0;
                                if(taxAmount.equalsIgnoreCase(""))
                                {
                                    textTaxAmt.setText("0.0");
                                }
                                else
                                {
                                    totalTaxAmt= Double.parseDouble(taxAmount);
                                    String amount= String.valueOf(Math.rint(totalTaxAmt));
                                    textTaxAmt.setText(amount);
                                }

                                double subtotal=Double.parseDouble(textSubTotal.getText().toString());
                                double disAmt=Double.parseDouble(textDisAmt.getText().toString());
                                double gTotal=Math.rint(subtotal + totalTaxAmt-disAmt);
                                textGrandTotal.setText(String.valueOf(gTotal));
                                txtBalance.setText(String.valueOf(gTotal));

                                funInsertBillHd(billNo, customerCode);
                                funInsertBillModifierDtl(billNo);
                                //this call here bcoz of tax webservice data load is completed
                                //funInsertBillHd(billNo, customerCode);
                                //funInsertBillModifierDtl(billNo);


                                if(type.equalsIgnoreCase("Settle"))
                                {
                                    funInsertBillSettlementDtl();
                                }

                                //  new funInsertBillTaxDtlWS().execute(billNo);
                                funInsertBillTaxDtl(billNo);
                                if(clsGlobalFunctions.gActivePromotions.equals("Y"))
                                {
                                    if(listPromotionItems.size()>0)
                                    {
                                        funInsertPromotionDtl(billNo);
                                    }
                                }
                                funInsertBillDtl(billNo, cardNo,listBillSeriesWiseItem);
                                if(hmBillSettlementDtl.size()>0)
                                {
                                    if(hmBillSettlementDtl.containsKey("Complementary"))
                                    {
                                        funInsertComplementoryDtl();
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
                SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
        }
    }

*/



    public void funGenerateBillNo(String POSCode,String cardNo) {
        if (ConnectivityHelper.isConnected()) {
            App.getAPIHelper().funGenerateBillNo(POSCode,new BaseAPIHelper.OnRequestComplete<HashMap>() {
                @Override
                public void onSuccess(HashMap map) {
                    try
                    {
                        if (null != map)
                        {
                            HashMap<String,String> hmBill= map;
                            String cardNo ="";
                            for (Map.Entry<String, String> entry : hmBill.entrySet())
                            {
                                billNo=entry.getValue();
                            }

                            Toast.makeText(clsBillSettlement.this, "BillNo=" + billNo, Toast.LENGTH_LONG).show();
                            funSaveBill();
                            //funInsertBillHd(billNo, customerCode);
                            //  funInsertBillModifierDtl(billNo);
/*
                            if(type.equalsIgnoreCase("Settle"))
                            {
                              //  funInsertBillSettlementDtl();
                            }
                            //  new funInsertBillTaxDtlWS().execute(billNo);
                            //funInsertBillTaxDtl(billNo);
                            if(clsGlobalFunctions.gActivePromotions.equals("Y"))
                            {
                                if(listPromotionItems.size()>0)
                                {
                              //      funInsertPromotionDtl(billNo);
                                }
                            }
                            //funInsertBillDtl(billNo, cardNo,listDirectBillSelectedItems);
                            if(hmBillSettlementDtl.size()>0)
                            {
                                if(hmBillSettlementDtl.containsKey("Complementary"))
                                {
                                    //funInsertComplementoryDtl();
                                }
                            }*/
                        }
                        else
                        {
                            Toast.makeText(mActivity, "Error!!", Toast.LENGTH_LONG).show();
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



    private JsonArray funInsertBillTaxDtl() {
        JsonArray arrTaxDtl = new JsonArray();
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                showDialog();

                JsonObject objTaxDtl=new JsonObject();

                for(int cnt=0;cnt<listDirectBillSelectedItems.size();cnt++)
                {
                    clsDirectBillSelectedListItemBean objDirectBillItem=(clsDirectBillSelectedListItemBean) listDirectBillSelectedItems.get(cnt);
                    JsonObject objRows=new JsonObject();

                    objRows.addProperty("BillNo", billNo);
                    objRows.addProperty("POSCode",clsGlobalFunctions.gPOSCode);
                    objRows.addProperty("ItemCode",objDirectBillItem.getStrItemCode());
                    objRows.addProperty("ItemName",objDirectBillItem.getStrDesc());
                    objRows.addProperty("Quantity",objDirectBillItem.getQty());
                    objRows.addProperty("Amount",objDirectBillItem.getAmount());
                    objRows.addProperty("ClientCode",clsGlobalFunctions.gClientCode);
                    objRows.addProperty("BillDate", clsGlobalFunctions.funGetPOSDateTime());
                    arrTaxDtl.add(objRows);

                }

            } else {
                SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
        }
        return  arrTaxDtl;
    }


    private void funSaveBill() {
        JsonObject jObjBillData=new JsonObject();

        if(formName.equalsIgnoreCase("Make Bill"))
        {
            jObjBillData.add("BillHDData", funInsertBillHd());
            jObjBillData.add("BillSettlementData", funInsertBillSettlementDtl());
            if(hmBillSettlementDtl.size()>0)
            {
                if(hmBillSettlementDtl.containsKey("Complementary"))
                {
                    jObjBillData.add("BillComplementaryData", funInsertComplementoryDtl());
                }
            }

        }
        else{
            jObjBillData.add("BillHDData", funInsertBillHd(billNo,customerCode));
            jObjBillData.add("BillModifierData", funInsertBillModifierDtl(billNo));
            if(type.equalsIgnoreCase("Settle"))
            {
                jObjBillData.add("BillSettlementData", funInsertBillSettlementDtl());
            }
            jObjBillData.add("BillTaxData", funInsertBillTaxDtl());

            if(clsGlobalFunctions.gActivePromotions.equals("Y"))
            {
                if(listPromotionItems.size()>0)
                {
                    jObjBillData.add("BillPromoDiscountData", funInsertPromotionDtl(billNo));
                }
            }
            jObjBillData.add("BillDtlData", funInsertBillDtl(billNo,customerCode));

            if(hmBillSettlementDtl.size()>0)
            {
                if(hmBillSettlementDtl.containsKey("Complementary"))
                {
                    jObjBillData.add("BillComplementaryData", funInsertComplementoryDtl());

                }
            }
        }



        //JsonArray jsonArray=;

        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                showDialog();
                App.getAPIHelper().funSaveAllBillData(jObjBillData,clsGlobalFunctions.gEnableBillSeries,clsGlobalFunctions.gPOSCode, new BaseAPIHelper.OnRequestComplete<HashMap>() {
                    @Override
                    public void onSuccess(HashMap mapResponse) {
                        dismissDialog();
                        if (null != mapResponse)
                        {
                            try
                            {
                                List listResponse=(List)mapResponse.get("response");
                                String response="";
                               // intNoOfBills=listResponse.size();

                                for(int i=0;i<listResponse.size();i++){
                                    LinkedTreeMap<String,String> hmBill= (LinkedTreeMap<String, String>) listResponse.get(i);;
                                    String cardNo ="";
                                    for (Map.Entry<String, String> entry : hmBill.entrySet())
                                    {
                                        if(formName.equalsIgnoreCase("Make Bill")){
                                            intNoOfBills=1;
                                        }else{
                                            if(entry.getKey().equals("BillHDData")){
                                                intNoOfBills++;
                                            }
                                        }

                                        if(entry.getKey().equals("NoBillSeries")){
                                            Toast.makeText(mActivity, "Please Create Bill Series", Toast.LENGTH_SHORT).show();
                                            return ;
                                        }
                                        if(entry.getKey().equals("GenaratedBillSeriesNo")){
                                            billNo=entry.getValue();
                                            Toast.makeText(mActivity, "BillNo=" + billNo, Toast.LENGTH_LONG).show();
                                        }

                                        response=entry.getValue();
                                        if(formName.equalsIgnoreCase("Make Bill"))
                                        {
                                            if(hmBillSettlementDtl.size()>0)
                                            {
                                                if(hmBillSettlementDtl.containsKey("Complementary"))
                                                {
                                                    if (response.equals("BillComplementory"))
                                                    {
                                                        funGenerateBillTextFile(billNo);
                                                        System.out.println(response);
                                                        Toast.makeText(clsBillSettlement.this, "Settled successfully..", Toast.LENGTH_SHORT).show();
                                                        //finish();

                                                    }
                                                }
                                                else
                                                {
                                                    if (response.equals("BillSettlement"))
                                                    {
                                                        funGenerateBillTextFile(billNo);
                                                        System.out.println(response);
                                                        Toast.makeText(clsBillSettlement.this, "Settled successfully..", Toast.LENGTH_SHORT).show();
                                                       // finish();
                                                    }
                                                }
                                            }
                                        }
                                        else
                                        {
                                            System.out.println("RES= "+response);
                                            if(type.equals("Print"))
                                            {
                                                if (response.equals("BillDtl"))
                                                {
                                                    funGenerateBillTextFile(billNo);
                                                    listDirectBillSelectedItems.clear();
                                                    arrListItemDtl.clear();

                                                    System.out.println(response);
                                                    Toast.makeText(clsBillSettlement.this, "Billed Successfully..", Toast.LENGTH_SHORT).show();
                                                   //finish();
                                                }

                                            }
                                            else
                                            {
                                                if(hmBillSettlementDtl.size()>0)
                                                {
                                                    if(hmBillSettlementDtl.containsKey("Complementary"))
                                                    {
                                                        if (response.equals("BillComplementory"))
                                                        {
                                                            funGenerateBillTextFile(billNo);
                                                            listDirectBillSelectedItems.clear();
                                                            arrListItemDtl.clear();
                                                            Toast.makeText(clsBillSettlement.this, "Billed Successfully..", Toast.LENGTH_SHORT).show();
                                                        }
                                                        System.out.println(response);

                                                        //finish();

                                                    }
                                                    else
                                                    {
                                                        if (response.equals("BillDtl"))
                                                        {
                                                            funGenerateBillTextFile(billNo);
                                                            listDirectBillSelectedItems.clear();
                                                            arrListItemDtl.clear();
                                                           // finish();
                                                            Toast.makeText(clsBillSettlement.this, "Billed Successfully..", Toast.LENGTH_SHORT).show();
                                                        }
                                                        System.out.println(response);
                                                       // finish();
                                                    }
                                                }
                                            }
                                        }
                                    }
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


    public void funGenerateBillTextFileWebService(String billNumber) {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                String reprint="N";
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
                                if(formName.equalsIgnoreCase("Make Bill")){
                                    finish();
                                }
                                intNoOfBills--;
                                if(intNoOfBills==0) {
                                    finish();
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



    private void funGenerateAndPrintTextfile(final String bill)
    {

        AlertDialog.Builder builder1 = new AlertDialog.Builder(clsBillSettlement.this);
        builder1.setMessage("Do You Want To Print Bill???");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        if(clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("External Printer"))// External Printer Type
                        {
                            funGenerateBillTextFileWebService(bill);
                        }
                        else
                        {
                            funPrintBillDataFromWS(bill);
                        }
                        /**/
                    }
                }
        );
        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        dialog.cancel();
                        finish();

                    }
                }
        );
        AlertDialog alert11 = builder1.create();
        alert11.show();



    }



    public void funGenerateDirectBillerKOTTextFileWebService(String billNumber) {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                String reprint="N";
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


    private void funGetCustomerList(final String settlementType, final String settlementName)
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetCustomerList("Customer",clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsCustomerMaster>>() {
                    @Override
                    public void onSuccess(ArrayList<clsCustomerMaster> arrListTemp) {
                        dismissDialog();
                        if (null != arrListTemp) {
                            try
                            {
                                if (arrListTemp.size()>0)
                                {

                                    if(arrListTemp.size()==1)
                                    {
                                        for(int cnt=0;cnt<arrListTemp.size();cnt++)
                                        {
                                            clsCustomerMaster objMaster=arrListTemp.get(cnt);
                                            if(objMaster.getCustomerCode().equals("No Customer Found"))
                                            {
                                                funFillCustomerSearchDailog(new ArrayList<clsCustomerMaster>(),settlementType,settlementName);
                                            }
                                            else
                                            {
                                                arrListCustomerMaster=arrListTemp;
                                                funFillCustomerSearchDailog(arrListTemp,settlementType,settlementName);
                                            }

                                        }
                                    }
                                    else
                                    {
                                        arrListCustomerMaster=arrListTemp;
                                        funFillCustomerSearchDailog(arrListTemp,settlementType,settlementName);
                                    }
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
                SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
        }
    }


    private void funFillCustomerSearchDailog(final List<clsCustomerMaster> arrList, final String settlementType, final String settlementName)
    {
        CustomSearchView customSearchView;
        dialog = new Dialog(clsBillSettlement.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.customersearchmaindialogform);
        customSearchView=(CustomSearchView) dialog.findViewById(R.id.edtSearchCustomer);
        final EditText searchTable= customSearchView.getEditText();
        dialoglist = (GridView) dialog.findViewById(R.id.gvcustomersearchdialoglistview);

        searchTable.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                ArrayList arrayListtemp = new ArrayList();
                for (int cnt = 0; cnt <arrListCustomerMaster.size(); cnt++)
                {
                    clsCustomerMaster objCustomer = (clsCustomerMaster) arrListCustomerMaster.get(cnt);
                    if (objCustomer.getCustomerName().toLowerCase().contains(s.toString().toLowerCase()))
                    {
                        arrayListtemp.add(arrListCustomerMaster.get(cnt));
                    }
                }
                funFillCustomerListDialog(arrayListtemp,settlementType,settlementName);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        dialog.show();
        Window window = dialog.getWindow();
        //window.setLayout(500, 750);
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.getWindowManager().getDefaultDisplay();

        funFillCustomerListDialog(arrList,settlementType,settlementName);
    }

    private void funFillCustomerListDialog(List<clsCustomerMaster> arrList, final String settlementType, final String settlementName )
    {
        final List<clsCustomerMaster> arrListTempTableInfo=arrList;
        ArrayList<String> arrListCustomerInfo=new ArrayList<String>();
        for(int cnt=0;cnt<arrList.size();cnt++)
        {
            clsCustomerMaster objCustomerMaster=arrList.get(cnt);
            arrListCustomerInfo.add(objCustomerMaster.getCustomerCode()+"#"+objCustomerMaster.getCustomerName()
                    +"#"+objCustomerMaster.getCustomerMobileNo());
        }

        clsUnReservedTableListDialogAdapter objOrderAdapter = new clsUnReservedTableListDialogAdapter(clsBillSettlement.this, arrListCustomerInfo);
        dialoglist.setAdapter(objOrderAdapter);
        dialoglist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                clsCustomerMaster objCustomerMaster = (clsCustomerMaster) arrListTempTableInfo.get(position);
                dialog.dismiss();
                customerDtlsForCredit=objCustomerMaster.getCustomerCode()+"#"+objCustomerMaster.getCustomerName();
                funOpenDialogForCreditSettlement(settlementType,settlementName);
                onResume();
                dialog.dismiss();
            }
        });
    }



    public void funGetReasonList(final String settlementName, final String settlementType) {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetReasonList("strComplementary", clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsReasonMaster>>()
                {
                    @Override
                    public void onSuccess(ArrayList<clsReasonMaster> arrListTemp)
                    {
                        dismissDialog();
                        if (null != arrListTemp) {
                            if (arrListTemp.size() > 0)
                            {
                                hmReason=new TreeMap();
                                reasonList=new ArrayList<String>();
                                for(int cnt=0;cnt<arrListTemp.size();cnt++)
                                {
                                    clsReasonMaster objReason=(clsReasonMaster)arrListTemp.get(cnt);
                                    hmReason.put(objReason.getStrReasonName(), objReason.getStrReasonCode());
                                }

                                Set setReason=hmReason.keySet();
                                Iterator itrReason=setReason.iterator();

                                while(itrReason.hasNext())
                                {
                                    reasonName= (String) itrReason.next();
                                    reasonList.add(reasonName);
                                }

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(clsBillSettlement.this);
                                builder1.setMessage("Do You Want to add Reason and Remark???");
                                builder1.setCancelable(true);
                                builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                funSelectReasonAndRemark(settlementName,settlementType);
                                            }
                                        }
                                );

                                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                dialog.cancel();
                                            }
                                        }
                                );
                                AlertDialog alert11 = builder1.create();
                                alert11.show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage, int errorCode)
                    {
                        dismissDialog();
                    }
                });
            } else {
                SnackBarUtils.showSnackBar(clsBillSettlement.this, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsBillSettlement.this, R.string.setup_your_server_settings);
        }
    }

    private void funSelectReasonAndRemark(final String settlementName, final String settlementType)
    {
        final Dialog dialog = new Dialog(clsBillSettlement.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogforreasonremark);
        final Spinner spinnerReason = (Spinner) dialog.findViewById(R.id.reasonSpinner);
        final EditText edtRemark= (EditText) dialog.findViewById(R.id.edtKotRemarks);
        TextView textPayMode = (TextView) dialog.findViewById(R.id.txtBillPayMode);
        final Button btnOk=(Button) dialog.findViewById(R.id.btnOk) ;
        final Button btnClose=(Button) dialog.findViewById(R.id.btnClose) ;
        textPayMode.setText(settlementName);
        CommonUtils.showKeyboard(edtRemark,true);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (clsBillSettlement.this, R.layout.spinneritemtextview,reasonList);

        dataAdapter.setDropDownViewResource
                (R.layout.spinnerdropdownitem);
        spinnerReason.setAdapter(dataAdapter);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                try{
                    reasonCode = String.valueOf(hmReason.get(spinnerReason.getSelectedItem().toString()));
                    if(edtRemark.getText().toString().isEmpty())
                    {
                        Toast.makeText(clsBillSettlement.this,"Remark should not be empty!!",Toast.LENGTH_LONG).show();
                        return;
                    }
                    else
                    {
                        remark=new clsUtility().funCheckSpecialCharacters(edtRemark.getText().toString());
                        paidAmt = 0.0;
                        bal = 0.0;
                        balance=0;

                        double actualComplimentoryAmt=Double.parseDouble(txtBalance.getText().toString())-Double.parseDouble(textTaxAmt.getText().toString());

                        textSubTotal.setText(String.valueOf(actualComplimentoryAmt));
                        textGrandTotal.setText(String.valueOf(actualComplimentoryAmt));
                        txtBalance.setText(String.valueOf(actualComplimentoryAmt));
                        textTaxAmt.setText("0.0");

                        objSettleDtl = (clsSettlementDtl) hmSettleDtl.get(settlementName);
                        clsSettlementOption objBillSettlementDtl = new clsSettlementOption();
                        objBillSettlementDtl.setStrSettelmentType(settlementType);
                        objBillSettlementDtl.setStrSettelmentCode(objSettleDtl.getStrSettlementCode());
                        objBillSettlementDtl.setDblSettlementAmt(paidAmt);
                        objBillSettlementDtl.setDblPaidAmt(paidAmt);
                        objBillSettlementDtl.setStrExpiryDate("");
                        objBillSettlementDtl.setStrSettelmentDesc(objSettleDtl.getStrSettlementName());
                        objBillSettlementDtl.setStrCardName("");
                        objBillSettlementDtl.setStrRemark(remark);
                        objBillSettlementDtl.setDblConvertionRatio(0.0);
                        objBillSettlementDtl.setDblActualAmt(actualComplimentoryAmt);
                        objBillSettlementDtl.setDblRefundAmt(0.0);
                        objBillSettlementDtl.setStrGiftVoucherCode("");
                        objBillSettlementDtl.setStrClientCode(clsGlobalFunctions.gClientCode);
                        objBillSettlementDtl.setStrDataPostFlag("N");
                        objBillSettlementDtl.setStrCustomerCode("");
                        objBillSettlementDtl.setStrCardNo("");


                        hmBillSettlementDtl.put(settlementType, objBillSettlementDtl);
                        updateField(balance);
                        funGetSettleListData();
                    }
                    dialog.dismiss();

                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
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





    private void funOpenDialogForSelectedSettleMode(final String settlementType, final String settlementName)
    {

        LinearLayout linearBillAmt,linearPaidAmt,linearRemark,linearSlip,linearSlipExpiryDt;
        if(settlementType.equals("Cash"))
        {
            dialog = new Dialog(clsBillSettlement.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialogforselectedsettlemode);
            textPayMode = (TextView) dialog.findViewById(R.id.txtBillPayMode);
            textViewBalRmk=(TextView) dialog.findViewById(R.id.textBalanceRemark);
            edtBalanceRemark=(EditText) dialog.findViewById(R.id.edtBalRemark);
            textViewBillAmt=(TextView) dialog.findViewById(R.id.textViewBillAmt);
            txtBillAmt=(TextView) dialog.findViewById(R.id.txtBillAmt);
            textViewPaidAmt=(TextView) dialog.findViewById(R.id.textViewPaidAmt);
            edtPaidAmount=(EditText) dialog.findViewById(R.id.edtPaidAmt);
            Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
            Button btnOk= (Button) dialog.findViewById(R.id.btnOk);
            linearBillAmt=(LinearLayout)dialog.findViewById(R.id.linearBillAmt);
            linearPaidAmt=(LinearLayout)dialog.findViewById(R.id.linearPaidAmt);
            linearRemark=(LinearLayout)dialog.findViewById(R.id.linearRemark);
            linearSlip=(LinearLayout)dialog.findViewById(R.id.linearSlip);
            linearSlipExpiryDt=(LinearLayout)dialog.findViewById(R.id.linearSlipExpiryDt);

            CommonUtils.showKeyboard(edtPaidAmount,true);
            linearBillAmt.setVisibility(View.VISIBLE);
            linearPaidAmt.setVisibility(View.VISIBLE);
            linearRemark.setVisibility(View.VISIBLE);
            linearSlip.setVisibility(View.INVISIBLE);
            linearSlipExpiryDt.setVisibility(View.INVISIBLE);
            textViewBillAmt.setText("BillAmt:");
            textPayMode.setText(settlementName);
            txtBillAmt.setGravity(Gravity.RIGHT);
            txtBillAmt.setText(txtBalance.getText().toString());
            edtBalanceRemark.setVisibility(View.VISIBLE);
            edtBalanceRemark.setText("");

            edtPaidAmount.setText(txtBalance.getText().toString());
            edtPaidAmount.requestFocus();
            edtPaidAmount.setSelection(0,edtPaidAmount.getText().length());
            edtPaidAmount.setCursorVisible(true);
            edtPaidAmount.setSelectAllOnFocus(true);
            //edtPaidAmount.setFocusable(true);

            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    CommonUtils.hideKeyboard(edtPaidAmount);
                    dialog.dismiss();
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (Double.valueOf(edtPaidAmount.getText().toString()) > Double.valueOf(txtBalance.getText().toString()))
                    {
                        Toast.makeText(clsBillSettlement.this, "Enter Proper Amount!!!", Toast.LENGTH_LONG).show();
                        return;
                    }
                    String remark="";
                    try{
                        remark=new clsUtility().funCheckSpecialCharacters(edtBalanceRemark.getText().toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    funSetSetteledData(settlementType,settlementName,Double.valueOf(edtPaidAmount.getText().toString()),remark,"","");
                    CommonUtils.hideKeyboard(edtPaidAmount);
                    dialog.dismiss();
                }
            });

            dialog.show();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.getWindowManager().getDefaultDisplay();
        }
        if(settlementType.equals("Credit Card"))
        {
            dialog = new Dialog(clsBillSettlement.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialogforselectedsettlemode);
            textPayMode = (TextView) dialog.findViewById(R.id.txtBillPayMode);
            textViewBalRmk=(TextView) dialog.findViewById(R.id.textBalanceRemark);
            edtBalanceRemark=(EditText) dialog.findViewById(R.id.edtBalRemark);
            textViewBillAmt=(TextView) dialog.findViewById(R.id.textViewBillAmt);
            txtBillAmt=(TextView) dialog.findViewById(R.id.txtBillAmt);
            textViewPaidAmt=(TextView) dialog.findViewById(R.id.textViewPaidAmt);
            edtPaidAmount=(EditText) dialog.findViewById(R.id.edtPaidAmt);
            CommonUtils.showKeyboard(edtPaidAmount,true);
            final EditText edtSlipNo=(EditText) dialog.findViewById(R.id.edtSlipNo);
            final EditText edtSlipExpiryDt=(EditText) dialog.findViewById(R.id.edtSlipExpiryDt);
            ImageView imgViewDate=(ImageView) dialog.findViewById(R.id.imgViewCaledar);
            Button btnClose = (Button) dialog.findViewById(R.id.btnClose);
            Button btnOk= (Button) dialog.findViewById(R.id.btnOk);
            final Calendar calendar=Calendar.getInstance();
            String myFormat = "MM/yy";
            final SimpleDateFormat sdf= new SimpleDateFormat(myFormat, Locale.US);
            final DatePickerDialog.OnDateSetListener datePicker= new DatePickerDialog.OnDateSetListener()
            {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth)
                {
                    //calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, monthOfYear);
                    calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                    edtSlipExpiryDt.setText(sdf.format(calendar.getTime()));
                }

            };

            textViewBillAmt.setText("BillAmt:");
            textPayMode.setText(settlementName);
            txtBillAmt.setGravity(Gravity.RIGHT);
            txtBillAmt.setText(txtBalance.getText().toString());
            edtBalanceRemark.setVisibility(View.VISIBLE);
            edtBalanceRemark.setText("");
            edtPaidAmount.setText(txtBalance.getText().toString());
            edtPaidAmount.requestFocus();
            edtPaidAmount.setSelection(0,edtPaidAmount.getText().length());
            edtPaidAmount.setCursorVisible(true);
            edtPaidAmount.setSelectAllOnFocus(true);
            btnClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    CommonUtils.hideKeyboard(edtPaidAmount);
                    dialog.dismiss();
                }
            });

            btnOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {
                    if (Double.valueOf(edtPaidAmount.getText().toString()) > Double.valueOf(txtBalance.getText().toString()))
                    {
                        Toast.makeText(clsBillSettlement.this, "Enter Proper Amount!!!", Toast.LENGTH_LONG).show();
                        return;
                    }
//                    if (edtSlipNo.getText().toString().isEmpty())
//                    {
//                        Toast.makeText(clsBillSettlement.this, "Enter Credit Card No!!!", Toast.LENGTH_LONG).show();
//                        return;
//                    }
//                    if (edtSlipExpiryDt.getText().toString().isEmpty())
//                    {
//                        Toast.makeText(clsBillSettlement.this, "Enter Expiry Date!!!", Toast.LENGTH_LONG).show();
//                        return;
//                    }
                    String remark="";
                    try{
                        remark=new clsUtility().funCheckSpecialCharacters(edtBalanceRemark.getText().toString());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    funSetSetteledData(settlementType,settlementName,Double.valueOf(edtPaidAmount.getText().toString()),remark,edtSlipNo.getText().toString(),edtSlipExpiryDt.getText().toString());
                    CommonUtils.hideKeyboard(edtPaidAmount);
                    dialog.dismiss();
                }
            });

            imgViewDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    new DatePickerDialog(clsBillSettlement.this, datePicker, calendar
                            .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                            calendar.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

            dialog.show();
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);
            window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
            window.getWindowManager().getDefaultDisplay();
        }
        else if(settlementType.equals("Credit"))
        {
            funGetCustomerList(settlementType,settlementName);
        }
        else if(settlementType.equals("Complementary"))
        {
            if (hmBillSettlementDtl.size() > 0)
            {
                Toast.makeText(clsBillSettlement.this, "Coplimentary Settlement is Not Allowed In MultiSettlement!!!", Toast.LENGTH_LONG).show();
                return;
            }
            funGetReasonList(settlementName,settlementType);
        }

    }


    private void funOpenDialogForCreditSettlement(final String settlementType, final String settlementName)
    {
        childDialog = new Dialog(clsBillSettlement.this);
        childDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        childDialog.setContentView(R.layout.dialogforselectedsettlemode);
        textPayMode = (TextView) childDialog.findViewById(R.id.txtBillPayMode);
        textViewBalRmk=(TextView) childDialog.findViewById(R.id.textBalanceRemark);
        edtBalanceRemark=(EditText) childDialog.findViewById(R.id.edtBalRemark);
        textViewBillAmt=(TextView) childDialog.findViewById(R.id.textViewBillAmt);
        txtBillAmt=(TextView) childDialog.findViewById(R.id.txtBillAmt);
        textViewPaidAmt=(TextView) childDialog.findViewById(R.id.textViewPaidAmt);
        edtPaidAmount=(EditText) childDialog.findViewById(R.id.edtPaidAmt);
        CommonUtils.showKeyboard(edtPaidAmount,true);
        Button btnClose = (Button) childDialog.findViewById(R.id.btnClose);
        Button btnOk= (Button) childDialog.findViewById(R.id.btnOk);
        LinearLayout linearBillAmt=(LinearLayout)childDialog.findViewById(R.id.linearBillAmt);
        LinearLayout linearPaidAmt=(LinearLayout)childDialog.findViewById(R.id.linearPaidAmt);
        LinearLayout linearRemark=(LinearLayout)childDialog.findViewById(R.id.linearRemark);
        LinearLayout linearSlip=(LinearLayout)childDialog.findViewById(R.id.linearSlip);
        LinearLayout linearSlipExpiryDt=(LinearLayout)childDialog.findViewById(R.id.linearSlipExpiryDt);


        linearRemark.setVisibility(View.INVISIBLE);
        linearBillAmt.setVisibility(View.VISIBLE);
        linearPaidAmt.setVisibility(View.VISIBLE);
        linearSlip.setVisibility(View.INVISIBLE);
        linearSlipExpiryDt.setVisibility(View.INVISIBLE);
        textViewPaidAmt.setText("Remark");
        edtPaidAmount.setInputType(TYPE_NUMBER_FLAG_DECIMAL);
        if(!customerDtlsForCredit.isEmpty())
        {
            String []custData=customerDtlsForCredit.split("#");
            textViewBillAmt.setText("Customer");
            txtBillAmt.setText(custData[1]);
            txtBillAmt.setGravity(Gravity.LEFT);
            customerCode=custData[0];

        }
        textPayMode.setText(settlementName);

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                CommonUtils.hideKeyboard(edtPaidAmount);
                childDialog.dismiss();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(edtPaidAmount.getText().toString().isEmpty())
                {
                    Toast.makeText(clsBillSettlement.this, "Remark should not be emty", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    remark=edtPaidAmount.getText().toString();
                    bal = Double.parseDouble(txtBalance.getText().toString());
                    paidAmt=Double.parseDouble(txtBalance.getText().toString());
                    balance=paidAmt-bal;

                    objSettleDtl = (clsSettlementDtl) hmSettleDtl.get(settlementName);
                    if (hmBillSettlementDtl.containsKey(settlementType))
                    {
                        clsSettlementOption objBillSettlementDtl = hmBillSettlementDtl.get(settleType);
                        double temp_paidAmount = objBillSettlementDtl.getDblPaidAmt();
                        double temp_settleAmount = objBillSettlementDtl.getDblSettlementAmt();
                        temp_paidAmount += paidAmt;
                        temp_settleAmount += paidAmt;
                        objBillSettlementDtl.setDblSettlementAmt(temp_settleAmount);
                        objBillSettlementDtl.setDblPaidAmt(temp_paidAmount);
                        hmBillSettlementDtl.put(settlementType, objBillSettlementDtl);
                    }
                    else
                    {
                        clsSettlementOption objBillSettlementDtl = new clsSettlementOption();
                        objBillSettlementDtl.setStrSettelmentType(settlementType);
                        objBillSettlementDtl.setStrSettelmentCode(objSettleDtl.getStrSettlementCode());
                        objBillSettlementDtl.setDblSettlementAmt(paidAmt);
                        objBillSettlementDtl.setDblPaidAmt(paidAmt);
                        objBillSettlementDtl.setStrExpiryDate("");
                        objBillSettlementDtl.setStrSettelmentDesc(objSettleDtl.getStrSettlementName());
                        objBillSettlementDtl.setStrCardName("");
                        objBillSettlementDtl.setStrRemark(remark);
                        objBillSettlementDtl.setDblConvertionRatio(0.0);
                        objBillSettlementDtl.setDblActualAmt(Double.parseDouble(textGrandTotal.getText().toString()));
                        objBillSettlementDtl.setDblRefundAmt(0.0);
                        objBillSettlementDtl.setStrGiftVoucherCode("");
                        objBillSettlementDtl.setStrClientCode(clsGlobalFunctions.gClientCode);
                        objBillSettlementDtl.setStrDataPostFlag("N");
                        objBillSettlementDtl.setStrCustomerCode(customerCode);
                        objBillSettlementDtl.setStrCardNo("");


                        hmBillSettlementDtl.put(settlementType, objBillSettlementDtl);
                    }
                    updateField(balance);
                    funGetSettleListData();
                    CommonUtils.hideKeyboard(edtPaidAmount);
                    childDialog.dismiss();
                }
            }
        });

        childDialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.getWindowManager().getDefaultDisplay();

    }


    private void funSetSetteledData(String settlementType,String settlementName,double paidAmt,String remark,String creditCardNo,String exprityDate)
    {
        if (listSettleDtl.size() > 0)
        {
            settlementType = hmSettleDtl.get(settlementName).getStrSettlementType();
            switch (settlementType)
            {
                case "Cash":
                    bal = Double.parseDouble(txtBalance.getText().toString());
                    balance=paidAmt-bal;
                    //customerCode="";

                    objSettleDtl = (clsSettlementDtl) hmSettleDtl.get(settlementName);
                    if (hmBillSettlementDtl.containsKey(settlementType))
                    {
                        clsSettlementOption objBillSettlementDtl = hmBillSettlementDtl.get(settleType);
                        double temp_paidAmount = objBillSettlementDtl.getDblPaidAmt();
                        double temp_settleAmount = objBillSettlementDtl.getDblSettlementAmt();
                        temp_paidAmount += paidAmt;
                        temp_settleAmount += paidAmt;
                        objBillSettlementDtl.setDblSettlementAmt(temp_settleAmount);
                        objBillSettlementDtl.setDblPaidAmt(temp_paidAmount);
                        hmBillSettlementDtl.put(settlementType, objBillSettlementDtl);
                    }
                    else
                    {
                        clsSettlementOption objBillSettlementDtl = new clsSettlementOption();
                        objBillSettlementDtl.setStrSettelmentType(settlementType);
                        objBillSettlementDtl.setStrSettelmentCode(objSettleDtl.getStrSettlementCode());
                        objBillSettlementDtl.setDblSettlementAmt(paidAmt);
                        objBillSettlementDtl.setDblPaidAmt(paidAmt);
                        objBillSettlementDtl.setStrExpiryDate("");
                        objBillSettlementDtl.setStrSettelmentDesc(objSettleDtl.getStrSettlementName());
                        objBillSettlementDtl.setStrCardName("");
                        objBillSettlementDtl.setStrRemark(remark);
                        objBillSettlementDtl.setDblConvertionRatio(0.0);
                        objBillSettlementDtl.setDblActualAmt(Double.parseDouble(textGrandTotal.getText().toString()));
                        objBillSettlementDtl.setDblRefundAmt(0.0);
                        objBillSettlementDtl.setStrGiftVoucherCode("");
                        objBillSettlementDtl.setStrClientCode(clsGlobalFunctions.gClientCode);
                        objBillSettlementDtl.setStrDataPostFlag("N");
                        objBillSettlementDtl.setStrCustomerCode("");
                        objBillSettlementDtl.setStrCardNo("");


                        hmBillSettlementDtl.put(settlementType, objBillSettlementDtl);
                    }
                    updateField(balance);
                    funGetSettleListData();
                    break;


                case "Credit Card":
                    bal = Double.parseDouble(txtBalance.getText().toString());
                    balance=paidAmt-bal;
                    //customerCode="";

                    objSettleDtl = (clsSettlementDtl) hmSettleDtl.get(settlementName);
                    if (hmBillSettlementDtl.containsKey(settlementType))
                    {
                        clsSettlementOption objBillSettlementDtl = hmBillSettlementDtl.get(settleType);
                        double temp_paidAmount = objBillSettlementDtl.getDblPaidAmt();
                        double temp_settleAmount = objBillSettlementDtl.getDblSettlementAmt();
                        temp_paidAmount += paidAmt;
                        temp_settleAmount += paidAmt;
                        objBillSettlementDtl.setDblSettlementAmt(temp_settleAmount);
                        objBillSettlementDtl.setDblPaidAmt(temp_paidAmount);
                        hmBillSettlementDtl.put(settlementType, objBillSettlementDtl);
                    }
                    else
                    {
                        clsSettlementOption objBillSettlementDtl = new clsSettlementOption();
                        objBillSettlementDtl.setStrSettelmentType(settlementType);
                        objBillSettlementDtl.setStrSettelmentCode(objSettleDtl.getStrSettlementCode());
                        objBillSettlementDtl.setDblSettlementAmt(paidAmt);
                        objBillSettlementDtl.setDblPaidAmt(paidAmt);
                        objBillSettlementDtl.setStrExpiryDate(exprityDate);
                        objBillSettlementDtl.setStrSettelmentDesc(objSettleDtl.getStrSettlementName());
                        objBillSettlementDtl.setStrCardName(creditCardNo);
                        objBillSettlementDtl.setStrRemark(remark);
                        objBillSettlementDtl.setDblConvertionRatio(0.0);
                        objBillSettlementDtl.setDblActualAmt(Double.parseDouble(textGrandTotal.getText().toString()));
                        objBillSettlementDtl.setDblRefundAmt(0.0);
                        objBillSettlementDtl.setStrGiftVoucherCode("");
                        objBillSettlementDtl.setStrClientCode(clsGlobalFunctions.gClientCode);
                        objBillSettlementDtl.setStrDataPostFlag("N");
                        objBillSettlementDtl.setStrCustomerCode("");
                        objBillSettlementDtl.setStrCardNo("");


                        hmBillSettlementDtl.put(settlementType, objBillSettlementDtl);
                    }
                    updateField(balance);
                    funGetSettleListData();
                    break;

            }


        }

        else
        {
            Toast.makeText(clsBillSettlement.this, "Settle Mode Not Found", Toast.LENGTH_LONG).show();
        }
    }




    public void funGetReasonList() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetReasonList("strDiscount", clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsReasonMaster>>()
                {
                    @Override
                    public void onSuccess(ArrayList<clsReasonMaster> arrListTemp)
                    {
                        dismissDialog();
                        if (null != arrListTemp) {
                            if (arrListTemp.size() > 0)
                            {
                                hmReason=new TreeMap();
                                reasonList=new ArrayList<String>();
                                for(int cnt=0;cnt<arrListTemp.size();cnt++)
                                {
                                    clsReasonMaster objReason=(clsReasonMaster)arrListTemp.get(cnt);
                                    hmReason.put(objReason.getStrReasonName(), objReason.getStrReasonCode());
                                }

                                Set setReason=hmReason.keySet();
                                Iterator itrReason=setReason.iterator();

                                while(itrReason.hasNext())
                                {
                                    reasonName= (String) itrReason.next();
                                    reasonList.add(reasonName);
                                }

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(clsBillSettlement.this);
                                builder1.setMessage("Do You Want to add Reason and Remark???");
                                builder1.setCancelable(true);
                                builder1.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                funSelectReasonAndRemark();
                                            }
                                        }
                                );

                                builder1.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                Toast.makeText(clsBillSettlement.this, "Enter Reason & Remark", Toast.LENGTH_LONG).show();
                                                funGetReasonList();
                                            }
                                        }
                                );
                                AlertDialog alert11 = builder1.create();
                                alert11.show();


                            }
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage, int errorCode)
                    {
                        dismissDialog();
                    }
                });
            } else {
                SnackBarUtils.showSnackBar(clsBillSettlement.this, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsBillSettlement.this, R.string.setup_your_server_settings);
        }
    }



    private void funSelectReasonAndRemark()
    {
        final Dialog dialog = new Dialog(clsBillSettlement.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogforreasonremark);
        final Spinner spinnerReason = (Spinner) dialog.findViewById(R.id.reasonSpinner);
        final EditText edtRemark= (EditText) dialog.findViewById(R.id.edtKotRemarks);
        LinearLayout linearRemark=(LinearLayout) dialog.findViewById(R.id.linearPaidAmt);
        TextView textPayMode = (TextView) dialog.findViewById(R.id.txtBillPayMode);
        final Button btnOk=(Button) dialog.findViewById(R.id.btnOk) ;
        final Button btnClose=(Button) dialog.findViewById(R.id.btnClose) ;
        linearRemark.setVisibility(View.INVISIBLE);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (clsBillSettlement.this, R.layout.spinneritemtextview,reasonList);

        dataAdapter.setDropDownViewResource
                (R.layout.spinnerdropdownitem);
        spinnerReason.setAdapter(dataAdapter);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                reasonCode = String.valueOf(hmReason.get(spinnerReason.getSelectedItem().toString()));
                discReasonRemark=reasonCode+"#"+spinnerReason.getSelectedItem().toString();
                dialog.dismiss();

            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
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


    public void funGetBillSeriesList(){
        billNo="BS0000001";
        funSaveBill();
    }


    private void funPrintBillDataFromWS(String voucherNo) {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funPrintBillDataFromWS(voucherNo,"","PrintBill", new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        dismissDialog();
                        if (null != jObj) {
                            try
                            {
                                funPrintBillFormat(jObj);
                                if(formName.equals("Make Bill")){
                                      finish();
                                }
                                intNoOfBills--;
                                if(intNoOfBills==0) {
                                    finish();
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


    private void funPrintDirectBillerKOTFromWS(String voucherNo) {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funPrintDirectBillerKOTFromWS(clsGlobalFunctions.gPOSCode,voucherNo,clsGlobalFunctions.gDirectBillerAreaCode, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        dismissDialog();
                        if (null != jObj) {
                            try
                            {
                                StringBuilder sbPrintkot=new StringBuilder();
                                JsonArray arrJsonKOT=new JsonArray();
                                arrJsonKOT=(JsonArray) jObj.get("printKOTData");
                                int j=1;
                                for(int i=0;i<=arrJsonKOT.size();i++)
                                {
                                    JsonObject objRows = (JsonObject)arrJsonKOT.get(i);
                                    String printKOTData="";
                                   // printKOTData = objRows.get("KOT "+i).getAsString();
                                    printKOTData = objRows.get("KOT "+j).toString();
                                  //  printKOTData = (String) objRows.get("KOT "+i);
                                    sbPrintkot=new StringBuilder(printKOTData);
                                    Toast.makeText(clsBillSettlement.this, "KOT Printed from Bluetooth Printer" + sbPrintkot.toString(), Toast.LENGTH_LONG).show();
                                    System.out.println("KOT Print"+sbPrintkot.toString());
                                    j++;
                                    new clsPrintDemo().sendData(sbPrintkot.toString(),clsPrintDemo.mmOutputStream,clsPrintDemo.mmInputStream);
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


    private void funPrintBillFormat(JsonObject jObj)
    {
        try {

            JsonArray jsonArrBillHd = (JsonArray) jObj.get("BillHdInfo");

            String line = "----------------------------------------";
            StringBuilder sbPrintBill = new StringBuilder();

            clsPrintFormatAPI objPrint = new clsPrintFormatAPI();

            sbPrintBill.append(objPrint.funGetStringWithAlignment("INVOICE", "Center", 40));

            if(clsGlobalFunctions.gClientName.length()>40)
            {
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gClientName.substring(0, 30), "Center", 40));
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gClientName.substring(30), "Center", 40));
            }
            else
            {
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment(clsGlobalFunctions.gClientName, "Center", 40));
            }


            sbPrintBill.append("\n");

            JsonObject jObjBillHd = (JsonObject)jsonArrBillHd.get(0);

            // Bill header level details
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Bill No : " + jObjBillHd.get("BillNo").getAsString(), "Left", 20));
            String billDateTime = jObjBillHd.get("BillDate").getAsString();
            String billDate=billDateTime.split(" ")[0];
            String bdate=billDate.split("-")[2]+"-"+billDate.split("-")[1]+"-"+billDate.split("-")[0];
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Date : " + bdate, "Left", 20));
            sbPrintBill.append("\n");
            String tableName=jObjBillHd.get("Table").getAsString();
            if(!tableName.isEmpty())
            {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Table : " + tableName, "Left", 20));
                String waiterName=jObjBillHd.get("Waiter").getAsString();
                if(!waiterName.isEmpty())
                {
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("Waiter : " + waiterName, "Left", 20));
                }
                sbPrintBill.append("\n");
            }

            String PAX=jObjBillHd.get("PAX").getAsString();
            if(!PAX.isEmpty())
            {
                if(Integer.parseInt(PAX)>0) {
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("PAX : " + PAX, "Left", 40));
                    sbPrintBill.append("\n");
                }
            }
            String user=jObjBillHd.get("User").getAsString();
            if(!user.isEmpty())
            {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Cashier : " + user, "Left", 40));
                sbPrintBill.append("\n");
            }
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 40));



            // Item Level Details
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment("ITEM NAME ","Left", 40));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment("     QTY         RATE             AMOUNT","Left", 40));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 40));


            JsonArray jsonArrBillDtl = (JsonArray)jObj.get("BillDtlInfo");
            for (int i = 0; i < jsonArrBillDtl.size(); i++) {
                JsonObject jObjItemDtl = (JsonObject) jsonArrBillDtl.get(i);
                String itemName = jObjItemDtl.get("ItemName").getAsString();
                String itemQty = jObjItemDtl.get("Quantity").getAsString();
                String itemRate = jObjItemDtl.get("Rate").getAsString();
                String totalAmount = jObjItemDtl.get("Amount").getAsString();



                if(itemName.length()>40)
                {
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("" + itemName,"Left", 40));
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("" + itemName.substring(40,itemName.length()), "Left", 40));
                }
                else
                {
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment("" + itemName,"Left", 40));
                }
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment(" " + itemQty + " ", "Right", 9));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(itemRate + " ", "Right", 13));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(totalAmount + " ", "Right", 19));
            }

            JsonArray jsonArrModBillDtl = (JsonArray)jObj.get("ModBillDtlInfo");
            if(jsonArrModBillDtl.size()>0)
            {
                for (int i = 0; i < jsonArrModBillDtl.size(); i++) {
                    JsonObject jObjItemDtl = (JsonObject) jsonArrModBillDtl.get(i);
                    String itemName = jObjItemDtl.get("ModItemName").getAsString();
                    String itemQty = jObjItemDtl.get("Quantity").getAsString();
                    String itemRate = jObjItemDtl.get("Rate").getAsString();
                    String totalAmount = jObjItemDtl.get("Amount").getAsString();

//                    sbPrintBill.append(objPrint.funGetStringWithAlignment(itemName, "Left", 18));
//                    // sbPrintBill.append("\n");
//                    sbPrintBill.append(objPrint.funGetStringWithAlignment(itemQty, "Right", 7));
//                    // sbPrintBill.append(objPrint.funGetStringWithAlignment(itemRate, "Right", 10));
//                    sbPrintBill.append(objPrint.funGetStringWithAlignment(totalAmount, "Right", 7));
//                    sbPrintBill.append("\n");



                    if(itemName.length()>40)
                    {
                        sbPrintBill.append(objPrint.funGetStringWithAlignment("" + itemName,"Left", 40));
                        sbPrintBill.append("\n");
                        sbPrintBill.append(objPrint.funGetStringWithAlignment("" + itemName.substring(40,itemName.length()), "Left", 40));
                    }
                    else
                    {
                        sbPrintBill.append("\n");
                        sbPrintBill.append(objPrint.funGetStringWithAlignment("" + itemName,"Left", 40));
                    }
                    sbPrintBill.append("\n");
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(" " + itemQty + " ", "Right", 9));
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(itemRate + " ", "Right", 13));
                    sbPrintBill.append(objPrint.funGetStringWithAlignment(totalAmount + " ", "Right", 19));
                }


            }
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 40));
            sbPrintBill.append("\n");

            // Sub Total
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Sub Total   : ", "Left", 30));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("SubTotal").getAsString(), "Right", 10));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 40));
            sbPrintBill.append("\n");


            // TaxLevel Details
            JsonArray jsonArrBillTaxDtl = (JsonArray)jObj.get("BillTaxDtlInfo");
            boolean flgTaxDtl = false;
            for (int i = 0; i < jsonArrBillTaxDtl.size(); i++) {
                JsonObject jObjTaxDtl = (JsonObject) jsonArrBillTaxDtl.get(i);
                String taxDesc = jObjTaxDtl.get("TaxDesc").getAsString();
                String taxAmt = jObjTaxDtl.get("TaxAmt").getAsString();
                sbPrintBill.append(objPrint.funGetStringWithAlignment(taxDesc, "Left", 30));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(taxAmt, "Right", 10));
                sbPrintBill.append("\n");
                flgTaxDtl = true;
            }
            if (flgTaxDtl) {
                sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 40));
                sbPrintBill.append("\n");
            }

            // Grand Total
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Total       : ", "Left", 30));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("GrandTotal").getAsString(), "Right", 10));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 40));
            sbPrintBill.append("\n");

            // Bill Settlement Details
            boolean flgBillSettlement=false;
            JsonArray jsonArrBillSettlementDtl = (JsonArray)jObj.get("BillSettleDtlInfo");
            for (int i = 0; i < jsonArrBillSettlementDtl.size(); i++) {
                JsonObject jObjSettlementDtl = (JsonObject) jsonArrBillSettlementDtl.get(i);
                String settlementDesc = jObjSettlementDtl.get("SettlementDesc").getAsString();
                String settlementAmt = jObjSettlementDtl.get("SettlementAmt").getAsString();
                sbPrintBill.append(objPrint.funGetStringWithAlignment(settlementDesc, "Left", 30));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(settlementAmt, "Right", 10));
                sbPrintBill.append("\n");
                flgBillSettlement=true;
            }

            if(flgBillSettlement) {
                sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 40));
                sbPrintBill.append("\n");
            }

            /*
            JSONObject jsonObjCardBalance = jObj.getJSONObject("CardDetails");
            sbPrintBill.append(objPrint.funGetStringWithAlignment("Card Balance : ", "Left", 20));
            sbPrintBill.append(objPrint.funGetStringWithAlignment(jsonObjCardBalance.get("Balance").toString(), "Right", 12));
            sbPrintBill.append("\n");
            sbPrintBill.append(objPrint.funGetStringWithAlignment(line, "Left", 32));
            sbPrintBill.append("\n");*/

            //sbPrintBill=funPrintVatAndServiceTaxNo(sbPrintBill);

            // Member Details

            boolean flgMemberDtls=false;
            if (!jObjBillHd.get("MemCode").getAsString().isEmpty()) {
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Member Code   : ", "Left", 20));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("MemCode").getAsString(), "Left", 20));
                sbPrintBill.append("\n");
                sbPrintBill.append(objPrint.funGetStringWithAlignment("Member Name   : ", "Left", 20));
                sbPrintBill.append(objPrint.funGetStringWithAlignment(jObjBillHd.get("MemName").getAsString(), "Left", 20));
                sbPrintBill.append("\n");
                flgMemberDtls=true;
            }

            sbPrintBill.append(objPrint.funGetStringWithAlignment("(INCLUSIVE OF ALL TAXES)", "Left",40));

            sbPrintBill.append("\n");
            sbPrintBill.append("\n");
            sbPrintBill.append("\n");
            sbPrintBill.append("\n");



            if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer"))   // To print bill on Bluetooth Device
            {

                Toast.makeText(clsBillSettlement.this, "Bill Printed from Bluetooth Printer" + sbPrintBill.toString(), Toast.LENGTH_LONG).show();
                System.out.println("Bill Print"+sbPrintBill.toString());
                new clsPrintDemo().sendData(sbPrintBill.toString(),clsPrintDemo.mmOutputStream,clsPrintDemo.mmInputStream);
            }

            // Function to print bill on POS Gold Device.
            //funPrintBillForPOSGoldDevice(sbPrintBill.toString());

        } catch (Exception e) {
            e.printStackTrace();
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