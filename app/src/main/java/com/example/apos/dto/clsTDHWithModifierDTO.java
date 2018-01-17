package com.example.apos.dto;

import com.example.apos.bean.clsTDHBean;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Vinayak on 18/09/2017.
 */

public class clsTDHWithModifierDTO {
    @SerializedName("TDHMainItemCode")
    private String strTDHMainItemCode;

    @SerializedName("TDHMainItemMaxQty")
    private String TDHMainItemMaxQty;

    @SerializedName("ModifierGroupList")
    ArrayList<clsTDHBean> listModifierGroupList;

    public String getStrTDHMainItemCode() {
        return strTDHMainItemCode;
    }

    public void setStrTDHMainItemCode(String strTDHMainItemCode) {
        this.strTDHMainItemCode = strTDHMainItemCode;
    }

    public String getTDHMainItemMaxQty() {
        return TDHMainItemMaxQty;
    }

    public void setTDHMainItemMaxQty(String TDHMainItemMaxQty) {
        this.TDHMainItemMaxQty = TDHMainItemMaxQty;
    }

    public ArrayList<clsTDHBean> getListModifierGroupList() {
        return listModifierGroupList;
    }

    public void setListModifierGroupList(ArrayList<clsTDHBean> listModifierGroupList) {
        this.listModifierGroupList = listModifierGroupList;
    }
}
