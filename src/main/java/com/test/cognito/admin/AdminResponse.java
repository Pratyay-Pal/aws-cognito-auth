package com.test.cognito.admin;

public class AdminResponse {

	private String message;	
	
	public AdminResponse() {
		this.message = "ADMIN LOGIN";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	
	
}
