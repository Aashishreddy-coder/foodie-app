package com.foodie.foodieapp.service;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;

import jakarta.annotation.PostConstruct;

import com.foodie.foodieapp.dto.PaymentRequest;
import com.foodie.foodieapp.dto.StripeResponse;






@Service
public class StripeService {
   @Value("${stripe.secretKey}")
    private String secretKey;


    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;  // This works
    }

    public StripeResponse checkOutProducts(PaymentRequest paymentRequest) {
        



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

                    Map<String, String> metadata = new HashMap<>();
                    metadata.put("restaurantId", paymentRequest.getRestaurantId());
                    metadata.put("restaurantName", paymentRequest.getRestaurantName());
                    metadata.put("address", paymentRequest.getAddress());
                    metadata.put("distance", paymentRequest.getDistance());
                    metadata.put("orderId", paymentRequest.getOrderId());
                    metadata.put("orderItems", paymentRequest.getOrderItems());
                    metadata.put("time", paymentRequest.getTime());
                    metadata.put("amount", paymentRequest.getAmount().toString());
                    



            SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/delivery?session_id={CHECKOUT_SESSION_ID}")
                .setCancelUrl("http://localhost:3000/orders")
                .addLineItem(lineItem)
                .putAllMetadata(metadata)
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

    public StripeResponse getSessionDetails(String sessionId){

        try{
            Session session = Session.retrieve(sessionId);
            Map<String, String> metadata = session.getMetadata();
            return StripeResponse.builder()
                .status("success")
                .message("Payment session retrieved successfully")
                .sessionId(session.getId())
                .sessionUrl(session.getUrl())
                .metadata(metadata)
                .build();
        } catch (StripeException e) {
            return StripeResponse.builder()
                .status("error")
                .message("Failed to retrieve payment session: " + e.getMessage())
                .build();
        }


        





        




    

        



        

        





        

    }





       

    
    
    
}
