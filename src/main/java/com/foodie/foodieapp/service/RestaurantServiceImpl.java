package com.foodie.foodieapp.service;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;

import com.foodie.foodieapp.domain.Restaurant;
import com.foodie.foodieapp.dto.RestaurantDTO;
import com.foodie.foodieapp.repository.RestaurantRespository;
import com.foodie.foodieapp.utils.GeoUtils;

@Service
public class RestaurantServiceImpl implements RestaurantService {

    @Autowired
    private RestaurantRespository restaurantRespository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public Restaurant createRestaurant(Restaurant restaurant) {
        if (restaurant.getGeoLocation() == null) {
            throw new RuntimeException("Restaurant coordinates are required");
        }

        // Check if restaurant with same name exists nearby
        List<Restaurant> nearbyRestaurants = restaurantRespository.findByGeoLocationNear(restaurant.getGeoLocation());
        for (Restaurant nearby : nearbyRestaurants) {
            if (nearby.getRestaurantName().equalsIgnoreCase(restaurant.getRestaurantName())) {
                double distance = GeoUtils.calculateDistance(
                    restaurant.getGeoLocation().getY(),
                    restaurant.getGeoLocation().getX(),
                    nearby.getGeoLocation().getY(),
                    nearby.getGeoLocation().getX()
                );
                if (distance < 0.1) { // 100 meters = 0.1 km
                    throw new RuntimeException("Restaurant with name '" + restaurant.getRestaurantName() + 
                        "' already exists within 100 meters of these coordinates");
                }
            }
        }
        
        return restaurantRespository.save(restaurant);
    }

    @Override
    public Restaurant getRestaurantById(String id) {
        return restaurantRespository.findById(id).orElse(null);
    }

    @Override
    public List<Restaurant> getRestaurantByName(String name) {
        return restaurantRespository.findByRestaurantNameContainingIgnoreCase(name);
    }

    @Override
    public List<Restaurant> getRestaurantsByCity(String city) {
        return restaurantRespository.findByCityIgnoreCase(city);
    }

   

    @Override
    public List<RestaurantDTO> getRestaurantsByLocation(Double latitude, Double longitude, String city, String query) {
        List<Restaurant> restaurants;
    
        if (latitude != null && longitude != null) {
            GeoJsonPoint userLocation = new GeoJsonPoint(longitude, latitude);
    
            if (city != null && !city.isEmpty() && query != null && !query.isEmpty()) {
                restaurants = restaurantRespository.findByGeoLocationNearAndCityIgnoreCaseAndRestaurantNameContainingIgnoreCase(userLocation, city, query);
            } else if (city != null && !city.isEmpty()) {
                restaurants = restaurantRespository.findByGeoLocationNearAndCityIgnoreCase(userLocation, city);
            }
            else if (query != null && !query.isEmpty()) {
                restaurants = restaurantRespository.findByGeoLocationNearAndRestaurantNameContainingIgnoreCase(userLocation, query);
            }
            else {
                restaurants = restaurantRespository.findByGeoLocationNear(userLocation);
            }
        } else {
            if (city != null && !city.isEmpty() && query != null && !query.isEmpty()) {
                restaurants = restaurantRespository.findByCityAndRestaurantNameIgnoreCase(city, query);
            } else if (city != null && !city.isEmpty()) {
                restaurants = restaurantRespository.findByCityIgnoreCase(city);
            }
            else if (query != null && !query.isEmpty()) {
                restaurants = restaurantRespository.findByRestaurantNameContainingIgnoreCase(query);
            }
            else {
                restaurants = restaurantRespository.findAll();
            }
        }
    
        // Map to DTO and calculate distance if lat/lon provided
        List<RestaurantDTO> restaurantDTOs = new ArrayList<>();
        for (Restaurant r : restaurants) {
            RestaurantDTO dto = modelMapper.map(r, RestaurantDTO.class);
            if (latitude != null && longitude != null && r.getGeoLocation() != null) {
                double dist = GeoUtils.calculateDistance(latitude, longitude, r.getGeoLocation().getY(), r.getGeoLocation().getX());
                dto.setDistanceInKm(dist);
            } else {
                dto.setDistanceInKm(null);
            }
            restaurantDTOs.add(dto);
        }
    
        return restaurantDTOs;
    }




    

    @Override
    public List<Restaurant> getAllRestaurants() {
        return restaurantRespository.findAll();


    }


    
}
