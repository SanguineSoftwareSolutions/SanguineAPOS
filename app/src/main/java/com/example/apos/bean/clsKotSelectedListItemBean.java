package com.example.apos.bean;

public class clsKotSelectedListItemBean {

    private String strItemCode;
    private String strDesc;
    private long qty;
    private double amount;
    private double itemSalePrice;

    public clsKotSelectedListItemBean(String strItemCodeTemp, String strTempDesc, long tempQty, double tempAmount, double itemSalePrice){
        this.strItemCode = strItemCodeTemp;
        this.strDesc = strTempDesc;
        this.qty = tempQty;
        this.amount = tempAmount;
        this.itemSalePrice=itemSalePrice;
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
