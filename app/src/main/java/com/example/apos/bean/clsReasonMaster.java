package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prashant on 12/9/2015.
 */
public class clsReasonMaster
{
    @SerializedName("ReasonCode")
    private String strReasonCode;

    @SerializedName("ReasonName")
    private String strReasonName;

    public String getStrReasonCode() {
        return strReasonCode;
    }

    public void setStrReasonCode(String strReasonCode) {
        this.strReasonCode = strReasonCode;
    }

    public String getStrReasonName() {
        return strReasonName;
    }

    public void setStrReasonName(String strReasonName) {
        this.strReasonName = strReasonName;
    }
}
