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
import com.example.apos.activity.clsKotScreen;
import com.example.apos.adapter.clsCustomerOrderMenuItemsGridAdapter;
import com.example.apos.adapter.clskotMenuItemsGridAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsKotMenuItemsBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsKotMenuItemSelectionListener;
import com.example.apos.listeners.clsMakeKotActListener;
import com.example.apos.util.clsPrintDemo;
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
 * Created by User on 27-04-2017.
 */

public class clsCustomerOrderMenuFragment  extends Fragment implements clsKotMenuItemSelectionListener
{
    private GridView gvMenuItems;
    private ConnectivityManager connectivityManager;
    private clsKotMenuItemSelectionListener menuItemSelectionListener=null;
    private ArrayList arrListMenuItemMaster;
    private Dialog pgDialog;
    Activity mActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // TODO Auto-generated method stub
        View myFragmentView = inflater.inflate(R.layout.customerordermenulist, container, false);
        mActivity=getActivity();
        widget(myFragmentView);

        try
        {
            menuItemSelectionListener = (clsKotMenuItemSelectionListener) clsCustomerOrderMenuFragment.this;
        }
        catch(Exception ex)
        {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }
        if (new clsUtility().isInternetConnectionAvailable(connectivityManager))
        {
            //new MenuWebService().execute();
            funGetMenuHeadListForCustomerOrder();
        }
        else
        {
            Toast.makeText(getActivity(),"Internet Connection Not Found",Toast.LENGTH_LONG).show();
        }

        return myFragmentView;
    }




    private void widget(View myFragmentView)
    {
        clsCustomerOrderScreen.txtHeading.setText("MENU");
        gvMenuItems = (GridView) myFragmentView.findViewById(R.id.gvkotmenuitemlist);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
    }

    public void funSetMenuList(ArrayList arrList)
    {
        gvMenuItems .setAdapter(new clsCustomerOrderMenuItemsGridAdapter(clsCustomerOrderScreen.mActivity,clsCustomerOrderScreen.mActivity, arrList, menuItemSelectionListener));
        clsCustomerOrderScreen.btnRefresh.setVisibility(View.VISIBLE);
    }

    @Override
    public void getMenuItemSelectionResult(String strMenuItemCode, String strMenuItemName, String strMenuType)
    {
        clsCustomerOrderScreen.screenName="ItemScreen";
        clsCustomerOrderScreen.txtMenuName.setText(Html.fromHtml( "<b>"+"MENU"+ "</b>" +
                "<br>"+strMenuItemName+"<br/>"));


        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //add a fragment
        Bundle bundle1 = new Bundle();
        bundle1.putString("MenuCode", strMenuItemCode);
        bundle1.putString("MenuName", strMenuItemName);
        bundle1.putString("MenuType", strMenuType);
        Fragment newFragment = new clsCustomerOrderItemFragment();
        newFragment.setArguments(bundle1);

        // Create new transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        transaction.replace(R.id.menuLinearLayout, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    public void funGetMenuHeadListForCustomerOrder() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetMenuHeadListForCustomerOrder(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gClientCode,new BaseAPIHelper.OnRequestComplete<ArrayList<clsKotMenuItemsBean>>() {
                    @Override
                    public void onSuccess(ArrayList<clsKotMenuItemsBean> arrListMenuMaster) {
                        dismissDialog();
                        try{
                            if (arrListMenuMaster.size()>0)
                            {
                                arrListMenuItemMaster=arrListMenuMaster;
                                funSetMenuList(arrListMenuMaster);
                            }

                        }catch(Exception e){
                            e.printStackTrace();
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
