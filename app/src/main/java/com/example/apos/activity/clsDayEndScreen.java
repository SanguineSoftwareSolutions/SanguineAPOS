package com.example.apos.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.bean.clsKotItemsListBean;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


/**
 * Created by Monika on 8/5/2017.
 */

public class clsDayEndScreen extends Activity implements View.OnClickListener
{
    private ConnectivityManager connectivityManager;
    private Button btnDayStart,btnDayEnd,btnClose;
    private TextView txtShiftNo,txtDayEndDate,txtPOSName;
    private String shiftDate="";
    private int shiftNo;



    public static HashMap<String,clsKotItemsListBean> gblHmCartData=new HashMap<String,clsKotItemsListBean>();
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dayendscreen);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        widget();
        txtShiftNo.setText("Shift No - "+clsGlobalFunctions.gShiftNo);
        shiftNo=clsGlobalFunctions.gShiftNo;
        txtDayEndDate.setText(clsGlobalFunctions.gPOSDateHeader);
        txtPOSName.setText(clsGlobalFunctions.gPOSName);
        if (clsGlobalFunctions.gShiftEnd.equals("") && clsGlobalFunctions.gDayEnd.equals("N"))
        {
            btnDayStart.setEnabled(true);
            btnDayEnd.setEnabled(false);
            btnDayEnd.setBackgroundResource(R.drawable.gray_button);
        }
        else if (clsGlobalFunctions.gShiftEnd.equals("N") && clsGlobalFunctions.gDayEnd.equals("N"))
        {
            btnDayStart.setEnabled(false);
            btnDayEnd.setEnabled(true);
            btnDayStart.setBackgroundResource(R.drawable.gray_button);
        }


        funGetPOSDayEndDetails();
    }



    private void widget()
    {
        btnDayStart = (Button) findViewById(R.id.btnDayStart);
        btnDayEnd = (Button) findViewById(R.id.btnDayEnd);
        btnClose = (Button) findViewById(R.id.btnDayEndClose);
        btnDayStart.setOnClickListener(this);
        btnDayEnd.setOnClickListener(this);
        btnClose.setOnClickListener(this);
        txtShiftNo = (TextView) findViewById(R.id.txtShiftNo);
        txtDayEndDate = (TextView) findViewById(R.id.txtDayEndDate);
        txtPOSName = (TextView) findViewById(R.id.txtPOSName);
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
    }





    @Override
    public void onClick(View view)
    {
        switch(view.getId())
        {
            case R.id.btnDayStart:
              funStartDay();
              break;

            case R.id.btnDayEnd:
                AlertDialog.Builder builder1 = new AlertDialog.Builder(clsDayEndScreen.this);
                builder1.setMessage("Do You Want To End Day???");
                builder1.setCancelable(true);
                builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {
                              funEndDay();
                            }
                        }
                );

                builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                dialog.cancel();
                            }
                        }
                );
                AlertDialog alert11 = builder1.create();
                alert11.show();

              break;

            case R.id.btnDayEndClose:
                Intent intent = new Intent(clsDayEndScreen.this, clsMainMenu.class);
                intent.putExtra("PosName", clsGlobalFunctions.gPOSName);
                intent.putExtra("PosCode", clsGlobalFunctions.gPOSCode);
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }


    private void funStartDay()
    {
        try
        {
            funCallWebService objWS=new funCallWebService();
            String []newUrl=clsGlobalFunctions.gAPOSWebSrviceURL.toString().split("APOSIntegration");
            String url=newUrl[0]+"DayEndProcessTransaction/funShiftStartProcess?strPOSCode="+clsGlobalFunctions.gPOSCode+"&shiftNo="+shiftNo;
            objWS.execute(url);
            objWS.get();
            JSONObject mJsonObj=new JSONObject();
            mJsonObj= objWS.get();

            clsGlobalFunctions.gShiftEnd=mJsonObj.get("shiftEnd").toString();
            clsGlobalFunctions.gDayEnd=mJsonObj.get("DayEnd").toString();
            clsGlobalFunctions.gShiftNo=Integer.valueOf(mJsonObj.get("shiftNo").toString());


            Intent intent = new Intent(clsDayEndScreen.this, clsMainMenu.class);
            intent.putExtra("PosName", clsGlobalFunctions.gPOSName);
            intent.putExtra("PosCode", clsGlobalFunctions.gPOSCode);
            startActivity(intent);
            finish();



        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }



    private void funEndDay()
    {
        try
        {
            funCheckPendingBillAndKOT();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    private void funCheckPendingBillAndKOT()
    {
        try
        {
            String checkBills="N",checkTables="Y";
            funCallWebService objWS=new funCallWebService();
            String []newUrl=clsGlobalFunctions.gAPOSWebSrviceURL.toString().split("APOSIntegration");
            String url=newUrl[0]+"DayEndProcessTransaction/funCheckPendingBillsAndTables?strPOSCode="+clsGlobalFunctions.gPOSCode+"&POSDate="+clsGlobalFunctions.gPOSStartDate;
            objWS.execute(url);
            objWS.get();
            JSONObject mJsonObj=new JSONObject();
            mJsonObj= objWS.get();
            boolean pendingBillFound=Boolean.parseBoolean(mJsonObj.get("PendingBills").toString());
            boolean pendingKOTFound=Boolean.parseBoolean(mJsonObj.get("BusyTables").toString());
//            if(pendingBillFound)
//        {
//            Toast.makeText(clsDayEndScreen.this,"Please Settle Pending Bills",Toast.LENGTH_LONG).show();
//            return;
//        }
//            if(pendingKOTFound)
//            {
//                Toast.makeText(clsDayEndScreen.this,"Sorry Tables are Busy Now",Toast.LENGTH_LONG).show();
//                return;
//            }

            if(checkBills.equals("N") && checkBills.equals("N"))
            {
                funEndDayProcess();
            }

        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    private void funEndDayProcess()
    {
        try
        {
            funDayEndWS objWS=new funDayEndWS();
            String []newUrl=clsGlobalFunctions.gAPOSWebSrviceURL.toString().split("APOSIntegration");
            String url=newUrl[0]+"DayEndProcessConsolidate/funDayEndProcessWithoutDetails";
            objWS.execute(url);
            objWS.get();
            String result="";
            result= objWS.get();

          System.out.println("result"+result);
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


    @Override
    public void onBackPressed()
    {
        Intent intent = new Intent(clsDayEndScreen.this, clsMainMenu.class);
        intent.putExtra("PosName", clsGlobalFunctions.gPOSName);
        intent.putExtra("PosCode", clsGlobalFunctions.gPOSCode);
        startActivity(intent);
        finish();
    }




    private void funGetPOSDayEndDetails()
    {
        try
        {
            funCallWebService objWS=new funCallWebService();
            String []newUrl=clsGlobalFunctions.gAPOSWebSrviceURL.toString().split("APOSIntegration");
            String url=newUrl[0]+"clsUtilityController/funGetPOSWiseDayEndData?POSCode="+clsGlobalFunctions.gPOSCode+"&UserCode="+clsGlobalFunctions.gUserCode;
            objWS.execute(url);
            objWS.get();
            JSONObject mJsonObj=new JSONObject();
            mJsonObj= objWS.get();

            if(null!=mJsonObj.get("startDate"))
            {
                shiftDate = mJsonObj.get("startDate").toString();

                DateFormat df= new SimpleDateFormat("yyyy-MM-dd");
                Date date=df.parse(mJsonObj.get("startDate").toString());
                txtDayEndDate.setText(new SimpleDateFormat("dd-MM-yyyy").format(date));

                txtShiftNo.setText(Integer.valueOf(mJsonObj.get("ShiftNo").toString()));
                shiftNo = Integer.valueOf(mJsonObj.get("ShiftNo").toString());
                shiftNo = Integer.valueOf(mJsonObj.get("ShiftNo").toString());
            }
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
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

    private class funDayEndWS extends AsyncTask<String, Void, String>
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
        protected String doInBackground(String... params) {

            HttpResponse response=null;
            String output = "", op = "";

            try
            {
                JSONObject objDayEndDtl=new JSONObject();
                objDayEndDtl.put("strPOSCode", clsGlobalFunctions.gPOSCode);
                objDayEndDtl.put("ShiftNo", shiftNo);
                objDayEndDtl.put("userCode", clsGlobalFunctions.gUserCode);
                objDayEndDtl.put("strPOSDate", clsGlobalFunctions.gPOSStartDate);
                objDayEndDtl.put("strClientCode", clsGlobalFunctions.gClientCode);

                System.out.println(params[0]);
                String hoURL = params[0];
                URL url = new URL(hoURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                OutputStream os = conn.getOutputStream();
                os.write(objDayEndDtl.toString().getBytes());
                os.flush();
                if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
                {
                    throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
                }
                BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

                while ((output = br.readLine()) != null)
                {
                    op += output;
                }
                System.out.println("Result= " + op);
                conn.disconnect();

//                JSONParser parser = new JSONParser();
//                Object obj = parser.parse(op);
//                josnObjRet = (JSONObject) obj;




                while ((output = br.readLine()) != null) {
                    op += output;
                }
                System.out.println("DayEnd="+op);
                conn.disconnect();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return op;
        }

        protected void onPostExecute(String res)
        {

            Toast.makeText(clsDayEndScreen.this, "Day Ended Successfully!!!",Toast.LENGTH_SHORT).show();

            btnDayStart.setEnabled(true);
            btnDayEnd.setEnabled(false);
            btnDayEnd.setBackgroundResource(R.drawable.gray_button);
            btnDayStart.setBackgroundResource(R.drawable.button_blue);
            funGetPOSDayEndDetails();


//            AlertDialog.Builder builder1 = new AlertDialog.Builder(clsDayEndScreen.this);
//            builder1.setMessage("Do You Want To Start Day???");
//            builder1.setCancelable(true);
//            builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id)
//                        {
//                            dialog.dismiss();
//                            funStartDay();
//                        }
//                    }
//            );
//
//            builder1.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                        public void onClick(DialogInterface dialog, int id)
//                        {
//                            dialog.cancel();
//                        }
//                    }
//            );
//            AlertDialog alert11 = builder1.create();
//            alert11.show();


        }
    }


}
