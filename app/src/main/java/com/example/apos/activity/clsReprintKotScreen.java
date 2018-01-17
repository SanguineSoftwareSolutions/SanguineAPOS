package com.example.apos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.bewo.mach.tools.MACHServices;
import com.example.apos.adapter.clsReprintKotViewPageAdapter;
import com.example.apos.fragments.clsLoadItemListForReprintKot;
import com.example.apos.listeners.clsReprintKotActListener;
import com.example.apos.util.clsSlidingTabLayout;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class clsReprintKotScreen extends ActionBarActivity
{
    public static ViewPager pager;
    clsReprintKotViewPageAdapter reprintKotAdapter;
    clsSlidingTabLayout tabs;
    public static Activity mActivity;
    static MACHServices machService;
    Intent iData;
    Thread DareTimeThread = null;
    CharSequence Titles[]={"Kot List","KOT Details"};
    int Numboftabs =2;
    public static String selectedPOS="All";


    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.reprintkotscreen);

        iData = getIntent();
        Bundle extras = iData.getExtras();
        if(extras != null)
            selectedPOS= extras.getString("selectedPOS");

        Runnable runnable = new CountDownRunner();
        DareTimeThread = new Thread(runnable);
        DareTimeThread.start();

        mActivity = clsReprintKotScreen.this;

        reprintKotAdapter =  new clsReprintKotViewPageAdapter(this.getSupportFragmentManager(),Titles,Numboftabs,pager);
        pager = (ViewPager) findViewById(R.id.pager_reprintkot);
        pager.setAdapter(reprintKotAdapter);
        tabs = (clsSlidingTabLayout) findViewById(R.id.tabs_reprintkot);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new clsSlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);
        pager.setCurrentItem(0);

    }


    public void funSetSelectedKotTableData(String tableNo, String tableName)
    {


    }

    public void setSelectedKotDetails(String kotNo, String tableNo, String POSCode)
    {
        pager.setCurrentItem(1);
       // clsLoadItemListForReprintKot.funLoadKotItemList(kotNo,tableNo,POSCode);
        Toast.makeText(clsReprintKotScreen.mActivity,"kotno is:"+kotNo+"tableNO:"+tableNo+"poscode:"+POSCode,Toast.LENGTH_LONG).show();
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
                try {
                    TextView txtCurrentTime = (TextView) findViewById(R.id.tvReprintPosDate);
                    String formattedDate = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                    txtCurrentTime.setText(clsGlobalFunctions.gPOSDateHeader+" "+formattedDate);
                } catch (Exception e) {
                }
            }
        });
    }





    @Override
    protected void onPause() {
        // TODO Auto-generated method stub
        super.onPause();
    }

    @Override
    protected void onRestart() {
        // TODO Auto-generated method stub
        super.onRestart();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }

    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        machService = new MACHServices(getApplicationContext());
        machService.mach_initialize(getApplicationContext());

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int connectStatus = machService.mach_connect();
        Toast.makeText(getApplicationContext(), "Connection Status : " + connectStatus, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        machService.mach_release(getApplicationContext());
    }

}

