package com.example.apos.activity;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.apos.App;
import com.example.apos.adapter.clsCounterAdapter;
import com.example.apos.adapter.clsReprintKotViewPageAdapter;
import com.example.apos.api.BaseAPIHelper;
import com.example.apos.bean.clsPosSelectionMaster;
import com.example.apos.config.CommonUtils;
import com.example.apos.config.ConnectivityHelper;
import com.example.apos.config.SnackBarUtils;
import com.example.apos.config.StringUtils;
import com.example.apos.fragments.clsLoadItemListForReprintKot;
import com.example.apos.listeners.clsReprintKotActListener;
import com.example.apos.util.clsSlidingTabLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class clsReprintDocuments extends ActionBarActivity implements clsReprintKotActListener
{
    public static ViewPager pager;
    clsReprintKotViewPageAdapter reprintKotAdapter;
    clsSlidingTabLayout tabs;
    clsCounterAdapter listDialogAdapter;
    Button btnKOT,btnBill,btnDirectBiller;
    private Spinner spinnerPOS;
    ListView list;
    Map<String,String> hmCounter=new HashMap<String,String>();
    ArrayList<String> poslist= new ArrayList<String>();
    TreeMap hmPos=new TreeMap();
    public static HashMap<String,String> hmPOSCodeName=new HashMap<>();
    public static Activity mActivity;
    private Dialog pgDialog;
    CharSequence Titles[]={"Kot List","KOT Details"};
    int Numboftabs =2;
    public static String selectedPOS="All", reprintOperation ="KOT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity=this;
        setContentView(R.layout.reprintdocumentscreen);
        list= (ListView) findViewById(R.id.listitemdialog);
        getSupportActionBar().hide();

        btnKOT = (Button) findViewById(R.id.btnRPKOT);
        btnBill = (Button) findViewById(R.id.btnRPBill);
        btnDirectBiller = (Button) findViewById(R.id.btnRPdirectBill);
        spinnerPOS= (Spinner) findViewById(R.id.reprint_spinnerPos);
        funGetPOSList();

        if(poslist.size()>0){
            spinnerPOS.setSelection(poslist.indexOf(clsGlobalFunctions.gPOSCode));
        }

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

        btnKOT.setOnClickListener(new View.OnClickListener()
        {
          @Override
            public void onClick(View arg0)
            {
                reprintOperation ="KOT";
                Titles[0]="KOT List";
                Titles[1]="KOT Details";
                selectedPOS= (String) hmPos.get(spinnerPOS.getSelectedItem().toString());
                reprintKotAdapter =  new clsReprintKotViewPageAdapter(getSupportFragmentManager(),Titles,Numboftabs,pager);
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
        });

        btnBill.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                reprintOperation ="Bill";
                Titles[0]="Bill List";
                Titles[1]="Bill Details";
                selectedPOS= (String) hmPos.get(spinnerPOS.getSelectedItem().toString());
                reprintKotAdapter =  new clsReprintKotViewPageAdapter(getSupportFragmentManager(),Titles,Numboftabs,pager);
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
        });

        btnDirectBiller.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View arg0)
            {
                reprintOperation ="DirectBiller";
                selectedPOS= (String) hmPos.get(spinnerPOS.getSelectedItem().toString());
                Titles[0]="DirectBiller List";
                Titles[1]="Bill Details";
                reprintKotAdapter =  new clsReprintKotViewPageAdapter(getSupportFragmentManager(),Titles,Numboftabs,pager);
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
        });
    }



    private void funGetPOSList()
    {
        if (!StringUtils.isEmpty(clsGlobalFunctions.gAPOSWebSrviceURL)) {
            if (ConnectivityHelper.isConnected()) {
                App.getAPIHelper().funGetPOSList(clsGlobalFunctions.gUserCode, clsGlobalFunctions.gClientCode, new BaseAPIHelper.OnRequestComplete<ArrayList<clsPosSelectionMaster>>() {
                    @Override
                    public void onSuccess(ArrayList<clsPosSelectionMaster> arrList) {
                        if (null != arrList) {
                            try
                            {

                                String posName="";
                                hmPos=new TreeMap();
                                // hmPos.put("All","All");
                                for(int cnt=0;cnt<arrList.size();cnt++)
                                {
                                    clsPosSelectionMaster objPosMaster=(clsPosSelectionMaster)arrList.get(cnt);
                                    hmPos.put(objPosMaster.getStrPosName(), objPosMaster.getStrPosCode());
                                    hmPOSCodeName.put(objPosMaster.getStrPosCode(),objPosMaster.getStrPosName());
                                }

                                Set setPOS=hmPos.keySet();
                                Iterator itrPos=setPOS.iterator();
                                while(itrPos.hasNext())
                                {
                                    posName= (String) itrPos.next();
                                    poslist.add(posName);
                                }
                                ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>
                                        (mActivity,  R.layout.spinneritemtextview,poslist);

                                dataAdapter.setDropDownViewResource
                                        (R.layout.spinnerdropdownitem);
                                spinnerPOS.setAdapter(dataAdapter);

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
                SnackBarUtils.showSnackBar(mActivity, R.string.no_internet_connection);
            }
        } else {
            SnackBarUtils.showSnackBar(mActivity, R.string.setup_your_server_settings);
        }
    }
    protected void showDialog() {
        if (null == pgDialog) {
            pgDialog = CommonUtils.getProgressDialog(mActivity, 0, false);
        }
        pgDialog.show();
    }
    protected void dismissDialog() {
        if (null != pgDialog) {
            pgDialog.dismiss();
        }
    }


    @Override
    public void funSetSelectedKotTableData(String tableNo, String tableName) {

    }

    @Override
    public void setSelectedKotDetails(String kotNo, String tableNo, String POSCode,String reprintOperation)
    {
        pager.setCurrentItem(1);
        clsLoadItemListForReprintKot.funLoadKotItemList(kotNo,tableNo,POSCode,reprintOperation);
        if(reprintOperation.equalsIgnoreCase("KOT"))
        {
            Toast.makeText(mActivity,"KOT No is:"+kotNo+"tableNO:"+tableNo+"poscode:"+POSCode,Toast.LENGTH_SHORT).show();
        }
        else if(reprintOperation.equalsIgnoreCase("BILL"))
        {
            Toast.makeText(mActivity,"BILL No is:"+kotNo,Toast.LENGTH_SHORT).show();
        }
        else if(reprintOperation.equalsIgnoreCase("DirectBiller"))
        {
            Toast.makeText(mActivity,"Direct Biller KOT No is:"+kotNo,Toast.LENGTH_SHORT).show();
        }
    }
}