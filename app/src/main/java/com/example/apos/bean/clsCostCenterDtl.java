package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Monika on 10/7/2017.
 */

public class clsCostCenterDtl
{
    @SerializedName("CostCenterCode")
    private String strCostCenterCode;

    @SerializedName("CostCenterName")
    private String strCostCenterName;

    public String getStrCostCenterCode() {
        return strCostCenterCode;
    }

    public void setStrCostCenterCode(String strCostCenterCode) {
        this.strCostCenterCode = strCostCenterCode;
    }

    public String getStrCostCenterName() {
        return strCostCenterName;
    }

    public void setStrCostCenterName(String strCostCenterName) {
        this.strCostCenterName = strCostCenterName;
    }
}
