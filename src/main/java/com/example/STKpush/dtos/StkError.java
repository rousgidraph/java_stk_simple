package com.example.STKpush.dtos;

public class StkError{
	private String requestId;
	private String errorMessage;
	private String errorCode;

	public String getRequestId(){
		return requestId;
	}

	public String getErrorMessage(){
		return errorMessage;
	}

	public String getErrorCode(){
		return errorCode;
	}
}
