package com.example.apos.bean;

/**
 * Created by Prashant on 7/22/2015.
 */
public class clsItemPriceRateBean
{

    private String strItemCode;
    private String strItemName;
    private String strTextColor;
    private String strPriceMonday;
    private String strPriceTuesday;
    private String strPriceWednesday;
    private String strPriceThursday;
    private String strPriceFriday;
    private String strPriceSaturday;
    private String strPriceSunday;
    private String tmeTimeFrom;
    private String strAMPMFrom;
    private String tmeTimeTo;
    private String strAMPMTo;
    private String strCostCenterCode;
    private String strHourlyPricing;
    private String strSubMenuHeadCode;
    private String dteFromDate;
    private String dteToDate;


    public clsItemPriceRateBean(String strItemCode, String strItemName, String strTextColor, String strPriceMonday, String strPriceTuesday, String strPriceWednesday, String strPriceThursday, String strPriceFriday, String strPriceSaturday, String strPriceSunday, String tmeTimeFrom, String strAMPMFrom, String tmeTimeTo, String strAMPMTo, String strCostCenterCode, String strHourlyPricing, String strSubMenuHeadCode, String dteFromDate, String dteToDate) {
        this.strItemCode = strItemCode;
        this.strItemName = strItemName;
        this.strTextColor = strTextColor;
        this.strPriceMonday = strPriceMonday;
        this.strPriceTuesday = strPriceTuesday;
        this.strPriceWednesday = strPriceWednesday;
        this.strPriceThursday = strPriceThursday;
        this.strPriceFriday = strPriceFriday;
        this.strPriceSaturday = strPriceSaturday;
        this.strPriceSunday = strPriceSunday;
        this.tmeTimeFrom = tmeTimeFrom;
        this.strAMPMFrom = strAMPMFrom;
        this.tmeTimeTo = tmeTimeTo;
        this.strAMPMTo = strAMPMTo;
        this.strCostCenterCode = strCostCenterCode;
        this.strHourlyPricing = strHourlyPricing;
        this.strSubMenuHeadCode = strSubMenuHeadCode;
        this.dteFromDate = dteFromDate;
        this.dteToDate = dteToDate;
    }

    public String getStrPriceSunday() {
        return strPriceSunday;
    }

    public void setStrPriceSunday(String strPriceSunday) {
        this.strPriceSunday = strPriceSunday;
    }

    public String getStrItemCode() {
        return strItemCode;
    }

    public void setStrItemCode(String strItemCode) {
        this.strItemCode = strItemCode;
    }

    public String getStrItemName() {
        return strItemName;
    }

    public void setStrItemName(String strItemName) {
        this.strItemName = strItemName;
    }

    public String getStrTextColor() {
        return strTextColor;
    }

    public void setStrTextColor(String strTextColor) {
        this.strTextColor = strTextColor;
    }

    public String getStrPriceMonday() {
        return strPriceMonday;
    }

    public void setStrPriceMonday(String strPriceMonday) {
        this.strPriceMonday = strPriceMonday;
    }

    public String getStrPriceTuesday() {
        return strPriceTuesday;
    }

    public void setStrPriceTuesday(String strPriceTuesday) {
        this.strPriceTuesday = strPriceTuesday;
    }

    public String getStrPriceWednesday() {
        return strPriceWednesday;
    }

    public void setStrPriceWednesday(String strPriceWednesday) {
        this.strPriceWednesday = strPriceWednesday;
    }

    public String getStrPriceThursday() {
        return strPriceThursday;
    }

    public void setStrPriceThursday(String strPriceThursday) {
        this.strPriceThursday = strPriceThursday;
    }

    public String getStrPriceFriday() {
        return strPriceFriday;
    }

    public void setStrPriceFriday(String strPriceFriday) {
        this.strPriceFriday = strPriceFriday;
    }

    public String getStrPriceSaturday() {
        return strPriceSaturday;
    }

    public void setStrPriceSaturday(String strPriceSaturday) {
        this.strPriceSaturday = strPriceSaturday;
    }

    public String getTmeTimeFrom() {
        return tmeTimeFrom;
    }

    public void setTmeTimeFrom(String tmeTimeFrom) {
        this.tmeTimeFrom = tmeTimeFrom;
    }

    public String getStrAMPMFrom() {
        return strAMPMFrom;
    }

    public void setStrAMPMFrom(String strAMPMFrom) {
        this.strAMPMFrom = strAMPMFrom;
    }

    public String getTmeTimeTo() {
        return tmeTimeTo;
    }

    public void setTmeTimeTo(String tmeTimeTo) {
        this.tmeTimeTo = tmeTimeTo;
    }

    public String getStrAMPMTo() {
        return strAMPMTo;
    }

    public void setStrAMPMTo(String strAMPMTo) {
        this.strAMPMTo = strAMPMTo;
    }

    public String getStrCostCenterCode() {
        return strCostCenterCode;
    }

    public void setStrCostCenterCode(String strCostCenterCode) {
        this.strCostCenterCode = strCostCenterCode;
    }

    public String getStrHourlyPricing() {
        return strHourlyPricing;
    }

    public void setStrHourlyPricing(String strHourlyPricing) {
        this.strHourlyPricing = strHourlyPricing;
    }

    public String getStrSubMenuHeadCode() {
        return strSubMenuHeadCode;
    }

    public void setStrSubMenuHeadCode(String strSubMenuHeadCode) {
        this.strSubMenuHeadCode = strSubMenuHeadCode;
    }

    public String getDteFromDate() {
        return dteFromDate;
    }

    public void setDteFromDate(String dteFromDate) {
        this.dteFromDate = dteFromDate;
    }

    public String getDteToDate() {
        return dteToDate;
    }

    public void setDteToDate(String dteToDate) {
        this.dteToDate = dteToDate;
    }


}
