package com.foodie.foodieapp.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.foodie.foodieapp.domain.Dish;

@Repository
public interface DishRepository extends MongoRepository<Dish, String> {



    List<Dish> findByNameContainingIgnoreCaseAndRestaurantIdIn(String name, List<String> restaurantIds);

    List<Dish> findByRestaurantId(String restaurantId);

}
