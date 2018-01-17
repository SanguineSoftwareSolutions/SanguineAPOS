package com.example.apos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.apos.adapter.clsVoidKotViewPageAdapter;
import com.example.apos.fragments.clsLoadVoidKot;
import com.example.apos.fragments.clsVoidKotList;
import com.example.apos.listeners.clsVoidKotActListener;
import com.example.apos.util.clsSlidingTabLayout;
import com.example.apos.util.Utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by sanguine on 9/19/2015.
 */
public class clsVoidKotScreen extends ActionBarActivity implements clsVoidKotActListener
{
    private TextView tvHeaderTimeStamp=null;

    public static ViewPager pager;
    clsVoidKotViewPageAdapter voidKotAdapter;
    clsSlidingTabLayout tabs;
    public static Activity mActivity;
    Intent iData;
    Thread DareTimeThread = null;
    CharSequence Titles[]={"Kot","Table","List"};
    int Numboftabs =3;


    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.voidkotscreen);

        iData = getIntent();
        Runnable runnable = new CountDownRunner();
        DareTimeThread = new Thread(runnable);
        DareTimeThread.start();

        widgetInit();
        mActivity = clsVoidKotScreen.this;

        voidKotAdapter =  new clsVoidKotViewPageAdapter(this.getSupportFragmentManager(),Titles,Numboftabs,pager);
        pager = (ViewPager) findViewById(R.id.pager_kot);
        pager.setAdapter(voidKotAdapter);
        tabs = (clsSlidingTabLayout) findViewById(R.id.tabs_kot);
        tabs.setDistributeEvenly(true);
        tabs.setCustomTabColorizer(new clsSlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });
        tabs.setViewPager(pager);
        pager.setCurrentItem(1);

    }

    private void widgetInit() {

        tvHeaderTimeStamp = (TextView) findViewById(R.id.tvPosName);
        tvHeaderTimeStamp.setText(Utils.getCurrentDate());

    }

   /* public static void setTableSelectedResult(String strTableNo, String strTableName) {

        pager.setCurrentItem(2);
        clsVoidKotList.funLoadKotList(strTableNo);

    }*/

    @Override
    public void funSetSelectedKotTableData(String tableNo, String tableName)
    {
        pager.setCurrentItem(2);
        new clsVoidKotList().funLoadKotList(tableNo);

    }
    @Override
    public void setSelectedKotItemList(String strKotNo) {
        pager.setCurrentItem(0);
        new clsLoadVoidKot().funLoadKotItemList(strKotNo);
        Toast.makeText(clsVoidKotScreen.mActivity,"kotno is:"+strKotNo,Toast.LENGTH_LONG).show();

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
                    TextView txtCurrentTime = (TextView) findViewById(R.id.tv_direct_bill_header_timestamp);
                    String formattedDate = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                    txtCurrentTime.setText(clsGlobalFunctions.gPOSDateHeader+" "+formattedDate);
                } catch (Exception e) {
                }
            }
        });
    }


}

