package com.example.STKpush.controller;


import com.example.STKpush.dtos.*;
import com.example.STKpush.service.DarajaServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*")
@AllArgsConstructor
@RestController()
@RequestMapping("mobile-money")
@Slf4j
public class MpesaController {
    DarajaServiceImpl darajaService;

    @GetMapping()
    public String hello(){
        //System.out.println(darajaService.getAccess());
        return "eND POINT HIT UP";
    }

    @GetMapping(path = "/auth")
    public AccessToken getAccess(){
        AccessToken returnable = darajaService.getAccess();

        System.out.println(returnable);
        return returnable;
    }

    @PostMapping(path = "/stk_push")
    public AcknowledgeResponse trigger_stk(@RequestBody ExternalStkrequest externalStkrequest){
        return   darajaService.stk_request(externalStkrequest);


    }

    @PostMapping("/stk_status")
    public AcknowledgeResponse stkStatus(@RequestBody ExternalStkStatusCheckRequest RequestID){

        return darajaService.checkStkStatus(RequestID.getCheckoutRequestID());
    }

    @PostMapping("/snitch")
    public void snitch(@RequestBody String message ){
        System.out.println(message);
    }

    @PostMapping("/stk")
    public void stk_callback(@RequestBody StkAsyncResponse something){
        System.out.println("Finished with code " );
        System.out.println(something.getBody().getStkCallback().getResultCode());
        System.out.println("Full response : \n"+something.toString());


    }

}
