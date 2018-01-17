package com.example.apos.listeners;

/**
 * Created by sanguine on 10/20/2015.
 */
public interface clsDirectBillerActListener
{
    void funRefreshItemDtl(String strItemCode, String strItemName, String strSubGroupCode, double dblSalePrice);
    void setMenuItemSelectionCodeResult(String strMenuItemCode, String strMenuItemName,String strMenuType);
    void setSelectedOptionResult(String strCustomerCodet,String strCustomerType);

}
