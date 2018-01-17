package com.example.apos.wsoperations;

import com.example.apos.bean.clsItemPriceRateBean;

import java.util.ArrayList;

/**
 * Created by Prashant on 7/22/2015.
 */
public class clsItemPriceRateOperations
{
    public ArrayList<clsItemPriceRateBean> funGetTableList(String posCode,String clientCode)
    {
        ArrayList arrListItemPriceRateMaster = new ArrayList();

        clsItemPriceRateBean objTable1=new clsItemPriceRateBean("I001","AAB","GREEN","110","120","130","140","100","160","170","12:30","GH","HGHH","5:30","C001","10","MH01","21 JULY 2015","01 Aug 2015");

        clsItemPriceRateBean objTable2=new clsItemPriceRateBean("I002","BAC","RED","110","120","130","140","150","160","170","12:30","GH","HGHH","5:30","C001","10","MH01","21 JULY 2015","01 Aug 2015");

        clsItemPriceRateBean objTable3=new clsItemPriceRateBean("I003","CDC","PINK","110","120","130","140","250","160","170","12:30","GH","HGHH","5:30","C001","10","MH01","21 JULY 2015","01 Aug 2015");

        clsItemPriceRateBean objTable4=new clsItemPriceRateBean("I004","DDD","YELLOW","110","120","130","140","200","160","170","12:30","GH","HGHH","5:30","C001","10","MH01","21 JULY 2015","01 Aug 2015");


        arrListItemPriceRateMaster.add(objTable1);
        arrListItemPriceRateMaster.add(objTable2);
        arrListItemPriceRateMaster.add(objTable3);
        arrListItemPriceRateMaster.add(objTable4);

        return arrListItemPriceRateMaster;
    }

}
