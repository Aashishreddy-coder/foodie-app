package com.foodie.foodieapp.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.foodie.foodieapp.dto.PaymentRequest;
import com.foodie.foodieapp.dto.StripeResponse;




@Service
public class StripeService {
   @Value("${stripe.secretKey}")
    private String secretKey;

    public StripeResponse checkOutProducts(PaymentRequest paymentRequest) {
        Stripe.apiKey = secretKey;

        try {
            // First create the product data
            SessionCreateParams.LineItem.PriceData.ProductData productData =
                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                    .setName(paymentRequest.getName())
                    .build();

            SessionCreateParams.LineItem.PriceData priceData =
                SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("inr")
                    .setProductData(productData)
                    .setUnitAmount(paymentRequest.getAmount())
                    .build();

            SessionCreateParams.LineItem lineItem =
                SessionCreateParams.LineItem.builder()
                    .setPriceData(priceData)
                    .setQuantity(paymentRequest.getQuantity())
                    .build();

            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:8085/success")
                .setCancelUrl("http://localhost:8080/cancel")
                .addLineItem(lineItem)
                .build();

            Session session = Session.create(params);

            return StripeResponse.builder()
                .status("success")
                .message("Payment session created successfully")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .build();

        } catch (StripeException e) {
            return StripeResponse.builder()
                .status("error")
                .message("Failed to create payment session: " + e.getMessage())
                .build();
        }
    }


       

    
    
    
}
