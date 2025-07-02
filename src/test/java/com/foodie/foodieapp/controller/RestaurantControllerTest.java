package com.foodie.foodieapp.controller;

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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.foodie.foodieapp.domain.Restaurant;
import com.foodie.foodieapp.dto.LocationDetailsResponse;
import com.foodie.foodieapp.dto.RestaurantDTO;
import com.foodie.foodieapp.service.RestaurantService;

@ExtendWith(MockitoExtension.class)
public class RestaurantControllerTest {

    @Mock
    private RestaurantService restaurantService;

    @InjectMocks
    private RestaurantController restaurantController;

    private Restaurant testRestaurant;
    private RestaurantDTO testRestaurantDTO;
    private String testRestaurantId = "rest123";
    private String testRestaurantName = "Test Restaurant";
    private String testCity = "Test City";

    @BeforeEach
    void setUp() {
        // Setup test restaurant
        testRestaurant = new Restaurant();
        testRestaurant.setId(testRestaurantId);
        testRestaurant.setRestaurantName(testRestaurantName);
        testRestaurant.setCity(testCity);
        testRestaurant.setGeoLocation(new GeoJsonPoint(12.9716, 77.5946));

        // Setup test DTO
        testRestaurantDTO = new RestaurantDTO();
        testRestaurantDTO.setId(testRestaurantId);
        testRestaurantDTO.setRestaurantName(testRestaurantName);
        testRestaurantDTO.setCity(testCity);
        testRestaurantDTO.setGeoLocation(new GeoJsonPoint(12.9716, 77.5946));
        testRestaurantDTO.setDistanceInKm(5.0);
    }

    @Test
    void createRestaurant_ShouldReturnCreatedRestaurant() {
        // given
        when(restaurantService.createRestaurant(any(Restaurant.class))).thenReturn(testRestaurant);

        // when
        ResponseEntity<Restaurant> response = restaurantController.createRestaurant(testRestaurant);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testRestaurantName, response.getBody().getRestaurantName());
        verify(restaurantService).createRestaurant(any(Restaurant.class));
    }

    @Test
    void getRestaurantById_ShouldReturnRestaurant_WhenExists() {
        // given
        when(restaurantService.getRestaurantById(testRestaurantId)).thenReturn(testRestaurant);

        // when
        ResponseEntity<Restaurant> response = restaurantController.getRestaurantById(testRestaurantId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testRestaurantName, response.getBody().getRestaurantName());
    }

    @Test
    void getRestaurantById_ShouldReturnNotFound_WhenNotExists() {
        // given
        when(restaurantService.getRestaurantById(testRestaurantId)).thenReturn(null);

        // when
        ResponseEntity<Restaurant> response = restaurantController.getRestaurantById(testRestaurantId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void getRestaurantsByLocation_ShouldReturnMatchingRestaurants() {
        // given
        List<RestaurantDTO> expectedRestaurants = new ArrayList<>();
        expectedRestaurants.add(testRestaurantDTO);
        when(restaurantService.getRestaurantsByLocation(any(), any(), any(), any()))
            .thenReturn(expectedRestaurants);

        // when
        ResponseEntity<List<RestaurantDTO>> response = restaurantController.getRestaurantsByLocation(
            12.9716, 77.5946, testCity, testRestaurantName);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testRestaurantName, response.getBody().get(0).getRestaurantName());
    }

    @Test
    void getRestaurantsByCity_ShouldReturnCityRestaurants() {
        // given
        List<Restaurant> expectedRestaurants = new ArrayList<>();
        expectedRestaurants.add(testRestaurant);
        when(restaurantService.getRestaurantsByCity(testCity)).thenReturn(expectedRestaurants);

        // when
        ResponseEntity<List<Restaurant>> response = restaurantController.getRestaurantsByCity(testCity);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testRestaurantName, response.getBody().get(0).getRestaurantName());
    }

    @Test
    void getRestaurantsByName_ShouldReturnMatchingRestaurants() {
        // given
        List<Restaurant> expectedRestaurants = new ArrayList<>();
        expectedRestaurants.add(testRestaurant);
        when(restaurantService.getRestaurantByName(testRestaurantName)).thenReturn(expectedRestaurants);

        // when
        ResponseEntity<List<Restaurant>> response = restaurantController.getRestaurantsByName(testRestaurantName);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testRestaurantName, response.getBody().get(0).getRestaurantName());
    }

    @Test
    void getAllRestaurants_ShouldReturnAllRestaurants() {
        // given
        List<Restaurant> expectedRestaurants = new ArrayList<>();
        expectedRestaurants.add(testRestaurant);
        when(restaurantService.getAllRestaurants()).thenReturn(expectedRestaurants);

        // when
        ResponseEntity<List<Restaurant>> response = restaurantController.getAllRestaurants();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testRestaurantName, response.getBody().get(0).getRestaurantName());
    }

    @Test
    void getDistanceToRestaurant_ShouldReturnDistance_WhenRestaurantExists() {
        // given
        LocationDetailsResponse distanceResponse = new LocationDetailsResponse();
        distanceResponse.setDistanceInMeters(5000.0);
        distanceResponse.setAddress("123 Test St");
        distanceResponse.setTimeInMinutes(10.0);

        when(restaurantService.getRestaurantById(testRestaurantId)).thenReturn(testRestaurant);
        when(restaurantService.calculateDistanceToRestaurant(any(), any(), any()))
            .thenReturn(distanceResponse);

        // when
        ResponseEntity<?> response = restaurantController.getDistanceToRestaurant(
            testRestaurantId, 12.9716, 77.5946);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof LocationDetailsResponse);
        LocationDetailsResponse responseBody = (LocationDetailsResponse) response.getBody();
        assertEquals(5000.0, responseBody.getDistanceInMeters());
        assertEquals("123 Test St", responseBody.getAddress());
        assertEquals(10.0, responseBody.getTimeInMinutes());
    }

    @Test
    void getDistanceToRestaurant_ShouldReturnNotFound_WhenRestaurantNotExists() {
        // given
        when(restaurantService.getRestaurantById(testRestaurantId)).thenReturn(null);

        // when
        ResponseEntity<?> response = restaurantController.getDistanceToRestaurant(
            testRestaurantId, 12.9716, 77.5946);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Restaurant not found", response.getBody());
    }

    @Test
    void getDistanceToRestaurant_ShouldReturnError_WhenCalculationFails() {
        // given
        when(restaurantService.getRestaurantById(testRestaurantId)).thenReturn(testRestaurant);
        when(restaurantService.calculateDistanceToRestaurant(any(), any(), any()))
            .thenReturn(null);

        // when
        ResponseEntity<?> response = restaurantController.getDistanceToRestaurant(
            testRestaurantId, 12.9716, 77.5946);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Could not calculate distance", response.getBody());
    }
} 