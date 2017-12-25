package com.leadiq.dto;

public class TResponse implements ResponseDto{
	private String status;
	private String message;
	
	public TResponse(String status,String message) {
		this(status);
		this.message = status;
	}
	
	public TResponse(String status) {
		super();
		this.status = status;
		this.message = "";
	}


	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
	
}
