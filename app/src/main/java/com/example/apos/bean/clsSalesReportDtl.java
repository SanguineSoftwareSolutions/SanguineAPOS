package com.example.apos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sanguine on 10/24/2015.
 */
public class clsSalesReportDtl
{
    String strbillno;
    String dtebilldate;
    String billtime;
    String tableName;
    String posName;
    String payMode;
    String DeliveryCharge;
    String subTotal;
    String discountPer;
    String discountAmt;
    String taxAmt;
    String grandTotal;
    String remarks;
    String Tip;
    String DiscRemarks;
    String CustName;
    String strItemName;
    String strItemCode;
    String strMenuName;
    String strQuantity;

    public String getPosCode() {
        return posCode;
    }

    public void setPosCode(String posCode) {
        this.posCode = posCode;
    }

    String strSalePer;
    String dblSettleAmt;
    String strMenuCode;
    String posCode;

    public String getStrMenuCode() {
        return strMenuCode;
    }

    public void setStrMenuCode(String strMenuCode) {
        this.strMenuCode = strMenuCode;
    }

    List<clsPosSelectionMaster> arrListPosDtl = new ArrayList<clsPosSelectionMaster>();

    public List<clsPosSelectionMaster> getArrListPosDtl() {
        return arrListPosDtl;
    }

    public void setArrListPosDtl(List<clsPosSelectionMaster> arrListPosDtl) {
        this.arrListPosDtl = arrListPosDtl;
    }

    public String getDblSettleAmt() {
        return dblSettleAmt;
    }

    public void setDblSettleAmt(String dblSettleAmt) {
        this.dblSettleAmt = dblSettleAmt;
    }

    public String getStrSalePer() {
        return strSalePer;
    }

    public void setStrSalePer(String strSalePer) {
        this.strSalePer = strSalePer;
    }

    public String getStrMenuName() {
        return strMenuName;
    }

    public void setStrMenuName(String strMenuName) {
        this.strMenuName = strMenuName;
    }

    public String getStrQuantity() {
        return strQuantity;
    }

    public void setStrQuantity(String strQuantity) {
        this.strQuantity = strQuantity;
    }

    public String getStrbillno() {
        return strbillno;
    }

    public void setStrbillno(String strbillno) {
        this.strbillno = strbillno;
    }

    public String getDtebilldate() {
        return dtebilldate;
    }

    public void setDtebilldate(String dtebilldate) {
        this.dtebilldate = dtebilldate;
    }

    public String getBilltime() {
        return billtime;
    }

    public void setBilltime(String billtime) {
        this.billtime = billtime;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPosName() {
        return posName;
    }

    public void setPosName(String posName) {
        this.posName = posName;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getDeliveryCharge() {
        return DeliveryCharge;
    }

    public void setDeliveryCharge(String deliveryCharge) {
        DeliveryCharge = deliveryCharge;
    }

    public String getSubTotal() {
        return subTotal;
    }

    public void setSubTotal(String subTotal) {
        this.subTotal = subTotal;
    }

    public String getDiscountPer() {
        return discountPer;
    }

    public void setDiscountPer(String discountPer) {
        this.discountPer = discountPer;
    }

    public String getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(String discountAmt) {
        this.discountAmt = discountAmt;
    }

    public String getTaxAmt() {
        return taxAmt;
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

    public void setTaxAmt(String taxAmt) {
        this.taxAmt = taxAmt;
    }

    public String getGrandTotal() {
        return grandTotal;
    }

    public void setGrandTotal(String grandTotal) {
        this.grandTotal = grandTotal;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }

    public String getTip() {
        return Tip;
    }

    public void setTip(String tip) {
        Tip = tip;
    }

    public String getDiscRemarks() {
        return DiscRemarks;
    }

    public void setDiscRemarks(String discRemarks) {
        DiscRemarks = discRemarks;
    }

    public String getCustName() {
        return CustName;
    }

    public void setCustName(String custName) {
        CustName = custName;
    }
}
