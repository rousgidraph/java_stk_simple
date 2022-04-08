package com.example.STKpush.service;

import com.example.STKpush.dtos.*;

public interface DarajaService {
    /*
    *
    * */
    public AccessToken getAccess();



    public AcknowledgeResponse stk_request(ExternalStkrequest req);

    public AcknowledgeResponse checkStkStatus(String checkoutRequestID);
}
