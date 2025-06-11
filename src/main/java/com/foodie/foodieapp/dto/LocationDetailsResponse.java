package com.foodie.foodieapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LocationDetailsResponse {
    private double distanceInMeters;
    private String address;
    private double timeInMinutes;
    
} 