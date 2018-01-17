package com.example.apos.api;

/**
 * Created by ganesh on 8/11/16.
 */

public class ServerError {

	private int statusCode;
	private String message;

	public ServerError(int statusCode, String message) {
		this.statusCode = statusCode;
		this.message = message;
	}

	public int getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(int statusCode) {
		this.statusCode = statusCode;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
