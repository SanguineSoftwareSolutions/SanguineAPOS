package com.example.apos.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
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
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.adapter.clsKotTableAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.listeners.clsKotTableListSelectionListener;
import com.example.apos.listeners.clsMakeKotActListener;
import com.example.apos.listeners.clsNCKOTActListener;
import com.example.apos.util.clsUtility;
import com.example.apos.views.CustomSearchView;

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

import butterknife.BindView;

public class clsNCKOTTableFragment extends Fragment implements clsKotTableListSelectionListener
{
    private clsKotTableListSelectionListener tableSelectionListener;
    private EditText edtTableSearch;
    public ArrayList<clsTableMaster> arrListTableMaster;
    private  String keyCase="upperCase";
    public clsNCKOTActListener objNCKOTActListener;
    private ConnectivityManager connectivityManager;

    @BindView(R.id.TableCustomSearchView)
    CustomSearchView customSearchView;
    @BindView(R.id.vgContent)
    ViewGroup vgContent;
    @BindView(R.id.vgProgress)
    ViewGroup vgProgress;
    @BindView(R.id.kottable_gridview)
    GridView kotTableGridview;

    public static Activity mActivity;
    private Dialog pgDialog;
    public static clsNCKOTTableFragment getInstance()
    {
        clsNCKOTTableFragment mNCKotTable = new clsNCKOTTableFragment();
        return mNCKotTable;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        mActivity=getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        mActivity=getActivity();
        View rootView = inflater.inflate(R.layout.kot_screen, container, false);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        try
        {
            tableSelectionListener = (clsKotTableListSelectionListener) clsNCKOTTableFragment.this;
            objNCKOTActListener=(clsNCKOTActListener)getActivity();
        }
        catch(Exception ex)
        {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }
        vgContent=(ViewGroup) rootView.findViewById(R.id.vgContent);
        vgProgress=(ViewGroup) rootView.findViewById(R.id.vgProgress);
        customSearchView=(CustomSearchView) rootView.findViewById(R.id.TableCustomSearchView);
        edtTableSearch  = customSearchView.getEditText();
        edtTableSearch.setFocusable(true);
        edtTableSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.toString()!=null&&arrListTableMaster!=null){
                    ArrayList arrayListtemp = new ArrayList();
                    for (int cnt = 0; cnt <arrListTableMaster.size(); cnt++)
                    {
                        clsTableMaster obj = (clsTableMaster) arrListTableMaster.get(cnt);
                        if (obj.getStrTableName().toLowerCase().contains(s.toString().toLowerCase()))
                        {
                            arrayListtemp.add(arrListTableMaster.get(cnt));
                        }
                    }
                    funFillTableGrid(arrayListtemp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //edtTableSearch.setOnClickListener();
        kotTableGridview = (GridView) rootView.findViewById(R.id.kottable_gridview);

        if (new clsUtility().isInternetConnectionAvailable(connectivityManager))
        {
            funGetTableList();
        }
        else
        {
            Toast.makeText(getActivity(),"Internet Connection Not Found",Toast.LENGTH_LONG).show();
        }

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        init();
    }

    private void init() {
        mActivity = getActivity();
        showProgress();
        connectivityManager = (ConnectivityManager) mActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        try {
            tableSelectionListener = (clsKotTableListSelectionListener) clsNCKOTTableFragment.this;
            objNCKOTActListener = (clsNCKOTActListener) mActivity;
        } catch (Exception ex) {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }
        if (ConnectivityHelper.isConnected()) {
            funGetTableList();
        } else {
            showErrorState();
            Toast.makeText(mActivity, "Internet Connection Not Found", Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void getTableListSelected(String strTableNo, String strTableName)
    {
        String status="";
        for(clsTableMaster objTable:arrListTableMaster)
        {
            if(objTable.getStrTableNo().equals(strTableNo))
            {
                status=objTable.getStrTableStatus();
                break;
            }
        }
        //  clsKotScreen.setTableSelectedResult(strTableNo, strTableName,status);
        objNCKOTActListener.setTableSelectedResult(strTableNo, strTableName,status);
        edtTableSearch.setText(" ");
        funFillTableGrid(arrListTableMaster);
    }

    @Override
    public void getDirectKotItemListSelected(String strTableNo, String strTableName,String strWaiterNo,String strWaiterName)
    {
        String status="",cardType="";
        double kotAmt=0,cardBalance=0;
        int paxNo=1;
        for(clsTableMaster objTable:arrListTableMaster)
        {
            if(objTable.getStrTableNo().equals(strTableNo))
            {
                status=objTable.getStrTableStatus();
                kotAmt=objTable.getDblKOTAmt();
                cardBalance=objTable.getDblCardBalanace();
                cardType=objTable.getStrCardType();
                paxNo=objTable.getIntPaxNo();
                break;
            }
        }
        objNCKOTActListener.setDirectKotItemListSelectedResult1(strTableNo, strTableName, strWaiterNo, strWaiterName,status,kotAmt,cardBalance,cardType,paxNo);
        // clsKotScreen.setDirectKotItemListSelectedResult1(strTableNo, strTableName, strWaiterNo, strWaiterName,status);
    }

    private void funFillTableGrid(ArrayList arrListTableMaster)
    {
        kotTableGridview.setAdapter(new clsKotTableAdapter(getActivity(), arrListTableMaster,tableSelectionListener));
    }


    public void funGetTableList() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();
                App.getAPIHelper().funGetTableList(clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gCMSIntegrationYN, clsGlobalFunctions.gTreatMemberAsTable, new BaseAPIHelper.OnRequestComplete<ArrayList<clsTableMaster>>() {
                    @Override
                    public void onSuccess(ArrayList<clsTableMaster> arrListTemp) {
                        dismissDialog();
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                dismissProgress();
                            }
                        }, 5000);

                        if (null != arrListTemp) {
                            if (arrListTemp.size() > 0) {
                                arrListTableMaster = arrListTemp;
                                funFillTableGrid(arrListTableMaster);
                                dismissProgress();
                            }
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage, int errorCode) {
                        dismissDialog();
                        showErrorState();
                    }

                });
            } else {
                SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
                showErrorState();
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


    private void showProgress() {
        vgContent.setVisibility(View.GONE);
        vgProgress.setVisibility(View.VISIBLE);
    }

    private void dismissProgress() {
        vgContent.setVisibility(View.VISIBLE);
        vgProgress.setVisibility(View.GONE);
    }


    private void showErrorState() {
        dismissProgress();
        vgContent.setVisibility(View.GONE);
    }


}
