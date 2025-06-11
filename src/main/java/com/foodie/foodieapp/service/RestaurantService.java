package com.foodie.foodieapp.service;

import java.util.List;

import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.foodie.foodieapp.domain.Restaurant;
import com.foodie.foodieapp.dto.LocationDetailsResponse;
import com.foodie.foodieapp.dto.RestaurantDTO;

public interface RestaurantService {

    Restaurant createRestaurant(Restaurant restaurant);

    Restaurant getRestaurantById(String id);

    List<Restaurant> getRestaurantByName(String name);

    List<Restaurant> getRestaurantsByCity(String city);

    List<RestaurantDTO> getRestaurantsByLocation(Double latitude, Double longitude, String city, String query);

    List<Restaurant> getAllRestaurants();

    LocationDetailsResponse calculateDistanceToRestaurant(String restaurantId, Double latitude, Double longitude);
}
