package com.foodie.foodieapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
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
import org.modelmapper.ModelMapper;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import com.foodie.foodieapp.domain.Dish;
import com.foodie.foodieapp.domain.Restaurant;
import com.foodie.foodieapp.dto.DishDTO;
import com.foodie.foodieapp.dto.DishRequest;
import com.foodie.foodieapp.repository.DishRepository;

@ExtendWith(MockitoExtension.class)
public class DishServiceTest {

    @Mock
    private DishRepository dishRepository;

    @Mock
    private RestaurantService restaurantService;

    @Mock
    private FileService fileService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DishServiceImpl dishService;

    private Dish testDish;
    private Restaurant testRestaurant;
    private DishDTO testDishDTO;
    private String testDishId = "dish123";
    private String testRestaurantId = "rest123";
    private String testDishName = "Test Dish";
    private String dishImagePath = "dish-images";

    @BeforeEach
    void setUp() {
        // Set the dishpath value using reflection
        ReflectionTestUtils.setField(dishService, "dishpath", dishImagePath);

        // Setup test restaurant
        testRestaurant = new Restaurant();
        testRestaurant.setId(testRestaurantId);
        testRestaurant.setRestaurantName("Test Restaurant");

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
    void createDish_ShouldCreateNewDish_WhenValidData() throws IOException {
        // given
        DishRequest dishRequest = new DishRequest();
        dishRequest.setName(testDishName);
        dishRequest.setRestaurantId(testRestaurantId);
        dishRequest.setPrice("10.99");
        dishRequest.setDescription("Test Description");
        dishRequest.setCuisine("Test Cuisine");

        MultipartFile image = new MockMultipartFile(
            "image", 
            "test.jpg",
            "image/jpeg",
            "test image content".getBytes()
        );

        when(fileService.uploadImage(eq(dishImagePath), any(MultipartFile.class))).thenReturn("test.jpg");
        when(modelMapper.map(dishRequest, Dish.class)).thenReturn(testDish);
        when(dishRepository.save(any(Dish.class))).thenReturn(testDish);

        // when
        Dish result = dishService.createDish(dishRequest, image);

        // then
        assertNotNull(result);
        assertEquals(testDishId, result.getId());
        assertEquals(testDishName, result.getName());
        assertEquals(testRestaurantId, result.getRestaurantId());
        assertEquals("test.jpg", result.getImage());
        verify(fileService).uploadImage(eq(dishImagePath), any(MultipartFile.class));
        verify(modelMapper).map(dishRequest, Dish.class);
        verify(dishRepository).save(any(Dish.class));
    }

    @Test
    void getDishById_ShouldReturnDish_WhenExists() {
        // given
        when(dishRepository.findById(testDishId)).thenReturn(java.util.Optional.of(testDish));
        when(restaurantService.getRestaurantById(testRestaurantId)).thenReturn(testRestaurant);
        when(modelMapper.map(testDish, DishDTO.class)).thenReturn(testDishDTO);

        // when
        DishDTO result = dishService.getDishById(testDishId, 12.9716, 77.5946);

        // then
        assertNotNull(result);
        assertEquals(testDishId, result.getId());
        assertEquals(testDishName, result.getName());
        assertEquals(testRestaurant.getRestaurantName(), result.getRestaurantName());
        verify(modelMapper).map(testDish, DishDTO.class);
    }

    @Test
    void getDishById_ShouldThrowException_WhenNotFound() {
        // given
        when(dishRepository.findById(testDishId)).thenReturn(java.util.Optional.empty());

        // when & then
        assertThrows(RuntimeException.class, () -> dishService.getDishById(testDishId, 12.9716, 77.5946));
    }

    @Test
    void getDishesByRestaurant_ShouldReturnDishes() {
        // given
        List<Dish> dishes = new ArrayList<>();
        dishes.add(testDish);
        when(dishRepository.findByRestaurantId(testRestaurantId)).thenReturn(dishes);
        when(restaurantService.getRestaurantById(testRestaurantId)).thenReturn(testRestaurant);
        when(modelMapper.map(testDish, DishDTO.class)).thenReturn(testDishDTO);

        // when
        List<DishDTO> results = dishService.getDishesByRestaurant(testRestaurantId, 5.0);

        // then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testDishName, results.get(0).getName());
        assertEquals(5.0, results.get(0).getDistanceInKm());
        assertEquals(testRestaurant.getRestaurantName(), results.get(0).getRestaurantName());
        verify(modelMapper).map(testDish, DishDTO.class);
    }

    @Test
    void getAllDishes_ShouldReturnAllDishes() {
        // given
        List<Dish> dishes = new ArrayList<>();
        dishes.add(testDish);
        when(dishRepository.findAll()).thenReturn(dishes);
        when(modelMapper.map(testDish, DishDTO.class)).thenReturn(testDishDTO);

        // when
        List<DishDTO> results = dishService.getAllDishes();

        // then
        assertNotNull(results);
        assertEquals(1, results.size());
        assertEquals(testDishName, results.get(0).getName());
        verify(modelMapper).map(testDish, DishDTO.class);
    }
} 