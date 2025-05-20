package com.foodie.foodieapp.dto;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantDTO {
    private String id;
    private String restaurantName;
    private String city;

    private GeoJsonPoint geoLocation;
    private Double distanceInKm;  // add distance here
    // getters, setters, constructors
}

