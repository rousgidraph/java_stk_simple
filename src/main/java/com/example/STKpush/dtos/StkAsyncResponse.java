package com.example.STKpush.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class StkAsyncResponse{
	@JsonProperty("Body")
	private Body body;
}