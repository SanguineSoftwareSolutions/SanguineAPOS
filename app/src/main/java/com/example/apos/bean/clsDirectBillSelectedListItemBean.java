package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by BeWo Tech-4 on 06-07-2015.
 */
public class clsDirectBillSelectedListItemBean implements Serializable {

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

    @SerializedName("strCustomerCode")
    private  String strCustomerCode;

    @SerializedName("strCustomerType")
    private  String strCustomerType;

    @SerializedName("billNo")
    private String billNo;

    private double taxAmount;

    public double getTaxAmount() {
        return taxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        this.taxAmount = taxAmount;
    }

    public String getBillNo() {
        return billNo;
    }

    public void setBillNo(String billNo) {
        this.billNo = billNo;
    }

    public String getStrCustomerName() {
        return strCustomerName;
    }

    public void setStrCustomerName(String strCustomerName) {
        this.strCustomerName = strCustomerName;
    }

    private  String strCustomerName;

    public String getStrCustomerCode() {
        return strCustomerCode;
    }

    public void setStrCustomerCode(String strCustomerCode) {
        this.strCustomerCode = strCustomerCode;
    }

    public String getStrCustomerType() {
        return strCustomerType;
    }

    public void setStrCustomerType(String strCustomerType) {
        this.strCustomerType = strCustomerType;
    }

    public String getStrOperationType() {
        return strOperationType;
    }

    public void setStrOperationType(String strOperationType) {
        this.strOperationType = strOperationType;
    }

    public String getStrTakeAway() {
        return strTakeAway;
    }

    public void setStrTakeAway(String strTakeAway) {
        this.strTakeAway = strTakeAway;
    }

    private String strOperationType;
    private  String strTakeAway;

    public clsDirectBillSelectedListItemBean(String strItemCode, String strDesc, long qty, double amount, double itemSalePrice, boolean isModifier,String customerCode,String customerType,String operationType,String takeAway) {
        this.strItemCode = strItemCode;
        this.strDesc = strDesc;
        this.qty = qty;
        this.amount = amount;
        this.itemSalePrice = itemSalePrice;
        this.isModifier = isModifier;
        this.strCustomerCode= customerCode;
        this.strCustomerType = customerType;
        this.strOperationType= operationType;
        this.strTakeAway = takeAway;
    }

    public boolean isModifier() {
        return isModifier;
    }

    public void setIsModifier(boolean isModifier) {
        this.isModifier = isModifier;
    }

    public String getStrDesc() {
        return strDesc;
    }

    public void setStrDesc(String strDesc) {
        this.strDesc = strDesc;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public long getQty() {

        return qty;
    }

    public void setQty(long qty) {
        this.qty = qty;
    }

    public String getStrItemCode() {
        return strItemCode;
    }

    public void setStrItemCode(String strItemCode) {
        this.strItemCode = strItemCode;
    }

    public double getItemSalePrice() {
        return itemSalePrice;
    }

    public void setItemSalePrice(double itemSalePrice) {
        this.itemSalePrice = itemSalePrice;
    }
}
