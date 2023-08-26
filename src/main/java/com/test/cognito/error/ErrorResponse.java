package com.test.cognito.error;

import java.time.LocalDateTime;

public class ErrorResponse {
	
	private LocalDateTime timestamp;
	private String ErrorDescription;	
	
	public ErrorResponse(LocalDateTime timestamp, String errorDescription) {
		this.timestamp = timestamp;
		ErrorDescription = errorDescription;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}

	public String getErrorDescription() {
		return ErrorDescription;
	}
	public void setErrorDescription(String errorDescription) {
		ErrorDescription = errorDescription;
	}
}
