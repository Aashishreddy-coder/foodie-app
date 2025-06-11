package com.foodie.foodieapp.domain;

import java.time.LocalDateTime;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {
    @Id
    private String id;
    private String orderId;
    private String userEmail;
    private String restaurantId;
    private String restaurantName;
    private String items;
    private String totalAmount;
    private String paymentStatus;
    private String sessionId;
    private String currentTime;
    private String deliveryTime;
    private String time;

    private String distance;
   
    private String deliveryAddress;
    private String deliveryStatus;


    

}
