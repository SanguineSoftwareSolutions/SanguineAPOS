package com.example.apos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.apos.fragments.clsLoadItemListForReprintKot;
import com.example.apos.fragments.clsLoadKotForReprint;

public class clsReprintKotViewPageAdapter extends FragmentStatePagerAdapter {

    private CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when clsDirectBillerViewPagerAdapter is created
    private int NumbOfTabs; // Store the number of tabs, this will also be passed when the clsDirectBillerViewPagerAdapter is created
    private ViewPager mViewPager;
    //private String reprintOperation;
    // Build a Constructor and assign the passed Values to appropriate values in the class
    public clsReprintKotViewPageAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, ViewPager viewPager ) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.mViewPager = viewPager;
       // this.reprintOperation=reprintOperation;
    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position) {

        if(position == 0) // if the position is 0 we are returning the First tab
        {
            clsLoadKotForReprint tabLoadReprintKot = clsLoadKotForReprint.getInstance();
            return tabLoadReprintKot;
        }
        else             // As we are having 2 tabs if the position is now 0 it must be 1 so we are returning second tab
        {
            clsLoadItemListForReprintKot tabReprintKotList = clsLoadItemListForReprintKot.getInstance();
            return tabReprintKotList;

        }
    }

    // This method return the titles for the Tabs in the Tab Strip

    @Override
    public CharSequence getPageTitle(int position) {
        return Titles[position];
    }

    // This method return the Number of tabs for the tabs Strip

    @Override
    public int getCount() {
        return NumbOfTabs;
    }

   /* public String getPrintSelect(){
        return reprintOperation;
    }*/
}


