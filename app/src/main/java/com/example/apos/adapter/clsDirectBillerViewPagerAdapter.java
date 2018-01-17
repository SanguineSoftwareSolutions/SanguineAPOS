package com.example.apos.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.example.apos.fragments.clsDirectBillerOptionsFragment;
import com.example.apos.fragments.clsDirectBillerItemListFragment;
import com.example.apos.fragments.clsDirectBillerClsMenuItemFragment;


/**
 * Created by BeWo Tech-4 on 06-07-2015.
 */
public class clsDirectBillerViewPagerAdapter extends FragmentStatePagerAdapter {

    private CharSequence Titles[]; // This will Store the Titles of the Tabs which are Going to be passed when clsDirectBillerViewPagerAdapter is created
    private int NumbOfTabs; // Store the number of tabs, this will also be passed when the clsDirectBillerViewPagerAdapter is created
    private ViewPager mViewPager;

    // Build a Constructor and assign the passed Values to appropriate values in the class
    public clsDirectBillerViewPagerAdapter(FragmentManager fm, CharSequence mTitles[], int mNumbOfTabsumb, ViewPager viewPager) {
        super(fm);

        this.Titles = mTitles;
        this.NumbOfTabs = mNumbOfTabsumb;
        this.mViewPager = viewPager;

    }

    //This method return the fragment for the every position in the View Pager
    @Override
    public Fragment getItem(int position)
    {
      switch (position)
        {

            case 0:
                clsDirectBillerItemListFragment tabItemsList = clsDirectBillerItemListFragment.getInstance();
                return tabItemsList;
            case 1:

                clsDirectBillerClsMenuItemFragment menuItems = new clsDirectBillerClsMenuItemFragment();
                return menuItems;

            case 2:
                clsDirectBillerOptionsFragment tabOptions = clsDirectBillerOptionsFragment.getInstance();
                return tabOptions;
        }
        return  null;


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
}