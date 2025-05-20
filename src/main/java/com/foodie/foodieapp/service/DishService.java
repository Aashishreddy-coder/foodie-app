package com.foodie.foodieapp.service;

import java.io.IOException;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.foodie.foodieapp.domain.Dish;
import com.foodie.foodieapp.dto.DishDTO;
import com.foodie.foodieapp.dto.DishRequest;

public interface DishService {

 

    List<DishDTO> searchDishesByNameAndRestaurantIds(String name, String city, Double latitude, Double longitude);

    List<DishDTO> getDishesByRestaurant(String restaurantId, Double distanceInKm);

    Dish createDish(DishRequest dishRequest, MultipartFile image) throws IOException;

    List<DishDTO> getAllDishes();

}
