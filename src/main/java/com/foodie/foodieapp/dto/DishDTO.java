package com.foodie.foodieapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DishDTO {
    private String id;
    private String name;
    private String description;
    private String price;
    private String image;
    private String cuisine;
    private String restaurantId;
    private String restaurantName;  
    private Double distanceInKm;    
} 