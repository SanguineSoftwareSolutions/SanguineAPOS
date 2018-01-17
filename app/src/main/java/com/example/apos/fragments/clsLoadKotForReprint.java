package com.example.apos.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsReprintDocuments;
import com.example.apos.adapter.clsReprintKotListAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsVoidKotListDtl;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsReprintKotActListener;
import com.example.apos.listeners.clsReprintKotSelectionListener;
import com.example.apos.util.clsUtility;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.ArrayList;

public class clsLoadKotForReprint extends Fragment implements clsReprintKotSelectionListener
{
    public static clsReprintKotSelectionListener kotSelectionListener;
    private static GridView gridviewKotList;
    private static ArrayList arrListKotMaster;
    public clsReprintKotActListener objReprintKotActListener;
    static clsReprintKotListAdapter listAdapter;
    private ConnectivityManager connectivityManager;
    private Dialog pgDialog;
    Activity mActivity;
    String reprintOperation="";
    TextView tvHeader1,tvHeader2,tvHeader3,tvHeader4,tvHeader5,tvHeader6;
    public static clsLoadKotForReprint getInstance()
    {
        clsLoadKotForReprint mLoadKot = new clsLoadKotForReprint();
        return mLoadKot;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mActivity= clsReprintDocuments.mActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.loadkotforreprintkot, container, false);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        mActivity= clsReprintDocuments.mActivity;
        reprintOperation =clsReprintDocuments.reprintOperation;
        try
        {
            kotSelectionListener = (clsReprintKotSelectionListener) clsLoadKotForReprint.this;
            objReprintKotActListener=(clsReprintKotActListener)getActivity();
        }
        catch(Exception ex)
        {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }
        gridviewKotList = (GridView) rootView.findViewById(R.id.gvKotLoadedlistForReprint);
        tvHeader1 = (TextView) rootView.findViewById(R.id.tv_reprintlist_Header1);
        tvHeader2 = (TextView) rootView.findViewById(R.id.tv_reprintlist_Header2);
        tvHeader3 = (TextView) rootView.findViewById(R.id.tv_reprintlist_Header3);
        tvHeader4 = (TextView) rootView.findViewById(R.id.tv_reprintlist_Header4);
        tvHeader5 = (TextView) rootView.findViewById(R.id.tv_reprintlist_Header5);
        tvHeader6 = (TextView) rootView.findViewById(R.id.tv_reprintlist_Header6);
        if (new clsUtility().isInternetConnectionAvailable(connectivityManager))
        {
            if(reprintOperation.equals("Bill")){
                funLoadBillListWS();
            }
            else if(reprintOperation.equals("DirectBiller")){
                funLoadDirectBillListWS();
            }
            else if(reprintOperation.equals("KOT")){
                funLoadKotListWS();
            }
        }
        else
        {
            Toast.makeText(getActivity(),"Internet Connection Not Found",Toast.LENGTH_LONG).show();
        }

        return rootView;
    }

    private void funSetReprintListHeaders(String arr[])
    {
        tvHeader1.setText(arr[0]);
        tvHeader2.setText(arr[1]);
        tvHeader3.setText(arr[2]);
        tvHeader4.setText(arr[3]);
        tvHeader5.setText(arr[4]);
        tvHeader6.setText(arr[5]);
    }

    private static void funFillGridView(ArrayList arrlist)
    {
        listAdapter=new clsReprintKotListAdapter(clsReprintDocuments.mActivity,clsReprintDocuments.mActivity, arrlist,kotSelectionListener);
        gridviewKotList.setAdapter(listAdapter);
    }

    @Override
    public void funReprintKotDetails(String kotNo,String tableNo,String posCode)
    {
        //Toast.makeText(getActivity(), "KotList", Toast.LENGTH_LONG).show();
        //clsVoidKotScreen.setSelectedKotItemList(strKotNo);
        objReprintKotActListener.setSelectedKotDetails(kotNo, tableNo, posCode, reprintOperation);
    }

    public void funLoadKotListWS()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                String tableNo="All";
                showDialog();
                App.getAPIHelper().funGetLiveKOTList(clsReprintDocuments.selectedPOS, tableNo, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsVoidKotListDtl>>() {
                    @Override
                    public void onSuccess(ArrayList<clsVoidKotListDtl> arrListTemp) {
                        dismissDialog();
                        if (null != arrListTemp) {
                            if (arrListTemp.size() > 0) {
                                arrListKotMaster=arrListTemp;
                                String arr[]={"KOT NO","Waiter","Table","User","Amt","POS"};
                                funSetReprintListHeaders(arr);
                                funFillGridView(arrListTemp);

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


    public void funLoadBillListWS()
    {
            if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
                if (ConnectivityHelper.isConnected())
                {
                    showDialog();
                    App.getAPIHelper().funLoadReprintBillList(clsReprintDocuments.selectedPOS,clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<JsonArray>() {
                        @Override
                        public void onSuccess(JsonArray jarrListTemp) {
                            dismissDialog();
                            if (null != jarrListTemp) {
                                if (jarrListTemp.size() > 0) {
                                    ArrayList arrList=new ArrayList<clsVoidKotListDtl>();
                                    JsonObject mJsonObject = new JsonObject();
                                    clsVoidKotListDtl obList=new clsVoidKotListDtl();
                                    for(int j=0;j<jarrListTemp.size();j++){
                                        mJsonObject=(JsonObject)jarrListTemp.get(j);
                                        obList=new clsVoidKotListDtl();
                                        obList.setStrKotNo(mJsonObject.get("BillNo").getAsString());
                                        obList.setStrTableName(mJsonObject.get("TableName").getAsString());
                                        obList.setStrWaiterName(mJsonObject.get("Time").getAsString());
                                        obList.setStrAmount(Double.parseDouble(mJsonObject.get("Amount").getAsString()));
                                        obList.setStrPOSName(clsReprintDocuments.hmPOSCodeName.get(mJsonObject.get("POSCode").getAsString()));
                                        obList.setStrTableNo("");
                                        arrList.add(obList);
                                    }
                                    arrListKotMaster=arrList;
                                    String arr[]={"BILL NO","Time","Table","User","Amt","POS"};
                                    funSetReprintListHeaders(arr);
                                    funFillGridView(arrList);
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


    public void funLoadDirectBillListWS()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                showDialog();
                App.getAPIHelper().funLoadDirectBillListWS(clsReprintDocuments.selectedPOS,clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<JsonArray>() {
                    @Override
                    public void onSuccess(JsonArray jarrListTemp) {
                        dismissDialog();
                        if (null != jarrListTemp) {
                            if (jarrListTemp.size() > 0) {
                                ArrayList arrList=new ArrayList<clsVoidKotListDtl>();
                                JsonObject mJsonObject;
                                clsVoidKotListDtl obList;
                                for(int j=0;j<jarrListTemp.size();j++){
                                    mJsonObject=(JsonObject)jarrListTemp.get(j);
                                    obList=new clsVoidKotListDtl();
                                    obList.setStrKotNo(mJsonObject.get("BillNo").getAsString());
                                    obList.setStrTableName(mJsonObject.get("Time").getAsString());
                                    obList.setStrWaiterName("");
                                    obList.setStrAmount(Double.parseDouble(mJsonObject.get("Amount").getAsString()));
                                    obList.setStrPOSName(mJsonObject.get("POSCode").getAsString());
                                    obList.setStrTableNo("");
                                    arrList.add(obList);

                                }
                                arrListKotMaster=arrList;
                                String arr[]={"BILL NO"," ","Time","User","Amt","POS"};
                                funSetReprintListHeaders(arr);
                                funFillGridView(arrList);
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
