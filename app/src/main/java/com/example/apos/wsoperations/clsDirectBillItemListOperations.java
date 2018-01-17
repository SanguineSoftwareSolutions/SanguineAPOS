package com.example.apos.wsoperations;

import com.example.apos.bean.clsDirectBillItemsListBean;

import java.util.ArrayList;

/**
 * Created by Prashant on 8/11/2015.
 */
public class clsDirectBillItemListOperations
{

    public ArrayList<clsDirectBillItemsListBean> funGetTableList(String posCode,String clientCode)
    {
        ArrayList arrListItemPriceRateMaster = new ArrayList();

       /*clsDirectBillItemsListBean objTable1=new clsDirectBillItemsListBean("I001","AAA","I1001","m001",30.0,null);

        clsDirectBillItemsListBean objTable2=new clsDirectBillItemsListBean("I002","BBB","I1001","m001",30.0,null);
        clsDirectBillItemsListBean objTable3=new clsDirectBillItemsListBean("I003","CCC","I2001","m002",50.0,null);
        clsDirectBillItemsListBean objTable4=new clsDirectBillItemsListBean("I004","DDD","I3001","m003",100.0,null);

        arrListItemPriceRateMaster.add(objTable1);
        arrListItemPriceRateMaster.add(objTable2);
        arrListItemPriceRateMaster.add(objTable3);
        arrListItemPriceRateMaster.add(objTable4);


*/

        return arrListItemPriceRateMaster;
    }

}


