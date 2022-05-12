package com.example.STKpush.service;

import com.example.STKpush.config.MpesaConfigurations;
import com.example.STKpush.dtos.*;
import com.example.STKpush.utils.utility;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.sql.Time;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

import static com.example.STKpush.utils.CONSTANTS.*;


@Service
@Slf4j
@NoArgsConstructor
public class DarajaServiceImpl implements DarajaService{
    @Autowired
    MpesaConfigurations configs;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    OkHttpClient okHttpClient;

    private AccessToken tokenExist = null;
    /*
    public DarajaServiceImpl (MpesaConfigurations configg, ObjectMapper mapp, OkHttpClient oKK){
        this.configs = configg;
        this.objectMapper = mapp;
        this.okHttpClient = oKK;

    }*/
//sample bookmark

    @Override
    public AccessToken getAccess() {
        /*
        Basic Auth over HTTPS, this is a base64 encoded string of an app's consumer key and consumer secret
        * Get consumer key and customer secret from config  and convert to base 64
        * Send that request and await a response
        * Parse the response into an Access token
        * */
        String encoded_credentials = utility.tobase64(String.format("%s:%s",configs.getConsumerKey(),configs.getConsumerSecret()));

        //System.out.println(encoded_credentials);
        Request request = new Request.Builder()
                .url(String.format("%s?grant_type=%s", configs.getOauthEndpoint(), configs.getGrantType()))
                .get()
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BASIC_AUTH_STRING, encoded_credentials))
                .addHeader(CACHE_CONTROL_HEADER,CACHE_CONTROL_HEADER_VALUE)
                .build();

        /**
         * optimisation : Check if there's a valid token available
         * */

        if(tokenExist != null){
            //time check logic
            LocalDateTime atTheMoment = LocalDateTime.now();
            //testing
            atTheMoment =  atTheMoment.plusHours(1);
            Duration difference = Duration.between(tokenExist.getAcquiredAt(),atTheMoment);
            Long diff = Math.abs(difference.toSeconds());
            Long diff_mills = Math.abs(difference.toMillis());
            log.info("The time since last token call "+diff+" time in millis : "+diff_mills);
            if(diff < Long.valueOf(tokenExist.getExpiresIn())){
                return tokenExist;
            }
        }



        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() != null;
            String holder = response.body().string();
            AccessToken returnable = objectMapper.readValue(holder, AccessToken.class);
            returnable.setAcquiredAt(LocalDateTime.now());
            tokenExist = returnable;
            return tokenExist;

        }catch (IOException e ){
            log.error(String.format("Could not get access token. -> %s", e.getLocalizedMessage()));
            return null;
        }

    }


    /**
     * @Return takes the phone number and the ammount and addes them to the stk request
     * @return*/
    @Override
    public AcknowledgeResponse stk_request(ExternalStkrequest req) {
        /**
         * This is the password used for encrypting the request sent: A base64 encoded string. (The base64 string is a combination of Shortcode+Passkey+Timestamp)
         * This is the Timestamp of the transaction, normaly in the formart of YEAR+MONTH+DATE+HOUR+MINUTE+SECOND (YYYYMMDDHHMMSS) Each part should be atleast two digits apart from the year which takes four digits.
         * set the details on the request
         * */

        stkRequest request = new stkRequest(); //preprare request payload
        String timeStamp = utility.getTransactionTimestamp(); //get the time stamp
        String stkpassword= utility.getStkPushPassword(configs.getShortcode(),configs.getStkPassKey(),timeStamp); //get the password

        //add fields to the request
        request.setAmount(req.getAmount());
        request.setPhoneNumber(req.getPhoneNumber());
        request.setAccountReference(utility.getTransactionUniqueNumber());
        request.setTransactionType(CUSTOMER_PAYBILL_ONLINE);
        request.setBusinessShortCode(configs.getShortcode());
        request.setPartyA(req.getPhoneNumber());
        request.setPassword(stkpassword);
        request.setPartyB(configs.getShortcode());
        request.setCallBackURL(configs.getStkCallbackUrl());
        request.setTimestamp(timeStamp);
        request.setTransactionDesc(String.format("%s Transaction", req.getPhoneNumber()));


        //get the token
        AccessToken token = this.getAccess();
        System.out.println("Got the access token"+ token);

        //convert the request payload to json for sending
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                Objects.requireNonNull(utility.toJson(request)));
        System.out.println("Prepared the payload : \n"+utility.toJson(request));


        Request requestt = new Request.Builder()
                .url(configs.getStkEndpoint())
                .post(body) // adding the payload to the actual request
                .addHeader(AUTHORIZATION_HEADER_STRING, String.format("%s %s", BEARER_AUTH_STRING, token.getAccessToken())) //add the auth token to the request
                .build();

        try {
            Response response = okHttpClient.newCall(requestt).execute();
            assert response.body() != null;
            String temp_hold = response.body().string();
            if(response.isSuccessful()){
                StkResponse stkResponse = objectMapper.readValue(temp_hold, StkResponse.class);
                return new AcknowledgeResponse("Push successfull \n "+stkResponse.getCheckoutRequestID()+" \n "+stkResponse.getResponseDescription());
            }else{
                StkError stkerror = objectMapper.readValue(temp_hold, StkError.class);
                return new AcknowledgeResponse("Something went wrong \n"+stkerror.getErrorMessage());
            }




        }catch (IOException e ){
            System.out.println("not a Safe zone : "+e.getCause() );

            log.error(String.format("Could not perform the STK push request -> %s", e.getLocalizedMessage()));
            return null;

        }

    }

    @Override
    public AcknowledgeResponse checkStkStatus(String checkoutRequestID) {
        /**
         * Check on the status of a transaction given the request id
         * get token
         * prepare the payload
         * send to saf
         * give response
         * This is the password used for encrypting the request sent: A base64 encoded string. (The base64 string is a combination of Shortcode+Passkey+Timestamp)
         * */
        StkStatusCheckRequest payload = new StkStatusCheckRequest();


        String timeStamp = utility.getTransactionTimestamp(); //get the time stamp
        String stkpassword= utility.getStkPushPassword(configs.getShortcode(),configs.getStkPassKey(),timeStamp); //get the password

        payload.setCheckoutRequestID(checkoutRequestID);
        payload.setTimestamp(timeStamp);
        payload.setPassword(stkpassword);
        payload.setBusinessShortCode(configs.getShortcode());

        //get the token
        AccessToken token = this.getAccess();
        System.out.println("Got token 👌👌");

        //convert the payload to json for sendong
        RequestBody body = RequestBody.create(JSON_MEDIA_TYPE,
                Objects.requireNonNull(utility.toJson(payload))
        );
        System.out.println("Created the payload"+utility.toJson(payload));

        Request request = new Request.Builder()
                .url(configs.getStkStatusCheckUrl())
                .post(body)
                .addHeader(AUTHORIZATION_HEADER_STRING,String.format("%s %s",BEARER_AUTH_STRING,token.getAccessToken())) //bearer dfgadsgergsdrfgva
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
            assert response.body() !=null   ;

            String holder = response.body().string();
            if(response.isSuccessful()){
                StkStatusCheckResponse response1 = objectMapper.readValue(holder,StkStatusCheckResponse.class);
                return new AcknowledgeResponse(response1.getResultDesc());
            }else{
                StkError  stkError = objectMapper.readValue(holder,StkError.class);
                return new AcknowledgeResponse("Something went wrong \n"+stkError.getErrorMessage());
            }

        }catch (IOException e){

            log.error(String.format("Could not perform the STK push status check  -> %s", e.getLocalizedMessage()));
            return null;

        }catch (NullPointerException nu ){
            log.error("Something threw a null "+nu);
            return null;
        }catch (Exception e ){
            System.out.println("If you reading this its too late : \n"+e.getCause()+" \n Message : "+e.getLocalizedMessage());
            return null;
        }

     }
}
