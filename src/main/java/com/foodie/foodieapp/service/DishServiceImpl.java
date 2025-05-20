package com.foodie.foodieapp.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.geo.GeoJsonPoint;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.foodie.foodieapp.domain.Dish;
import com.foodie.foodieapp.domain.Restaurant;
import com.foodie.foodieapp.dto.DishDTO;
import com.foodie.foodieapp.dto.DishRequest;
import com.foodie.foodieapp.dto.RestaurantDTO;
import com.foodie.foodieapp.repository.DishRepository;
import com.foodie.foodieapp.utils.GeoUtils;



@Service
public class DishServiceImpl implements DishService {

    @Autowired
    private DishRepository dishRepository;

    @Autowired
    private RestaurantService restaurantService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private FileService fileService;


    @Value("${project.dishimage}")
    private String dishpath;

  

    @Override
    public List<DishDTO> searchDishesByNameAndRestaurantIds(String name, String city, Double latitude, Double longitude) {
        
        List<RestaurantDTO> restaurants = restaurantService.getRestaurantsByLocation(latitude, longitude, city, null);
        
        if (restaurants.isEmpty()) {
            return new ArrayList<>();
        }
        
        Map<String, RestaurantDTO> restaurantMap = new HashMap<>();
        List<String> restaurantIds = new ArrayList<>();
        
        for (RestaurantDTO restaurant : restaurants) {
            restaurantMap.put(restaurant.getId(), restaurant);
            restaurantIds.add(restaurant.getId());
        }
        
        List<Dish> dishes;
        if (name != null && !name.trim().isEmpty()) {
            dishes = dishRepository.findByNameContainingIgnoreCaseAndRestaurantIdIn(name, restaurantIds);
        } else {
            // If name is null or empty, get all dishes for the restaurants
            dishes = new ArrayList<>();
            for (String restaurantId : restaurantIds) {
                dishes.addAll(dishRepository.findByRestaurantId(restaurantId));
            }
        }
        
        List<DishDTO> dishDTOs = new ArrayList<>();
        
        for (Dish dish : dishes) {
            DishDTO dto = modelMapper.map(dish, DishDTO.class);
            
            RestaurantDTO restDto = restaurantMap.get(dish.getRestaurantId());
            if (restDto != null) {
                dto.setRestaurantName(restDto.getRestaurantName());
                dto.setDistanceInKm(restDto.getDistanceInKm());
            } else {
                dto.setRestaurantName(null);
                dto.setDistanceInKm(null);
            }
            
            dishDTOs.add(dto);
        }
        
        // Sort dishes based on their restaurant's distance
        dishDTOs.sort((d1, d2) -> {
            Double dist1 = d1.getDistanceInKm();
            Double dist2 = d2.getDistanceInKm();
            if (dist1 == null && dist2 == null) return 0;
            if (dist1 == null) return 1;
            if (dist2 == null) return -1;
            return dist1.compareTo(dist2);
        });
        
        return dishDTOs;
    }

    public List<DishDTO> getDishesByRestaurant(String restaurantId, Double distanceInKm) {
        List<Dish> dishes = dishRepository.findByRestaurantId(restaurantId);
    
        List<DishDTO> dishDTOs = new ArrayList<>();
        for (Dish dish : dishes) {
            DishDTO dto = modelMapper.map(dish, DishDTO.class);
            dto.setDistanceInKm(distanceInKm);  
            dto.setRestaurantName(restaurantService.getRestaurantById(restaurantId).getRestaurantName());         
            dishDTOs.add(dto);
        }
    
        return dishDTOs;
    }

    @Override
    public Dish createDish(DishRequest dishRequest, MultipartFile image) throws IOException {


        String fileName=fileService.uploadImage(dishpath,image);


        Dish dish=modelMapper.map(dishRequest,Dish.class);

        dish.setImage(fileName);

        return dishRepository.save(dish);
        
       
    }

    @Override
    public List<DishDTO> getAllDishes() {
        List<Dish> dishes = dishRepository.findAll();
        List<DishDTO> dishDTOs = new ArrayList<>();
        for (Dish dish : dishes) {
            DishDTO dto = modelMapper.map(dish, DishDTO.class);
            dishDTOs.add(dto);
        }
        return dishDTOs;
    }


    
    

   




    


    

}

  
    
