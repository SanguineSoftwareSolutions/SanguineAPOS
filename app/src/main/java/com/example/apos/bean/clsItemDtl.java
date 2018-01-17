package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Monika on 11/17/2015.
 */
public class clsItemDtl
{
    @SerializedName("ItemName")
    private String strItemName;

    @SerializedName("ItemQty")
    private String strQty;

    @SerializedName("ItemAmt")
    private String strAmount;



    public String getStrItemName() {

        return strItemName;
    }

    public void setStrItemName(String strItemName) {
        this.strItemName = strItemName;
    }

    public String getStrQty() {
        return strQty;
    }

    public void setStrQty(String strQty) {
        this.strQty = strQty;
    }

    public String getStrAmount() {
        return strAmount;
    }

    public void setStrAmount(String strAmount) {
        this.strAmount = strAmount;
    }
}
