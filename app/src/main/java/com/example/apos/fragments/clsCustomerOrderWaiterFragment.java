package com.example.apos.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsCustomerOrderScreen;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.adapter.clsKotWaiterAdapter;
import com.example.apos.adapter.clskotMenuItemsGridAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsKotMenuItemsBean;
import com.example.apos.bean.clsWaiterMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsKotMenuItemSelectionListener;
import com.example.apos.listeners.clsKotWaiterListSelectionListener;
import com.example.apos.util.clsUtility;

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

/**
 * Created by User on 29-04-2017.
 */

public class clsCustomerOrderWaiterFragment  extends Fragment implements clsKotWaiterListSelectionListener
{
    private clsKotWaiterListSelectionListener waiterSelectionListener;
    private ConnectivityManager connectivityManager;
    private GridView kotwaiterGridview;
    public ArrayList arrListWaiterMaster;
    private Dialog pgDialog;
    Activity mActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mActivity=getActivity();
        // TODO Auto-generated method stub
        View myFragmentView = inflater.inflate(R.layout.customerorderwaiterlist, container, false);
        funWidget(myFragmentView);
        try
        {
            waiterSelectionListener = (clsKotWaiterListSelectionListener) clsCustomerOrderWaiterFragment.this;
        }
        catch(Exception ex)
        {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }
        if (new clsUtility().isInternetConnectionAvailable(connectivityManager))
        {
            //new clsWaiterWebService().execute();
            funGetWaiterList();
        }
        else
        {
            Toast.makeText(getActivity(),"Internet Connection Not Found",Toast.LENGTH_LONG).show();
        }

        return myFragmentView;
    }

    private void funWidget(View myFragmentView)
    {
        kotwaiterGridview = (GridView) myFragmentView.findViewById(R.id.kotwaiter_gridview);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    @Override
    public void getWaiterListSelected(String strWaiterNo, String strWaiterName)
    {
        clsCustomerOrderScreen.screenName="MenuScreen";
        clsCustomerOrderScreen.txtWaiterName.setText(Html.fromHtml( "<b>"+"WAITER"+ "</b>" +
                "<br>"+strWaiterName+"<br/>"));
        clsCustomerOrderScreen.waiterNo=strWaiterNo;
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

    private void funFillWaiterGrid(ArrayList arrListWaiterMaster)
    {
        kotwaiterGridview.setAdapter(new clsKotWaiterAdapter(getActivity(),this.getActivity(), arrListWaiterMaster,waiterSelectionListener));
       // clsCustomerOrderScreen.btnRefresh.setVisibility(View.VISIBLE);
    }

    public void funGetWaiterList() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetWaiterList(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gUserCode, true, new BaseAPIHelper.OnRequestComplete<ArrayList<clsWaiterMaster>>() {
                    @Override
                    public void onSuccess(ArrayList<clsWaiterMaster> arrListTemp) {
                        dismissDialog();
                        if (null != arrListTemp) {
                            if (arrListTemp.size() > 0) {
                                arrListWaiterMaster=arrListTemp;
                                funFillWaiterGrid(arrListWaiterMaster);
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
