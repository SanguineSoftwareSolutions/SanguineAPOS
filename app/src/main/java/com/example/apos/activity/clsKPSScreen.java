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
import com.example.apos.adapter.clsKPSAdapter;
import com.example.apos.adapter.clsKPSListItemAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsBillDtl;
import com.example.apos.bean.clsKotItemsListBean;
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

/**
 * Created by Monika on 8/26/2017.
 */


public class clsKPSScreen extends Activity implements clsKPSAdapter.customButtonListener, clsKPSListItemAdapter.customButtonListener
{
    private ConnectivityManager connectivityManager;
    private GridView gvKPSGridview;
    Map<String,  List<clsBillDtl>> hmKOTDtl = null;
    List<clsBillDtl> arrListKOTItem=null;
    final Handler handler = new Handler();
    Timer timer = new Timer();
    public static clsKPSScreen mActivity;
    Map<String,  String> hmSelectedKOTDtl = null;
    Intent iData;
    String waiterNo="",waiterName="";
    private TextView txtKPSText,txtDate,txtKPSWaiterName;
    private Dialog pgDialog;
    Thread DareTimeThread = null;
    public static HashMap<String,clsKotItemsListBean> gblHmCartData=new HashMap<String,clsKotItemsListBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.kpsscreen);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        widget();
        Runnable runnable = new CountDownRunner();
        DareTimeThread = new Thread(runnable);
        DareTimeThread.start();
        callAsynchronousTask();
    }

    private void widget()
    {
        iData = getIntent();
        waiterNo=iData.getStringExtra("waiterNo");
        waiterName=iData.getStringExtra("waiterName");
        mActivity=clsKPSScreen.this;
        gvKPSGridview = (GridView) findViewById(R.id.kpsgridview);
        txtKPSText = (TextView) findViewById(R.id.tv_kpstext);
        txtKPSWaiterName = (TextView) findViewById(R.id.tv_KPSWaiteName);
        txtDate = (TextView) findViewById(R.id.tv_kpsdate);
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        arrListKOTItem=new ArrayList<>();
        hmKOTDtl = new TreeMap<>();
        hmSelectedKOTDtl=new HashMap<String,  String>();
        txtKPSText.setText("KPS ");
        txtKPSWaiterName.setText(waiterName);

    }

    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    TextView txtCurrentTime = (TextView) findViewById(R.id.tv_kpsdate);
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
        final Dialog dialog = new Dialog(clsKPSScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogforselecteditemorkot);
        hmSelectedKOTDtl.clear();
        hmSelectedKOTDtl.put(value,"Selected");
        TextView txtKOTOrItem = (TextView) dialog.findViewById(R.id.txtKOTOrItem);
        Button btnProcessKOT = (Button) dialog.findViewById(R.id.btnProcessKDS);
        Button btnClose = (Button) dialog.findViewById(R.id.btnCloseKDS);
        txtKOTOrItem.setText(value);
        btnProcessKOT.setText("PickUp KOT");

        btnProcessKOT.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                funPickedUpKOTItemForKPSWS(value,"KOT");
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
        final Dialog dialog = new Dialog(clsKPSScreen.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialogforselecteditemorkot);
        TextView txtKOTOrItem = (TextView) dialog.findViewById(R.id.txtKOTOrItem);
        TextView txtKOTOrTableNo = (TextView) dialog.findViewById(R.id.txtKOTOrTableNo);
        Button btnProcessKOTItem = (Button) dialog.findViewById(R.id.btnProcessKDS);
        Button btnClose = (Button) dialog.findViewById(R.id.btnCloseKDS);
        txtKOTOrItem.setText("Item Picked : "+value.split("#")[1]);
        txtKOTOrTableNo.setText("KOT No:" + value.split("#")[2]+"  Table Name:" + value.split("#")[3]);
        btnProcessKOTItem.setText("PickUp Item");

        btnProcessKOTItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
              //  new funPickedUpKOTItemForKPSWS().execute(value,"Item");
                funPickedUpKOTItemForKPSWS(value,"Item");
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


    private class funCallWebService extends AsyncTask<String, Void, JSONObject>
    {
        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException {
            InputStream in = entity.getContent();
            StringBuffer out = new StringBuffer();
            int n = 1;
            while (n > 0) {
                byte[] b = new byte[4096];
                n = in.read(b);
                if (n > 0) out.append(new String(b, 0, n));
            }
            return out.toString();
        }


        @Override
        protected JSONObject doInBackground(String... params) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(params[0]);
            String text = null;
            JSONObject jObj=new JSONObject();
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
                jObj = new JSONObject(text);

            } catch (Exception e) {
            }
            return jObj;
        }

        protected void onPostExecute(JSONObject mJsonObj)
        {

        }
    }

    private void funRefreshForm()
    {
        funResetDefault();
        funGetKOTDetailsForKPS();
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


    private void funGetKOTDetailsForKPS() {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                String dateTime=clsGlobalFunctions.gPOSDate;
                String posDate=dateTime.replaceAll(" ","%20");
                App.getAPIHelper().funGetProcessedKOTDetailsForKPS(clsGlobalFunctions.gPOSCode, waiterNo,posDate, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
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
                                        { }
                                        else
                                        {
                                            clsBillDtl objKot = new clsBillDtl();
                                            objKot.setStrKOTNo(mJsonObject.get("KOTNo").getAsString());
                                            objKot.setStrItemCode(mJsonObject.get("ItemCode").getAsString());
                                            objKot.setStrItemName(mJsonObject.get("ItemName").getAsString());
                                            objKot.setDblRate(Double.parseDouble(mJsonObject.get("Rate").getAsString()));
                                            objKot.setDblQuantity(Double.parseDouble(mJsonObject.get("Qty").getAsString()));
                                            objKot.setDblAmount(Double.parseDouble(mJsonObject.get("Amount").getAsString()));
                                            objKot.setStrKOTDate(mJsonObject.get("KOTDate").getAsString());
                                            objKot.setStrKOTTime(mJsonObject.get("KOTTime").getAsString());
                                            objKot.setStrTableNo(mJsonObject.get("TableNo").getAsString());
                                            objKot.setStrTableName(mJsonObject.get("TableName").getAsString());
                                            objKot.setStrWaiterNo(mJsonObject.get("WaiterNo").getAsString());
                                            objKot.setStrWaiterName(mJsonObject.get("WaiterName").getAsString());
                                            objKot.setTmeOrderProcessing(mJsonObject.get("ProcessTime").getAsString());
                                            objKot.setStrKOTTimeDifferenceResult(mJsonObject.get("CheckedTimeDiffResult").getAsString());

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

                                            Date delay = df.parse(objKOT1.getTmeOrderProcessing());
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

                                clsKPSAdapter adapter= new clsKPSAdapter(getBaseContext(),hmKOTDtl,arrListItem,hmSelectedKOTDtl);
                                adapter.setCustomButtonListner((clsKPSAdapter.customButtonListener) clsKPSScreen.this);
                                gvKPSGridview.setAdapter(adapter);
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


    private void funPickedUpKOTItemForKPSWS(String value,String type) {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected())
            {
                JsonObject objKPSDtl=new JsonObject();
                try
                {
                   if(type.equals("KOT"))
                    {
                        if(hmKOTDtl.size()>0)
                        {
                            if(hmKOTDtl.containsKey(value))
                            {
                                JsonArray arrKPSClass = new JsonArray();
                                List<clsBillDtl> arrSelectedKotItemList=hmKOTDtl.get(value);

                                for(int cnt=0;cnt<arrSelectedKotItemList.size();cnt++)
                                {
                                    clsBillDtl objBillDtl=arrSelectedKotItemList.get(cnt);
                                    JsonObject objRows=new JsonObject();
                                    objRows.addProperty("ItemCode",objBillDtl.getStrItemCode());
                                    arrKPSClass.add(objRows);
                                }
                                objKPSDtl.add("KOTItemDtl", arrKPSClass);
                            }
                        }

                        objKPSDtl.addProperty("ProcessingType","KOT");
                        objKPSDtl.addProperty("KOTNo",value);
                        objKPSDtl.addProperty("waiterNo",waiterNo);
                    }
                    else
                    {
                        objKPSDtl.addProperty("ProcessingType","Item");
                        objKPSDtl.addProperty("ItemCode",value.split("#")[0]);
                        objKPSDtl.addProperty("KOTNo",value.split("#")[2]);
                        objKPSDtl.addProperty("waiterNo",value.split("#")[5]);
                    }
                    objKPSDtl.addProperty("UserCode",clsGlobalFunctions.gUserCode);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                showDialog();
                App.getAPIHelper().funUpdatePickedUpKOTItemsForKPS(objKPSDtl, new BaseAPIHelper.OnRequestComplete<JsonObject>() {
                    @Override
                    public void onSuccess(JsonObject jObj) {
                        dismissDialog();
                        if (null != jObj) {
                            try {
                                String result = jObj.get("result").getAsString();
                                if(result.equalsIgnoreCase("success"))
                                {
                                    Toast.makeText(getApplicationContext(), "KOT Item Picked Successfully!!!!!", Toast.LENGTH_LONG).show();
                                }
                                else
                                {
                                    Toast.makeText(getApplicationContext(), "Failed to Pick Up!!!!", Toast.LENGTH_LONG).show();
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
                SnackBarUtils.showSnackBar(clsKPSScreen.mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsKPSScreen.mActivity, R.string.setup_your_server_settings);
        }
    }



    protected void showDialog() {
        if (null == pgDialog) {
            pgDialog = CommonUtils.getProgressDialog(clsKPSScreen.this, 0, false);
        }
        pgDialog.show();
    }

    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }


}
