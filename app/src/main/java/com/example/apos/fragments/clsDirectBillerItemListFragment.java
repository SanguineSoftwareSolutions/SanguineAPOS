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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.apos.adapter.clsDirectBillItemsListGridViewAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsDirectBillItemsListBean;
import com.example.apos.bean.clsDirectBillMenuItemsBean;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsDirectBillerActListener;
import com.example.apos.listeners.clsDirectBillerItemListSelectionListener;
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
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;


public class clsDirectBillerItemListFragment extends Fragment implements clsDirectBillerItemListSelectionListener{

    @BindView(R.id.edtdirectbillitemslistsearch)
    CustomSearchView customSearchView;
    private static String TAG = "BeWo_Restaurant_" + clsDirectBillerItemListFragment.class.getName();
    private EditText edtItemListSearch;
    private static Button btnRefresh;
    private static GridView gvItemsList;
    public static ArrayList arrListItemMaster;
    private static String menuType="";
    private List<clsDirectBillItemsListBean> listDirectBillItemsLists;
    private static clsDirectBillerItemListSelectionListener objItemListSelectionListener = null;
    private  String keyCase="upperCase";
    private  clsDirectBillerActListener objDirectBillerActListener;
    private ConnectivityManager connectivityManager;
    private Dialog pgDialog;
    public static Activity mActivity;
    ImageView imvRefresh;
    public static clsDirectBillerItemListFragment getInstance()
    {
        clsDirectBillerItemListFragment mClsDirectBillerItemListFragment = new clsDirectBillerItemListFragment();
        return mClsDirectBillerItemListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mActivity=getActivity();
        super.onCreate(savedInstanceState);
      //  getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.directbilleritemlist, container, false);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);

        try
           {
               objItemListSelectionListener = (clsDirectBillerItemListSelectionListener) clsDirectBillerItemListFragment.this;
               objDirectBillerActListener=(clsDirectBillerActListener)getActivity();
           }
        catch (Exception ex)
           {
             Log.i(TAG, " Unable to initialize database." + ex.getMessage());
           }

        listDirectBillItemsLists = new ArrayList<clsDirectBillItemsListBean>();
        gvItemsList = (GridView) rootView.findViewById(R.id.gvdirectbillitemslist);
        imvRefresh=(ImageView) rootView.findViewById(R.id.imvRefresh);
        customSearchView=(CustomSearchView) rootView.findViewById(R.id.edtdirectbillitemslistsearch);
        edtItemListSearch = customSearchView.getEditText();
        edtItemListSearch.requestFocus() ;
        CommonUtils.showKeyboard(edtItemListSearch,true);
        edtItemListSearch.addTextChangedListener(new TextWatcher()
        {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if(s.toString()!=null&&arrListItemMaster!=null){
                    ArrayList arrayListtemp = new ArrayList();
                    for (int cnt = 0; cnt <arrListItemMaster.size(); cnt++) {
                        clsDirectBillItemsListBean obj = (clsDirectBillItemsListBean) arrListItemMaster.get(cnt);
                        if (obj.getStrItemName().toLowerCase().contains(s.toString().toLowerCase())) {
                            arrayListtemp.add(arrListItemMaster.get(cnt));
                        }
                    }
                    funSetList(arrayListtemp);
                }
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
                    edtItemListSearch.setText("");
                    if (clsGlobalFunctions.gCounterWiseBilling.equals("Yes")) {
                        new clsDirectBillerItemListFragment().funGetItemPriceDtlCounterWise("All");
                    } else {
                       // new clsDirectBillerItemListFragment().funGetItemPriceDtl("All");
                        funGetItemPricingDtl("All","");
                    }

                } else {
                    Toast.makeText(getActivity(), "Internet Connection Not Found", Toast.LENGTH_LONG).show();
                }
            }
        });


        if (new clsUtility().isInternetConnectionAvailable(connectivityManager))
        {
            //new  MenuItemPriceListWS().execute("All");
            if (clsGlobalFunctions.gCounterWiseBilling.equals("Yes")) {
                new clsDirectBillerItemListFragment().funGetItemPriceDtlCounterWise("All");
            } else {
               // new clsDirectBillerItemListFragment().funGetItemPriceDtl("All");
                funGetItemPricingDtl("All","");
            }
        }
        else
        {
            Toast.makeText(getActivity(),"Internet Connection Not Found",Toast.LENGTH_LONG).show();
        }

        return rootView;
    }

    private static void funSetList(ArrayList arrList)
    {
        clsDirectBillItemsListGridViewAdapter DirectBillItemsListGridViewAdapter
                = new clsDirectBillItemsListGridViewAdapter(clsDirectBill.mActivity, clsDirectBill.mActivity, arrList, objItemListSelectionListener);
        gvItemsList.setAdapter(DirectBillItemsListGridViewAdapter);
    }

    public void populateSelectedMenuItems(String strMenuItemCode, String strMenuItemName,String strMenuType)
    {
        if (clsGlobalFunctions.gCounterWiseBilling.equals("Yes")) {
            new clsDirectBillerItemListFragment().funGetItemPriceDtlCounterWise(strMenuItemCode, strMenuType);
        } else {
            //new clsDirectBillerItemListFragment().funGetItemPriceDtl(strMenuItemCode, strMenuType);
            funGetItemPricingDtl(strMenuType,strMenuItemCode);
        }
        menuType=strMenuType;

    }


    @Override
    public void getItemsListSelectedForOder(String strItemCode, String strItemName, String strSubGroupCode, double dblSalePrice)
    {
        objDirectBillerActListener.funRefreshItemDtl(strItemCode, strItemName, strSubGroupCode, dblSalePrice);
     //   edtItemListSearch.setText("");
        funSetList(arrListItemMaster);
    }

    public void funGetItemPriceDtlCounterWise(String... params) {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                clsGlobalFunctions objGlobal = new clsGlobalFunctions();
                boolean flgSuperUser = true;
                if (clsGlobalFunctions.gSuperUser.equals("No")) {
                    flgSuperUser = false;
                }
                boolean flgAllItems = true;
                String menuType = "";
                if (!params[0].equals("All")) {
                    flgAllItems = false;
                    menuType = params[1];
                }
                showDialog();
                App.getAPIHelper().funGetItemPriceDtlCounterWise(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gDirectBillerAreaCode, params[0], clsGlobalFunctions.gAreaWisePricing, objGlobal.funGetCurrentDate(), objGlobal.funGetCurrentDate(), flgAllItems, menuType, new BaseAPIHelper.OnRequestComplete<ArrayList<clsKotItemsListBean>>() {
                    @Override
                    public void onSuccess(ArrayList<clsKotItemsListBean> arrListTemp) {
                        dismissDialog();
                        ArrayList arrListItemMastertmp = new ArrayList<clsDirectBillItemsListBean>();
                        String pricingDay = new clsUtility().funGetDayForPricing();
                        ArrayList<clsKotItemsListBean> arrListItemMastertemp = new ArrayList();
                        if (null != arrListTemp) {
                            clsKotItemsListBean obj = new clsKotItemsListBean();
                            clsDirectBillItemsListBean objDBiller = new clsDirectBillItemsListBean();
                            if (arrListTemp.size() > 0) {
                                for (int i = 0; i < arrListTemp.size(); i++) {
                                    obj = arrListTemp.get(i);
                                    objDBiller = new clsDirectBillItemsListBean();
                                    objDBiller.setStrItemCode(obj.getStrItemCode());
                                    objDBiller.setStrItemName(obj.getStrItemName());
                                    objDBiller.setStrSubGroupCode("");
                                    objDBiller.setStrExternalCode(obj.getStrExternalCode());
                                    objDBiller.setDblSalePrice(new clsUtility().funGetitemPriceOnDay(pricingDay, obj));
                                    arrListItemMastertmp.add(objDBiller);

                                }
                                if (arrListItemMastertmp.size() > 0) {
                                    arrListItemMaster = arrListItemMastertmp;
                                    funSetList(arrListItemMastertmp);
                                } else {
                                    funSetList(new ArrayList());
                                }
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

    private void funGetItemPricingDtl(String type,String menuCode)
    {
        if (clsGlobalFunctions.gHashMapItemList.size() > 0) {
            String areaCode=clsGlobalFunctions.gDirectBillerAreaCode;
            arrListItemMaster = clsGlobalFunctions.gHashMapItemList.get(areaCode);
            ArrayList arrListItemMastertmp = new ArrayList<clsDirectBillItemsListBean>();
            String pricingDay = new clsUtility().funGetDayForPricing();
            if (null != arrListItemMaster)
            {
                clsKotItemsListBean obj = new clsKotItemsListBean();
                clsDirectBillItemsListBean objDBiller = new clsDirectBillItemsListBean();
                if (arrListItemMaster.size() > 0)
                {
                    if(type.equalsIgnoreCase("All"))
                    {
                        for (int i = 0; i < arrListItemMaster.size(); i++) {
                            obj = (clsKotItemsListBean) arrListItemMaster.get(i);
                            objDBiller = new clsDirectBillItemsListBean();
                            objDBiller.setStrItemCode(obj.getStrItemCode());
                            objDBiller.setStrItemName(obj.getStrItemName());
                            objDBiller.setStrSubGroupCode("");
                            objDBiller.setStrExternalCode(obj.getStrExternalCode());
                            objDBiller.setDblSalePrice(new clsUtility().funGetitemPriceOnDay(pricingDay, obj));
                            arrListItemMastertmp.add(objDBiller);
                        }
                        if (arrListItemMastertmp.size() > 0) {
                            arrListItemMaster = arrListItemMastertmp;
                            funSetList(arrListItemMastertmp);
                        }
                    }
                    else
                    {
                        ArrayList arrayListtemp = new ArrayList();
                        for (int i = 0; i < arrListItemMaster.size(); i++)
                        {
                            obj = (clsKotItemsListBean) arrListItemMaster.get(i);
                            if(type.equalsIgnoreCase("MenuHead"))
                            {
                                if (obj.getMenuCode().toString().trim().equalsIgnoreCase(menuCode))
                                {
                                    objDBiller = new clsDirectBillItemsListBean();
                                    objDBiller.setStrItemCode(obj.getStrItemCode());
                                    objDBiller.setStrItemName(obj.getStrItemName());
                                    objDBiller.setStrSubGroupCode("");
                                    objDBiller.setStrExternalCode(obj.getStrExternalCode());
                                    objDBiller.setDblSalePrice(new clsUtility().funGetitemPriceOnDay(pricingDay, obj));
                                    arrayListtemp.add(objDBiller);
                                }
                            }
                            else
                            {
                                if (obj.getSubMenuHeadCode().toString().trim().equalsIgnoreCase(menuCode))
                                {
                                    objDBiller = new clsDirectBillItemsListBean();
                                    objDBiller.setStrItemCode(obj.getStrItemCode());
                                    objDBiller.setStrItemName(obj.getStrItemName());
                                    objDBiller.setStrSubGroupCode("");
                                    objDBiller.setStrExternalCode(obj.getStrExternalCode());
                                    objDBiller.setDblSalePrice(new clsUtility().funGetitemPriceOnDay(pricingDay, obj));
                                    arrayListtemp.add(objDBiller);
                                }
                            }

                        }
                        if (arrayListtemp.size() > 0) {
                            arrListItemMaster = arrayListtemp;
                            funSetList(arrayListtemp);
                        }
                    }
                }
                else {
                    funSetList(new ArrayList());
                }
            }
        }
        else
        {
            funSetList(new ArrayList());
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