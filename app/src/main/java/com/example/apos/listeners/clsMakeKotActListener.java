package com.example.apos.listeners;

/**
 * Created by sanguine on 10/20/2015.
 */
public interface clsMakeKotActListener
{
    void setTableSelectedResult(String strTableNo, String strTableName, String status,String areaCode);
    void setDirectKotItemListSelectedResult1(String strTableNo, String strTableName, String strWaiterNo, String strWaiterName, String status, double kotAmt, double cardBalance, String cardType, int paxNo, String cardNo, String strLinkedWaiterNo,String areaCode);
    void setWaiterSelectedResult(String strWaiterNo, String strWaiterName);
    void funRefreshItemDtl(String strItemCode, String strItemName, String strSubGroupCode, double dblSalePrice);
    void setMenuItemSelectionCodeResult(String strMenuItemCode, String strMenuItemName, String strMenuType);
    void setSelectedPaxResult(int strPaxNo);
    void setSelectedOtherOptionsResult(String strNcKot, String strRemark, String strReasonCode);
}
