package com.example.STKpush.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;

public class StkStatusCheckResponse{

	@JsonProperty("MerchantRequestID")
	private String merchantRequestID;

	@JsonProperty("ResponseCode")
	private String responseCode;

	@JsonProperty("CheckoutRequestID")
	private String checkoutRequestID;

	@JsonProperty("ResponseDescription")
	private String responseDescription;

	@JsonProperty("ResultDesc")
	private String resultDesc;

	@JsonProperty("ResultCode")
	private String resultCode;

	public void setMerchantRequestID(String merchantRequestID){
		this.merchantRequestID = merchantRequestID;
	}

	public String getMerchantRequestID(){
		return merchantRequestID;
	}

	public void setResponseCode(String responseCode){
		this.responseCode = responseCode;
	}

	public String getResponseCode(){
		return responseCode;
	}

	public void setCheckoutRequestID(String checkoutRequestID){
		this.checkoutRequestID = checkoutRequestID;
	}

	public String getCheckoutRequestID(){
		return checkoutRequestID;
	}

	public void setResponseDescription(String responseDescription){
		this.responseDescription = responseDescription;
	}

	public String getResponseDescription(){
		return responseDescription;
	}

	public void setResultDesc(String resultDesc){
		this.resultDesc = resultDesc;
	}

	public String getResultDesc(){
		return resultDesc;
	}

	public void setResultCode(String resultCode){
		this.resultCode = resultCode;
	}

	public String getResultCode(){
		return resultCode;
	}
}