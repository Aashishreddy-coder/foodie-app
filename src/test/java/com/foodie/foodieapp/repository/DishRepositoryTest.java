package com.foodie.foodieapp.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.foodie.foodieapp.domain.Dish;

@DataMongoTest
public class DishRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DishRepository dishRepository;

    private Dish testDish;
    private String restaurantId = "rest123";

    @BeforeEach
    void setUp() {
        testDish = new Dish();
        testDish.setName("Test Dish");
        testDish.setRestaurantId(restaurantId);
        testDish.setPrice("15.99");
        mongoTemplate.save(testDish);
    }

    @Test
    void findByRestaurantId_ShouldReturnDishes_WhenDishesExist() {
        // when
        List<Dish> found = dishRepository.findByRestaurantId(restaurantId);

        // then
        assertFalse(found.isEmpty());
        assertEquals(restaurantId, found.get(0).getRestaurantId());
    }

    @Test
    void findByNameContainingIgnoreCaseAndRestaurantIdIn_ShouldReturnDishes_WhenMatches() {
        // given
        List<String> restaurantIds = Arrays.asList(restaurantId);

        // when
        List<Dish> found = dishRepository.findByNameContainingIgnoreCaseAndRestaurantIdIn(
            "Test", restaurantIds);

        // then
        assertFalse(found.isEmpty());
        assertEquals("Test Dish", found.get(0).getName());
        assertEquals(restaurantId, found.get(0).getRestaurantId());
    }

    @Test
    void findByRestaurantId_ShouldReturnEmptyList_WhenNoDishesExist() {
        // when
        List<Dish> found = dishRepository.findByRestaurantId("nonexistentRestaurant");

        // then
        assertTrue(found.isEmpty());
    }
} 