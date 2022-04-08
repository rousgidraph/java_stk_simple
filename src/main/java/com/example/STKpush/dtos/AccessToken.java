package com.example.STKpush.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class AccessToken{

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private String expiresIn;

    public String getAccessToken(){
        return accessToken;
    }

    public String getExpiresIn(){
        return expiresIn;
    }

    @Override
     public String toString(){
        return 
            "AccessToken{" + 
            "access_token = '" + accessToken + '\'' + 
            ",expires_in = '" + expiresIn + '\'' + 
            "}";
        }
}