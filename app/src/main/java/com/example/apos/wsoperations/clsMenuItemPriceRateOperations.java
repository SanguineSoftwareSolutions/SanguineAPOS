package com.example.apos.wsoperations;

import com.example.apos.bean.clsMenuItemPriceRateBean;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by apos on 7/25/2015.
 */

public class clsMenuItemPriceRateOperations


{
    public ArrayList<clsMenuItemPriceRateBean> funGetTableList(String posCode,String clientCode)
    {
        ArrayList  listDataHeader = new ArrayList();
        HashMap listDataChild = new HashMap<String, List<String>>();

        listDataHeader.add("Startup");
        listDataHeader.add("Soups");
        listDataHeader.add("Veg");

        List<String> Startup = new ArrayList<String>();
        Startup.add("Butter Milk");
        Startup.add("Ice Tea");
        Startup.add("Masala Papad");


        List<String> Soups = new ArrayList<String>();
        Soups.add("Tomato Soup");
        Soups.add("Mushroom");


        List<String> Veg = new ArrayList<String>();
        Veg.add("Paneer Tikka");
        Veg.add("Veg Tufani");
        Veg.add("Veg Kolhapuri");


        listDataChild.put(listDataHeader.get(0), Startup); // Header, Child data
        listDataChild.put(listDataHeader.get(1), Soups);
        listDataChild.put(listDataHeader.get(2), Veg);

             return listDataHeader;
    }

}
