package com.example.apos.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsDirectBill;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsKotScreen;
import com.example.apos.adapter.clsDirectBillMenuItemsGridAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsDirectBillMenuItemsBean;
import com.example.apos.bean.clsKotMenuItemsBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsDirectBillerActListener;
import com.example.apos.listeners.clsDirectBillerItemListSelectionListener;
import com.example.apos.listeners.clsMenuItemSelectionListener;
import com.example.apos.util.clsUtility;
import com.example.apos.views.CustomSearchView;
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
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class clsDirectBillerClsMenuItemFragment extends Fragment implements clsMenuItemSelectionListener {

    @BindView(R.id.edtdirectbillmenuitemsearch)
    CustomSearchView customSearchView;
    private static String TAG = "BeWo_Restaurant_" + clsDirectBillerClsMenuItemFragment.class.getName();
    private static EditText edtMenuSearch;
    private static GridView gvMenuItems;
    private static ArrayList arrListMenuItemMaster=new ArrayList();
    private  String keyCase="upperCase";
    private List<clsDirectBillMenuItemsBean> listDirectBillMenuItems;
    private static clsMenuItemSelectionListener menuItemSelectionListener;
    private clsDirectBillerItemListSelectionListener itemListSelectionListener = null;
    private clsDirectBillerActListener objDirectBillerActListener;
    private ConnectivityManager connectivityManager;
    private Dialog pgDialog;
    ImageView imvRefresh;
    public static Activity mActivity;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mActivity=getActivity();
        View rootView = inflater.inflate(R.layout.directbillermenuitems, container, false);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
      //  getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        try
        {
            menuItemSelectionListener = (clsMenuItemSelectionListener) clsDirectBillerClsMenuItemFragment.this;
            objDirectBillerActListener=(clsDirectBillerActListener)getActivity();
        }
        catch(Exception ex)
        {
            Log.i(TAG, " Unable to initialize database." +ex.getMessage());
        }

       // edtMenuSearch.setOnClickListener();
        gvMenuItems = (GridView) rootView.findViewById(R.id.gvdirectbillmenuitemlist);
        listDirectBillMenuItems = new ArrayList<clsDirectBillMenuItemsBean>();


        customSearchView=(CustomSearchView) rootView.findViewById(R.id.edtdirectbillmenuitemsearch);
        imvRefresh=(ImageView) rootView.findViewById(R.id.imvRefresh);
        edtMenuSearch = customSearchView.getEditText();
        edtMenuSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                ArrayList arrayListtemp = new ArrayList();
                for (int cnt = 0; cnt < arrListMenuItemMaster.size(); cnt++){
                    clsDirectBillMenuItemsBean obj = (clsDirectBillMenuItemsBean) arrListMenuItemMaster.get(cnt);
                    if (obj.getStrMenuItemName().toLowerCase().contains(s.toString().toLowerCase()))
                    {
                        arrayListtemp.add(arrListMenuItemMaster.get(cnt));
                    }
                }
                funSetMenuList(arrayListtemp);
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        imvRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (new clsUtility().isInternetConnectionAvailable(connectivityManager))
                {
                    edtMenuSearch.setText("");
                    // new  MenuWebService().execute();
                    if (clsGlobalFunctions.gCounterWiseBilling.equals("Yes")) {
                        funGetCounterWiseMenuList();
                    } else {
                        funGetMenuHeadList();
                    }
                } else {
                    Toast.makeText(getActivity(), "Internet Connection Not Found", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (new clsUtility().isInternetConnectionAvailable(connectivityManager))
        {
           // new  MenuWebService().execute();
            if (clsGlobalFunctions.gCounterWiseBilling.equals("Yes")) {
                funGetCounterWiseMenuList();
            } else {
                funGetMenuHeadList();
            }
        }
        else
        {
            Toast.makeText(getActivity(),"Internet Connection Not Found",Toast.LENGTH_LONG).show();
        }
        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    public void populateSelectedSubMenuItems(String strMenuItemCode, String strMenuItemName)
    {
        //new  SubMenuHeadWebService().execute(strMenuItemCode);
        funGetSubMenuHeadList(strMenuItemCode);
    }

    public void funSetMenuList(ArrayList arrList)
    {
        gvMenuItems.setAdapter(new clsDirectBillMenuItemsGridAdapter(clsDirectBill.mActivity, clsDirectBill.mActivity, arrList, menuItemSelectionListener));
    }

    @Override
    public void getMenuItemSelectionResult(String strMenuItemCode, String strMenuItemName, String strMenuType)
    {
        objDirectBillerActListener.setMenuItemSelectionCodeResult(strMenuItemCode, strMenuItemName,strMenuType);
       // edtMenuSearch.setText("");
       // funSetMenuList(arrListMenuItemMaster);
    }

    public void funGetCounterWiseMenuList() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                //final ArrayList arrListMenuMaster = new ArrayList();
                showDialog();
                App.getAPIHelper().funGetCounterWiseMenuList(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gCounterCode, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        dismissDialog();
                        if (null != jObj) {
                            try {
                                ArrayList<clsDirectBillMenuItemsBean>arrListMenuMaster = new ArrayList();
                                JsonArray mJsonArray = (JsonArray) jObj.get("MenuHeadList");
                                JsonObject mJsonObject = new JsonObject();
                                for (int i = 0; i < mJsonArray.size(); i++) {
                                    mJsonObject = (JsonObject) mJsonArray.get(i);
                                    if (mJsonObject.get("MenuName").getAsString().equals("")) {
                                        //memberInfo = "no data";
                                    } else {
                                        clsDirectBillMenuItemsBean obj = new clsDirectBillMenuItemsBean();
                                        obj.setStrMenuItemName(mJsonObject.get("MenuName").getAsString().toString());
                                        obj.setStrMenuItemCode(mJsonObject.get("MenuCode").getAsString().toString());
                                        obj.setStrMenuType("MenuHead");
                                        arrListMenuMaster.add(obj);
                                    }
                                }
                                if (arrListMenuMaster.size() > 0) {
                                    arrListMenuItemMaster=arrListMenuMaster;
                                    clsDirectBillerClsMenuItemFragment obj=new clsDirectBillerClsMenuItemFragment();
                                    obj.funSetMenuList(arrListMenuMaster);
                                }
                                else
                                {
                                    clsDirectBillerClsMenuItemFragment obj=new clsDirectBillerClsMenuItemFragment();
                                    obj.funSetMenuList(new ArrayList());
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

    public void funGetSubMenuHeadList(String menuCode) {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                //arrListMenuItemMaster=new ArrayList<clsKotMenuItemsBean>();
                showDialog();
                App.getAPIHelper().funGetSubMenuList(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gCounterCode, menuCode, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        dismissDialog();
                        if (null != jObj) {
                            try {
                                ArrayList<clsDirectBillMenuItemsBean>arrListMenuMaster = new ArrayList();
                                JsonArray mJsonArray = (JsonArray) jObj.get("SubMenuList");
                                JsonObject mJsonObject = new JsonObject();
                                for (int i = 0; i < mJsonArray.size(); i++) {
                                    mJsonObject = (JsonObject) mJsonArray.get(i);
                                    if (mJsonObject.get("SubMenuName").getAsString().equals("")) {
                                        //memberInfo = "no data";
                                    } else {
                                        clsDirectBillMenuItemsBean obj = new clsDirectBillMenuItemsBean();
                                        obj.setStrMenuItemName(mJsonObject.get("SubMenuName").getAsString());
                                        obj.setStrMenuItemCode(mJsonObject.get("SubMenuCode").getAsString());
                                        obj.setStrMenuType("SubMenuHead");
                                        arrListMenuMaster.add(obj);
                                    }
                                }
                                if (arrListMenuMaster.size() > 0) {
                                    arrListMenuItemMaster=arrListMenuMaster;
                                    funSetMenuList(arrListMenuItemMaster);
                                }
                                else
                                {
                                    clsDirectBillerClsMenuItemFragment obj=new clsDirectBillerClsMenuItemFragment();
                                    obj.funSetMenuList(new ArrayList());

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


    public void funGetMenuHeadList()
    {
        arrListMenuItemMaster = new ArrayList();
        ArrayList<clsDirectBillMenuItemsBean>arrListMenuMaster = new ArrayList();
        if (clsGlobalFunctions.gArrListMenuItemMaster.size() > 0)
        {
           for (int i = 0; i < clsGlobalFunctions.gArrListMenuItemMaster.size(); i++) {
                clsKotMenuItemsBean objMenu = (clsKotMenuItemsBean) clsGlobalFunctions.gArrListMenuItemMaster.get(i);
                clsDirectBillMenuItemsBean obj = new clsDirectBillMenuItemsBean();
                obj.setStrMenuItemName(objMenu.getStrMenuItemName());
                obj.setStrMenuItemCode(objMenu.getStrMenuItemCode());
                obj.setStrMenuType(objMenu.getStrMenuType());
                arrListMenuMaster.add(obj);
            }
            if (arrListMenuMaster.size() > 0) {
                arrListMenuItemMaster=arrListMenuMaster;
                funSetMenuList(arrListMenuItemMaster);
            }
        }
        else
        {
            funSetMenuList(new ArrayList());
        }
    }

    protected void showDialog() {
        if (null == pgDialog ) {
            if(mActivity ==null)
                mActivity=clsDirectBill.mActivity;
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