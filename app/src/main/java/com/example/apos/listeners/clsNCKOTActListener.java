package com.example.apos.listeners;

public interface clsNCKOTActListener
{
    void setTableSelectedResult(String strTableNo, String strTableName, String status);
    void setDirectKotItemListSelectedResult1(String strTableNo, String strTableName, String strWaiterNo, String strWaiterName, String status, double kotAmt, double cardBalance, String cardType, int paxNo);
    void setWaiterSelectedResult(String strWaiterNo, String strWaiterName);
    void funRefreshItemDtl(String strItemCode, String strItemName, String strSubGroupCode, double dblSalePrice);
    void setSelectedPaxResult(int strPaxNo);
    void setSelectedOtherOptionsResult(String strNcKot, String strRemark, String strReasonCode);
}
