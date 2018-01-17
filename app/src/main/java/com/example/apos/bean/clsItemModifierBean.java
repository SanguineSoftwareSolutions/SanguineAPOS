package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

public class clsItemModifierBean {
	@SerializedName("ModifierName")
	private String strModifierName;
	@SerializedName("ModifierCode")
	private String strModifierCode;

	@SerializedName("ItemCode")
	private String strItemCode;

	@SerializedName("Rate")
	private double rate;

    /*
    public clsItemModifierBean(String strModifierName, String strModifierCode, String strItemCode, double rate) {
        this.strModifierName = strModifierName;
        this.strModifierCode = strModifierCode;
        this.strItemCode = strItemCode;
        this.rate = rate;
    }*/

	public String getStrModifierName() {
		return strModifierName;
	}

	public void setStrModifierName(String strModifierName) {
		this.strModifierName = strModifierName;
	}

	public String getStrModifierCode() {
		return strModifierCode;
	}

	public void setStrModifierCode(String strModifierCode) {
		this.strModifierCode = strModifierCode;
	}

	public String getStrItemCode() {
		return strItemCode;
	}

	public void setStrItemCode(String strItemCode) {
		this.strItemCode = strItemCode;
	}

	public double getRate() {
		return rate;
	}

	public void setRate(double rate) {
		this.rate = rate;
	}
}