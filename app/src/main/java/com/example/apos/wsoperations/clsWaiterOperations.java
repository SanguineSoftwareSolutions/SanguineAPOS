package com.example.apos.wsoperations;

import android.os.AsyncTask;

import com.example.apos.bean.clsWaiterMaster;

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

public class clsWaiterOperations
{
    ArrayList arrListWaiterMaster1 = new ArrayList();

    public ArrayList<clsWaiterMaster> funGetWaiterList(String posCode,String clientCode) {

        new WebService().execute();
        /*
        clsWaiterMaster objWaiter=new clsWaiterMaster();
        objWaiter.setStrWaterNo("W001");
        objWaiter.setStrWaiterName("AAA");

        clsWaiterMaster objWaiter1=new clsWaiterMaster();
        objWaiter1.setStrWaterNo("W002");
        objWaiter1.setStrWaiterName("BBB");

        arrListWaiterMaster.add(objWaiter);
        arrListWaiterMaster.add(objWaiter1);*/

        /*
        ArrayList arrListWaiterMaster = new ArrayList();
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(
                "http://192.168.1.132:8080/prjSanguineWebService/APOSIntegration/funGetWaiterList"
                 +"?POSCode=P03&CMSIntegration=N&memberAsTable=N");

        try {

            HttpResponse response = httpclient.execute(httppost);
            String jsonResult = inputStreamToString(response.getEntity().getContent()).toString();

            JSONObject jObj = new JSONObject(jsonResult);
            JSONArray mJsonArray = (JSONArray) jObj.get("WaiterList");
            JSONObject mJsonObject = new JSONObject();
            for (int i = 0; i < mJsonArray.length(); i++)
            {
                mJsonObject = (JSONObject) mJsonArray.get(i);
                if (mJsonObject.get("WaiterName").toString().equals(""))
                {
                    //memberInfo = "no data";
                }
                else
                {
                    clsWaiterMaster objWaiter = new clsWaiterMaster();
                    objWaiter.setStrWaiterName(mJsonObject.get("WaiterName").toString());
                    objWaiter.setStrWaterNo(mJsonObject.get("WaiterNo").toString());
                    arrListWaiterMaster.add(objWaiter);
                }
            }

        }catch(Exception e)
        {
            e.printStackTrace();
        }
        return arrListWaiterMaster;*/


/*
        RequestParams params = new RequestParams();
        params.put("POSCode", posCode);
        params.put("CMSIntegration", "N");
        params.put("memberAsTable", "N");

        SyncHttpClient client = new SyncHttpClient();
        client.get("http://192.168.1.132:8080/prjSanguineWebService/APOSIntegration/funGetWaiterList"
                , params, new AsyncHttpResponseHandler() {

            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                //prgDialog.hide();
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray mJsonArray = (JSONArray) jObj.get("WaiterList");
                    JSONObject mJsonObject = new JSONObject();
                    for (int i = 0; i < mJsonArray.length(); i++)
                    {
                        mJsonObject = (JSONObject) mJsonArray.get(i);
                        if (mJsonObject.get("WaiterName").toString().equals(""))
                        {
                            //memberInfo = "no data";
                        }
                        else
                        {
                            clsWaiterMaster objWaiter = new clsWaiterMaster();
                            objWaiter.setStrWaiterName(mJsonObject.get("WaiterName").toString());
                            objWaiter.setStrWaterNo(mJsonObject.get("WaiterNo").toString());
                            arrListWaiterMaster.add(objWaiter);
                        }
                    }
                }catch(Exception e)
                {
                    e.printStackTrace();
                }
            }

            // When the response returned by REST has Http response code other than '200'
            @Override
            public void onFailure(int statusCode, Throwable error,
                                  String content) {
                // Hide Progress Dialog
                //prgDialog.hide();
                // When Http response code is '404'
                if (statusCode == 404) {
                    //Toast.makeText(getApplicationContext(), "Requested resource not found", Toast.LENGTH_LONG).show();
                }
                // When Http response code is '500'
                else if (statusCode == 500) {
                    //Toast.makeText(getApplicationContext(), "Something went wrong at server end", Toast.LENGTH_LONG).show();
                }
                // When Http response code other than 404, 500
                else {
                    //Toast.makeText(getApplicationContext(), "Unexpected Error occcured! [Most common Error: Device might not be connected to Internet or remote server is not up and running]", Toast.LENGTH_LONG).show();
                }
            }

        });*/

        return arrListWaiterMaster1;
    }

        private class WebService extends AsyncTask<Void, Void, ArrayList> {
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
            protected ArrayList doInBackground(Void... params) {

                ArrayList arrListWaiterMaster = new ArrayList();
                HttpClient httpClient = new DefaultHttpClient();
                HttpContext localContext = new BasicHttpContext();
                HttpGet httpGet = new HttpGet("http://192.168.1.132:8080/prjSanguineWebService/APOSIntegration/funGetWaiterList?POSCode=P03&CMSIntegration=N&memberAsTable=N");
                String text = null;
                try {
                    HttpResponse response = httpClient.execute(httpGet, localContext);
                    HttpEntity entity = response.getEntity();
                    text = getASCIIContentFromEntity(entity);
                    JSONObject jObj = new JSONObject(text);
                    JSONArray mJsonArray = (JSONArray) jObj.get("WaiterList");
                    JSONObject mJsonObject = new JSONObject();
                    for (int i = 0; i < mJsonArray.length(); i++) {
                        mJsonObject = (JSONObject) mJsonArray.get(i);
                        if (mJsonObject.get("WaiterName").toString().equals("")) {
                            //memberInfo = "no data";
                        } else {
                            clsWaiterMaster objWaiter = new clsWaiterMaster();
                            objWaiter.setStrWaiterName(mJsonObject.get("WaiterName").toString());
                            objWaiter.setStrWaterNo(mJsonObject.get("WaiterNo").toString());
                            arrListWaiterMaster.add(objWaiter);
                        }
                    }
                }
                catch (Exception e) {
                }
                return arrListWaiterMaster;
            }
            protected void onPostExecute(ArrayList arrListTemp) {
                arrListWaiterMaster1=arrListTemp;
            }
        }
}




