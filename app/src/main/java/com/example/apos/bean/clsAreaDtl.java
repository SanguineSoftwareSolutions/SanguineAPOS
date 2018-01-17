package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Monika on 11/6/2017.
 */

public class clsAreaDtl
{
    @SerializedName("AreaCode")
    private String strAreaCode;

    @SerializedName("AreaName")
    private String strAreaName;

    public String getStrAreaCode() {
        return strAreaCode;
    }

    public void setStrAreaCode(String strAreaCode) {
        this.strAreaCode = strAreaCode;
    }

    public String getStrAreaName() {
        return strAreaName;
    }

    public void setStrAreaName(String strAreaName) {
        this.strAreaName = strAreaName;
    }
}
