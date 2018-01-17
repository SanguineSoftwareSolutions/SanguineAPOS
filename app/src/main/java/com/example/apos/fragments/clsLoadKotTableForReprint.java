package com.example.apos.fragments;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.apos.activity.R;
import com.example.apos.activity.clsGlobalFunctions;
import com.example.apos.adapter.clsReprintKotTableAdapter;
import com.example.apos.adapter.clsVoidKotTableAdapter;
import com.example.apos.bean.clsTableMaster;
import com.example.apos.listeners.clsReprintKotActListener;
import com.example.apos.listeners.clsReprintKotTableListener;
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

public class clsLoadKotTableForReprint extends Fragment implements clsReprintKotTableListener
{
    private clsReprintKotTableListener tableSelectionListener;
    private Spinner spinnerTableList;
    private GridView kotTableGridview;
    ArrayList arrListTableMaster;
    ArrayList arrListKotMaster;
    private static String tableNo;
    public clsReprintKotActListener objReprintKotActListener;
    private ConnectivityManager connectivityManager;

    public static clsLoadKotTableForReprint getInstance()
    {
        clsLoadKotTableForReprint mKotTable = new clsLoadKotTableForReprint();
        return mKotTable;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.loadtableforreprintkot, container, false);
        connectivityManager = (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        try
        {
            tableSelectionListener = (clsReprintKotTableListener) clsLoadKotTableForReprint.this;
            objReprintKotActListener=(clsReprintKotActListener)getActivity();
        }
        catch(Exception ex)
        {
            Log.i("Tg", " Unable to initialize database." + ex.getMessage());
        }

        kotTableGridview = (GridView) rootView.findViewById(R.id.reprintKottable_Gridview);


        if (new clsUtility().isInternetConnectionAvailable(connectivityManager))
        {
            new LoadTable().execute();
        }
        else
        {
            Toast.makeText(getActivity(),"Internet Connection Not Found",Toast.LENGTH_LONG).show();
        }

        return rootView;
    }

    private void funFillSpinner(ArrayList arrlist)
    {
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                (this.getActivity(), android.R.layout.simple_spinner_item,arrlist);

        dataAdapter.setDropDownViewResource
                (android.R.layout.simple_spinner_dropdown_item);
        spinnerTableList.setAdapter(dataAdapter);

    }

    @Override
    public void getTableListSelected(String tableNo,String tableName)
    {
        //clsVoidKotScreen.setTableSelectedResult(tableNo, tableName);
        objReprintKotActListener.funSetSelectedKotTableData(tableNo, tableName);
    }


    private class LoadTable extends AsyncTask<Void, Void, ArrayList> {
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

            ArrayList arrListTableMaster = new ArrayList();
            HttpClient httpClient = new DefaultHttpClient();
            HttpContext localContext = new BasicHttpContext();
            HttpGet httpGet = new HttpGet(clsGlobalFunctions.gAPOSWebSrviceURL+"/funGetBusyTableList?POSCode="+clsGlobalFunctions.gPOSCode+"&CMSIntegration="+clsGlobalFunctions.gCMSIntegrationYN+"&memberAsTable="+clsGlobalFunctions.gTreatMemberAsTable+"");
            String text = null;
            try {
                HttpResponse response = httpClient.execute(httpGet, localContext);
                HttpEntity entity = response.getEntity();
                text = getASCIIContentFromEntity(entity);
                JSONObject jObj = new JSONObject(text);
                JSONArray mJsonArray = (JSONArray) jObj.get("TableList");
                JSONObject mJsonObject = new JSONObject();



                for (int i = 0; i < mJsonArray.length(); i++) {
                    mJsonObject = (JSONObject) mJsonArray.get(i);
                    if (mJsonObject.get("TableName").toString().equals("")) {
                        //memberInfo = "no data";
                    } else {
                        clsTableMaster objTable = new clsTableMaster();
                        objTable.setStrTableName(mJsonObject.get("TableName").toString());
                        objTable.setStrTableNo(mJsonObject.get("TableNo").toString());
                        arrListTableMaster.add(objTable);
                    }
                }
            } catch (Exception e) {
            }
            return arrListTableMaster;
        }

        protected void onPostExecute(ArrayList arrListTemp) {
            if (arrListTemp.size()>0) {

                arrListTableMaster=arrListTemp;
                ArrayList<String> tablelist= new ArrayList<String>();
                for(int cnt=0;cnt<arrListTemp.size();cnt++)
                {
                    clsTableMaster objTable=(clsTableMaster)arrListTemp.get(cnt);
                    tablelist.add(objTable.getStrTableName());
                    tableNo=objTable.getStrTableNo();
                }

                funFillTableGrid(arrListTableMaster);

            }
        }
    }

    private void funFillTableGrid(ArrayList arrListTableMaster)
    {
        kotTableGridview.setAdapter(new clsReprintKotTableAdapter(getActivity(),this.getActivity(), arrListTableMaster,tableSelectionListener));

    }







}
