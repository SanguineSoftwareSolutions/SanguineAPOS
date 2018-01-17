package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Monika on 6/29/2017.
 */

public class clsTDHBean {
	private String strModifierGroupName;

	@SerializedName("ModifierGroupCode")
	private String strModifierGroupCode;

	@SerializedName("ModifierCode")
	private String strTDHItemCode;
	@SerializedName("ModifierName")
	private String strTDHItemName;

	@SerializedName("maxQty")
	private double dblQty;

	private String strDefaultYN;

	@SerializedName("ApplyMaxItemLimit")
	private String strApplyMaxItemLimit;
	@SerializedName("ApplyMinItemLimit")
	private String strApplyMinItemLimit;
	@SerializedName("ItemMaxLimit")
	private int intItemMaxLimit;
	@SerializedName("ItemMinLimit")
	private int intItemMinLimit;
	private String isSelected;

	@SerializedName("Rate")
	private double dblRate;

	@SerializedName("ModifierItemList")
	private ArrayList<clsTDHBean> listModifierItemList;

	public double getDblRate() {
		return dblRate;
	}

	public void setDblRate(double dblRate) {
		this.dblRate = dblRate;
	}

	public String getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(String isSelected) {
		this.isSelected = isSelected;
	}

	public String getStrModifierGroupName() {
		return strModifierGroupName;
	}

	public void setStrModifierGroupName(String strModifierGroupName) {
		this.strModifierGroupName = strModifierGroupName;
	}

	public String getStrModifierGroupCode() {
		return strModifierGroupCode;
	}

	public void setStrModifierGroupCode(String strModifierGroupCode) {
		this.strModifierGroupCode = strModifierGroupCode;
	}

	public String getStrTDHItemCode() {
		return strTDHItemCode;
	}

	public void setStrTDHItemCode(String strTDHItemCode) {
		this.strTDHItemCode = strTDHItemCode;
	}

	public String getStrTDHItemName() {
		return strTDHItemName;
	}

	public void setStrTDHItemName(String strTDHItemName) {
		this.strTDHItemName = strTDHItemName;
	}

	public double getDblQty() {
		return dblQty;
	}

	public void setDblQty(double dblQty) {
		this.dblQty = dblQty;
	}

	public String getStrDefaultYN() {
		return strDefaultYN;
	}

	public void setStrDefaultYN(String strDefaultYN) {
		this.strDefaultYN = strDefaultYN;
	}

	public String getStrApplyMaxItemLimit() {
		return strApplyMaxItemLimit;
	}

	public void setStrApplyMaxItemLimit(String strApplyMaxItemLimit) {
		this.strApplyMaxItemLimit = strApplyMaxItemLimit;
	}

	public String getStrApplyMinItemLimit() {
		return strApplyMinItemLimit;
	}

	public void setStrApplyMinItemLimit(String strApplyMinItemLimit) {
		this.strApplyMinItemLimit = strApplyMinItemLimit;
	}

	public int getIntItemMaxLimit() {
		return intItemMaxLimit;
	}

	public void setIntItemMaxLimit(int intItemMaxLimit) {
		this.intItemMaxLimit = intItemMaxLimit;
	}

	public int getIntItemMinLimit() {
		return intItemMinLimit;
	}

	public void setIntItemMinLimit(int intItemMinLimit) {
		this.intItemMinLimit = intItemMinLimit;
	}

	public ArrayList<clsTDHBean> getListModifierItemList() {
		return listModifierItemList;
	}

	public void setListModifierItemList(ArrayList<clsTDHBean> listModifierItemList) {
		this.listModifierItemList = listModifierItemList;
	}
}