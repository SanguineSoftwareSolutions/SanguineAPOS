package com.example.apos.fragments;

import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsMakeBillScreen;
import com.example.apos.activity.clsVoidKotScreen;
import com.example.apos.adapter.clsVoidKotListAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.bean.clsVoidKotListDtl;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsVoidKotActListener;
import com.example.apos.listeners.clsVoidKotListSelectionListener;

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
import java.util.ArrayList;


public class clsVoidKotList extends Fragment implements clsVoidKotListSelectionListener
{
    public  static clsVoidKotListSelectionListener kotSelectionListener;
    private  static GridView gridviewKotList;
    public clsVoidKotActListener objVoidKotActListener;
    clsVoidKotListAdapter listAdapter;
    private Dialog pgDialog;

    public static clsVoidKotList getInstance()
    {
        clsVoidKotList mLoadKot = new clsVoidKotList();
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
        View rootView = inflater.inflate(R.layout.voidkotlist, container, false);

        try
        {
            kotSelectionListener = (clsVoidKotListSelectionListener) clsVoidKotList.this;
            objVoidKotActListener=(clsVoidKotActListener)getActivity();
        }
        catch(Exception ex)
        {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }
        gridviewKotList = (GridView) rootView.findViewById(R.id.gvkotloadedlist);

        return rootView;
    }

    public  void funLoadKotList(String tableNo)
    {
        funLoadKotListWS(tableNo);
    }


    private  void funFillGridView(ArrayList arrlist)
    {
        listAdapter=new clsVoidKotListAdapter(clsVoidKotScreen.mActivity,clsVoidKotScreen.mActivity, arrlist,kotSelectionListener);
        gridviewKotList.setAdapter(listAdapter);
    }




    @Override
    public void getVoidKotListSelected(String strKotNo)
    {
        Toast.makeText(getActivity(),"KotList",Toast.LENGTH_LONG).show();
        objVoidKotActListener.setSelectedKotItemList(strKotNo);
    }




    public  void funLoadKotListWS(String tblNo)
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                showDialog();
                App.getAPIHelper().funGetLiveKOTList(clsGlobalFunctions.gPOSCode, tblNo, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsVoidKotListDtl>>() {
                    @Override
                    public void onSuccess(ArrayList<clsVoidKotListDtl> arrListTemp) {
                        if (null != arrListTemp) {
                            if (arrListTemp.size() > 0) {
                                funFillGridView(arrListTemp);
                                dismissDialog();
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
                SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsMakeBillScreen.mActivity, R.string.setup_your_server_settings);
        }
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
