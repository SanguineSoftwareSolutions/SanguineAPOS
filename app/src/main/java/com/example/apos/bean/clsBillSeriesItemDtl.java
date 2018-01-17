package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Vinayak on 06/01/2018.
 */

public class clsBillSeriesItemDtl {


    @SerializedName("strItemCode")
    private String strItemCode;

    @SerializedName("strDesc")
    private String strDesc;

    @SerializedName("qty")
    private long qty;

    @SerializedName("amount")
    private double amount;

    @SerializedName("itemSalePrice")
    private double itemSalePrice;

    @SerializedName("isModifier")
    private boolean isModifier;

    @SerializedName("billNo")
    private String billNo;

    private double taxAmount;

    public String getStrItemCode() {
        return strItemCode;
    }

    public void setStrItemCode(String strItemCode) {
        this.strItemCode = strItemCode;
    }

    public String getStrDesc() {
        return strDesc;
    }

    public void setStrDesc(String strDesc) {
        this.strDesc = strDesc;
    }

    public long getQty() {
        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getItemSalePrice() {
        return itemSalePrice;
    }

    public void setItemSalePrice(double itemSalePrice) {
        this.itemSalePrice = itemSalePrice;
    }

    public boolean isModifier() {
        return isModifier;
    }

    public void setModifier(boolean modifier) {
        isModifier = modifier;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }


}
