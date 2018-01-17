package com.example.apos.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import com.example.apos.App;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsPosSelectionMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.fragments.clsAllTableFragment;
import com.example.apos.fragments.clsBilledTableFragment;
import com.example.apos.fragments.clsNormalTableFragment;
import com.example.apos.fragments.clsOccupiedTableFragment;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeMap;

public class clsViewTableStatusScreen extends Activity
{
    private TextView tvHeaderTimeStamp=null;
    public static Activity mActivity;
    Intent iData;
    Thread DareTimeThread = null;
    ArrayList arrPosList,arrStatusList,arrAreaList;
    private TreeMap hmPos,hmArea;
    private Spinner spinnerPos,spinnerStatus,spinnerArea;
    private LinearLayout linearAll,linearVacant,linearOccupied,linearBilled,linearReserved;
    String posCode="All",areaCode="All",viewStatus="All";
    public static TextView txtPaxCount;
    private Dialog pgDialog;


    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        setContentView(R.layout.tablestatusviewscreen);

        iData = getIntent();
        Runnable runnable = new CountDownRunner();
        DareTimeThread = new Thread(runnable);
        DareTimeThread.start();

        widgetInit();
        mActivity = clsViewTableStatusScreen.this;
        funLoadFilterData();

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Bundle bundle1 = new Bundle();
        bundle1.putString("POSCode", posCode);
        bundle1.putString("AreaCode", areaCode);
        clsAllTableFragment myFragment = new clsAllTableFragment();
        myFragment.setArguments(bundle1);
        fragmentTransaction.add(R.id.myfragment, myFragment);
        fragmentTransaction.commit();



