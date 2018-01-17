package com.example.apos.bean;

import com.google.gson.annotations.SerializedName;


public class clsWaiterMaster {
	@SerializedName("WaiterName")
	private String strWaiterName;

	@SerializedName("WaiterNo")
	private String strWaterNo;

	public String getStrWaiterName() {
		return strWaiterName;
	}

	public void setStrWaiterName(String strWaiterName) {
		this.strWaiterName = strWaiterName;
	}

	public String getStrWaterNo() {
		return strWaterNo;
	}

	public void setStrWaterNo(String strWaterNo) {
		this.strWaterNo = strWaterNo;
	}


}