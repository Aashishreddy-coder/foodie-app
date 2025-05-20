package com.foodie.foodieapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "dishes")
public class Dish {

    @Id
    private String id;

    private String name;

    private String description;

    private String price;

    private String image;

    private String cuisine;

   private String restaurantId;

}


   

    
    
    
    
    
    
    



