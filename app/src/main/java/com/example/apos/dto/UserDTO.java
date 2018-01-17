package com.example.apos.dto;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by Abhilash on 9/9/17.
 *
 * @author Abhilash
 */

public class UserDTO implements Serializable {

	@SerializedName("Status")
	private String status;

	@SerializedName("UserName")
	private String userName;

	@SerializedName("SuperType")
	private String userType;

	@SerializedName("UserCode")
	private String UserCode;

	@SerializedName("WaiterNo")
	private  String strWaiterNo;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getUserType() {
		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getUserCode() {return UserCode;}

	public void setUserCode(String userCode) {UserCode = userCode;}

	public String getStrWaiterNo() {
		return strWaiterNo;
	}

	public void setStrWaiterNo(String strWaiterNo) {
		this.strWaiterNo = strWaiterNo;
	}
}
