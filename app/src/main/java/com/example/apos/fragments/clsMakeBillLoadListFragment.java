package com.example.apos.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsBillSettlement;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsKotScreen;
import com.example.apos.activity.clsMakeBillScreen;
import com.example.apos.adapter.clsMakeBillITaxAdapter;
import com.example.apos.adapter.clsMakeBillItemListAdapter;
import com.example.apos.api.BaseAPIHelper;

import com.example.apos.bean.clsBillDtl;
import com.example.apos.bean.clsBillHd;
import com.example.apos.bean.clsMakeBillItemDtls;
import com.example.apos.bean.clsPromotionItems;
import com.example.apos.bean.clsReasonMaster;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.bean.clsTaxCalculationDtls;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsMakeBillActListener;
import com.example.apos.listeners.clsMakeBillItemListListener;

import com.example.apos.listeners.clsMakeBillTableListener;
import com.example.apos.util.clsPrintDemo;
import com.example.apos.util.clsUtility;
import com.example.apos.util.mach.clsPrintFormatAPI;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.internal.LinkedTreeMap;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class clsMakeBillLoadListFragment extends Fragment implements clsMakeBillItemListListener
{
    public static TextView txtTableName;
    private static String tableNo,waiterNo,counterCode,areaCode,discReasonRemark="";
    private static ListView listMakeBillItems;
    private static ListView makeBillTaxList,makeBillDiscList;
    public static  clsGlobalFunctions objGlobal;
    private static ArrayList arrListMakeBillItemDtls;
    private static ArrayList arrListMakeBillTaxDtls;
    private static EditText edtAmount;
    String billNo;
    private static int paxNo=0;
    public static Button makeBillButton,btnBack;
    static double  subTotalAmt;
    static double taxTotalAmt;
    static double grandTotal;
    static double discTotalAmt;
    static clsMakeBillItemListAdapter makeBillItemListAdapter;
    static clsMakeBillITaxAdapter makeBillTaxAdapter;
    public static clsMakeBillItemListListener makeBillItemSelectionListener;
    private clsMakeBillActListener objMakeBillActListener;
    private clsMakeBillTableListener objMakeBillTableListener;
    private BluetoothAdapter mBluetoothAdapter;
    private static String customerCode="";
    public static List<clsPromotionItems> listPromotionItems=null;
    private Dialog pgDialog;
    private static TreeMap hmReason;
    ArrayList<String> reasonList;
    int intNoOfBills=0;
    public static clsMakeBillLoadListFragment getInstance() {
        clsMakeBillLoadListFragment mMakeBill = new clsMakeBillLoadListFragment();
        return mMakeBill;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.makebillloadlist, container, false);
        if(clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer")) {
            mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            if (!mBluetoothAdapter.isEnabled())
            {
                Toast.makeText(clsMakeBillScreen.mActivity,"Bluetooth is disable",Toast.LENGTH_LONG).show();
            }
            else
            {
                new clsPrintDemo().funPrintViaBluetoothPrinter(mBluetoothAdapter);
            }
        }
        try
        {
            objMakeBillActListener=(clsMakeBillActListener)getActivity();
            objMakeBillTableListener=(clsMakeBillTableListener)clsMakeBillLoadTable.getInstance();
        }
        catch(Exception ex)
        {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }
        tableNo="";
        waiterNo="";
        counterCode="";
        areaCode="";
        subTotalAmt=0;
        taxTotalAmt=0;
        discTotalAmt=0;
        billNo="";
        arrListMakeBillItemDtls=new ArrayList();
        edtAmount=(EditText)rootView.findViewById(R.id.edtMakeBillTotalAmt);
        btnBack=(Button)rootView.findViewById(R.id.btnMakeBillback);
        makeBillButton=(Button)rootView.findViewById(R.id.btnMakeBill);
        makeBillButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if(!txtTableName.getText().toString().isEmpty())
                {
                    if(arrListMakeBillItemDtls.size()>0)
                    {
                        makeBillButton.setEnabled(false);
                        if(Double.parseDouble(edtAmount.getText().toString())>0)
                        {
                            clsMakeBillItemDtls objItemDtl = (clsMakeBillItemDtls) arrListMakeBillItemDtls.get(0);
                            // new GenerateBillNoWS().execute(clsGlobalFunctions.gPOSCode, objItemDtl.getStrCardNo());

                            if(clsGlobalFunctions.gEnableBillSeries.equals("Y")){

                                funGetBillSeriesList();
                            }else{
                                funGenerateBillNo(clsGlobalFunctions.gPOSCode, objItemDtl.getStrCardNo());
                            }
                        }
                        makeBillButton.setEnabled(true);
                    }
                }
                else
                {
                    Toast.makeText(clsMakeBillScreen.mActivity, "Please select table", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().finish();
                if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer")) {
                    try {
                        if (clsGlobalFunctions.gBillPrinterType.equalsIgnoreCase("Bluetooth Printer")) {
                            new clsPrintDemo().closeBT(clsPrintDemo.mmOutputStream, clsPrintDemo.mmInputStream, clsPrintDemo.socket);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        txtTableName = (TextView)rootView.findViewById(R.id.tv_make_bill_Table_Name);
        listMakeBillItems = (ListView) rootView.findViewById(R.id.listmakebillselecteditems);
        makeBillTaxList=(ListView)rootView.findViewById(R.id.makeBillTaxList);
        makeBillDiscList=(ListView)rootView.findViewById(R.id.makeBillDiscList);
        return rootView;
    }


    private static void funFillListView(ArrayList arrlist)
    {
        makeBillItemListAdapter=new clsMakeBillItemListAdapter(clsMakeBillScreen.mActivity,clsMakeBillScreen.mActivity, arrlist,makeBillItemSelectionListener);
        listMakeBillItems.setAdapter(makeBillItemListAdapter);
    }

    private static void funFillBillTaxDtlGrid(ArrayList arrListBillTaxDtl)
    {
        makeBillTaxAdapter=new clsMakeBillITaxAdapter(clsMakeBillScreen.mActivity,clsMakeBillScreen.mActivity, arrListBillTaxDtl);
        makeBillTaxList.setAdapter(makeBillTaxAdapter);
    }
    private static void funFillBillDiscDtlGrid(ArrayList arrListBillDiscDtl)
    {
        makeBillTaxAdapter=new clsMakeBillITaxAdapter(clsMakeBillScreen.mActivity,clsMakeBillScreen.mActivity, arrListBillDiscDtl);
        makeBillTaxList.setAdapter(makeBillTaxAdapter);
    }



    @Override
    public void funSetItemDtlForSelectedTable(final String tableNo,String tableName)
    {
        txtTableName.setText(tableName);

        if(clsGlobalFunctions.gActivePromotions.equals("Y"))
        {
            JSONObject jsonObject=new JSONObject();
            try
            {
                funCheckPromotion(tableNo,"Yes","Yes");
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        else
        {
            String applyPromotion = "No";
            String flgApplyPromoOnBill = "No";
            //  new LoadItemList().execute(tableNo, applyPromotion, flgApplyPromoOnBill);
            funLoadItemList(tableNo,applyPromotion,flgApplyPromoOnBill);
        }

    }


    private void funClearObjects()
    {
        tableNo="";
        waiterNo="";
        counterCode="";
        areaCode="";
        subTotalAmt=0;
        taxTotalAmt=0;
        discTotalAmt=0;
        billNo="";
        arrListMakeBillItemDtls=null;
        arrListMakeBillTaxDtls=null;

    }


    private void funResetFields()
    {
        txtTableName.setText("");
        edtAmount.setText("0.00");
        funFillListView(new ArrayList());
        funFillBillTaxDtlGrid(new ArrayList());
        funFillBillDiscDtlGrid(new ArrayList());
    }


    private JsonArray funInsertBillHd(String billNo,String cardNo) {
        JsonArray mJsonArray = new JsonArray();
        try {
            List<clsBillHd> listBillHdObject = new ArrayList<clsBillHd>();
            clsGlobalFunctions objGlobal = new clsGlobalFunctions();
            grandTotal = (subTotalAmt + taxTotalAmt) - discTotalAmt;
            JsonObject jObj = new JsonObject();
            JsonObject mJsonObject = new JsonObject();
            mJsonObject.addProperty("BillNo", billNo);
            mJsonObject.addProperty("AdvBookingNo", "");
            mJsonObject.addProperty("BillDate", clsGlobalFunctions.funGetPOSDateTime());
            mJsonObject.addProperty("POSCode", clsGlobalFunctions.gPOSCode);
            mJsonObject.addProperty("SettelmentMode", "");
            mJsonObject.addProperty("DiscountAmt", discTotalAmt);
            double finalDiscPer = (discTotalAmt / subTotalAmt) * 100;
            mJsonObject.addProperty("DiscountPer", finalDiscPer);
            mJsonObject.addProperty("TaxAmount", taxTotalAmt);
            mJsonObject.addProperty("SubTotal", subTotalAmt);
            mJsonObject.addProperty("GrandTotal", grandTotal);
            mJsonObject.addProperty("TakeAway", "No");
            mJsonObject.addProperty("OperationType","DineIn");
            mJsonObject.addProperty("UserCreated", clsGlobalFunctions.gUserCode);
            mJsonObject.addProperty("UserEdited", clsGlobalFunctions.gUserCode);
            mJsonObject.addProperty("DateCreated", objGlobal.funGetCurrentDateTime());
            mJsonObject.addProperty("DateEdited", objGlobal.funGetCurrentDateTime());
            mJsonObject.addProperty("ClientCode", clsGlobalFunctions.gClientCode);
            mJsonObject.addProperty("TableNo", tableNo);
            mJsonObject.addProperty("WaiterNo", waiterNo);
            if(clsGlobalFunctions.gMemberCodeForKotInMposByCardSwipe.equals("Y"))
            {
                mJsonObject.addProperty("CustomerCode", cardNo);
            }
            else
            {
                mJsonObject.addProperty("CustomerCode", customerCode);
            }

            mJsonObject.addProperty("ManualBillNo", "");
            mJsonObject.addProperty("ShiftCode", 1);
            mJsonObject.addProperty("PaxNo", paxNo);
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
            mJsonObject.addProperty("CounterCode", counterCode);
            mJsonObject.addProperty("DeliveryCharges", 0.0);
            mJsonObject.addProperty("CouponCode", "");
            mJsonObject.addProperty("AreaCode", areaCode);
            mJsonObject.addProperty("DiscountRemark", "");
            mJsonObject.addProperty("TakeAwayRemark", "");
            mJsonObject.addProperty("CardNo", cardNo);
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

    private JsonArray funInsertBillTaxDtl(String billNo) {
        JsonArray mJsonArrBillTaxDtl = new JsonArray();
        try {
            List<clsTaxCalculationDtls> listBillTaxDtlObject = new ArrayList<clsTaxCalculationDtls>();
            clsGlobalFunctions objGlobal = new clsGlobalFunctions();
            JsonObject jObjBillTxaDtl = new JsonObject();


            for (int l = 0; l < arrListMakeBillItemDtls.size(); l++) {
                clsMakeBillItemDtls objMakeBillItemBean = (clsMakeBillItemDtls) arrListMakeBillItemDtls.get(l);

                JsonObject mJsonObjectBillTaxDtl = new JsonObject();

                mJsonObjectBillTaxDtl.addProperty("BillNo", billNo);
                mJsonObjectBillTaxDtl.addProperty("POSCode",clsGlobalFunctions.gPOSCode);
                mJsonObjectBillTaxDtl.addProperty("ItemCode",objMakeBillItemBean.getStrItemCode());
                mJsonObjectBillTaxDtl.addProperty("ItemName",objMakeBillItemBean.getStrItemName());
                mJsonObjectBillTaxDtl.addProperty("Quantity",objMakeBillItemBean.getDblQuantity());
                mJsonObjectBillTaxDtl.addProperty("Amount",objMakeBillItemBean.getDblAmount());
                mJsonObjectBillTaxDtl.addProperty("ClientCode",clsGlobalFunctions.gClientCode);
                mJsonObjectBillTaxDtl.addProperty("BillDate", clsGlobalFunctions.funGetPOSDateTime());

                mJsonArrBillTaxDtl.add(mJsonObjectBillTaxDtl);

            }
            //jObjBillTxaDtl.add("BillTaxDtl", mJsonArrBillTaxDtl);
            //funSaveBill(jObjBillTxaDtl);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mJsonArrBillTaxDtl;
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
                    JsonObject mJsonObjectBillDtl = new JsonObject();

                    if(objPromoBillList.getPromoType().equalsIgnoreCase("Discount"))
                    {
                        mJsonObjectBillDtl.addProperty("BillNo", billNo);
                        mJsonObjectBillDtl.addProperty("ClientCode", clsGlobalFunctions.gClientCode);
                        mJsonObjectBillDtl.addProperty("UserCode", clsGlobalFunctions.gUserCode);
                        mJsonObjectBillDtl.addProperty("POSCode", clsGlobalFunctions.gPOSCode);
                        mJsonObjectBillDtl.addProperty("CurrentDateTime",objGlobal.funGetCurrentDateTime());
                        mJsonObjectBillDtl.addProperty("POSDate", clsGlobalFunctions.funGetPOSDateTime());
                        mJsonObjectBillDtl.addProperty("DiscAmt", objPromoBillList.getDiscAmt());
                        mJsonObjectBillDtl.addProperty("DiscPer",objPromoBillList.getDiscPer());
                        mJsonObjectBillDtl.addProperty("DiscOnAmt", objPromoBillList.getDiscOnAmt());
                        mJsonObjectBillDtl.addProperty("DiscOnType", "ItemWise");
                        mJsonObjectBillDtl.addProperty("DiscOnValue", objPromoBillList.getItemName());
                        mJsonObjectBillDtl.addProperty("Reason", discReasonRemark.split("#")[0]);
                        mJsonObjectBillDtl.addProperty("Remark", discReasonRemark.split("#")[1]);
                        mJsonDiscArrBillDtl.add( mJsonObjectBillDtl);
                    }
                    else
                    {
                        mJsonObjectBillDtl.addProperty("ItemCode", objPromoBillList.getItemCode());
                        mJsonObjectBillDtl.addProperty("PromoCode", objPromoBillList.getPromoCode());
                        mJsonObjectBillDtl.addProperty("BillNo", billNo);
                        mJsonObjectBillDtl.addProperty("PromoType", objPromoBillList.getPromoType());
                        Double rate=Double.valueOf(objPromoBillList.getBillAmt())/objPromoBillList.getFreeItemQty();
                        mJsonObjectBillDtl.addProperty("Quantity", objPromoBillList.getFreeItemQty());
                        mJsonObjectBillDtl.addProperty("Rate",rate);
                        mJsonObjectBillDtl.addProperty("Amount", objPromoBillList.getBillAmt());
                        mJsonObjectBillDtl.addProperty("ClientCode", clsGlobalFunctions.gClientCode);
                        mJsonObjectBillDtl.addProperty("DataPostFlag", "N");
                        mJsonObjectBillDtl.addProperty("BillDate", clsGlobalFunctions.funGetPOSDateTime());
                        mJsonPromoArrBillDtl.add( mJsonObjectBillDtl);
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




    private JsonArray funInsertBillDtl(String billNo,String cardNo) {
        JsonArray mJsonArrBillDtl = new JsonArray();
        try {
            List<clsBillDtl> listBillDtlObject = new ArrayList<clsBillDtl>();
            clsGlobalFunctions objGlobal = new clsGlobalFunctions();
            JsonObject jObjBillDtl = new JsonObject();


            for (int l = 0; l < arrListMakeBillItemDtls.size(); l++) {
                clsMakeBillItemDtls objMakeBillItemBean = (clsMakeBillItemDtls) arrListMakeBillItemDtls.get(l);

                //I001321M99
                String itemCode=objMakeBillItemBean.getStrItemCode();
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
                    mJsonObjectBillDtl.addProperty("ItemName", objMakeBillItemBean.getStrItemName());
                    mJsonObjectBillDtl.addProperty("BillNo", billNo);
                    mJsonObjectBillDtl.addProperty("BillDate", clsGlobalFunctions.funGetPOSDateTime());
                    mJsonObjectBillDtl.addProperty("AdvBookingNo", "");
                    mJsonObjectBillDtl.addProperty("Quantity", objMakeBillItemBean.getDblQuantity());
                    mJsonObjectBillDtl.addProperty("Rate", objMakeBillItemBean.getDblRate());
                    mJsonObjectBillDtl.addProperty("Amount", objMakeBillItemBean.getDblAmount());
                    mJsonObjectBillDtl.addProperty("TaxAmount", 0.00);
                    mJsonObjectBillDtl.addProperty("KOTNo", objMakeBillItemBean.getStrKotNo());
                    mJsonObjectBillDtl.addProperty("ClientCode", clsGlobalFunctions.gClientCode);
                    mJsonObjectBillDtl.addProperty("CustomerCode",cardNo);
                    mJsonObjectBillDtl.addProperty("OrderProcessedTime", objMakeBillItemBean.getOrderProcessingTime());
                    mJsonObjectBillDtl.addProperty("DataPostFlag", "N");
                    mJsonObjectBillDtl.addProperty("MMSDataPostFlag", "N");
                    mJsonObjectBillDtl.addProperty("ManualKOTNo", "");
                    mJsonObjectBillDtl.addProperty("tdhYN", "N");
                    mJsonObjectBillDtl.addProperty("PromoCode", "");
                    mJsonObjectBillDtl.addProperty("CounterCode", counterCode);
                    mJsonObjectBillDtl.addProperty("WaiterNo", waiterNo);
                    mJsonObjectBillDtl.addProperty("DiscountAmt", discountAmt);
                    String dateTime=clsGlobalFunctions.gPOSDate;
                    String posDate=dateTime.replaceAll(" ","%20");
                    mJsonObjectBillDtl.addProperty("BillDt", posDate);
                    mJsonObjectBillDtl.addProperty("OrderPickedUpdTime", objMakeBillItemBean.getOrderPickedupTime());
                    mJsonObjectBillDtl.addProperty("AreaCode",objMakeBillItemBean.getStrAreaCode());
                    mJsonObjectBillDtl.addProperty("OperationType","DineIn");
                    mJsonObjectBillDtl.addProperty("POSCode",clsGlobalFunctions.gPOSCode);
                    mJsonObjectBillDtl.addProperty("UserCode", clsGlobalFunctions.gUserCode);
                    mJsonArrBillDtl.add( mJsonObjectBillDtl);
                }
            }

           // jObjBillDtl.add("BillDtlInfo", mJsonArrBillDtl);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mJsonArrBillDtl;
    }


    private JsonArray funInsertBillModifierDtl(String billNo) {
        JsonArray mJsonArrBillModDtl = new JsonArray();
        try {
            List<clsBillDtl> listBillDtlObject = new ArrayList<clsBillDtl>();
            clsGlobalFunctions objGlobal = new clsGlobalFunctions();
            JsonObject jObjBillModDtl = new JsonObject();

            for (int l = 0; l < arrListMakeBillItemDtls.size(); l++) {
                clsMakeBillItemDtls objMakeBillItemBean = (clsMakeBillItemDtls) arrListMakeBillItemDtls.get(l);

                //I001321M99
                String itemCode=objMakeBillItemBean.getStrItemCode();
                if(itemCode.contains("M") && itemCode.length()>=10)
                {

                    JsonObject mJsonObjectBillModDtl = new JsonObject();

                    String modifierCode=itemCode.substring(7, 11);
                    itemCode=itemCode.substring(0,7);
                    mJsonObjectBillModDtl.addProperty("BillNo", billNo);
                    mJsonObjectBillModDtl.addProperty("ClientCode", clsGlobalFunctions.gClientCode);
                    mJsonObjectBillModDtl.addProperty("ItemCode",itemCode);
                    mJsonObjectBillModDtl.addProperty("ModifierCode",modifierCode);
                    mJsonObjectBillModDtl.addProperty("ModifierName",objMakeBillItemBean.getStrItemName());
                    mJsonObjectBillModDtl.addProperty("Rate",objMakeBillItemBean.getDblRate());
                    mJsonObjectBillModDtl.addProperty("Quantity",objMakeBillItemBean.getDblQuantity());
                    mJsonObjectBillModDtl.addProperty("Amount",objMakeBillItemBean.getDblAmount());
                    mJsonObjectBillModDtl.addProperty("CustomerCode","");
                    mJsonObjectBillModDtl.addProperty("DataPostFlag","N");
                    mJsonObjectBillModDtl.addProperty("MMSDataPostFlag","N");
                    mJsonObjectBillModDtl.addProperty("DefaultModifierSelection","N");
                    mJsonObjectBillModDtl.addProperty("DiscPer","0.00");
                    mJsonObjectBillModDtl.addProperty("DiscAmt","0.00");
                    mJsonObjectBillModDtl.addProperty("BillDate", clsGlobalFunctions.funGetPOSDateTime());
                    mJsonArrBillModDtl.add( mJsonObjectBillModDtl);
                }
            }

            //jObjBillModDtl.add("BillModifierDtl", mJsonArrBillModDtl);
            //funSaveBill(jObjBillModDtl);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return mJsonArrBillModDtl;
    }


    private void funGenerateBillTextFile(final String bill)
    {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(clsMakeBillScreen.mActivity);
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
//                            if (mBluetoothAdapter.isEnabled())
//                            {
//                                funPrintBillDataFromWS(bill);
//                            }
                            funPrintBillDataFromWS(bill);
                        }

                    }
                }
        );
        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    { dialog.cancel();
                    }
                }
        );
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }



    private void funLoadItemList(final String tbleNo,String applyPromotion,String flgApplyPromoOnBill) {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetTableBillData(clsGlobalFunctions.gPOSCode, tbleNo, clsGlobalFunctions.gClientCode, clsGlobalFunctions.gPOSDate, applyPromotion,flgApplyPromoOnBill, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        dismissDialog();
                        if (null != jObj) {
                            try {
                                subTotalAmt=0;
                                taxTotalAmt=0;
                                discTotalAmt=0;
                                ArrayList arrListItemDtl=new ArrayList();
                                JsonArray mJsonArray = (JsonArray) jObj.get("BillItemDetails");
                                JsonObject mJsonObject = new JsonObject();

                                for (int i = 0; i < mJsonArray.size(); i++) {
                                    mJsonObject = (JsonObject) mJsonArray.get(i);
                                    if (mJsonObject.get("TableNo").getAsString().equals("")) {
                                        //memberInfo = "no data";
                                    } else
                                    {
                                        clsMakeBillItemDtls objItemDtl = new clsMakeBillItemDtls();
                                        objItemDtl.setStrTableNo(mJsonObject.get("TableNo").getAsString());
                                        objItemDtl.setStrWaiterNo(mJsonObject.get("WaiterNo").getAsString());
                                        objItemDtl.setStrItemCode(mJsonObject.get("ItemCode").getAsString());
                                        objItemDtl.setStrItemName(mJsonObject.get("ItemName").getAsString());
                                        objItemDtl.setDblQuantity(Double.parseDouble(mJsonObject.get("ItemQty").getAsString()));
                                        objItemDtl.setDblAmount(Double.parseDouble(mJsonObject.get("Amount").getAsString()));
                                        objItemDtl.setDblRate(Double.parseDouble(mJsonObject.get("Rate").getAsString()));
                                        objItemDtl.setStrKotNo(mJsonObject.get("KOTNo").getAsString());
                                        objItemDtl.setStrPosCode(mJsonObject.get("POSCode").getAsString());
                                        objItemDtl.setStrCounterCode(mJsonObject.get("CounterCode").getAsString());
                                        objItemDtl.setStrAreaCode(mJsonObject.get("AreaCode").getAsString());
                                        objItemDtl.setStrCardNo(mJsonObject.get("CardNo").getAsString());
                                        objItemDtl.setOrderProcessingTime(mJsonObject.get("OrderProcessTime").getAsString());
                                        objItemDtl.setOrderPickedupTime(mJsonObject.get("OrderPickupTime").getAsString());
                                        objItemDtl.setIntPaxNo(Integer.parseInt(mJsonObject.get("PaxNo").getAsString()));
                                        paxNo=Integer.parseInt(mJsonObject.get("PaxNo").getAsString());
                                        customerCode=mJsonObject.get("CustomerCode").getAsString();
                                        arrListItemDtl.add(objItemDtl);

                                        tableNo=mJsonObject.get("TableNo").getAsString();
                                        waiterNo=mJsonObject.get("WaiterNo").getAsString();
                                        areaCode=mJsonObject.get("AreaCode").getAsString();
                                        counterCode=mJsonObject.get("CounterCode").getAsString();
                                        subTotalAmt+=Double.parseDouble(String.valueOf(objItemDtl.getDblAmount()));

                                    }
                                }

                                ArrayList<clsTaxCalculationDtls> arrListBillTaxDtl=new ArrayList<clsTaxCalculationDtls>();
                                JsonArray jsonArrBillTaxDtl = (JsonArray) jObj.get("BillTaxDetails");
                                for (int i = 0; i < jsonArrBillTaxDtl.size(); i++)
                                {
                                    mJsonObject = (JsonObject) jsonArrBillTaxDtl.get(i);
                                    if (mJsonObject.get("TaxCode").getAsString().equals(""))
                                    {
                                    }
                                    else
                                    {
                                        clsTaxCalculationDtls objBillTaxDtl=new clsTaxCalculationDtls();
                                        objBillTaxDtl.setTaxCode(mJsonObject.get("TaxCode").getAsString());
                                        objBillTaxDtl.setTaxName(mJsonObject.get("TaxName").getAsString());
                                        objBillTaxDtl.setTaxCalculationType(mJsonObject.get("TaxType").getAsString());
                                        objBillTaxDtl.setTaxableAmount(Double.parseDouble(mJsonObject.get("TaxableAmount").getAsString()));
                                        objBillTaxDtl.setTaxAmount(Double.parseDouble(mJsonObject.get("TaxAmount").getAsString()));
                                        taxTotalAmt+=Double.parseDouble(String.valueOf(objBillTaxDtl.getTaxAmount()));

                                        arrListBillTaxDtl.add(objBillTaxDtl);
                                    }
                                }


                                listPromotionItems=new ArrayList<clsPromotionItems>();


                                JsonArray jsonArrBillPromoDtl = (JsonArray) jObj.get("BillPromoDetails");
                                if(jsonArrBillPromoDtl.size()>0)
                                {
                                    for (int i = 0; i < jsonArrBillPromoDtl.size(); i++)
                                    {
                                        mJsonObject = (JsonObject) jsonArrBillPromoDtl.get(i);
                                        if (mJsonObject.get("PromoCode").getAsString().equals(""))
                                        {
                                        }
                                        else
                                        {
                                            clsPromotionItems objBillPromoDtl=new clsPromotionItems();
                                            objBillPromoDtl.setItemCode(mJsonObject.get("ItemCode").getAsString());
                                            objBillPromoDtl.setPromoCode(mJsonObject.get("PromoCode").getAsString());
                                            objBillPromoDtl.setPromoType(mJsonObject.get("PromoType").getAsString());
                                            objBillPromoDtl.setBillAmt(mJsonObject.get("PromoAmt").getAsString());
                                            objBillPromoDtl.setFreeItemQty(Double.valueOf(mJsonObject.get("PromoQty").getAsString()));
                                            listPromotionItems.add(objBillPromoDtl);
                                        }
                                    }
                                }

                                JsonArray jsonArrBillPromoDiscDtl = (JsonArray) jObj.get("BillPromoDiscDetails");
                                if(jsonArrBillPromoDiscDtl.size()>0)
                                {
                                    for (int i = 0; i < jsonArrBillPromoDiscDtl.size(); i++)
                                    {
                                        mJsonObject = (JsonObject) jsonArrBillPromoDiscDtl.get(i);
                                        clsPromotionItems objBillPromoDtl=new clsPromotionItems();
                                        objBillPromoDtl.setItemCode(mJsonObject.get("ItemCode").getAsString());
                                        objBillPromoDtl.setItemName(mJsonObject.get("ItemName").getAsString());
                                        objBillPromoDtl.setPromoType(mJsonObject.get("PromoType").getAsString());
                                        objBillPromoDtl.setDiscOnAmt(Double.valueOf(mJsonObject.get("DiscountOnAmt").getAsString()));
                                        objBillPromoDtl.setDiscAmt(Double.valueOf(mJsonObject.get("DiscountAmt").getAsString()));
                                        objBillPromoDtl.setDiscPer(Double.valueOf(mJsonObject.get("DiscountPer").getAsString()));
                                        listPromotionItems.add(objBillPromoDtl);
                                        discTotalAmt+=Double.valueOf(mJsonObject.get("DiscountAmt").getAsString());
                                    }
                                }


                                grandTotal = (subTotalAmt + taxTotalAmt) - discTotalAmt;
                                String finalresult = new Double(grandTotal).toString();
                                edtAmount.setText(finalresult);
                                if(arrListItemDtl.size()>0)
                                {
                                    arrListMakeBillItemDtls = arrListItemDtl;
                                    funFillListView(arrListItemDtl);
                                }

                                if(arrListBillTaxDtl.size()>0)
                                {
                                    arrListMakeBillTaxDtls=arrListBillTaxDtl;
                                    funFillBillTaxDtlGrid(arrListBillTaxDtl);

                                }
                                ArrayList<clsTaxCalculationDtls> arrListBillDiscDtl=new ArrayList<clsTaxCalculationDtls>();
                                clsTaxCalculationDtls objBillDiscDtl=new clsTaxCalculationDtls();
                                objBillDiscDtl.setTaxName("Subtotal");
                                objBillDiscDtl.setTaxAmount(subTotalAmt);
                                arrListBillDiscDtl.add(objBillDiscDtl);
                                objBillDiscDtl=new clsTaxCalculationDtls();
                                objBillDiscDtl.setTaxName("Discount");
                                objBillDiscDtl.setTaxAmount(discTotalAmt);
                                arrListBillDiscDtl.add(objBillDiscDtl);
                                funFillBillDiscDtlGrid(arrListBillDiscDtl);


                                dismissDialog();


                            } catch (Exception e) {
                                e.printStackTrace();
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
                SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, R.string.setup_your_server_settings);
        }
    }



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
                            Toast.makeText(clsMakeBillScreen.mActivity, "BillNo=" + billNo, Toast.LENGTH_LONG).show();
                            funSaveBill();

                            /*

                            funInsertBillHd(billNo, cardNo);
                            if(null!=arrListMakeBillTaxDtls)
                            {
                                funInsertBillTaxDtl(billNo);
                            }
                            funInsertBillModifierDtl(billNo);
                            if(clsGlobalFunctions.gActivePromotions.equals("Y"))
                            {
                                if(listPromotionItems.size()>0)
                                {
                                    funInsertPromotionDtl(billNo);
                                }
                            }
                            funInsertBillDtl(billNo, cardNo);
                            */
                        }
                        else
                        {
                            Toast.makeText(clsMakeBillScreen.mActivity, "Error!!", Toast.LENGTH_LONG).show();
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
            SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, "No internet available or not connected to any network");
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
                SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, R.string.setup_your_server_settings);
        }
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
                            try {
                               // new clsUtility().funPrintBillFormatWise(jObj, "makeBill");
                                funPrintBillFormat(jObj);

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



    public void funLoadMakeBillTable() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                showDialog();
                App.getAPIHelper().funGetBusyTableList(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gCMSIntegrationYN, clsGlobalFunctions.gTreatMemberAsTable, new BaseAPIHelper.OnRequestComplete<ArrayList<clsTableMaster>>() {

                    @Override
                    public void onSuccess(ArrayList<clsTableMaster> arrListTemp) {
                        dismissDialog();
                        if (null != arrListTemp) {
                            if (arrListTemp.size() > 0)
                            {
                                clsMakeBillScreen obj = (clsMakeBillScreen) getActivity();
                                obj.funRefreshTableGrid(arrListTemp);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage, int errorCode) {
                        dismissDialog();
                    }
                });
            } else {
                SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, R.string.setup_your_server_settings);
        }
    }



    private void funCheckPromotion(final String tbleNo,String applyPromotion,String flgApplyPromoOnBill)
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                App.getAPIHelper().funGetTableBillData(clsGlobalFunctions.gPOSCode, tbleNo, clsGlobalFunctions.gClientCode, clsGlobalFunctions.gPOSDate, applyPromotion,flgApplyPromoOnBill, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        if (null != jObj) {
                            try
                            {
                                JsonArray jsonArrBillPromoDtl = (JsonArray) jObj.get("BillPromoDetails");
                                JsonArray jsonArrBillPromoDiscDtl = (JsonArray) jObj.get("BillPromoDiscDetails");
                                if(jsonArrBillPromoDtl.size()>0 || jsonArrBillPromoDiscDtl.size()>0)
                                {
                                    if(clsGlobalFunctions.gPopUpToApplyPromotionsOnBill.equals("Y"))
                                    {
                                        AlertDialog.Builder builder1 = new AlertDialog.Builder(clsMakeBillScreen.mActivity);
                                        builder1.setMessage("Do want to Calculate Promotions for this Bill??");
                                        builder1.setCancelable(true);
                                        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id)
                                                    {
                                                        funGetReasonList();
                                                        String applyPromotion = "Yes";
                                                        String flgApplyPromoOnBill = "Yes";
                                                        //new LoadItemList().execute(tableNo,applyPromotion,flgApplyPromoOnBill);
                                                        funLoadItemList(tbleNo,applyPromotion,flgApplyPromoOnBill);

                                                    }
                                                }
                                        );

                                        builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                                    public void onClick(DialogInterface dialog, int id)
                                                    {
                                                        dialog.cancel();
                                                        String applyPromotion = "Yes";
                                                        String flgApplyPromoOnBill = "No";
                                                        // new LoadItemList().execute(tableNo, applyPromotion, flgApplyPromoOnBill);
                                                        funLoadItemList(tbleNo,applyPromotion,flgApplyPromoOnBill);

                                                    }
                                                }
                                        );
                                        AlertDialog alert11 = builder1.create();
                                        alert11.show();
                                    }
                                    else
                                    {
                                        funGetReasonList();
                                        String applyPromotion = "Yes";
                                        String flgApplyPromoOnBill = "Yes";
                                        funLoadItemList(tbleNo,applyPromotion,flgApplyPromoOnBill);
                                    }

                                }
                                else
                                {
                                    String applyPromotion = "Yes";
                                    String flgApplyPromoOnBill = "No";
                                    funLoadItemList(tbleNo,applyPromotion,flgApplyPromoOnBill);
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
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
                SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, R.string.setup_your_server_settings);
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
                                    String reasonName= (String) itrReason.next();
                                    reasonList.add(reasonName);
                                }

                                AlertDialog.Builder builder1 = new AlertDialog.Builder(clsMakeBillScreen.mActivity);
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
                                                Toast.makeText(clsMakeBillScreen.mActivity, "Enter Reason & Remark", Toast.LENGTH_LONG).show();
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
                SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, R.string.setup_your_server_settings);
        }
    }


    private void funSelectReasonAndRemark()
    {
        final Dialog dialog = new Dialog(clsMakeBillScreen.mActivity);
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
                (clsMakeBillScreen.mActivity, R.layout.spinneritemtextview,reasonList);

        dataAdapter.setDropDownViewResource
                (R.layout.spinnerdropdownitem);
        spinnerReason.setAdapter(dataAdapter);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                discReasonRemark= String.valueOf(hmReason.get(spinnerReason.getSelectedItem().toString()))+"#"+spinnerReason.getSelectedItem().toString();
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

    private void funSaveBill() {

        JsonObject jObjBillData=new JsonObject();
        String cardNo ="";
        jObjBillData.add("BillHDData", funInsertBillHd(billNo, cardNo));
        if(null!=arrListMakeBillTaxDtls)
        {
            jObjBillData.add("BillTaxData", funInsertBillTaxDtl(billNo));
        }
        jObjBillData.add("BillModifierData", funInsertBillModifierDtl(billNo));
        if(clsGlobalFunctions.gActivePromotions.equals("Y"))
        {
            if(listPromotionItems.size()>0)
            {
                jObjBillData.add("BillPromoDiscountData", funInsertPromotionDtl(billNo));
            }
        }

        jObjBillData.add("BillDtlData", funInsertBillDtl(billNo,cardNo));

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
                                    LinkedTreeMap<String,String> hmBill= (LinkedTreeMap<String, String>) listResponse.get(i);
                                    String cardNo ="";
                                    for (Map.Entry<String, String> entry : hmBill.entrySet()) {

                                        if(entry.getKey().equals("NoBillSeries")){
                                            Toast.makeText(clsMakeBillScreen.mActivity, "Please Create Bill Series", Toast.LENGTH_SHORT).show();
                                            return ;
                                        }
                                        if(entry.getKey().equals("GenaratedBillSeriesNo")){
                                            billNo=entry.getValue();
                                            Toast.makeText(clsMakeBillScreen.mActivity, "BillNo=" + billNo, Toast.LENGTH_LONG).show();
                                        }
                                        response=entry.getValue();

                                        if (response.equals("BillDtl"))
                                        {

                                            funGenerateBillTextFile(billNo);
                                            billNo = "";
                                            funClearObjects();
                                            funResetFields();
                                            System.out.println(response);
                                            funLoadMakeBillTable();
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
                SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, R.string.setup_your_server_settings);
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

                Toast.makeText(getActivity(), "Bill Printed from Bluetooth Printer" + sbPrintBill.toString(), Toast.LENGTH_LONG).show();
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
            pgDialog = CommonUtils.getProgressDialog(clsMakeBillScreen.mActivity, 0, false);
        }
        pgDialog.show();
    }

    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }



}