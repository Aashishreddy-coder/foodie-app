package com.foodie.foodieapp.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {
    private String dishId;
    private String dishName;
    private double price;
    private int quantity;
}




