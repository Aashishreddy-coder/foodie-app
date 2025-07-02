package com.foodie.foodieapp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodie.foodieapp.domain.Dish;
import com.foodie.foodieapp.dto.DishDTO;
import com.foodie.foodieapp.dto.DishRequest;
import com.foodie.foodieapp.service.DishService;

@ExtendWith(MockitoExtension.class)
public class DishControllerTest {

    @Mock
    private DishService dishService;

    @InjectMocks
    private DishController dishController;

    private ObjectMapper objectMapper;
    private Dish testDish;
    private DishDTO testDishDTO;
    private String testDishId = "dish123";
    private String testRestaurantId = "rest123";
    private String testDishName = "Test Dish";

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();

        // Setup test dish
        testDish = new Dish();
        testDish.setId(testDishId);
        testDish.setName(testDishName);
        testDish.setRestaurantId(testRestaurantId);
        testDish.setPrice("10.99");
        testDish.setDescription("Test Description");
        testDish.setCuisine("Test Cuisine");

        // Setup test DTO
        testDishDTO = new DishDTO();
        testDishDTO.setId(testDishId);
        testDishDTO.setName(testDishName);
        testDishDTO.setRestaurantId(testRestaurantId);
        testDishDTO.setPrice("10.99");
        testDishDTO.setDescription("Test Description");
        testDishDTO.setCuisine("Test Cuisine");
    }

    @Test
    void createDish_ShouldReturnCreatedDish_WhenValidData() throws IOException {
        // given
        DishRequest dishRequest = new DishRequest();
        dishRequest.setName(testDishName);
        dishRequest.setRestaurantId(testRestaurantId);
        dishRequest.setPrice("10.99");
        dishRequest.setDescription("Test Description");
        dishRequest.setCuisine("Test Cuisine");

        String dishJson = objectMapper.writeValueAsString(dishRequest);
        MockMultipartFile image = new MockMultipartFile(
            "image", 
            "test.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );

        when(dishService.createDish(any(DishRequest.class), any())).thenReturn(testDish);

        // when
        ResponseEntity<?> response = dishController.createDish(dishJson, image);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testDish, response.getBody());
        verify(dishService).createDish(any(DishRequest.class), any());
    }

    @Test
    void createDish_ShouldReturnError_WhenExceptionOccurs() throws IOException {
        // given
        DishRequest dishRequest = new DishRequest();
        String dishJson = objectMapper.writeValueAsString(dishRequest);
        MockMultipartFile image = new MockMultipartFile(
            "image", 
            "test.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );

        when(dishService.createDish(any(DishRequest.class), any()))
            .thenThrow(new RuntimeException("Test error"));

        // when
        ResponseEntity<?> response = dishController.createDish(dishJson, image);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Test error", response.getBody());
    }

    @Test
    void searchDishes_ShouldReturnMatchingDishes() {
        // given
        List<DishDTO> expectedDishes = new ArrayList<>();
        expectedDishes.add(testDishDTO);
        when(dishService.searchDishesByNameAndRestaurantIds(any(), any(), any(), any()))
            .thenReturn(expectedDishes);

        // when
        ResponseEntity<List<DishDTO>> response = dishController.searchDishes(
            "test", "city", 12.9716, 77.5946);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testDishName, response.getBody().get(0).getName());
    }

    @Test
    void getDishesByRestaurant_ShouldReturnRestaurantDishes() {
        // given
        List<DishDTO> expectedDishes = new ArrayList<>();
        expectedDishes.add(testDishDTO);
        when(dishService.getDishesByRestaurant(any(), any())).thenReturn(expectedDishes);

        // when
        ResponseEntity<List<DishDTO>> response = dishController.getDishesByRestaurant(
            testRestaurantId, 5.0);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testDishName, response.getBody().get(0).getName());
    }

    @Test
    void getAllDishes_ShouldReturnAllDishes() {
        // given
        List<DishDTO> expectedDishes = new ArrayList<>();
        expectedDishes.add(testDishDTO);
        when(dishService.getAllDishes()).thenReturn(expectedDishes);

        // when
        ResponseEntity<List<DishDTO>> response = dishController.getAllDishes();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testDishName, response.getBody().get(0).getName());
    }

    @Test
    void getDishById_ShouldReturnDish_WhenExists() {
        // given
        when(dishService.getDishById(any(), any(), any())).thenReturn(testDishDTO);

        // when
        ResponseEntity<DishDTO> response = dishController.getDishById(
            testDishId, 12.9716, 77.5946);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testDishName, response.getBody().getName());
    }
} 