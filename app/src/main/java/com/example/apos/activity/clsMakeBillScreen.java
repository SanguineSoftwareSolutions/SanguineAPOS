package com.example.apos.activity;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.bewo.mach.MACHPrinter;
import com.bewo.mach.tools.MACHServices;
import com.example.apos.adapter.clsMakeBillViewPageAdapter;
import com.example.apos.fragments.clsMakeBillLoadListFragment;
import com.example.apos.fragments.clsMakeBillLoadTable;
import com.example.apos.listeners.clsMakeBillActListener;
import com.example.apos.listeners.clsMakeBillItemListListener;

import com.example.apos.listeners.clsMakeBillTableListener;
import com.example.apos.util.clsPrintDemo;
import com.example.apos.util.clsSlidingTabLayout;
import com.example.apos.util.Utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by sanguine on 9/24/2015.
 */
public class clsMakeBillScreen extends ActionBarActivity implements clsMakeBillActListener
{
    private TextView tvHeaderTimeStamp=null;
    public static ViewPager pager;
    clsMakeBillViewPageAdapter makeBilladapter;
    clsSlidingTabLayout tabs;
    public static Activity mActivity;
    Intent iData;
    Thread DareTimeThread = null;
    CharSequence Titles[]={"MakeBill","Table"};
    int Numboftabs =2;
    static MACHServices machService;
    private clsMakeBillItemListListener objMakeBillItemListListener;
    private clsMakeBillTableListener objMakeBillTableListener;
    private clsMakeBillLoadTable makeBillLoadTable;
    Activity activity;


    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        getSupportActionBar().hide();
        setContentView(R.layout.makebillscreen);
        widgetInit();

        iData = getIntent();
        Runnable runnable = new CountDownRunner();
        DareTimeThread = new Thread(runnable);
        DareTimeThread.start();

        mActivity = clsMakeBillScreen.this;
        objMakeBillItemListListener=(clsMakeBillItemListListener)clsMakeBillLoadListFragment.getInstance();
        objMakeBillTableListener=(clsMakeBillTableListener)clsMakeBillLoadTable.getInstance();
        makeBillLoadTable=new clsMakeBillLoadTable();

        makeBilladapter =  new clsMakeBillViewPageAdapter(this.getSupportFragmentManager(),Titles,Numboftabs,pager);
        pager = (ViewPager) findViewById(R.id.pager_MakeBill);
        pager.setAdapter(makeBilladapter);
        tabs = (clsSlidingTabLayout) findViewById(R.id.tabs_MakeBill);
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

    }


    @Override
    public void funSetSelectedTableData(String strTableNo, String strTableName) {
        Titles= new CharSequence[]{strTableName, "Table"};
        pager.setCurrentItem(0);
        objMakeBillItemListListener.funSetItemDtlForSelectedTable(strTableNo, strTableName);
    }

    @Override
    public void funRefreshTableGrid(ArrayList arrListTableMaster)
    {
         clsMakeBillLoadTable obj=new clsMakeBillLoadTable();
         obj.funFillTableGrid(arrListTableMaster);
         pager.setCurrentItem(1);
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
                    TextView txtCurrentTime = (TextView) findViewById(R.id.tv_MakeBill_header_timestamp);
                    String formattedDate = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
                    txtCurrentTime.setText(clsGlobalFunctions.gPOSDateHeader+" "+formattedDate);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void funPrintBillForMachDevice(String billFormat) {
        if (machService.isMachActive()) {
            byte print_return = machService.print_text(billFormat, (byte) 0x00, (byte) 0x02, false, (byte) 0x00, false, false, (byte) 0x00);
            //For Magnetic Swipe
            //byte[] track = machService.getMSRTrackData(20);

            switch (print_return) {
                case MACHPrinter.BAT_LOW_ERROR:
                    Toast.makeText(clsMakeBillScreen.mActivity, "Printer Status : Battery Low ", Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_NO_PLATEN:
                    Toast.makeText(clsMakeBillScreen.mActivity, "Printer Status : No Paper ", Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_NO_PAPER:
                    Toast.makeText(clsMakeBillScreen.mActivity, "Printer Status : No Platen ", Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_NO_PRINTER:
                    Toast.makeText(clsMakeBillScreen.mActivity, "Printer Status : No Printer Module ", Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_STATUS_OK:
                    Toast.makeText(clsMakeBillScreen.mActivity, "Printer Status : OK ", Toast.LENGTH_LONG).show();
                    break;
                case MACHPrinter.THERMALPRINTER_TIMEOUT_APP:
                    Toast.makeText(clsMakeBillScreen.mActivity, "Printer Status : Timeout from the Application ", Toast.LENGTH_LONG).show();
                    break;
                default:
                    Toast.makeText(clsMakeBillScreen.mActivity, "Printer Status : " + print_return, Toast.LENGTH_LONG).show();
                    break;
            }
        } else
            Toast.makeText(clsMakeBillScreen.mActivity, "Printer Status : MACH is not active", Toast.LENGTH_LONG).show();
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
