package com.example.STKpush;

import com.example.STKpush.dtos.AcknowledgeResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class StkPushApplication {

	public static void main(String[] args) {
		SpringApplication.run(StkPushApplication.class, args);
	}

	// create beans


	@Bean
	public OkHttpClient getOkhttpClient(){
		return new OkHttpClient();
	}

	@Bean
	public ObjectMapper getObjectMapper(){
		return new ObjectMapper();
	}



}