        spinnerPos.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
            {
                funRefreshGridData();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinnerArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                funRefreshGridData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


       /* spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                funRefreshGridData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */

        linearAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                viewStatus="All";
                funRefreshGridData();
            }
        });

        linearVacant.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                viewStatus="Vacant";
                funRefreshGridData();
            }
        });

        linearOccupied.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                viewStatus="Occupied";
                funRefreshGridData();
            }
        });

        linearBilled.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                viewStatus="Billed";
                funRefreshGridData();
            }
        });

    }

    private void widgetInit() {
        tvHeaderTimeStamp = (TextView) findViewById(R.id.tv_tableStatus_header_timestamp);
        spinnerPos= (Spinner) findViewById(R.id.spinnerPos);
      //  spinnerStatus= (Spinner) findViewById(R.id.spinnerStatus);
        spinnerArea= (Spinner) findViewById(R.id.spinnerArea);
        txtPaxCount= (TextView) findViewById(R.id.txtTotalPax);
        linearAll= (LinearLayout) findViewById(R.id.linearAll);
        linearVacant= (LinearLayout) findViewById(R.id.linearVacant);
        linearOccupied= (LinearLayout) findViewById(R.id.linearOccupied);
        linearBilled= (LinearLayout) findViewById(R.id.linearBilled);
      //  linearReserved= (LinearLayout) findViewById(R.id.linearReserved);
        arrStatusList=new ArrayList();
        hmPos=new TreeMap();
        hmPos.put("All","All");
        hmArea=new TreeMap();
        hmArea.put("All","All");


    }


    class CountDownRunner implements Runnable
    {
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


    public void doWork() {
        runOnUiThread(new Runnable() {
            public void run() {
                try
                {
                    String formattedDate = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                    tvHeaderTimeStamp.setText(clsGlobalFunctions.gPOSDateHeader+" "+formattedDate);

                } catch (Exception e) {
                }
            }
        });
    }


    /**
     * HTTP request for pos-list.
     */

    private void funLoadFilterData()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                App.getAPIHelper().funGetPOSList(clsGlobalFunctions.gUserCode, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsPosSelectionMaster>>() {
                    @Override
                    public void onSuccess(ArrayList<clsPosSelectionMaster> arrList) {
                        if (null != arrList) {
                            try
                            {
                                ArrayList<String> poslist= new ArrayList<String>();
                                String posName="";
                               // arrPosList=arrList;

                                for(int cnt=0;cnt<arrList.size();cnt++)
                                {
                                    clsPosSelectionMaster objPosMaster=(clsPosSelectionMaster)arrList.get(cnt);
                                    hmPos.put(objPosMaster.getStrPosName(), objPosMaster.getStrPosCode());
                                }

                                Set setPOS=hmPos.keySet();
                                Iterator itrPos=setPOS.iterator();
                                while(itrPos.hasNext())
                                {
                                    posName= (String) itrPos.next();
                                    poslist.add(posName);
                                }
                                posCode=hmPos.get(posName).toString();



                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                        (clsViewTableStatusScreen.this, R.layout.spinneritemtextview,poslist);
                                dataAdapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
                                spinnerPos.setAdapter(dataAdapter);

                               /* arrStatusList.add("All");
                                arrStatusList.add("Vacant");
                                arrStatusList.add("Billed");
                                arrStatusList.add("Occupied");

                                ArrayAdapter<String> dataAdapterStatus = new ArrayAdapter<String>
                                        (clsViewTableStatusScreen.this, R.layout.spinneritemtextview,arrStatusList);
                                dataAdapterStatus.setDropDownViewResource(R.layout.spinnerdropdownitem);
                                spinnerStatus.setAdapter(dataAdapterStatus);
                                */
                                funGetAreaList();


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }
                    }

                    @Override
                    public void onFailure(String errorMessage, int errorCode)
                    {
                    }
                });
            } else {
                SnackBarUtils.showSnackBar(clsViewTableStatusScreen.this, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsViewTableStatusScreen.this, R.string.setup_your_server_settings);
        }
    }


    private void funGetAreaList()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                showDialog();

                App.getAPIHelper().funGetAreaList("Area",clsGlobalFunctions.gPOSCode, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<JsonArray>() {
                    @Override
                    public void onSuccess(JsonArray mJsonArray) {
                        dismissDialog();
                        if (null != mJsonArray) {
                            try
                            {
                                ArrayList<String> arealist= new ArrayList<String>();
                                String areaName="";


                                JsonObject mJsonObject = new JsonObject();
                                for (int i = 0; i < mJsonArray.size(); i++)
                                {
                                    mJsonObject = (JsonObject) mJsonArray.get(i);

                                    if (mJsonObject.get("AreaCode").getAsString().equals("")) {
                                        //memberInfo = "no data";
                                    }
                                    else
                                    {
                                        if(mJsonObject.get("AreaName").getAsString().equalsIgnoreCase("All"))
                                        {

                                        }
                                        else
                                        {
                                            hmArea.put(mJsonObject.get("AreaName").getAsString(), mJsonObject.get("AreaCode").getAsString());
                                        }
                                    }
                                }
                                Set setArea=hmArea.keySet();
                                Iterator itrArea=setArea.iterator();
                                while(itrArea.hasNext())
                                {
                                    areaName= (String) itrArea.next();
                                    arealist.add(areaName);
                                }
                                areaCode=hmArea.get(areaName).toString();
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                        (clsViewTableStatusScreen.this, R.layout.spinneritemtextview,arealist);
                                dataAdapter.setDropDownViewResource(R.layout.spinnerdropdownitem);
                                spinnerArea.setAdapter(dataAdapter);
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
                SnackBarUtils.showSnackBar(clsViewTableStatusScreen.this, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(clsViewTableStatusScreen.this, R.string.setup_your_server_settings);
        }
    }


    private void funRefreshGridData()
    {
        if(hmPos.size() > 0 )
        {
            if(spinnerPos != null && spinnerPos.getSelectedItem() !=null )
            {
                posCode = hmPos.get(spinnerPos.getSelectedItem().toString()).toString();
            }
        }
        if(hmArea.size() > 0 )
        {
            if(spinnerArea != null && spinnerArea.getSelectedItem() !=null )
            {
                areaCode = hmArea.get(spinnerArea.getSelectedItem().toString()).toString();
            }
        }

        Fragment newFragment = null;
        //String viewStatus=spinnerStatus.getSelectedItem().toString();
        switch (viewStatus) {
            case "All":
                Bundle bundle = new Bundle();
                bundle.putString("POSCode", posCode);
                bundle.putString("AreaCode", areaCode);
                newFragment = new clsAllTableFragment();
                newFragment.setArguments(bundle);
                break;

            case "Vacant":
                Bundle bundle1 = new Bundle();
                bundle1.putString("POSCode", posCode);
                bundle1.putString("AreaCode", areaCode);
                newFragment = new clsNormalTableFragment();
                newFragment.setArguments(bundle1);
                break;

            case "Billed":
                Bundle bundle2 = new Bundle();
                bundle2.putString("POSCode", posCode);
                bundle2.putString("AreaCode", areaCode);
                newFragment = new clsBilledTableFragment();
                newFragment.setArguments(bundle2);
                break;

            case "Occupied":
                Bundle bundle3 = new Bundle();
                bundle3.putString("POSCode", posCode);
                bundle3.putString("AreaCode", areaCode);
                newFragment = new clsOccupiedTableFragment();
                newFragment.setArguments(bundle3);
                break;


            default:
                break;
        }

        // Create new transaction
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.myfragment, newFragment);
        transaction.addToBackStack(null);
        // Commit the transaction
        transaction.commit();
    }


    protected void showDialog() {
        if (null == pgDialog) {
            pgDialog = CommonUtils.getProgressDialog(clsViewTableStatusScreen.this, 0, false);
        }
        pgDialog.show();
    }

    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }
}

