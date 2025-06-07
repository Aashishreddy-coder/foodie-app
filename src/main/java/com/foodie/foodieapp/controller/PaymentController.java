package com.foodie.foodieapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodie.foodieapp.dto.PaymentRequest;
import com.foodie.foodieapp.dto.StripeResponse;
import com.foodie.foodieapp.service.StripeService;

@RestController
@RequestMapping("/payment")
public class PaymentController {

    @Autowired
    private StripeService stripeService;

    @PostMapping("/checkout")
    public ResponseEntity<StripeResponse> checkoutProducts(@RequestBody PaymentRequest paymentRequest){

        StripeResponse stripeResponse = stripeService.checkOutProducts(paymentRequest);

        return new ResponseEntity<>(stripeResponse, HttpStatus.OK);



}

}
