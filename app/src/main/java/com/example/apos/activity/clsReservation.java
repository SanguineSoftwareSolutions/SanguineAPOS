package com.example.apos.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.bewo.mach.tools.MACHServices;
import com.example.apos.adapter.clsMakeBillViewPageAdapter;
import com.example.apos.adapter.clsTableReservationViewPageAdapter;
import com.example.apos.fragments.clsMakeBillLoadTable;
import com.example.apos.fragments.clsTableReservation;
import com.example.apos.listeners.clsMakeBillItemListListener;
import com.example.apos.listeners.clsMakeBillTableListener;
import com.example.apos.util.clsSlidingTabLayout;

public class clsReservation extends ActionBarActivity
{
    public static ViewPager pager;
    clsTableReservationViewPageAdapter reservationAdapter;
    clsSlidingTabLayout tabs;
    public static Activity mActivity;
    Intent iData;
    Thread DareTimeThread = null;
    CharSequence Titles[]={"Add Reservation","Reservation List"};
    int Numboftabs =2;
    private clsTableReservation tableReservation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        mActivity=this;
        setContentView(R.layout.tablereservationscreen);

        iData = getIntent();

        tableReservation=new clsTableReservation();

        reservationAdapter =  new clsTableReservationViewPageAdapter(this.getSupportFragmentManager(),Titles,Numboftabs,pager);
        pager = (ViewPager) findViewById(R.id.pager_TableReservation);
        pager.setAdapter(reservationAdapter);
        tabs = (clsSlidingTabLayout) findViewById(R.id.tabs_TableReservation);
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

}