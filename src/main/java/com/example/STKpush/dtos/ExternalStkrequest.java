package com.example.STKpush.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class ExternalStkrequest {
    @JsonProperty("PhoneNumber")
    private String phoneNumber;


    @JsonProperty("Amount")
    private String amount;



}
