package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Prashant on 9/23/2015.
 */
public class clsCustomerMaster {

    @SerializedName("CustCode")
    private String customerCode;

    @SerializedName("CustName")
    private String customerName;

    @SerializedName("CustType")
    private String customerType;

    private String customerCity;

    @SerializedName("BuildingName")
    private String customerBuildingName;

    @SerializedName("CustomerMobileNo")
    private String customerMobileNo;

    @SerializedName("CustTypeCode")
    private String customerTypeCode;

    public String getCustomerTypeCode() {
        return customerTypeCode;
    }

    public void setCustomerTypeCode(String customerTypeCode) {
        this.customerTypeCode = customerTypeCode;
    }


    public String getCustomerCity() {
        return customerCity;
    }

    public void setCustomerCity(String customerCity) {
        this.customerCity = customerCity;
    }

    public String getCustomerBuildingName() {
        return customerBuildingName;
    }

    public void setCustomerBuildingName(String customerBuildingName) {
        this.customerBuildingName = customerBuildingName;
    }




    public String getCustomerCode() {
        return customerCode;
    }

    public void setCustomerCode(String customerCode) {
        this.customerCode = customerCode;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getCustomerType() {
        return customerType;
    }

    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    public String getCustomerMobileNo() {
        return customerMobileNo;
    }

    public void setCustomerMobileNo(String customerMobileNo) {
        this.customerMobileNo = customerMobileNo;
    }
}
