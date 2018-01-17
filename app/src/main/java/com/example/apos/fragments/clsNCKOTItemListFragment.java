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
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsKotScreen;
import com.example.apos.activity.clsNCKOTScreen;
import com.example.apos.adapter.clsKotItemsListGridViewAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.bean.clsKotMenuItemsBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsKotItemListSelectionListener;
import com.example.apos.listeners.clsNCKOTActListener;
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


public class clsNCKOTItemListFragment extends Fragment implements clsKotItemListSelectionListener {

    @BindView(R.id.edtkotitemsearch)
    CustomSearchView customSearchView;
    private static String TAG = "BeWo_Restaurant_" + clsKotItemListFragment.class.getName();
    private static EditText edtItemListSearch;
    private static GridView gvItemsList;
    ImageView imvRefresh;
    public static ArrayList arrListItemMaster;
    private  String keyCase="upperCase";
    private List<clsKotItemsListBean> listKotItemsLists;
    private static clsKotItemListSelectionListener itemListSelectionListener = null;
    public clsNCKOTActListener objNCKotActListener;
    private ConnectivityManager connectivityManager;
    private Dialog pgDialog;
    Activity mActivity;
    public static clsNCKOTItemListFragment getInstance()
    {
        clsNCKOTItemListFragment mItemListFragment = new clsNCKOTItemListFragment();
        return mItemListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.kot_item_list, container, false);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        mActivity = getActivity();
        try
        {
            itemListSelectionListener = (clsKotItemListSelectionListener) clsNCKOTItemListFragment.this;
            objNCKotActListener=(clsNCKOTActListener)getActivity();
        }
        catch (Exception ex)
        {
            Log.i(TAG, " Unable to initialize database." + ex.getMessage());
        }

        listKotItemsLists = new ArrayList<clsKotItemsListBean>();
        gvItemsList = (GridView) rootView.findViewById(R.id.gvkotitemslist);
        imvRefresh=(ImageView) rootView.findViewById(R.id.imvRefresh);

        customSearchView=(CustomSearchView) rootView.findViewById(R.id.edtkotitemsearch);
        edtItemListSearch = customSearchView.getEditText();
        edtItemListSearch.setFocusable(true);
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
                    for (int cnt = 0; cnt <arrListItemMaster.size(); cnt++)
                    {
                        clsKotItemsListBean obj = (clsKotItemsListBean) arrListItemMaster.get(cnt);
                        if ((obj.getStrItemName().toLowerCase().contains(s.toString().toLowerCase())) || (obj.getStrExternalCode().toLowerCase().contains(s.toString().toLowerCase())) )
                        {
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
                        funGetItemPriceDtlCounterWise("All");
                    } else {
                        //funGetItemPriceDtl("All");
                        if (clsGlobalFunctions.gHashMapItemList.size() > 0) {
                            String areaCode=clsGlobalFunctions.gDirectBillerAreaCode;
                            arrListItemMaster = clsGlobalFunctions.gHashMapItemList.get(areaCode);
                            funSetList(arrListItemMaster);
                        } else {
                            funSetList(new ArrayList());
                        }
                    }

                } else {
                    Toast.makeText(getActivity(), "Internet Connection Not Found", Toast.LENGTH_LONG).show();
                }
            }
        });

        if (new clsUtility().isInternetConnectionAvailable(connectivityManager))
        {
            if (clsGlobalFunctions.gCounterWiseBilling.equals("Yes")) {
                funGetItemPriceDtlCounterWise("All");
            } else {
               // funGetItemPriceDtl("All");
                if (clsGlobalFunctions.gHashMapItemList.size() > 0) {
                    String areaCode=clsGlobalFunctions.gDirectBillerAreaCode;
                    arrListItemMaster = clsGlobalFunctions.gHashMapItemList.get(areaCode);
                    funSetList(arrListItemMaster);
                } else {
                    funSetList(new ArrayList());
                }
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
        //Toast.makeText(clsKotScreen.mActivity,"arraylist1="+arrList.size(),Toast.LENGTH_LONG).show();
        clsKotItemsListGridViewAdapter KotItemsListGridViewAdapter
                = new clsKotItemsListGridViewAdapter(clsNCKOTScreen.mActivity, clsNCKOTScreen.mActivity, arrList, itemListSelectionListener);
        gvItemsList.setAdapter(KotItemsListGridViewAdapter);
    }

    /*public void populateSelectedMenuItems(String strMenuItemCode, String strMenuItemName,String strMenuType)
    {
        if (clsGlobalFunctions.gCounterWiseBilling.equals("Yes")) {
            new clsNCKOTItemListFragment().funGetItemPriceDtlCounterWise(strMenuItemCode,strMenuType);
        } else {
            new clsNCKOTItemListFragment().funGetItemPriceDtl(strMenuItemCode,strMenuType);
        }
    }
    */


    @Override
    public void getItemsListSelectedForOder(String strItemCode, String strItemName, String strSubGroupCode, double dblSalePrice)
    {
        // clsKotScreen.funRefreshItemDtl(strItemCode, strItemName,strSubGroupCode,dblSalePrice);
        objNCKotActListener.funRefreshItemDtl(strItemCode, strItemName,strSubGroupCode,dblSalePrice);
        edtItemListSearch.setText("");
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
                        String pricingDay = new clsUtility().funGetDayForPricing();
                        ArrayList<clsKotItemsListBean> arrListItemMastertemp = new ArrayList();
                        if (null != arrListTemp) {
                            clsKotItemsListBean obj = new clsKotItemsListBean();
                            if (arrListTemp.size() > 0) {
                                for (int i = 0; i < arrListTemp.size(); i++) {
                                    obj = arrListTemp.get(i);
                                    obj.setDblSalePrice(new clsUtility().funGetitemPriceOnDay(pricingDay, obj));
                                    arrListItemMastertemp.add(obj);
                                }
                            }
                            if (arrListItemMastertemp.size() > 0) {
                                arrListItemMaster = arrListItemMastertemp;
                                funSetList(arrListItemMastertemp);
                            } else {
                                funSetList(new ArrayList());
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