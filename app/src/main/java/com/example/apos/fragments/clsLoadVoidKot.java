package com.example.apos.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsKotScreen;
import com.example.apos.activity.clsMakeBillScreen;
import com.example.apos.activity.clsMiniMakeKotScreen;
import com.example.apos.activity.clsVoidKotScreen;
import com.example.apos.adapter.clsKotSelectedItemsCustomBaseAdapter;
import com.example.apos.adapter.clsOrderDialogAdapter;
import com.example.apos.adapter.clsVoidKotItemListAdapter;
import com.example.apos.adapter.clsVoidKotListAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsItemModifierBean;
import com.example.apos.bean.clsKOTItemDtlBean;
import com.example.apos.bean.clsMakeBillItemDtls;
import com.example.apos.bean.clsReasonMaster;
import com.example.apos.bean.clsVoidKotListDtl;
import com.example.apos.bean.clsWaiterMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsVoidKotItemListListener;
import com.example.apos.listeners.clsVoidKotListSelectionListener;
import com.example.apos.util.Utils;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by sanguine on 9/19/2015.
 */
public class clsLoadVoidKot extends Fragment implements clsVoidKotItemListListener,View.OnClickListener,clsVoidKotItemListAdapter.customButtonListener
{
    private static ListView listKotItems;
    ArrayList arrListTableMaster;
    private static ArrayList<clsKOTItemDtlBean> arrListKOT;
    private static ArrayList<clsKOTItemDtlBean> arrListVoidKOTList=new ArrayList<clsKOTItemDtlBean>();
    public static clsVoidKotItemListListener kotItemSelectionListener;
    public static  clsGlobalFunctions objGlobal;
    static clsVoidKotItemListAdapter kotItemListAdapter;
    private static EditText edtKotTotalAmount = null;
    public static String KOTNo="";
    private static double subTotalAmt;
    private Button btnVoidKotBack = null, btnVoidKotDone = null,btnVoidFullKot = null;
    private ConnectivityManager connectivityManager;
    private Dialog pgDialog;
    public static TreeMap hmReason;
    public static ArrayList<String> reasonList;
    String reasonName="",reasonCode="",remark;



    public static clsLoadVoidKot getInstance()
    {
        clsLoadVoidKot mLoadKot = new clsLoadVoidKot();
        return mLoadKot;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.loadvoidkot, container, false);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        btnVoidKotBack = (Button)rootView.findViewById(R.id.btnvoidkotback);
        btnVoidKotDone=(Button)rootView.findViewById(R.id.btnvoidkotdone);
        btnVoidFullKot=(Button)rootView.findViewById(R.id.btnvoidkotfullkot);
        btnVoidKotBack.setOnClickListener(this);
        btnVoidKotDone.setOnClickListener(this);
        btnVoidFullKot.setOnClickListener(this);
        edtKotTotalAmount=(EditText)rootView.findViewById(R.id.edt_void_kot_total_order_amount);

        try
        {
            kotItemSelectionListener = (clsVoidKotItemListListener) clsLoadVoidKot.this;
        }
        catch(Exception ex)
        {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }
        subTotalAmt=0;
        listKotItems = (ListView) rootView.findViewById(R.id.listvoidkotselecteditems);

        funGetReasonList();


