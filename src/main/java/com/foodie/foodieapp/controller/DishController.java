package com.foodie.foodieapp.controller;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodie.foodieapp.domain.Dish;
import com.foodie.foodieapp.domain.Restaurant;
import com.foodie.foodieapp.dto.DishDTO;
import com.foodie.foodieapp.dto.DishRequest;
import com.foodie.foodieapp.service.DishService;
import com.foodie.foodieapp.service.RestaurantService;



@RestController
@RequestMapping("/dishes")
public class DishController {

    @Autowired
    private DishService dishService;

    @Autowired
    private RestaurantService restaurantService;


    @PostMapping("/create")
    public ResponseEntity<?> createDish(
            @RequestPart("dish") String dishJson,
            @RequestPart("image") MultipartFile image) throws IOException {
        
        ObjectMapper objectMapper = new ObjectMapper();
        DishRequest dishRequest = objectMapper.readValue(dishJson, DishRequest.class);
        
        try {
            Dish createdDish = dishService.createDish(dishRequest, image);
            return new ResponseEntity<>(createdDish, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

   

    @GetMapping("/search")
    public ResponseEntity<List<DishDTO>> searchDishes(@RequestParam(required = false) String name , @RequestParam(required = false) String city, @RequestParam(required = false) Double latitude, @RequestParam(required = false) Double longitude) {

      

            List<DishDTO> dishes = dishService.searchDishesByNameAndRestaurantIds(name, city, latitude, longitude);

        
           

            return new ResponseEntity<>(dishes, HttpStatus.OK);


       
    }

    @GetMapping("/restaurant/{restaurantId}")
    public ResponseEntity<List<DishDTO>> getDishesByRestaurant(@PathVariable String restaurantId, @RequestParam(required = false) Double distanceInKm) {
        List<DishDTO> dishes = dishService.getDishesByRestaurant(restaurantId, distanceInKm);
        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<DishDTO>> getAllDishes() {
        List<DishDTO> dishes = dishService.getAllDishes();
        return new ResponseEntity<>(dishes, HttpStatus.OK);
    }

   










    

    
    






  


    
}
