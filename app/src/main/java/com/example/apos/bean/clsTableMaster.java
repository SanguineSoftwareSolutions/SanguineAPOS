package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Prashant on 7/22/2015.
 */
public class clsTableMaster {

	@SerializedName("TableName")
	private String strTableName;

	@SerializedName("TableNo")
	private String strTableNo;

	@SerializedName("TableStatus")
	private String strTableStatus;

	@SerializedName("AreaCode")
	private String strAreaCode;

	@SerializedName("WaiterNo")
	private String strWaiterCode;

	@SerializedName("WaiterName")
	private String strWaiterName;

	@SerializedName("CardNo")
	private String strCardNo;

	@SerializedName("CardType")
	private String strCardType;

	@SerializedName("PaxNo")
	private int intPaxNo;
	@SerializedName("KOTAmt")
	private double dblKOTAmt;

	@SerializedName("CardBalance")
	private double dblCardBalanace;

	@SerializedName("linkedWaiterNo")
	private String strLinkedWaiterNo;

	@SerializedName("Time")
	private String strTime;

	public String getStrTime() {
		return strTime;
	}

	public void setStrTime(String strTime) {
		this.strTime = strTime;
	}

	public String getStrLinkedWaiterNo() {
		return strLinkedWaiterNo;
	}

	public void setStrLinkedWaiterNo(String strLinkedWaiterNo) {
		this.strLinkedWaiterNo = strLinkedWaiterNo;
	}

	public String getStrCardNo() {
		return strCardNo;
	}

	public void setStrCardNo(String strCardNo) {
		this.strCardNo = strCardNo;
	}

	public int getIntPaxNo() {
		return intPaxNo;
	}

	public void setIntPaxNo(int intPaxNo) {
		this.intPaxNo = intPaxNo;
	}

	public double getDblCardBalanace() {
		return dblCardBalanace;
	}

	public String getStrCardType() {
		return strCardType;
	}

	public void setStrCardType(String strCardType) {
		this.strCardType = strCardType;
	}

	public void setDblCardBalanace(double dblCardBalanace) {
		this.dblCardBalanace = dblCardBalanace;
	}


	ArrayList<String> arrListNormalTable = new ArrayList<String>();
	ArrayList<String> arrListBilledTable = new ArrayList<String>();
	ArrayList<String> arrListOccupiedTable = new ArrayList<String>();

	public ArrayList<String> getArrListNormalTable() {
		return arrListNormalTable;
	}

	public ArrayList<String> getArrListOccupiedTable() {
		return arrListOccupiedTable;
	}

	public void setArrListOccupiedTable(ArrayList<String> arrListOccupiedTable) {
		this.arrListOccupiedTable = arrListOccupiedTable;
	}

	public void setArrListNormalTable(ArrayList<String> arrListNormalTable) {
		this.arrListNormalTable = arrListNormalTable;
	}

	public ArrayList<String> getArrListBilledTable() {
		return arrListBilledTable;
	}

	public void setArrListBilledTable(ArrayList<String> arrListBilledTable) {
		this.arrListBilledTable = arrListBilledTable;
	}

	public String getStrWaiterName() {
		return strWaiterName;
	}

	public void setStrWaiterName(String strWaiterName) {
		this.strWaiterName = strWaiterName;
	}

	public String getStrWaiterCode() {
		return strWaiterCode;
	}

	public void setStrWaiterCode(String strWaiterCode) {
		this.strWaiterCode = strWaiterCode;
	}

	public String getStrTableName() {
		return strTableName;
	}

	public void setStrTableName(String strTableName) {
		this.strTableName = strTableName;
	}

	public String getStrTableNo() {
		return strTableNo;
	}

	public void setStrTableNo(String strTableNo) {
		this.strTableNo = strTableNo;
	}

	public String getStrTableStatus() {
		return strTableStatus;
	}

	public void setStrTableStatus(String strTableStatus) {
		this.strTableStatus = strTableStatus;
	}

	public String getStrAreaCode() {
		return strAreaCode;
	}

	public void setStrAreaCode(String strAreaCode) {
		this.strAreaCode = strAreaCode;
	}

	public double getDblKOTAmt() {
		return dblKOTAmt;
	}

	public void setDblKOTAmt(double dblKOTAmt) {
		this.dblKOTAmt = dblKOTAmt;
	}
}
