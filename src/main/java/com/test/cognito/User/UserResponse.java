package com.test.cognito.User;

public class UserResponse {

	private String message;	
	
	public UserResponse() {
		this.message = "USER LOGIN";
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}	
}
