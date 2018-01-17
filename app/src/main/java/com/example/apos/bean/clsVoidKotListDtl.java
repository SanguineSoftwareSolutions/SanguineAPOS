package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sanguine on 9/19/2015.
 */
public class clsVoidKotListDtl
{
    @SerializedName("KOTNo")
    private String strKotNo;

    @SerializedName("TableName")
    private String strTableName;

    @SerializedName("WaiterName")
    private String strWaiterName;

    @SerializedName("User")
    private String strUser;

    @SerializedName("Amount")
    private Double strAmount;

    @SerializedName("POSName")
    private String strPOSName;

    @SerializedName("POSCode")
    private String strPOSCode;

    @SerializedName("TableNo")
    private String strTableNo;

    public String getStrPOSCode() {
        return strPOSCode;
    }

    public void setStrPOSCode(String strPOSCode) {
        this.strPOSCode = strPOSCode;
    }

    public String getStrTableNo() {
        return strTableNo;
    }

    public void setStrTableNo(String strTableNo) {
        this.strTableNo = strTableNo;
    }

    public String getStrPOSName() {
        return strPOSName;
    }

    public void setStrPOSName(String strPOSName) {
        this.strPOSName = strPOSName;
    }

    public String getStrKotNo() {
        return strKotNo;
    }

    public void setStrKotNo(String strKotNo) {
        this.strKotNo = strKotNo;
    }

    public String getStrTableName() {
        return strTableName;
    }

    public void setStrTableName(String strTableName) {
        this.strTableName = strTableName;
    }

    public String getStrWaiterName() {
        return strWaiterName;
    }

    public void setStrWaiterName(String strWaiterName) {
        this.strWaiterName = strWaiterName;
    }

    public String getStrUser() {
        return strUser;
    }

    public void setStrUser(String strUser) {
        this.strUser = strUser;
    }

    public Double getStrAmount() {
        return strAmount;
    }

    public void setStrAmount(Double strAmount) {
        this.strAmount = strAmount;
    }
}
