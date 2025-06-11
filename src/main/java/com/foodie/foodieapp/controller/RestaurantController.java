package com.foodie.foodieapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.foodie.foodieapp.domain.Restaurant;
import com.foodie.foodieapp.dto.LocationDetailsResponse;
import com.foodie.foodieapp.dto.RestaurantDTO;
import com.foodie.foodieapp.service.RestaurantService;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {

    @Autowired
    private RestaurantService restaurantService;

    @PostMapping("/create")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        Restaurant createdRestaurant = restaurantService.createRestaurant(restaurant);
        return ResponseEntity.ok(createdRestaurant);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Restaurant> getRestaurantById(@PathVariable String id) {
        Restaurant restaurant = restaurantService.getRestaurantById(id);
        if (restaurant != null) {
            return ResponseEntity.ok(restaurant);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    

    @GetMapping("/nearby")
    public ResponseEntity<List<RestaurantDTO>> getRestaurantsByLocation(
            @RequestParam(required = false) Double latitude,
            @RequestParam(required = false) Double longitude,
            @RequestParam(required = false) String city,
            @RequestParam(required = false) String name) {
                
          
        List<RestaurantDTO> restaurants = restaurantService.getRestaurantsByLocation(latitude, longitude, city, name);
        return ResponseEntity.ok(restaurants);
    }
    
    

    @GetMapping("/city/{city}")
    public ResponseEntity<List<Restaurant>> getRestaurantsByCity(@PathVariable String city) {
        List<Restaurant> restaurants = restaurantService.getRestaurantsByCity(city);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<Restaurant>> getRestaurantsByName(@PathVariable String name) {
        List<Restaurant> restaurants = restaurantService.getRestaurantByName(name);
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Restaurant>> getAllRestaurants() {
        List<Restaurant> restaurants = restaurantService.getAllRestaurants();
        return ResponseEntity.ok(restaurants);
    }

    @GetMapping("/{id}/distance")
    public ResponseEntity<?> getDistanceToRestaurant(
            @PathVariable("id") String id,
            @RequestParam("latitude") Double latitude,
            @RequestParam("longitude") Double longitude) {
        try {
            Restaurant restaurant = restaurantService.getRestaurantById(id);
            if (restaurant == null) {
                return  new ResponseEntity<>("Restaurant not found",HttpStatus.NOT_FOUND);
            }

            LocationDetailsResponse distance = restaurantService.calculateDistanceToRestaurant(id, latitude, longitude);
            if (distance == null) {
                return new ResponseEntity<>("Could not calculate distance",HttpStatus.INTERNAL_SERVER_ERROR);
            }
            
            return ResponseEntity.ok(distance);
        } catch (Exception e) {
            return new ResponseEntity<>("Error calculating distance: " + e.getMessage(),HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
