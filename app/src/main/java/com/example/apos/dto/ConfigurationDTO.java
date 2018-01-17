package com.example.apos.dto;

import java.io.Serializable;

/**
 * Created by Abhilash on 9/9/17.
 *
 * @author Abhilash
 */

public class ConfigurationDTO implements Serializable {

	private String baseURL;
	private String serverName;
	private String KOT;
	private String billPrinter;
	private boolean active;

	public String getBILLPrinter() {
		return billPrinter;
	}

	public void setBILLPrinter(String BILLPrinter) {
		this.billPrinter = BILLPrinter;
	}

	public ConfigurationDTO() {
	}

	public ConfigurationDTO(String baseURL,String serverName,String billPrinter) {
		this.baseURL = baseURL;
		this.serverName=serverName;
		this.billPrinter=billPrinter;
	}

	public String getBaseURL() {
		return baseURL;
	}

	public String getServerName() {
		return serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public void setBaseURL(String baseURL) {
		this.baseURL = baseURL;
	}

	public String getKOT() {
		return KOT;
	}

	public void setKOT(String KOT) {
		this.KOT = KOT;
	}



	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
