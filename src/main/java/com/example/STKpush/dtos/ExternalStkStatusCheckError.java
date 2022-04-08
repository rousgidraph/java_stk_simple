package com.example.STKpush.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ExternalStkStatusCheckError{

	@JsonProperty("requestId")
	private String requestId;

	@JsonProperty("errorMessage")
	private String errorMessage;

	@JsonProperty("errorCode")
	private String errorCode;
}