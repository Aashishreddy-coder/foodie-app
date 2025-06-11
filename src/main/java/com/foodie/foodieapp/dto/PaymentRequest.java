package com.foodie.foodieapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
    private Long amount;
    private Long quantity;
    private String name;
    private String currency;
    private String restaurantId;
    private String restaurantName;
    private String address;
    private String distance;
    private String orderId;
    private String orderItems;
    private String time;

    


 
    


}
