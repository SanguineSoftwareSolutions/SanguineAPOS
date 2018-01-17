package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class clsPosSelectionMaster {

	@SerializedName("PosCode")
	private String strPosCode;
	@SerializedName("PosName")
	private String strPosName;
	@SerializedName("PosType")
	private String strPosType;
	@SerializedName("Counter")
	private String strCounterWiseBilling;
	@SerializedName("PrintVatNo")
	private String strPrintVatNo;
	@SerializedName("PrintServiceTaxNo")
	private String strPrintServiceTaxNo;
	@SerializedName("VatNo")
	private String strVatNo;
	@SerializedName("ServiceTaxNo")
	private String strServiceTaxNo;
	private double dblAmount;

	public double getDblAmount() {
		return dblAmount;
	}

	public void setDblAmount(double dblAmount) {
		this.dblAmount = dblAmount;
	}

	@SerializedName("SettleRows")
	List<clsSettlementDtl> arrListSettleMentDtl;
	@SerializedName("CounterDetails")
	List<clsCounterBeen> arrListCounterDtl;


	public String getStrPosCode() {
		return strPosCode;
	}

	public void setStrPosCode(String strPosCode) {
		this.strPosCode = strPosCode;
	}

	public String getStrPosName() {
		return strPosName;
	}

	public void setStrPosName(String strPosName) {
		this.strPosName = strPosName;
	}

	public String getStrPosType() {
		return strPosType;
	}

	public void setStrPosType(String strPosType) {
		this.strPosType = strPosType;
	}

	public List<clsSettlementDtl> getArrListSettleMentDtl() {
		return arrListSettleMentDtl;
	}

	public String getStrCounterWiseBilling() {
		return strCounterWiseBilling;
	}

	public void setStrCounterWiseBilling(String strCounterWiseBilling) {
		this.strCounterWiseBilling = strCounterWiseBilling;
	}


	public List<clsCounterBeen> getArrListCounterDtl() {
		return arrListCounterDtl;
	}

	public void setArrListCounterDtl(List<clsCounterBeen> arrListCounterDtl) {
		this.arrListCounterDtl = arrListCounterDtl;
	}

	public String getStrPrintVatNo() {
		return strPrintVatNo;
	}

	public void setStrPrintVatNo(String strPrintVatNo) {
		this.strPrintVatNo = strPrintVatNo;
	}

	public String getStrPrintServiceTaxNo() {
		return strPrintServiceTaxNo;
	}

	public void setStrPrintServiceTaxNo(String strPrintServiceTaxNo) {
		this.strPrintServiceTaxNo = strPrintServiceTaxNo;
	}

	public String getStrVatNo() {
		return strVatNo;
	}

	public void setStrVatNo(String strVatNo) {
		this.strVatNo = strVatNo;
	}

	public String getStrServiceTaxNo() {
		return strServiceTaxNo;
	}

	public void setStrServiceTaxNo(String strServiceTaxNo) {
		this.strServiceTaxNo = strServiceTaxNo;
	}
}
