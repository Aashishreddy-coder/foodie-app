package com.foodie.foodieapp.dto;

import lombok.Data;

@Data

public class DishRequest {
    

    private String name;

    private String description;

    private String price;

  

    private String cuisine;

   private String restaurantId;
}
