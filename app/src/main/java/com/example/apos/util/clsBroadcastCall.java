package com.example.apos.util;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.telephony.TelephonyManager;
import android.util.Log;

import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.example.apos.activity.clsCounterDtl;
import com.example.apos.activity.clsDirectBill;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.activity.clsMainMenu;
import com.example.apos.bean.clsCustomerMaster;
import com.example.apos.activity.clsCallingNumberDialog;


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
import java.util.Date;

/**
 * Created by admin on 21/01/2017.
 */

public class clsBroadcastCall extends BroadcastReceiver
{
    private static int lastState = TelephonyManager.CALL_STATE_IDLE;
    private static Date callStartTime;
    Context context1;
    private String savedNumber="",custDetail="";
    TextView txtMobileNumber,txtCustDetails;
    private String isCallingAvailable;
    LinearLayout linearCalling;
    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent)
    {
        context1 = context;
        isCallingAvailable=clsGlobalFunctions.gCallingAvailable;

        //new clsDirectBill();
        if (intent.getAction().equals("android.intent.action.NEW_OUTGOING_CALL"))
        {
            savedNumber = intent.getExtras().getString("android.intent.extra.PHONE_NUMBER");
        }
        else
        {
            Log.d("IncomingBroadcastReceiver: onReceive: ", "flag1");

            String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
            Log.d("IncomingBroadcastReceiver: onReceive: ", stateStr);

            String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
            System.out.println("Number :" + number);

            if(isCallingAvailable.equals("Yes"))
            {
                if (null != number)
                {
                    clsGlobalFunctions.gPhoneNo="";
                    clsGlobalFunctions.gCustName="";

                    clsGlobalFunctions.gPhoneNo = number.substring(number.length()-10);


                    //new GetCustomerInfo().execute(clsGlobalFunctions.gPhoneNo);

                /* if (number.startsWith("+91")) {

                    clsGlobalFunctions.gPhoneNo = number.replace("+91", "");
                } else {
                    clsGlobalFunctions.gPhoneNo = number;
                }
                //stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING) ||
                if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING) || stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {

                    Log.d("Ringing", "Phone is ringing");

                    new GetCustomerInfo().execute(clsGlobalFunctions.gPhoneNo);

                    Intent i = new Intent(context, clsCallingNumberDialog.class);
                    i.putExtras(intent);
                    i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    //Wait.oneSec();
                    context.startActivity(i);
                }*/

                    int state1 = 0;
                    if (stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE))
                    {
                        state1 = TelephonyManager.CALL_STATE_IDLE;

                    }
                    else if (stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK))
                    {
                        state1 = TelephonyManager.CALL_STATE_OFFHOOK;

                        Intent i = new Intent(context, clsDirectBill.class);
                        i.putExtras(intent);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(i);

                    }
                    else if (stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING))
                    {
                        state1 = TelephonyManager.CALL_STATE_RINGING;

                        Intent i = new Intent(context, clsDirectBill.class);
                        i.putExtras(intent);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                        context.startActivity(i);


                    /* // new GetCustomerInfo().execute(clsGlobalFunctions.gPhoneNo);


                    if(!clsGlobalFunctions.gCustName.isEmpty())
                    {
                        clsDirectBill.btnMemberName.setText(clsGlobalFunctions.gCustName);
                    }
                  */

                   /* final Dialog dialog = new Dialog(context);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.showcallingnumber);
                    txtMobileNumber = (TextView) dialog.findViewById(R.id.txtCallingNumber);
                    txtCustDetails=(TextView) dialog.findViewById(R.id.txtCallerDetails);
                    linearCalling=(LinearLayout) dialog.findViewById(R.id.linearCalling);
                    txtMobileNumber.setText("clsGlobalFunctions.gPhoneNo");
                    txtCustDetails.setText(custDetail);*/

                    }
                    //new clsCallingNumberDialog.

                }
            }

        }
    }
    private class GetCustomerInfo extends AsyncTask<String, Void, ArrayList> {
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
        protected ArrayList doInBackground(String... params) {
            ArrayList arrCustomerList= new ArrayList();
            String result="";
            String custPhone=params[0];
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(clsGlobalFunctions.gAPOSWebSrviceURL+"/funGetCustomerDetail?MobileNo="+params[0]+"&POSCode="+clsGlobalFunctions.gPOSCode+"&ClientCode="+clsGlobalFunctions.gClientCode);
            System.out.println(clsGlobalFunctions.gAPOSWebSrviceURL+"/funGetCustomerDetail?MobileNo="+params[0]+"&POSCode="+clsGlobalFunctions.gPOSCode+"&ClientCode="+clsGlobalFunctions.gClientCode);

            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
                JSONObject jObj = new JSONObject(text);
                JSONArray mJsonArray = (JSONArray) jObj.get("CustomerDetails");
                JSONObject mJsonObject = new JSONObject();

                for (int i = 0; i < mJsonArray.length(); i++) {
                    mJsonObject = (JSONObject) mJsonArray.get(i);
                    if (mJsonObject.get("CustCode").toString().equals("No Customer Found")) {
                        //memberInfo = "no data";
                    } else
                    {
                        clsCustomerMaster objCustomerMaster = new clsCustomerMaster();
                        objCustomerMaster.setCustomerCode(mJsonObject.get("CustCode").toString());
                        objCustomerMaster.setCustomerName(mJsonObject.get("CustName").toString());
                        objCustomerMaster.setCustomerType(mJsonObject.get("CustType").toString());
                        objCustomerMaster.setCustomerBuildingName(mJsonObject.get("BuildingName").toString());
                        objCustomerMaster.setCustomerTypeCode(mJsonObject.get("CustTypeCode").toString());

                        arrCustomerList.add(objCustomerMaster);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return arrCustomerList;
        }

        protected void onPostExecute(ArrayList arrayList)
        {
            if (arrayList.size()>0)
            {
                for(int cnt=0;cnt<arrayList.size();cnt++)
                {
                    clsCustomerMaster objCustomerMaster = (clsCustomerMaster) arrayList.get(cnt);
                    //clsGlobalFunctions.gCustName=objCustomerMaster.getCustomerName();
                    custDetail=objCustomerMaster.getCustomerName()+"\n"+objCustomerMaster.getCustomerBuildingName();
                    clsGlobalFunctions.gCustName=custDetail;
                    System.out.println("custDetail :\n"+custDetail);

                    Toast.makeText(context1,custDetail,Toast.LENGTH_LONG).show();
                    ArrayList<String>CustTypeList= new ArrayList<String>();
                    if(!objCustomerMaster.getCustomerType().isEmpty())
                    {
                        CustTypeList.add(objCustomerMaster.getCustomerType());
                    }
                 }

            }
            else
            {
                Toast.makeText(context1,"Customer Not Found",Toast.LENGTH_LONG).show();
            }
        }
    }
}
