package com.foodie.foodieapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodie.foodieapp.domain.Restaurant;
import com.foodie.foodieapp.dto.LocationDetailsResponse;
import com.foodie.foodieapp.dto.RestaurantDTO;
import com.foodie.foodieapp.repository.RestaurantRespository;

@ExtendWith(MockitoExtension.class)
public class RestaurantServiceTest {

    @Mock
    private RestaurantRespository restaurantRepository;

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private ObjectMapper objectMapper;

    @InjectMocks
    private RestaurantServiceImpl restaurantService;

    private Restaurant testRestaurant;
    private GeoJsonPoint testLocation;
    private String testRestaurantId = "rest123";
    private String testRestaurantName = "Test Restaurant";
    private String testCity = "Test City";

    @BeforeEach
    void setUp() {
        testLocation = new GeoJsonPoint(12.9716, 77.5946); // Example coordinates
        testRestaurant = new Restaurant();
        testRestaurant.setId(testRestaurantId);
        testRestaurant.setRestaurantName(testRestaurantName);
        testRestaurant.setCity(testCity);
        testRestaurant.setGeoLocation(testLocation);
    }

    @Test
    void createRestaurant_ShouldCreateNewRestaurant_WhenValidData() {
        // given
        when(restaurantRepository.findByGeoLocationNear(any(GeoJsonPoint.class)))
            .thenReturn(new ArrayList<>());
        when(restaurantRepository.save(any(Restaurant.class))).thenReturn(testRestaurant);

        // when
        Restaurant result = restaurantService.createRestaurant(testRestaurant);

        // then
        assertNotNull(result);
        assertEquals(testRestaurantName, result.getRestaurantName());
        assertEquals(testCity, result.getCity());
        verify(restaurantRepository).save(any(Restaurant.class));
    }

    @Test
    void createRestaurant_ShouldThrowException_WhenLocationIsNull() {
        // given
        testRestaurant.setGeoLocation(null);

        // when & then
        assertThrows(RuntimeException.class, () -> restaurantService.createRestaurant(testRestaurant));
    }

    @Test
    void createRestaurant_ShouldThrowException_WhenRestaurantExistsNearby() {
        // given
        List<Restaurant> nearbyRestaurants = new ArrayList<>();
        nearbyRestaurants.add(testRestaurant);
        when(restaurantRepository.findByGeoLocationNear(any(GeoJsonPoint.class)))
            .thenReturn(nearbyRestaurants);

        // when & then
        assertThrows(RuntimeException.class, () -> restaurantService.createRestaurant(testRestaurant));
    }

    @Test
    void getRestaurantById_ShouldReturnRestaurant_WhenExists() {
        // given
        when(restaurantRepository.findById(testRestaurantId)).thenReturn(java.util.Optional.of(testRestaurant));

        // when
        Restaurant result = restaurantService.getRestaurantById(testRestaurantId);

        // then
        assertNotNull(result);
        assertEquals(testRestaurantId, result.getId());
        assertEquals(testRestaurantName, result.getRestaurantName());
    }

    @Test
    void getRestaurantById_ShouldReturnNull_WhenNotExists() {
        // given
        when(restaurantRepository.findById(testRestaurantId)).thenReturn(java.util.Optional.empty());

        // when
        Restaurant result = restaurantService.getRestaurantById(testRestaurantId);

        // then
        assertNull(result);
    }

    @Test
    void getRestaurantByName_ShouldReturnMatchingRestaurants() {
        // given
        List<Restaurant> expectedRestaurants = new ArrayList<>();
        expectedRestaurants.add(testRestaurant);
        when(restaurantRepository.findByRestaurantNameContainingIgnoreCase(testRestaurantName))
            .thenReturn(expectedRestaurants);

        // when
        List<Restaurant> results = restaurantService.getRestaurantByName(testRestaurantName);

        // then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testRestaurantName, results.get(0).getRestaurantName());
    }

    @Test
    void getRestaurantsByCity_ShouldReturnMatchingRestaurants() {
        // given
        List<Restaurant> expectedRestaurants = new ArrayList<>();
        expectedRestaurants.add(testRestaurant);
        when(restaurantRepository.findByCityIgnoreCase(testCity))
            .thenReturn(expectedRestaurants);

        // when
        List<Restaurant> results = restaurantService.getRestaurantsByCity(testCity);

        // then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testCity, results.get(0).getCity());
    }

   

    @Test
    void getAllRestaurants_ShouldReturnAllRestaurants() {
        // given
        List<Restaurant> expectedRestaurants = new ArrayList<>();
        expectedRestaurants.add(testRestaurant);
        when(restaurantRepository.findAll()).thenReturn(expectedRestaurants);

        // when
        List<Restaurant> results = restaurantService.getAllRestaurants();

        // then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testRestaurantId, results.get(0).getId());
    }


} 