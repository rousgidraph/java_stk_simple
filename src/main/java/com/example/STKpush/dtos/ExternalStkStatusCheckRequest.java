package com.example.STKpush.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExternalStkStatusCheckRequest{

	@JsonProperty("CheckoutRequestID")
	private String checkoutRequestID;
}