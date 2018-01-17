package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant on 11/16/2015.
 */
public class clsBillListDtl
{
    @SerializedName("BillNo")
    private String strBillNo;

    @SerializedName("BillDate")
    private String dteBillDate;

    @SerializedName("POSName")
    private String strPOSName;

    @SerializedName("Discount")
    private String strDiscountAmt;

    @SerializedName("TaxAmt")
    private String strTaxAmt;

    @SerializedName("SubTotal")
    private String strSubTotal;

    @SerializedName("GrandTotal")
    private String strGrandTotal;

    @SerializedName("DPName")
    private String strDPName;

    @SerializedName("CustomerName")
    private String strCustName;

    @SerializedName("AreaName")
    private String strAreaName;

    @SerializedName("TableNo")
    private String strTableNo;

    @SerializedName("WaiterNo")
    private String strWaiterNo;

    @SerializedName("TableName")
    private String strTableName;

    @SerializedName("CustomerCode")
    private  String customerCode;

    @SerializedName("OperationType")
    private String operationType;

    @SerializedName("cardType")
    private String cardType;

    @SerializedName("cardNo")
    private String cardNo;

    public String getCardType() {
        return cardType;
    }

    public void setCardType(String cardType) {
        this.cardType = cardType;
    }

    public String getCardNo() {
        return cardNo;
    }

    public void setCardNo(String cardNo) {
        this.cardNo = cardNo;
    }

    @SerializedName("ItemRows")
    List<clsItemDtl> arrListItemtDtl = new ArrayList<clsItemDtl>();


    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getStrTableNo() {
        return strTableNo;
    }

    public String getStrWaiterNo() {
        return strWaiterNo;
    }

    public void setStrWaiterNo(String strWaiterNo) {
        this.strWaiterNo = strWaiterNo;
    }

    public void setStrTableNo(String strTableNo) {
        this.strTableNo = strTableNo;

    }
    public String getStrDPName() {
        return strDPName;
    }

    public void setStrDPName(String strDPName) {
        this.strDPName = strDPName;
    }

    public String getStrCustName() {
        return strCustName;
    }

    public void setStrCustName(String strCustName) {
        this.strCustName = strCustName;
    }

    public String getStrAreaName() {
        return strAreaName;
    }

    public void setStrAreaName(String strAreaName) {
        this.strAreaName = strAreaName;
    }

    public List<clsItemDtl> getArrListItemtDtl() {
        return arrListItemtDtl;
    }

    public void setArrListItemtDtl(List<clsItemDtl> arrListItemtDtl) {
        this.arrListItemtDtl = arrListItemtDtl;
    }

    public String getStrBillNo() {
        return strBillNo;
    }

    public void setStrBillNo(String strBillNo) {
        this.strBillNo = strBillNo;
    }

    public String getDteBillDate() {
        return dteBillDate;
    }

    public void setDteBillDate(String dteBillDate) {
        this.dteBillDate = dteBillDate;
    }

    public String getStrPOSName() {
        return strPOSName;
    }

    public void setStrPOSName(String strPOSName) {
        this.strPOSName = strPOSName;
    }

    public String getStrDiscountAmt() {
        return strDiscountAmt;
    }

    public void setStrDiscountAmt(String strDiscountAmt) {
        this.strDiscountAmt = strDiscountAmt;
    }

    public String getStrTaxAmt() {
        return strTaxAmt;
    }

    public void setStrTaxAmt(String strTaxAmt) {
        this.strTaxAmt = strTaxAmt;
    }

    public String getStrSubTotal() {
        return strSubTotal;
    }

    public void setStrSubTotal(String strSubTotal) {
        this.strSubTotal = strSubTotal;
    }

    public String getStrGrandTotal() {
        return strGrandTotal;
    }

    public void setStrGrandTotal(String strGrandTotal) {
        this.strGrandTotal = strGrandTotal;
    }

    public String getStrTableName() {
        return strTableName;
    }

    public void setStrTableName(String strTableName) {
        this.strTableName = strTableName;
    }





}
