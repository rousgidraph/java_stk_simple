package com.example.STKpush.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix="mpesa.daraja")
public class MpesaConfigurations {
    private String consumerKey;
    private String consumerSecret;
    private String grantType;
    private String oauthEndpoint;
    private String stkEndpoint;
    private String stkPassKey;
    private String shortcode;
    private String stkCallbackUrl;
    private String stkStatusCheckUrl;

}
