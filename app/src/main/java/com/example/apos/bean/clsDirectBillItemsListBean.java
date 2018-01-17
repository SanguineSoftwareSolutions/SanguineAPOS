package com.example.apos.bean;

/**
 * Created by BeWo Tech-4 on 08-07-2015.
 */
public class clsDirectBillItemsListBean {

    private String strItemCode;
    private String strItemName;
    private String strSubGroupCode;
    private String strExternalCode;
    private double dblSalePrice;

    public double getDblSalePrice() {
        return dblSalePrice;
    }

    public void setDblSalePrice(double dblSalePrice) {
        this.dblSalePrice = dblSalePrice;
    }

    public String getStrExternalCode() {
        return strExternalCode;
    }

    public void setStrExternalCode(String strExternalCode) {
        this.strExternalCode = strExternalCode;
    }

    public String getStrSubGroupCode() {
        return strSubGroupCode;
    }

    public void setStrSubGroupCode(String strSubGroupCode) {
        this.strSubGroupCode = strSubGroupCode;
    }

    public String getStrItemName() {
        return strItemName;
    }

    public void setStrItemName(String strItemName) {
        this.strItemName = strItemName;
    }

    public String getStrItemCode() {
        return strItemCode;
    }

    public void setStrItemCode(String strItemCode) {
        this.strItemCode = strItemCode;
    }
}
