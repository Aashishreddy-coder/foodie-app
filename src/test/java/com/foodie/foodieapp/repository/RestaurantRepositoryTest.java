package com.foodie.foodieapp.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;

import com.foodie.foodieapp.domain.Restaurant;

@DataMongoTest
public class RestaurantRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private RestaurantRespository restaurantRepository;

    private Restaurant testRestaurant;
    private String city = "TestCity";
    private GeoJsonPoint location;

    @BeforeEach
    void setUp() {
        testRestaurant = new Restaurant();
        testRestaurant.setRestaurantName("Test Restaurant");
        testRestaurant.setCity(city);
        location = new GeoJsonPoint(12.9716, 77.5946); // Example coordinates
        testRestaurant.setGeoLocation(location);
        mongoTemplate.save(testRestaurant);
    }

    @Test
    void findByCityIgnoreCase_ShouldReturnRestaurants_WhenRestaurantsExist() {
        // when
        List<Restaurant> found = restaurantRepository.findByCityIgnoreCase(city);

        // then
        assertFalse(found.isEmpty());
        assertEquals(city, found.get(0).getCity());
    }

    @Test
    void findByRestaurantNameContainingIgnoreCase_ShouldReturnRestaurants_WhenMatches() {
        // when
        List<Restaurant> found = restaurantRepository.findByRestaurantNameContainingIgnoreCase("Test");

        // then
        assertFalse(found.isEmpty());
        assertEquals("Test Restaurant", found.get(0).getRestaurantName());
    }

    @Test
    void findByGeoLocationNear_ShouldReturnRestaurants_WhenRestaurantsExist() {
        // given
        GeoJsonPoint nearbyLocation = new GeoJsonPoint(12.9717, 77.5947); // Slightly different coordinates

        // when
        List<Restaurant> found = restaurantRepository.findByGeoLocationNear(nearbyLocation);

        // then
        assertFalse(found.isEmpty());
        assertEquals(location, found.get(0).getGeoLocation());
    }
} 