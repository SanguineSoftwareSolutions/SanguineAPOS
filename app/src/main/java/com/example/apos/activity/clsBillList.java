package com.example.apos.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.adapter.clsBillListAdapter;

import com.example.apos.adapter.clsSalesMenuReportAdapter;
import com.example.apos.adapter.clsTotalListAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsBillListDtl;
import com.example.apos.bean.clsSalesReportDtl;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
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
import java.util.List;

public class clsBillList extends Activity implements clsBillListAdapter.customButtonListener
{
    clsBillListAdapter objBillListAdapter;
    GridView objBillList;
    ArrayList arrBillList = new ArrayList();
    private ConnectivityManager connectivityManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.billlistforsettlement);
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        objBillList= (GridView) findViewById(R.id.gvBillList);

      //  new CommonUtils()
        funGetBillListDtl();
    }

    @Override
    public void onButtonClickListner(int position, String value)
    {
        if(value.split("#")[1].equals("selectedrow"))
        {

            String billNo=value.split("#")[0];
            Toast.makeText(clsBillList.this, "item selected", Toast.LENGTH_LONG).show();
            Intent billSettleIntent = new Intent(clsBillList.this, clsBillSettlement.class);
            billSettleIntent .putExtra("BillNO",billNo);
            startActivity(billSettleIntent);
            finish();
        }
    }

    private void funSetList(ArrayList arrList)
    {
        objBillListAdapter=new clsBillListAdapter(clsBillList.this,arrList);
        objBillListAdapter.setCustomButtonListner(clsBillList.this);
        objBillList.setAdapter(objBillListAdapter);
    }

    public void funGetBillListDtl()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                App.getAPIHelper().funGetBillList(clsGlobalFunctions.gClientCode,clsGlobalFunctions.gPOSCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsBillListDtl>>() {
                    @Override
                    public void onSuccess(ArrayList<clsBillListDtl> arrListTemp)
                    {
                        if (null != arrListTemp)
                        {
                            if (arrListTemp.size()>0)
                            {
                                arrBillList = arrListTemp;
                                funSetList(arrBillList);
                            }
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage, int errorCode)
                    {
                    }
                });
            } else {
                SnackBarUtils.showSnackBar(this, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(this, R.string.setup_your_server_settings);
        }
    }



}
