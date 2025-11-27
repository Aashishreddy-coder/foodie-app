package com.foodie.foodieapp.domain;

import org.springframework.data.annotation.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@Document(collection = "orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Order {
    @Id
    private String id;
    private String userEmail;
    private String restaurantId;
    
   
    private List<OrderItem> items = new ArrayList<>();
    private double totalAmount;

    



}





