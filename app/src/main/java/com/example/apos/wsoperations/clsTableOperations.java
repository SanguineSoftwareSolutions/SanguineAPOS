package com.example.apos.wsoperations;

import com.example.apos.bean.clsTableMaster;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;

public class clsTableOperations {

    ArrayList arrListTableMaster = new ArrayList();
    public ArrayList<clsTableMaster> funGetTableList(String posCode, String clientCode) {

        RequestParams params = new RequestParams();
        params.put("POSCode", "P03");
        params.put("CMSIntegration", "N");
        params.put("memberAsTable", "N");


        AsyncHttpClient client = new AsyncHttpClient();
        client.get("http://192.168.1.132:8080/prjSanguineWebService/APOSIntegration/funGetTableList", params, new AsyncHttpResponseHandler() {

            // When the response returned by REST has Http response code '200'
            @Override
            public void onSuccess(String response) {
                // Hide Progress Dialog
                //prgDialog.hide();
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray mJsonArray = (JSONArray) jObj.get("TableList");
                    JSONObject mJsonObject = new JSONObject();
                    for (int i = 0; i < mJsonArray.length(); i++)
                    {
                        mJsonObject = (JSONObject) mJsonArray.get(i);
                        if (mJsonObject.get("TableName").toString().equals(""))
                        {
                            //memberInfo = "no data";
                        }
                        else
                        {
                            clsTableMaster objTable = new clsTableMaster();
                            objTable.setStrTableName(mJsonObject.get("TableName").toString());
                            objTable.setStrTableNo(mJsonObject.get("TableNo").toString());
                            objTable.setStrTableStatus(mJsonObject.get("TableStatus").toString());
                            arrListTableMaster.add(objTable);
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
        });

        return arrListTableMaster;
    }
}
