package com.foodie.foodieapp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.foodie.foodieapp.domain.Restaurant;

public interface RestaurantRespository extends MongoRepository<Restaurant, String> {

    List<Restaurant> findByCityIgnoreCase(String city);

    List<Restaurant> findByGeoLocationNear(GeoJsonPoint userLocation);
     

   
    List<Restaurant> findByGeoLocationNearAndCityIgnoreCase(GeoJsonPoint userLocation, String city);
    
  

    List<Restaurant> findByRestaurantNameContainingIgnoreCase(String name);

    List<Restaurant> findByGeoLocationNearAndCityIgnoreCaseAndRestaurantNameContainingIgnoreCase(GeoJsonPoint userlocation, String city, String name);

    List<Restaurant> findByCityAndRestaurantNameIgnoreCase(String city, String name);
   
    
    List<Restaurant> findByGeoLocationNearAndRestaurantNameContainingIgnoreCase(GeoJsonPoint userLocation, String name);


            

    
}