        return rootView;
    }




    public  void funLoadKotItemList(String kotNo)
    {
        KOTNo=kotNo;
        funLoadKotItemListWS(kotNo);
    }



    private  void funFillListView(ArrayList arrlist)
    {

        kotItemListAdapter=new clsVoidKotItemListAdapter(clsVoidKotScreen.mActivity,clsVoidKotScreen.mActivity, arrlist,kotItemSelectionListener);
        kotItemListAdapter.setCustomButtonListner(getInstance());
        listKotItems.setAdapter(kotItemListAdapter);

    }



    @Override
    public void getVoidKotItemList()
    {

    }

    @Override
    public void onClick(View v)
    {

        switch(v.getId()){
            case R.id.btnvoidkotback:
                funClearObjects();
                getActivity().finish();
                break;

            case R.id.btnvoidkotdone:
               // new VoidKOTWS().execute(KOTNo,"ItemVoid");
                if(KOTNo.isEmpty())
                {
                    Toast.makeText(clsVoidKotScreen.mActivity, "KOT Data not available to void", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    funVoidKOTWS(KOTNo,"ItemVoid");
                }
                break;


            case R.id.btnvoidkotfullkot:
                if(KOTNo.isEmpty())
                {
                    Toast.makeText(clsVoidKotScreen.mActivity, "KOT Data not available to void", Toast.LENGTH_LONG).show();
                    return;
                }
                else
                {
                    funSelectReasonAndRemark("FullVoid");
                    //funVoidKOTWS(KOTNo,"FullVoid"); // call after open reason dialog
                }

                break;

            default:
                break;
        }


    }




    private void funRefreshItemGrid()
    {



        int iListViewScrollPosition = -1;
        kotItemListAdapter=new clsVoidKotItemListAdapter(clsVoidKotScreen.mActivity,clsVoidKotScreen.mActivity, arrListKOT,kotItemSelectionListener);
        listKotItems.setItemsCanFocus(true);
        listKotItems.setTranscriptMode(AbsListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        kotItemListAdapter.setCustomButtonListner(getInstance());
        listKotItems.setAdapter(kotItemListAdapter);
        listKotItems.setSelection(kotItemListAdapter.getCount() - 1);
        if(iListViewScrollPosition != -1){
            listKotItems.setSelection(iListViewScrollPosition);
        }
        subTotalAmt=0;
        for(int l = 0; l < arrListKOT.size(); l++){
            subTotalAmt = subTotalAmt + arrListKOT.get(l).getDblAmount();
            edtKotTotalAmount.setText(String.valueOf(subTotalAmt));
        }
    }



    private void funClearObjects()
    {

        arrListKOT=null;

    }

    @Override
    public void onButtonClickListner(int position, String value)
    {
        String text=value.split("#")[1];

        AlertDialog.Builder builder1 = new AlertDialog.Builder(clsVoidKotScreen.mActivity);
        builder1.setMessage("Do You Want to add Reason and Remark???");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        funSelectReasonAndRemark("");
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



        if(value.split("#")[1].equals("deleterow"))
        {
            clsKOTItemDtlBean objKot = (clsKOTItemDtlBean) arrListKOT.get(position);
            arrListVoidKOTList.add(objKot);
            String strDeleteItemCode = arrListKOT.get(position).getStrItemCode();
            arrListKOT.remove(position);

            if(arrListKOT.size()>0 )
            {
                for (int cnt = 0; cnt <= arrListKOT.size(); cnt++)
                {
                    clsKOTItemDtlBean obj=null;
                    if(cnt==arrListKOT.size())
                    {
                        obj= (clsKOTItemDtlBean) arrListKOT.get(cnt-1);
                        if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
                        {
                            arrListVoidKOTList.add(objKot);
                            arrListKOT.remove(obj);
                        }
                        else
                        {
                            if(cnt>=2)
                            {
                                obj = (clsKOTItemDtlBean) arrListKOT.get(cnt - 2);
                                if (obj.getStrItemCode().contains(strDeleteItemCode + "M")) {
                                    arrListVoidKOTList.add(objKot);
                                    arrListKOT.remove(obj);
                                }
                            }
                        }
                    }
                    else
                    {
                        obj = (clsKOTItemDtlBean) arrListKOT.get(cnt);
                        if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
                        {
                            arrListVoidKOTList.add(objKot);
                            arrListKOT.remove(obj);
                        }
                        else
                        {
                            if(cnt!=0)
                            {
                                obj= (clsKOTItemDtlBean) arrListKOT.get(cnt-1);
                                if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
                                {
                                    arrListVoidKOTList.add(objKot);
                                    arrListKOT.remove(obj);
                                }
                            }
                        }
                    }
                }
            }

            funRefreshItemGrid();
        }

        else if(value.split("#")[1].equals("qtyrow"))
        {
            clsKOTItemDtlBean objKot = (clsKOTItemDtlBean) arrListKOT.get(position);
            double qty1 = 0;
            double qty = objKot.getDblItemQuantity();
            double amt = objKot.getDblAmount();
            double rate = objKot.getDblRate();

            if (qty > 1)
            {
                qty = qty - 1;
                amt = qty * rate;
                objKot.setDblItemQuantity(qty);
                objKot.setDblAmount(amt);

                double voidedQty=1;
                double voidedAmt=voidedQty*rate;

                boolean flgKOTPresent=false;
                for(int cnt=0;cnt<arrListVoidKOTList.size();cnt++)
                {
                    clsKOTItemDtlBean objVoidedKOT=arrListVoidKOTList.get(cnt);
                    if(objKot.getStrItemCode().equals(objVoidedKOT.getStrItemCode()))
                    {
                        flgKOTPresent=true;
                        voidedQty=objVoidedKOT.getDblItemQuantity()+1;
                        voidedAmt=voidedQty*rate;
                        objVoidedKOT.setDblItemQuantity(voidedQty);
                        objVoidedKOT.setDblAmount(voidedAmt);
                        arrListVoidKOTList.set(cnt,objVoidedKOT);
                        break;
                    }
                }

                if(!flgKOTPresent) {

                    clsKOTItemDtlBean objVoidedKOTItem = new clsKOTItemDtlBean("1", objKot.getStrTableNo()
                        , objKot.getStrCardNo(), objKot.getDblRedeemAmt(), objKot.getStrHomeDelivery(), objKot.getStrCustomerCode()
                        , clsGlobalFunctions.gPOSCode, objKot.getStrItemCode(), objKot.getStrItemName().toString()
                        , voidedQty, objKot.getDblAmount(), objKot.getStrWaiterNo(), objKot.getStrKOTNo()
                        , 1, "Y", objKot.getStrManualKOTNo(), clsGlobalFunctions.gUserCode,
                        clsGlobalFunctions.gUserCode, objGlobal.funGetCurrentDateTime(), objGlobal.funGetCurrentDateTime(), "No"
                        , "N", "N", "N", "N", objKot.getStrTakeAwayYesNo(), objKot.getStrActiveYN(), objKot.getDblBalance()
                        , objKot.getDblCreditLimit(), objKot.getStrCounterCode(), objKot.getStrPromoCode(), false,rate,objKot.getStrCardType());
                    arrListVoidKOTList.add(objVoidedKOTItem);
                }
                arrListKOT.set(position, objKot);
                funRefreshItemGrid();

            } else if (qty == 1)
            {
                clsKOTItemDtlBean objVoidedKOTItem=arrListKOT.get(position);
                arrListVoidKOTList.add(objVoidedKOTItem);
                String strDeleteItemCode = arrListKOT.get(position).getStrItemCode();
                arrListKOT.remove(position);

                if(arrListKOT.size()>0 )
                {
                    for (int cnt = 0; cnt <= arrListKOT.size(); cnt++)
                    {
                        clsKOTItemDtlBean obj=null;
                        if(cnt==arrListKOT.size())
                        {
                            obj= (clsKOTItemDtlBean) arrListKOT.get(cnt-1);
                            if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
                            {
                                arrListVoidKOTList.add(objKot);
                                arrListKOT.remove(obj);
                            }
                            else
                            {
                                if(cnt>=2)
                                {
                                    obj = (clsKOTItemDtlBean) arrListKOT.get(cnt - 2);
                                    if (obj.getStrItemCode().contains(strDeleteItemCode + "M")) {
                                        arrListVoidKOTList.add(objKot);
                                        arrListKOT.remove(obj);
                                    }
                                }
                            }
                        }
                        else
                        {
                            obj = (clsKOTItemDtlBean) arrListKOT.get(cnt);
                            if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
                            {
                                arrListVoidKOTList.add(objKot);
                                arrListKOT.remove(obj);
                            }
                            else
                            {
                                if(cnt!=0)
                                {
                                    obj= (clsKOTItemDtlBean) arrListKOT.get(cnt-1);
                                    if (obj.getStrItemCode().contains(strDeleteItemCode + "M"))
                                    {
                                        arrListVoidKOTList.add(objKot);
                                        arrListKOT.remove(obj);
                                    }
                                }
                            }
                        }
                    }
                }
                funRefreshItemGrid();
            }
        }
    }

    public  void funLoadKotItemListWS(String kotNo)
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                showDialog();
                App.getAPIHelper().funGetKOTData(clsGlobalFunctions.gPOSCode, kotNo, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        if (null != jObj)
                        {
                            arrListKOT=new ArrayList<clsKOTItemDtlBean>();
                            JsonArray mJsonArray = (JsonArray) jObj.get("KOTData");
                            JsonObject mJsonObject = new JsonObject();

                            for (int i = 0; i < mJsonArray.size(); i++) {
                                mJsonObject = (JsonObject) mJsonArray.get(i);
                                if (mJsonObject.get("KOTNo").toString().equals(""))
                                {
                                    //memberInfo = "no data";
                                } else
                                {

                                    double reedemAmt=Double.parseDouble(mJsonObject.get("RedeemAmt").getAsString());
                                    clsKOTItemDtlBean objItemDtl = new clsKOTItemDtlBean(mJsonObject.get("SerialNo").getAsString(), mJsonObject.get("TableNo").getAsString(), mJsonObject.get("CardNo").getAsString(), reedemAmt, "", mJsonObject.get("CustomerCode").getAsString(), clsGlobalFunctions.gPOSCode
                                            ,mJsonObject.get("ItemCode").getAsString() ,mJsonObject.get("ItemName").getAsString()
                                            ,Double.parseDouble(mJsonObject.get("ItemQty").getAsString()),Double.parseDouble(mJsonObject.get("Amount").getAsString())
                                            ,mJsonObject.get("WaiterNo").getAsString(), mJsonObject.get("KOTNo").getAsString(), 1, "Y", "", clsGlobalFunctions.gUserCode,
                                            clsGlobalFunctions.gUserCode, objGlobal.funGetCurrentDateTime(), objGlobal.funGetCurrentDateTime(), "No"
                                            , "N", "N", "N", "N", "", "","", "", "","",false,Double.parseDouble(mJsonObject.get("Rate").getAsString()), mJsonObject.get("CardType").getAsString());
                                    arrListKOT.add(objItemDtl);
                                }
                            }

                            if (arrListKOT.size() > 0)
                            {
                                double amount=0;
                                for(int cnt=0;cnt<arrListKOT.size();cnt++)
                                {
                                    clsKOTItemDtlBean objItemDtl=(clsKOTItemDtlBean)arrListKOT.get(cnt);
                                    amount=amount+Double.parseDouble(String.valueOf(objItemDtl.getDblAmount()));
                                    String finalresult = new Double(amount).toString();
                                    edtKotTotalAmount.setText(finalresult);
                                }
                                funFillListView(arrListKOT);
                                dismissDialog();
                            }
                        }
                        else{
                            dismissDialog();
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


    private void funVoidKOTWS(String KOTNo,String voidType) {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                JsonObject objVoidKOTDtl = new JsonObject();
                objVoidKOTDtl.addProperty("KOTNo",KOTNo);
                objVoidKOTDtl.addProperty("VoidType",voidType);
                clsUtility objUtility = new clsUtility();
                objVoidKOTDtl.addProperty("deviceName", objUtility.funGetHostName());
                objVoidKOTDtl.addProperty("macAddress", objUtility.funGetCurrentMACAddress(clsVoidKotScreen.mActivity));
                try
                 {

                     if(voidType.equals("FullVoid"))
                     {
                         JsonArray arrVoidKOT = new JsonArray();
                         JsonObject jObjUser=new JsonObject();

                         jObjUser.addProperty("User",clsGlobalFunctions.gUserCode);
                         JsonObject jObjClientCode=new JsonObject();
                         jObjClientCode.addProperty("ClientCode",clsGlobalFunctions.gClientCode);
                         JsonObject jObjDateTime=new JsonObject();
                         jObjDateTime.addProperty("DateTime",objGlobal.funGetCurrentDateTime());
                         JsonObject jObjVoidedDate=new JsonObject();
                         String dateTime=clsGlobalFunctions.gPOSDate;
                         String posDate=dateTime.replaceAll(" ","%20");
                         jObjVoidedDate.addProperty("VoidedDate", posDate);
                         JsonObject jObjReason=new JsonObject();
                         jObjReason.addProperty("Reason",reasonCode);
                         JsonObject jObjType=new JsonObject();
                         jObjType.addProperty("Type","");

                         JsonObject jObjManualKotNo=new JsonObject();
                         jObjManualKotNo.addProperty("ManualKOTNo","");
                         arrVoidKOT.add(jObjUser);
                         arrVoidKOT.add(jObjClientCode);
                         arrVoidKOT.add(jObjDateTime);
                         arrVoidKOT.add(jObjReason);
                         arrVoidKOT.add(jObjType);
                         arrVoidKOT.add(jObjManualKotNo);
                         arrVoidKOT.add(jObjVoidedDate);



                         objVoidKOTDtl.add("VoidKOTDtl", arrVoidKOT);

                     }
                     else
                     {
                         JsonArray arrKOTDtl = new JsonArray();
                         for (int cnt = 0; cnt < arrListKOT.size(); cnt++)
                         {
                             clsKOTItemDtlBean objKOTItemDtl = (clsKOTItemDtlBean) arrListKOT.get(cnt);
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
                             objRows.addProperty("strNCKotYN", objKOTItemDtl.getStrNCKotYN());
                             objRows.addProperty("strCustomerName", objKOTItemDtl.getStrCustomerName());
                             objRows.addProperty("strActiveYN", objKOTItemDtl.getStrActiveYN());
                             objRows.addProperty("dblBalance", objKOTItemDtl.getDblBalance());
                             objRows.addProperty("dblCreditLimit", objKOTItemDtl.getDblCreditLimit());
                             objRows.addProperty("strCounterCode", objKOTItemDtl.getStrCounterCode());
                             objRows.addProperty("strPromoCode", objKOTItemDtl.getStrPromoCode());
                             objRows.addProperty("dblRate", objKOTItemDtl.getDblRate());
                             objRows.addProperty("strCardType", objKOTItemDtl.getStrCardNo());

                             arrKOTDtl.add(objRows);
                         }
                         objVoidKOTDtl.add("KOTDtl", arrKOTDtl);

                         JsonArray arrVoidKOTDtl = new JsonArray();
                         for (int cnt = 0; cnt < arrListVoidKOTList.size(); cnt++)
                         {
                             clsKOTItemDtlBean objKOTItemDtl = (clsKOTItemDtlBean) arrListVoidKOTList.get(cnt);
                             JsonObject objRows = new JsonObject();
                             objRows.addProperty("TableNo", objKOTItemDtl.getStrTableNo());
                             objRows.addProperty("POSCode", objKOTItemDtl.getStrPOSCode());
                             objRows.addProperty("ItemCode", objKOTItemDtl.getStrItemCode());
                             objRows.addProperty("ItemName", objKOTItemDtl.getStrItemName());
                             objRows.addProperty("ItemQuantity", objKOTItemDtl.getDblItemQuantity());
                             objRows.addProperty("Amount", objKOTItemDtl.getDblAmount());
                             objRows.addProperty("WaiterNo", objKOTItemDtl.getStrWaiterNo());
                             objRows.addProperty("KOTNo", objKOTItemDtl.getStrKOTNo());
                             objRows.addProperty("ClientCode", clsGlobalFunctions.gClientCode);
                             objRows.addProperty("PaxNo", objKOTItemDtl.getIntPaxNo());
                             objRows.addProperty("Type", "");
                             objRows.addProperty("ReasonCode", reasonCode);
                             objRows.addProperty("UserCreated", objKOTItemDtl.getStrUserCreated());
                             objRows.addProperty("UserEdited", objKOTItemDtl.getStrUserEdited());
                             objRows.addProperty("DateCreated", objKOTItemDtl.getDteDateCreated());
                             String dateTime=clsGlobalFunctions.gPOSDate;
                             String posDate=dateTime.replaceAll(" ","%20");
                             objRows.addProperty("VoidedDate", posDate);
                             objRows.addProperty("ManualKOTNo", objKOTItemDtl.getStrManualKOTNo());
                             objRows.addProperty("PrintKOT", "Y");
                             arrVoidKOTDtl.add(objRows);
                         }
                         objVoidKOTDtl.add("VoidKOTDtl", arrVoidKOTDtl);
                     }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                showDialog();
                App.getAPIHelper().funVoidKOT(objVoidKOTDtl, new BaseAPIHelper.OnRequestComplete<HashMap>() {
                    @Override
                    public void onSuccess(HashMap map) {

                        if (null != map) {
                            try {
                                arrListKOT.clear();
                                arrListVoidKOTList.clear();
                                funRefreshItemGrid();
                                dismissDialog();
                                getActivity().finish();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else{
                            dismissDialog();
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage, int errorCode) {
                        dismissDialog();
                    }
                });
            } else {
                SnackBarUtils.showSnackBar(clsVoidKotScreen.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsVoidKotScreen.mActivity, R.string.setup_your_server_settings);
        }
    }






    public void funGetReasonList() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetReasonList("strKot", clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsReasonMaster>>() {
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
                                }else{
                                    dismissDialog();
                                }
                            }else{
                                dismissDialog();
                            }
                        }

                    @Override
                    public void onFailure(String errorMessage, int errorCode)
                    {
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


     private void funSelectReasonAndRemark(final String strFullVoid)
     {
         final Dialog dialog = new Dialog(clsVoidKotScreen.mActivity);
         dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
         dialog.setContentView(R.layout.dialogforreasonremark);
         final Spinner spinnerReason = (Spinner) dialog.findViewById(R.id.reasonSpinner);
         final EditText edtRemark= (EditText) dialog.findViewById(R.id.edtKotRemarks);
         final Button btnOk=(Button) dialog.findViewById(R.id.btnOk) ;
         final Button btnClose=(Button) dialog.findViewById(R.id.btnClose) ;
         CommonUtils.showKeyboard(edtRemark,true);
         ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                 (clsVoidKotScreen.mActivity, R.layout.spinneritemtextview,reasonList);

         dataAdapter.setDropDownViewResource
                 (R.layout.spinnerdropdownitem);
         spinnerReason.setAdapter(dataAdapter);

         btnOk.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                 CommonUtils.hideKeyboard(edtRemark);
                 reasonCode = String.valueOf(hmReason.get(spinnerReason.getSelectedItem().toString()));
                 if(edtRemark.getText().toString().isEmpty())
                 {
                     Toast.makeText(clsVoidKotScreen.mActivity,"Remark should not be empty!!",Toast.LENGTH_LONG).show();
                     return;
                 }
                 else
                 {
                     try {
                         remark = new clsUtility().funCheckSpecialCharacters(edtRemark.getText().toString());
                     }catch(Exception e){
                            e.printStackTrace();
                     }
                 }
                 if(strFullVoid.equals("FullVoid")){
                     funVoidKOTWS(KOTNo,"FullVoid");
                 }

                 dialog.dismiss();

             }
         });

         btnClose.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v)
             {
                 CommonUtils.hideKeyboard(edtRemark);
                 if(strFullVoid.equals("FullVoid")){
                     funVoidKOTWS(KOTNo,"FullVoid");
                 }
                 dialog.dismiss();
             }
         });
         dialog.show();
         Window window = dialog.getWindow();
         window.setGravity(Gravity.CENTER);
         window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
         window.getWindowManager().getDefaultDisplay();
     }



    /**
     * Progressbar methods for show and dismiss.
     */
    protected void showDialog() {
        if (null == pgDialog) {
            pgDialog = CommonUtils.getProgressDialog(clsVoidKotScreen.mActivity, 0, false);
        }
        pgDialog.show();
    }

    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }

}
