package com.example.apos.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.adapter.clsKDSAdapter;
import com.example.apos.adapter.clsKDSListItemAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsBillDtl;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.bean.clsMainMenuBean;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.util.Utils;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeMap;


public class clsKDSScreenFotKOT extends Activity implements clsKDSAdapter.customButtonListener, clsKDSListItemAdapter.customButtonListener
{
    private ConnectivityManager connectivityManager;
    private GridView gvKDSGridview;
    Map<String,  List<clsBillDtl>> hmKOTDtl = null;
    List<clsBillDtl> arrListKOTItem=null;
    final Handler handler = new Handler();
    Timer timer = new Timer();
    public static clsKDSScreenFotKOT mActivity;
    Map<String,  String> hmSelectedKOTDtl = null;
    Intent iData;
    String costCenterCode="",costCenterName="";
    private TextView txtKDSText,txtDate;
    private Dialog pgDialog;
    Thread DareTimeThread = null;

    public static HashMap<String,clsKotItemsListBean> gblHmCartData=new HashMap<String,clsKotItemsListBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kdsscreen);
        widget();
        Runnable runnable = new CountDownRunner();
        DareTimeThread = new Thread(runnable);
        DareTimeThread.start();
        callAsynchronousTask();
    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    TextView txtCurrentTime = (TextView) findViewById(R.id.tv_kdsdate);
                    String formattedDate = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                    txtCurrentTime.setText(clsGlobalFunctions.gPOSDateHeader+" "+formattedDate);
                } catch (Exception e) {
                }
            }
        });
    }

    class CountDownRunner implements Runnable {
        // @Override
        public void run() {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    doWork();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                } catch (Exception e) {
                }
            }
        }
    }
    private void widget()
    {
        iData = getIntent();
        costCenterCode=iData.getStringExtra("costCenterCode");
        costCenterName=iData.getStringExtra("costCenterName");
        mActivity=clsKDSScreenFotKOT.this;
        gvKDSGridview = (GridView) findViewById(R.id.kotkdsgridview);
        txtKDSText = (TextView) findViewById(R.id.tv_kdstext);
        txtDate = (TextView) findViewById(R.id.tv_kdsdate);
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        arrListKOTItem=new ArrayList<>();
        hmKOTDtl = new TreeMap<>();
        hmSelectedKOTDtl=new HashMap<String,  String>();
        txtKDSText.setText("KDS FOR "+costCenterName);

    }


    public void callAsynchronousTask()
    {

        TimerTask doAsynchronousTask = new TimerTask() {
            @Override
            public void run() {
                handler.post(new Runnable() {
                    public void run() {
                        try
                        {
                            funRefreshForm();
                        } catch (Exception e) {
                            // TODO Auto-generated catch block
                        }
                    }
                });
            }
        };
        timer.schedule(doAsynchronousTask, 0, 1000); //execute in every 1000 ms

    }


    @Override
    public void onKOTClickListner(int position, final String value)
    {
        final Dialog dialog = new Dialog(clsKDSScreenFotKOT.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogforselecteditemorkot);
        hmSelectedKOTDtl.clear();
        hmSelectedKOTDtl.put(value,"Selected");
        TextView txtKOTOrItem = (TextView) dialog.findViewById(R.id.txtKOTOrItem);
        Button btnProcessKOT = (Button) dialog.findViewById(R.id.btnProcessKDS);
        Button btnClose = (Button) dialog.findViewById(R.id.btnCloseKDS);
        txtKOTOrItem.setText(value);
        btnProcessKOT.setText("Process KOT");

        btnProcessKOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                funProcessKOTItemForKDSWS(value,"KOT");
                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.getWindowManager().getDefaultDisplay();

    }

    @Override
    public void onKOTItemClickListner(int position, final String value)
    {
        final Dialog dialog = new Dialog(clsKDSScreenFotKOT.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogforselecteditemorkot);
        TextView txtKOTOrItem = (TextView) dialog.findViewById(R.id.txtKOTOrItem);
        TextView txtKOTOrTableNo = (TextView) dialog.findViewById(R.id.txtKOTOrTableNo);
        Button btnProcessKOTItem = (Button) dialog.findViewById(R.id.btnProcessKDS);
        Button btnClose = (Button) dialog.findViewById(R.id.btnCloseKDS);
        if(value.split("#")[4].equals("Void"))
        {
            txtKOTOrItem.setText("Voided Item : "+value.split("#")[1]);
        }
        else
        {
            txtKOTOrItem.setText("Ordered Item : "+value.split("#")[1]);
        }
        txtKOTOrTableNo.setText("KOT No:" + value.split("#")[2]+"  Table Name:" + value.split("#")[3]);
        btnProcessKOTItem.setText("Process Item");

        btnProcessKOTItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                funProcessKOTItemForKDSWS(value,"Item");
                dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                dialog.dismiss();
            }
        });


        dialog.show();
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.getWindowManager().getDefaultDisplay();
    }


    private void funRefreshForm()
    {
        funResetDefault();
        funGetKOTDetailsForKDS();
    }

    private void funResetDefault()
    {
        hmKOTDtl.clear();
        arrListKOTItem.clear();
    }

    @Override
    public void onBackPressed()
    {
        funResetDefault();
        timer.purge();
        timer.cancel();
        timer=null;
        finish();
    }

    private void funGetKOTDetailsForKDS() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                String dateTime=clsGlobalFunctions.gPOSDate;
                String posDate=dateTime.replaceAll(" ","%20");
                App.getAPIHelper().funGetKOTDetailsForKDS(clsGlobalFunctions.gPOSCode, costCenterCode,posDate, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        if (null != jObj) {
                            try
                            {

                                JsonArray mJsonArray = (JsonArray) jObj.get("KOTDetails");
                                JsonObject mJsonObject = new JsonObject();
                                if(mJsonArray.size()>0)
                                {
                                    for (int i = 0; i < mJsonArray.size(); i++)
                                    {
                                        mJsonObject = (JsonObject) mJsonArray.get(i);
                                        if (mJsonObject.get("KOTNo").getAsString().equals(""))
                                        {

                                        }
                                        else
                                        {
                                            clsBillDtl objKot = new clsBillDtl();
                                            objKot.setStrKOTNo(mJsonObject.get("KOTNo").getAsString());
                                            objKot.setStrItemCode(mJsonObject.get("ItemCode").getAsString());
                                            objKot.setStrItemName(mJsonObject.get("ItemName").getAsString());
                                            objKot.setDblRate(Double.parseDouble(mJsonObject.get("Rate").getAsString()));
                                            objKot.setDblQuantity(Double.parseDouble(mJsonObject.get("Qty").getAsString()));
                                            objKot.setDblAmount(Double.parseDouble(mJsonObject.get("Amount").getAsString()));
                                            String []kotDate=mJsonObject.get("KOTDate").getAsString().split("-");
                                            objKot.setStrKOTDate(kotDate[2]+"-"+kotDate[1]+"-"+kotDate[0]+" "+mJsonObject.get("KOTTime").getAsString());
                                            objKot.setStrKOTTime(mJsonObject.get("KOTTime").getAsString());
                                            objKot.setStrTableNo(mJsonObject.get("TableNo").getAsString());
                                            objKot.setStrTableName(mJsonObject.get("TableName").getAsString());
                                            objKot.setStrKOTTimeDifferenceResult(mJsonObject.get("CheckedTimeDiffResult").getAsString());
                                            objKot.setStrItemType(mJsonObject.get("ItemType").getAsString());
                                            objKot.setStrWaiterNo(mJsonObject.get("WaiterNo").getAsString());



                                            if(hmKOTDtl.size()>0)
                                            {
                                                if(hmKOTDtl.containsKey(mJsonObject.get("KOTNo").getAsString()))
                                                {
                                                    arrListKOTItem=hmKOTDtl.get(mJsonObject.get("KOTNo").getAsString());
                                                    arrListKOTItem.add(objKot);
                                                }
                                                else
                                                {
                                                    arrListKOTItem=new ArrayList<>();
                                                    arrListKOTItem.add(objKot);
                                                    hmKOTDtl.put(mJsonObject.get("KOTNo").getAsString(),arrListKOTItem);
                                                }
                                            }
                                            else
                                            {
                                                arrListKOTItem=new ArrayList<>();
                                                arrListKOTItem.add(objKot);
                                                hmKOTDtl.put(mJsonObject.get("KOTNo").getAsString(),arrListKOTItem);
                                            }

                                        }
                                    }
                                }


                                List<clsBillDtl> arrListItem = new ArrayList();
                                if(hmKOTDtl.size()>0)
                                {
                                    final SimpleDateFormat df = new SimpleDateFormat("HH:mm:ss");
                                    final StringBuilder displayDelayTime = new StringBuilder();
                                    Date currentDate = new Date();
                                    Date currDate = df.parse(df.format(currentDate));
                                    int ch = currDate.getHours();
                                    int cm = currDate.getMinutes();
                                    int cs = currDate.getSeconds();
                                    int currentSeconds = (ch * 3600) + (cm * 60) + cs;


                                    for (Map.Entry<String, List<clsBillDtl>> entry : hmKOTDtl.entrySet())
                                    {
                                        clsBillDtl objKot=new clsBillDtl();
                                        objKot.setStrKOTNo(entry.getKey());
                                        arrListKOTItem=entry.getValue();
                                        for(int i=0;i<arrListKOTItem.size();i++)
                                        {
                                            clsBillDtl objKOT1=arrListKOTItem.get(0);
                                            objKot.setStrTableName(objKOT1.getStrTableName());

                                            Date delay = df.parse(objKOT1.getStrKOTTime());
                                            int dh = delay.getHours();
                                            int dm = delay.getMinutes();
                                            int ds = delay.getSeconds();
                                            int delaySeconds = (dh * 3600) + (dm * 60) + ds;

                                            int differenceSeconds = 0;
                                            if (currDate.getTime() > delay.getTime())
                                            {
                                                differenceSeconds = currentSeconds - delaySeconds;
                                            }
                                            else
                                            {
                                                differenceSeconds = delaySeconds - currentSeconds;
                                            }
                                            int hh = differenceSeconds / 3600;
                                            differenceSeconds = differenceSeconds % 3600;

                                            int mm = differenceSeconds / 60;
                                            differenceSeconds = differenceSeconds % 60;

                                            int ss = differenceSeconds;

                                            displayDelayTime.setLength(0);

                                            if (mm > 0)
                                            {
                                                if(mm<10)
                                                {
                                                    displayDelayTime.append("0"+mm + ":");
                                                }
                                                else
                                                {
                                                    displayDelayTime.append(mm + ":");
                                                }

                                            }
                                            if(mm==0)
                                            {
                                                displayDelayTime.append("00:");
                                            }
                                            if (ss > 0)
                                            {
                                                if(ss<10)
                                                {
                                                    displayDelayTime.append("0"+ss);
                                                }
                                                else
                                                {
                                                    displayDelayTime.append(ss);
                                                }
                                            }
                                            if(ss==0)
                                            {
                                                displayDelayTime.append("00");
                                            }
                                            objKot.setStrKOTTime(displayDelayTime.toString());
                                            break;
                                        }
                                        arrListItem.add(objKot);
                                    }

                                }

                                clsKDSAdapter adapter= new clsKDSAdapter(getBaseContext(),hmKOTDtl,arrListItem,hmSelectedKOTDtl);
                                adapter.setCustomButtonListner((clsKDSAdapter.customButtonListener) clsKDSScreenFotKOT.this);
                                gvKDSGridview.setAdapter(adapter);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    @Override
                    public void onFailure(String errorMessage, int errorCode) {
                    }
                });
            } else {
                SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
        }
    }


    private void funProcessKOTItemForKDSWS(String value,String type) {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                JsonObject objKDSDtl=new JsonObject();
                try
                {
                    if(type.equals("KOT"))
                    {
                        String itemType="";
                        if(hmKOTDtl.size()>0)
                        {
                            if(hmKOTDtl.containsKey(value))
                            {
                                JsonArray arrKDSClass = new JsonArray();
                                List<clsBillDtl> arrSelectedKotItemList=hmKOTDtl.get(value);

                                for(int cnt=0;cnt<arrSelectedKotItemList.size();cnt++)
                                {
                                    clsBillDtl objBillDtl=arrSelectedKotItemList.get(cnt);
                                    JsonObject objRows = new JsonObject();
                                    objRows.addProperty("ItemCode",objBillDtl.getStrItemCode());
                                    objRows.addProperty("kotDateTime",objBillDtl.getStrKOTDate());
                                    objRows.addProperty("waiterNo",objBillDtl.getStrWaiterNo());
                                    itemType=objBillDtl.getStrItemType();

                                    arrKDSClass.add(objRows);
                                }
                                objKDSDtl.add("KOTItemDtl", arrKDSClass);

                            }
                        }
                        objKDSDtl.addProperty("ItemType",itemType);
                        objKDSDtl.addProperty("ProcessingType","KOT");
                        objKDSDtl.addProperty("KOTNo",value);
                    }
                    else
                    {
                        objKDSDtl.addProperty("ProcessingType","Item");
                        objKDSDtl.addProperty("ItemCode",value.split("#")[0]);
                        objKDSDtl.addProperty("ItemType",value.split("#")[4]);
                        objKDSDtl.addProperty("KOTNo",value.split("#")[2]);
                        objKDSDtl.addProperty("kotDateTime",value.split("#")[6]);
                        objKDSDtl.addProperty("waiterNo",value.split("#")[5]);
                    }
                    objKDSDtl.addProperty("UserCode",clsGlobalFunctions.gUserCode);
                    objKDSDtl.addProperty("costCenterCode",costCenterCode);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                showDialog();
                App.getAPIHelper().funUpdateProcessedKOTItemsForKDS(objKDSDtl, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        dismissDialog();
                        if (null != jObj) {
                            try {
                                String result = jObj.get("result").getAsString();
                                if(result.equalsIgnoreCase("success"))
                                {
                                    Toast.makeText(getApplicationContext(), "KOTItem Processed Successfully!!!!!", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Failed to process!!!!", Toast.LENGTH_LONG).show();
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
                SnackBarUtils.showSnackBar(clsKDSScreenFotKOT.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsKDSScreenFotKOT.mActivity, R.string.setup_your_server_settings);
        }
    }


    protected void showDialog() {
        if (null == pgDialog) {
            pgDialog = CommonUtils.getProgressDialog(clsKDSScreenFotKOT.this, 0, false);
        }
        pgDialog.show();
    }

    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }



}
