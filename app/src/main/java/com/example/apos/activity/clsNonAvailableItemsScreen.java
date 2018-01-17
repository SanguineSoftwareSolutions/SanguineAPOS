package com.example.apos.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.apos.adapter.clsNonAvailableItemListAdapter;
import com.example.apos.bean.clsKotItemsListBean;
import com.example.apos.listeners.clsKotItemListSelectionListener;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * Created by User on 11-05-2017.
 */

  public class clsNonAvailableItemsScreen extends Activity implements clsKotItemListSelectionListener
    {
      private ConnectivityManager connectivityManager;
      private GridView gridViewItemList,gridNonAvailableItem;
      private EditText edtItemListSearch;
      private ArrayList arrListItemMaster;
      private List<clsKotItemsListBean> arrListSelectedItemMaster=new ArrayList<clsKotItemsListBean>();
      private clsKotItemListSelectionListener itemListSelectionListener = null;
      private HashMap<String,String> mapNonAvailableItem;
      private HashSet setNonAvailableItem;
      private  String keyCase="upperCase";
      private Button btnClose;
      @Override
        protected void onCreate(Bundle savedInstanceState) {
          super.onCreate(savedInstanceState);
          setContentView(R.layout.nonavailablescreen);
          itemListSelectionListener = (clsKotItemListSelectionListener) clsNonAvailableItemsScreen.this;
          funGetWidget();
          new MenuItemPriceListWS().execute("All");
          new funGetNonAvailableItemList().execute();

       /* edtItemListSearch.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v)
        });*/


        btnClose.setOnClickListener(new View.OnClickListener()
        {
          @Override
          public void onClick(View v)
          {
             finish();
          }
        });

      }




      private void isBack(View v)
      {

        CharSequence cc = edtItemListSearch.getText();
        if (cc != null && cc.length() > 0)
        {

          edtItemListSearch.setText("");
          edtItemListSearch.append(cc.subSequence(0, cc.length() - 1));
          String str= edtItemListSearch.getText().toString() ;

          ArrayList arrayListtemp = new ArrayList();
          if(null!=arrListItemMaster) {
            for (int cnt = 0; cnt < arrListItemMaster.size(); cnt++) {
              clsKotItemsListBean obj = (clsKotItemsListBean) arrListItemMaster.get(cnt);
              if (obj.getStrItemName().toLowerCase().contains(str.toLowerCase())) {
                arrayListtemp.add(arrListItemMaster.get(cnt));

              }
            }
          }
          funSetList(arrayListtemp);



        }
      }

      private void funGetWidget()
      {
        connectivityManager = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        gridViewItemList =(GridView) findViewById(R.id.gvitemslist);
        gridNonAvailableItem =(GridView) findViewById(R.id.gridNonAvailableItem);
        edtItemListSearch =(EditText) findViewById(R.id.edtitemslistsearch);
        btnClose=(Button) findViewById(R.id.btnClose);
        mapNonAvailableItem=new HashMap();
        setNonAvailableItem=new HashSet<String>();
      }


      private void funSetText(String text)
      {
        edtItemListSearch.setText(edtItemListSearch.getText().toString() + text);
        int position = edtItemListSearch.getText().length();
        Editable editObj= edtItemListSearch.getText();
        Selection.setSelection(editObj, position);

        String str= edtItemListSearch.getText().toString() ;

        ArrayList arrayListtemp = new ArrayList();
        for (int cnt = 0; cnt <arrListItemMaster.size(); cnt++)
        {
          clsKotItemsListBean obj = (clsKotItemsListBean) arrListItemMaster.get(cnt);

          if ((obj.getStrItemName().toLowerCase().contains(str.toLowerCase())) || (obj.getStrExternalCode().toLowerCase().contains(str.toLowerCase())) )
          {
            arrayListtemp.add(arrListItemMaster.get(cnt));
          }
        }
        funSetList(arrayListtemp);
      }

      private void funSetList(ArrayList arrList)
      {
        clsNonAvailableItemListAdapter itemsListGridViewAdapter
                = new clsNonAvailableItemListAdapter(clsNonAvailableItemsScreen.this,clsNonAvailableItemsScreen.this, arrList, itemListSelectionListener,"normal");
        gridViewItemList.setAdapter(itemsListGridViewAdapter);
      }

      private void funSetNonAvailableItemList(List<clsKotItemsListBean> arrList)
      {
        clsNonAvailableItemListAdapter itemsListGridViewAdapter
                = new clsNonAvailableItemListAdapter(clsNonAvailableItemsScreen.this,clsNonAvailableItemsScreen.this, arrListSelectedItemMaster, itemListSelectionListener,"NonAvailable");
        gridNonAvailableItem.setAdapter(itemsListGridViewAdapter);

      }


      @Override
      public void getItemsListSelectedForOder(final String strItemCode, final String strItemName, final String strSubGroupCode, final double dblSalePrice)
      {
       //Toast.makeText(clsNonAvailableItemsScreen.this,"strItemName="+strItemName,Toast.LENGTH_LONG).show();

        AlertDialog.Builder builder1 = new AlertDialog.Builder(clsNonAvailableItemsScreen.this);
        builder1.setMessage("Is not available?? Move Item!!! ");
        builder1.setCancelable(true);
        builder1.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                  public void onClick(DialogInterface dialog, int id)
                  {
                    if(setNonAvailableItem.contains(strItemName))
                    {
                      Toast.makeText(clsNonAvailableItemsScreen.this,"Item Already Not Available",Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                      setNonAvailableItem.add(strItemName);
                      clsKotItemsListBean obj = new clsKotItemsListBean();
                      obj.setStrItemCode(strItemCode);
                      obj.setStrItemName(strItemName);
                      obj.setStrSubGroupCode("");
                      obj.setDblSalePrice(dblSalePrice);
                      arrListSelectedItemMaster.add(obj);
                      funSetNonAvailableItemList(arrListSelectedItemMaster);
                      new funSaveNonAvailableItem().execute(strItemCode,strItemName);
                    }
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

      }





      private class MenuItemPriceListWS extends AsyncTask<String, Void, ArrayList>
      {
        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException
        {
          InputStream in = entity.getContent();
          StringBuffer out = new StringBuffer();
          int n = 1;
          while (n > 0)
          {
            byte[] b = new byte[4096];
            n = in.read(b);
            if (n > 0) out.append(new String(b, 0, n));
          }
          return out.toString();
        }


        @Override
        protected ArrayList doInBackground(String... params)
        {
          boolean flgAllItems=true;
          String menuType="";
          if(!params[0].equals("All"))
          {
            flgAllItems=false;
            menuType=params[1];
          }
          ArrayList arrListItemMaster = new ArrayList();
          HttpClient httpClient = new DefaultHttpClient();
          HttpContext localContext = new BasicHttpContext();
          clsGlobalFunctions objGlobal = new clsGlobalFunctions();
          String wsUrl=clsGlobalFunctions.gAPOSWebSrviceURL+"/funGetItemPriceDtl?POSCode="+clsGlobalFunctions.gPOSCode+"&areaCode="+clsGlobalFunctions.gDirectBillerAreaCode+"&menuHeadCode="+params[0]+"&areaWisePricing="+clsGlobalFunctions.gAreaWisePricing+"&fromDate="+objGlobal.funGetCurrentDate()+"&toDate="+objGlobal.funGetCurrentDate()+"&flgAllItems="+flgAllItems+"&menuType="+menuType;
          System.out.println("Counter Wise Billing= "+clsGlobalFunctions.gCounterWiseBilling);
          if(clsGlobalFunctions.gCounterWiseBilling.equals("Yes")) {
            wsUrl=clsGlobalFunctions.gAPOSWebSrviceURL+"/funGetItemPriceDtlCounterWise?POSCode="+clsGlobalFunctions.gPOSCode+"&areaCode="+clsGlobalFunctions.gDirectBillerAreaCode+"&menuHeadCode="+params[0]+"&areaWisePricing="+clsGlobalFunctions.gAreaWisePricing+"&fromDate="+objGlobal.funGetCurrentDate()+"&toDate="+objGlobal.funGetCurrentDate()+"&flgAllItems="+flgAllItems+"&counterCode="+clsGlobalFunctions.gCounterCode;
          }
          System.out.println(wsUrl);

          //HttpGet httpGet = new HttpGet(clsGlobalFunctions.gAPOSWebSrviceURL+"/funGetItemPriceDtl?POSCode="+clsGlobalFunctions.gPOSCode+"&areaCode="+clsGlobalFunctions.gDirectBillerAreaCode+"&menuHeadCode="+params[0]+"&areaWisePricing="+clsGlobalFunctions.gAreaWisePricing+"&fromDate="+objGlobal.funGetCurrentDate()+"&toDate="+objGlobal.funGetCurrentDate()+"&flgAllItems="+flgAllItems);
          HttpGet httpGet = new HttpGet(wsUrl);

          String text = null;

          try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            text = getASCIIContentFromEntity(entity);
            JSONObject jObj = new JSONObject(text);
            JSONArray mJsonArray = (JSONArray) jObj.get("tblmenuitempricingdtl");
            JSONObject mJsonObject = new JSONObject();
            for (int i = 0; i < mJsonArray.length(); i++)
            {
              mJsonObject = (JSONObject) mJsonArray.get(i);
              if (mJsonObject.get("ItemName").toString().equals(""))
              {
                //memberInfo = "no data";
              }
              else
              {
                clsKotItemsListBean obj = new clsKotItemsListBean();
                obj.setStrItemCode(mJsonObject.get("ItemCode").toString());
                obj.setStrItemName(mJsonObject.get("ItemName").toString());
                obj.setStrSubGroupCode("");
                obj.setStrExternalCode(mJsonObject.get("ExternalCode").toString());
                obj.setDblSalePrice(Double.parseDouble(mJsonObject.get("PriceMonday").toString()));
                arrListItemMaster.add(obj);
              }
            }
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
          System.out.println("Size=="+arrListItemMaster.size());
          return arrListItemMaster;
        }

    protected void onPostExecute(ArrayList arrListTemp)
        {
          if (arrListTemp.size()>0)
          {
            arrListItemMaster=arrListTemp;
            funSetList(arrListTemp);
          }
          else
          {
            funSetList(new ArrayList());

          }
        }
      }


      private class funSaveNonAvailableItem extends AsyncTask<String, Void, String>
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

          try {
            JSONObject objItemDtl=new JSONObject();
            JSONArray arrItemClass = new JSONArray();

            String dateTime=clsGlobalFunctions.funGetPOSDateTime();
            JSONObject objRows = new JSONObject();
            objRows.put("strItemCode", params[0]);
            objRows.put("strItemName", params[1]);
            objRows.put("strClientCode",clsGlobalFunctions.gClientCode);
            objRows.put("dteDate", dateTime);
            objRows.put("strPOSCode", clsGlobalFunctions.gPOSCode);

            arrItemClass.put(objRows);

            objItemDtl.put("NonAvailableItemDtl",arrItemClass);

            System.out.println(clsGlobalFunctions.gAPOSWebSrviceURL+"/funSaveNonAvailableItem");
            String hoURL = clsGlobalFunctions.gAPOSWebSrviceURL+"/funSaveNonAvailableItem";
            URL url = new URL(hoURL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json");
            OutputStream os = conn.getOutputStream();
            os.write(objItemDtl.toString().getBytes());
            os.flush();
            if (conn.getResponseCode() != HttpURLConnection.HTTP_CREATED)
            {
              throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));

            while ((output = br.readLine()) != null) {
              op += output;
            }
            System.out.println("result="+op);
            conn.disconnect();

          } catch (Exception e) {
            e.printStackTrace();
          }
          return op;
        }

        protected void onPostExecute(String res)
        {
          if(res.equals("true"))
          {
            Toast.makeText(clsNonAvailableItemsScreen.this, "Item Moved" ,Toast.LENGTH_SHORT).show();
          }
        }
      }




      private class funGetNonAvailableItemList extends AsyncTask<String, Void, ArrayList>
      {
        protected String getASCIIContentFromEntity(HttpEntity entity) throws IllegalStateException, IOException
        {
          InputStream in = entity.getContent();
          StringBuffer out = new StringBuffer();
          int n = 1;
          while (n > 0)
          {
            byte[] b = new byte[4096];
            n = in.read(b);
            if (n > 0) out.append(new String(b, 0, n));
          }
          return out.toString();
        }


        @Override
        protected ArrayList doInBackground(String... params)
        {
           ArrayList arrListSelectedItemMaster = new ArrayList();
          HttpClient httpClient = new DefaultHttpClient();
          HttpContext localContext = new BasicHttpContext();
          clsGlobalFunctions objGlobal = new clsGlobalFunctions();
          String wsUrl=clsGlobalFunctions.gAPOSWebSrviceURL+"/funGetNonAvailableItems?strClientCode="+clsGlobalFunctions.gClientCode;
          System.out.println("wsUrl= "+wsUrl);

          HttpGet httpGet = new HttpGet(wsUrl);

          String text = null;

          try {
            HttpResponse response = httpClient.execute(httpGet, localContext);
            HttpEntity entity = response.getEntity();
            text = getASCIIContentFromEntity(entity);
            JSONObject jObj = new JSONObject(text);
            JSONArray mJsonArray = (JSONArray) jObj.get("NonAvailableItemList");
            JSONObject mJsonObject = new JSONObject();
            for (int i = 0; i < mJsonArray.length(); i++)
            {
              mJsonObject = (JSONObject) mJsonArray.get(i);
              if (mJsonObject.get("strItemName").toString().equals(""))
              {
                //memberInfo = "no data";
              }
              else
              {
                clsKotItemsListBean obj = new clsKotItemsListBean();
                obj.setStrItemCode(mJsonObject.get("strItemCode").toString());
                obj.setStrItemName(mJsonObject.get("strItemName").toString());
                obj.setStrSubGroupCode("");
                obj.setStrExternalCode("");
                obj.setDblSalePrice(0);
                arrListSelectedItemMaster.add(obj);
                setNonAvailableItem.add(mJsonObject.get("strItemName").toString());
              }
            }
          }
          catch (Exception e)
          {
            e.printStackTrace();
          }
          System.out.println("Size=="+arrListSelectedItemMaster.size());
          return arrListSelectedItemMaster;
        }




        protected void onPostExecute(ArrayList arrListTemp)
        {
          if (arrListTemp.size()>0)
          {
            arrListSelectedItemMaster=arrListTemp;
            funSetNonAvailableItemList(arrListTemp);
          }
          else
          {
            funSetNonAvailableItemList(new ArrayList());
          }
        }
      }

  }
