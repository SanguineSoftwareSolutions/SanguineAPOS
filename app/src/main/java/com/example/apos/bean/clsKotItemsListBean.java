package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class clsKotItemsListBean implements Serializable {

	@SerializedName("ItemCode")
	private String strItemCode;

	@SerializedName("ItemName")
	private String strItemName;

	@SerializedName("TextColor")
	private String textColor;

	private String strSubGroupCode;

	@SerializedName("ExternalCode")
	private String strExternalCode;

	private double dblSalePrice;

	@SerializedName("itemImage")
	private String strItemImage;

	private String strItemQty;

	@SerializedName("PriceMonday")
	private double dblSalePriceMon;

	@SerializedName("PriceTuesday")
	private double dblSalePriceTues;

	@SerializedName("PriceWenesday")
	private double dblSalePriceWend;
	@SerializedName("PriceThursday")
	private double dblSalePriceThu;
	@SerializedName("PriceFriday")
	private double dblSalePriceFri;

	@SerializedName("PriceSaturday")
	private double dblSalePriceSat;

	@SerializedName("PriceSunday")
	private double dblSalePriceSun;

	@SerializedName("TimeFrom")
	private String timeFrom;

	@SerializedName("AMPMFrom")
	private String AMPMFrom;

	@SerializedName("TimeTo")
	private String timeTo;

	@SerializedName("AMPMTo")
	private String AMPMTo;

	@SerializedName("CostCenterCode")
	private String costCenterCode;

	@SerializedName("FromDate")
	private String fromDate;

	@SerializedName("ToDate")
	private String toDate;

	@SerializedName("HourlyPricing")
	private String hourlyPricing;

	@SerializedName("SubMenuHeadCode")
	private String subMenuHeadCode;

	@SerializedName("AreaCode")
	private String areaCode;

	@SerializedName("MenuCode")
	private String menuCode;

	public String getSubMenuHeadCode() {
		return subMenuHeadCode;
	}

	public void setSubMenuHeadCode(String subMenuHeadCode) {
		this.subMenuHeadCode = subMenuHeadCode;
	}

	public String getAreaCode() {
		return areaCode;
	}

	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}

	public String getMenuCode() {
		return menuCode;
	}

	public void setMenuCode(String menuCode) {
		this.menuCode = menuCode;
	}

	public String getTextColor() {
		return textColor;
	}

	public void setTextColor(String textColor) {
		this.textColor = textColor;
	}

	public double getDblSalePriceMon() {
		return dblSalePriceMon;
	}

	public void setDblSalePriceMon(double dblSalePriceMon) {
		this.dblSalePriceMon = dblSalePriceMon;
	}

	public double getDblSalePriceTues() {
		return dblSalePriceTues;
	}

	public void setDblSalePriceTues(double dblSalePriceTues) {
		this.dblSalePriceTues = dblSalePriceTues;
	}

	public double getDblSalePriceWend() {
		return dblSalePriceWend;
	}

	public void setDblSalePriceWend(double dblSalePriceWend) {
		this.dblSalePriceWend = dblSalePriceWend;
	}

	public double getDblSalePriceThu() {
		return dblSalePriceThu;
	}

	public void setDblSalePriceThu(double dblSalePriceThu) {
		this.dblSalePriceThu = dblSalePriceThu;
	}

	public double getDblSalePriceFri() {
		return dblSalePriceFri;
	}

	public void setDblSalePriceFri(double dblSalePriceFri) {
		this.dblSalePriceFri = dblSalePriceFri;
	}

	public double getDblSalePriceSat() {
		return dblSalePriceSat;
	}

	public void setDblSalePriceSat(double dblSalePriceSat) {
		this.dblSalePriceSat = dblSalePriceSat;
	}

	public double getDblSalePriceSun() {
		return dblSalePriceSun;
	}

	public void setDblSalePriceSun(double dblSalePriceSun) {
		this.dblSalePriceSun = dblSalePriceSun;
	}

	public String getStrItemQty() {
		return strItemQty;
	}

	public void setStrItemQty(String strItemQty) {
		this.strItemQty = strItemQty;
	}

	public String getStrItemImage() {
		return strItemImage;
	}

	public void setStrItemImage(String strItemImage) {
		this.strItemImage = strItemImage;
	}

	public double getDblSalePrice() {
		return dblSalePrice;
	}

	public void setDblSalePrice(double dblSalePrice) {
		this.dblSalePrice = dblSalePrice;
	}

	public String getStrExternalCode() {
		return strExternalCode;
	}

	public void setStrExternalCode(String strExternalCode) {
		this.strExternalCode = strExternalCode;
	}

	public String getStrSubGroupCode() {
		return strSubGroupCode;
	}

	public void setStrSubGroupCode(String strSubGroupCode) {
		this.strSubGroupCode = strSubGroupCode;
	}

	public String getStrItemName() {
		return strItemName;
	}

	public void setStrItemName(String strItemName) {
		this.strItemName = strItemName;
	}

	public String getStrItemCode() {
		return strItemCode;
	}

	public void setStrItemCode(String strItemCode) {
		this.strItemCode = strItemCode;
	}


}