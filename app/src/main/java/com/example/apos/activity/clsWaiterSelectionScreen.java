package com.example.apos.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;

import com.example.apos.App;
import com.example.apos.adapter.clsCostCenterAdapter;
import com.example.apos.adapter.clsWaiterAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsBillDtl;
import com.example.apos.bean.clsCostCenterDtl;
import com.example.apos.bean.clsWaiterMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
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
import java.util.List;

import butterknife.BindView;


/**
 * Created by Monika on 8/26/2017.
 */

public class clsWaiterSelectionScreen extends Activity implements clsWaiterAdapter.customButtonListener
{
    GridView gvWaiterGridView;
    EditText edtSearchWaiter;
    ArrayList<clsWaiterMaster> arrWaiterList=null;
    @BindView(R.id.waiterCustomSearchView)
    CustomSearchView customSearchView;
    private Dialog pgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.waiterselectiondialog);
        widget();

        funGetWaiterList();
        customSearchView = (CustomSearchView) findViewById(R.id.waiterCustomSearchView);
        edtSearchWaiter = customSearchView.getEditText();
        edtSearchWaiter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString() != null && arrWaiterList != null) {
                    ArrayList arrayListtemp = new ArrayList();
                    for (int cnt = 0; cnt < arrWaiterList.size(); cnt++) {
                        clsWaiterMaster obj = (clsWaiterMaster) arrWaiterList.get(cnt);
                        if (obj.getStrWaiterName().toLowerCase().contains(s.toString().toLowerCase())) {
                            arrayListtemp.add(arrWaiterList.get(cnt));
                        }
                    }
                    funFillWaiterDialog(arrayListtemp);
                }
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });


    }

    private void widget() {
        arrWaiterList=new ArrayList<>();
        gvWaiterGridView = (GridView) findViewById(R.id.gvWaiterGridview);
    }


    private void funFillWaiterDialog(ArrayList<clsWaiterMaster> arrList)
    {
        clsWaiterAdapter objWaiterAdapter = new clsWaiterAdapter(clsWaiterSelectionScreen.this, arrList);
        objWaiterAdapter.setCustomButtonListner((clsWaiterAdapter.customButtonListener) clsWaiterSelectionScreen.this);
        gvWaiterGridView.setAdapter(objWaiterAdapter);
    }



    @Override
    public void onWaiterClickListner(int position, String value)
    {
        Intent kpsScreen = new Intent(clsWaiterSelectionScreen.this, clsKPSScreen.class);
        kpsScreen .putExtra("waiterNo", value.split("#")[0]);
        kpsScreen .putExtra("waiterName", value.split("#")[1]);
        startActivity(kpsScreen);
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
                            if (arrListTemp.size() > 0)
                            {
                                arrWaiterList=arrListTemp;
                                funFillWaiterDialog(arrListTemp);
                            }
                        }
                    }

                    @Override
                    public void onFailure(String errorMessage, int errorCode) {
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


    protected void showDialog() {
        if (null == pgDialog) {
            pgDialog = CommonUtils.getProgressDialog(clsWaiterSelectionScreen.this, 0, false);
        }
        pgDialog.show();
    }

    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }
}
