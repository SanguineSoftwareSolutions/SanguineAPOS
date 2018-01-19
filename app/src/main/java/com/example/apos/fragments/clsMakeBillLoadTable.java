package com.example.apos.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsMakeBillScreen;
import com.example.apos.adapter.clsMakeBillTableAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsMakeBillActListener;
import com.example.apos.listeners.clsMakeBillTableListener;
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

public class clsMakeBillLoadTable extends Fragment implements clsMakeBillTableListener
{
    private static clsMakeBillTableListener objMakeBillTableListener;
    private static GridView makeBillTableGridview;
    ArrayList arrListMakeBillTableMaster;
    ArrayList arrListMakeBillRefeshTableMaster;
    private static clsMakeBillActListener objMakeBillActListener;
    private static clsMakeBillTableAdapter objTableAdapter;
    private ConnectivityManager connectivityManager;
    private Dialog pgDialog;

    public static clsMakeBillLoadTable getInstance() {
        clsMakeBillLoadTable mKotTable = new clsMakeBillLoadTable();
        return mKotTable;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.makebillloadtable, container, false);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        try
        {
            objMakeBillTableListener = (clsMakeBillTableListener) clsMakeBillLoadTable.this;
            objMakeBillActListener=(clsMakeBillActListener)getActivity();
        }
        catch(Exception ex)
        {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }
        arrListMakeBillTableMaster=new ArrayList();
        arrListMakeBillRefeshTableMaster=new ArrayList();

        funGetBusyTableList();
        return rootView;
    }


    @Override
    public void onActivityCreated (Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        makeBillTableGridview=(GridView) getView().findViewById(R.id.makeBillTable_Gridview);

    }

    @Override
    public void funGetSelectedTable(String tableNo, String tableName)
    {
        objMakeBillActListener.funSetSelectedTableData(tableNo, tableName);
    }


    public void funGetBusyTableList() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                showDialog();
                App.getAPIHelper().funGetBusyTableList(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gCMSIntegrationYN, clsGlobalFunctions.gTreatMemberAsTable, new BaseAPIHelper.OnRequestComplete<ArrayList<clsTableMaster>>() {
                    @Override
                    public void onSuccess(ArrayList<clsTableMaster> arrListTemp) {
                        dismissDialog();
                        if (null != arrListTemp) {
                            if (arrListTemp.size() > 0) {
                                arrListMakeBillTableMaster=arrListTemp;
                                funFillTableGrid(arrListTemp);

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

    public void funFillTableGrid(ArrayList arrListTableMaster)
    {
        objTableAdapter=new clsMakeBillTableAdapter(clsMakeBillScreen.mActivity, clsMakeBillScreen.mActivity, arrListTableMaster, objMakeBillTableListener);
        makeBillTableGridview.setAdapter(objTableAdapter);
    }

    /**
     * Progressbar methods for show and dismiss.
     */
    protected void showDialog() {
        if (null == pgDialog) {
            pgDialog = CommonUtils.getProgressDialog(getActivity(), 0, false);
        }
        pgDialog.show();
    }

    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }

}