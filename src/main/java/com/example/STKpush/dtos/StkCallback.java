package com.example.STKpush.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StkCallback{

	@JsonProperty("MerchantRequestID")
	private String merchantRequestID;

	@JsonProperty("CheckoutRequestID")
	private String checkoutRequestID;

	@JsonProperty("ResultDesc")
	private String resultDesc;

	@JsonProperty("ResultCode")
	private int resultCode;
}