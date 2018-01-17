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
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsBillDtl;
import com.example.apos.bean.clsCostCenterDtl;
import com.example.apos.bean.clsCustomerMaster;
import com.example.apos.bean.clsWaiterMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
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
import butterknife.ButterKnife;

/**
 * Created by Monika on 8/16/2017.
 */

public class clsCostCenterScreen extends Activity implements clsCostCenterAdapter.customButtonListener
{
    GridView gvCostCenterGridView;
    EditText edtSearchCostCenter;
    List<clsCostCenterDtl> arrCostCenterList=null;
    @BindView(R.id.costCenterCustomSearchView)
    CustomSearchView customSearchView;
    private Dialog pgDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.costcenterselectiondialog);
        ButterKnife.bind(clsCostCenterScreen.this);
        widget();

        funGetCostCenterList();
        customSearchView = (CustomSearchView) findViewById(R.id.costCenterCustomSearchView);
        edtSearchCostCenter = customSearchView.getEditText();
        edtSearchCostCenter.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString() != null && arrCostCenterList != null) {
                    ArrayList arrayListtemp = new ArrayList();
                    for (int cnt = 0; cnt < arrCostCenterList.size(); cnt++) {
                        clsCostCenterDtl obj = (clsCostCenterDtl) arrCostCenterList.get(cnt);
                        if (obj.getStrCostCenterName().toLowerCase().contains(s.toString().toLowerCase())) {
                            arrayListtemp.add(arrCostCenterList.get(cnt));
                        }
                    }
                    funFillCostCenterDialog(arrayListtemp);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    private void widget() {
        arrCostCenterList=new ArrayList<>();
        gvCostCenterGridView = (GridView) findViewById(R.id.gvCostCenterGridview);
    }


    private void funFillCostCenterDialog(List<clsCostCenterDtl> arrList)
    {
        clsCostCenterAdapter objCostCenterAdapter = new clsCostCenterAdapter(clsCostCenterScreen.this, arrList);
        objCostCenterAdapter.setCustomButtonListner((clsCostCenterAdapter.customButtonListener) clsCostCenterScreen.this);
        gvCostCenterGridView.setAdapter(objCostCenterAdapter);
    }



    @Override
    public void onCostCenterClickListner(int position, String value)
    {
        String costCenterCode=value.split("#")[1];
        Intent kdsScreen = new Intent(clsCostCenterScreen.this, clsKDSScreenFotKOT.class);
        kdsScreen .putExtra("costCenterCode", value.split("#")[0]);
        kdsScreen .putExtra("costCenterName", value.split("#")[1]);
        startActivity(kdsScreen);
    }



    private void funGetCostCenterList()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();

                App.getAPIHelper().funGetCostCenterList("CostCenter",clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<JsonArray>() {
                    @Override
                    public void onSuccess(JsonArray mJsonArray) {
                        dismissDialog();
                        if (null != mJsonArray) {
                            try
                            {
                                List<clsCostCenterDtl> arrListTemp=new ArrayList<clsCostCenterDtl>();
                                JsonObject mJsonObject = new JsonObject();
                                for (int i = 0; i < mJsonArray.size(); i++) {
                                    mJsonObject = (JsonObject) mJsonArray.get(i);

                                    if (mJsonObject.get("CostCenterCode").getAsString().equals("")) {
                                        //memberInfo = "no data";
                                    } else
                                    {
                                        clsCostCenterDtl objCostCenter = new clsCostCenterDtl();
                                        objCostCenter.setStrCostCenterCode(mJsonObject.get("CostCenterCode").getAsString());
                                        objCostCenter.setStrCostCenterName(mJsonObject.get("CostCenterName").getAsString());
                                        arrListTemp.add(objCostCenter);
                                    }
                                }
                                if(arrListTemp.size()>0)
                                {
                                    arrCostCenterList=arrListTemp;
                                    funFillCostCenterDialog(arrListTemp);
                                }
                            }
                            catch (Exception e) {
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
                SnackBarUtils.showSnackBar(clsCostCenterScreen.this, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsCostCenterScreen.this, R.string.setup_your_server_settings);
        }
    }



    protected void showDialog() {
        if (null == pgDialog) {
            pgDialog = CommonUtils.getProgressDialog(clsCostCenterScreen.this, 0, false);
        }
        pgDialog.show();
    }

    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }
}
