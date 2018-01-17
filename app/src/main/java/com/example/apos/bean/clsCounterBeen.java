package com.example.apos.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Prashant on 8/28/2015.
 */
public class clsCounterBeen
{
    private String strCounterCode;
    private String strCounterName;

    List<clsMenuDtl> arrListMemuDtl = new ArrayList<clsMenuDtl>();

    public String getStrCounterCode() {
        return strCounterCode;
    }

    public void setStrCounterCode(String strCounterCode) {
        this.strCounterCode = strCounterCode;
    }

    public String getStrCounterName() {
        return strCounterName;
    }

    public void setStrCounterName(String strCounterName) {
        this.strCounterName = strCounterName;
    }

    public List<clsMenuDtl> getArrListMemuDtl() {
        return arrListMemuDtl;
    }

    public void setArrListMemuDtl(List<clsMenuDtl> arrListMemuDtl) {
        this.arrListMemuDtl = arrListMemuDtl;
    }
}
