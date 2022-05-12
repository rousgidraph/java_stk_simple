package com.example.STKpush.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

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

    @JsonIgnore
    private LocalDateTime acquiredAt;

    @Override
     public String toString(){
        return 
            "AccessToken{" + 
            "access_token = '" + accessToken + '\'' + 
            ",expires_in = '" + expiresIn + '\'' + 
            "}";
        }
}