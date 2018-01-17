package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Monika on 8/15/2015.
 */
public class clsSettlementDtl {

	@SerializedName("SettlementCode")
	private String strSettlementCode;

	@SerializedName("SettlementDesc")
	private String strSettlementName;

	@SerializedName("SettlementType")
	private String strSettlementType;

	@SerializedName("BillPrintOnSettlementYN")
	private String strBillPrintOnSettlementYN;

	public String getStrBillPrintOnSettlementYN() {
		return strBillPrintOnSettlementYN;
	}

	public void setStrBillPrintOnSettlementYN(String strBillPrintOnSettlementYN) {
		this.strBillPrintOnSettlementYN = strBillPrintOnSettlementYN;
	}

	public String getStrSettlementCode() {
		return strSettlementCode;
	}

	public void setStrSettlementCode(String strSettlementCode) {
		this.strSettlementCode = strSettlementCode;
	}

	public String getStrSettlementName() {
		return strSettlementName;
	}

	public void setStrSettlementName(String strSettlementName) {
		this.strSettlementName = strSettlementName;
	}

	public String getStrSettlementType() {
		return strSettlementType;
	}

	public void setStrSettlementType(String strSettlementType) {
		this.strSettlementType = strSettlementType;
	}
}
