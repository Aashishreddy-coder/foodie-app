package com.foodie.foodieapp.controller;

import com.foodie.foodieapp.domain.Favourites;
import com.foodie.foodieapp.service.FavouritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/favourites")
public class FavouritesController {

    @Autowired
    private FavouritesService favouriteService;

    @PostMapping("/save")
    public ResponseEntity<?> saveDishes(@RequestBody Favourites favourites){
        favouriteService.saveDishes(favourites);
        return new ResponseEntity<>("Dish saved successfully",HttpStatus.CREATED);
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getAllDishes(@PathVariable Long userId){
        return new ResponseEntity<>(favouriteService.getAllDishes(userId), HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDishes(@PathVariable String id){
        favouriteService.deleteDishes(id);
        return new ResponseEntity<>("Dish deleted successfully",HttpStatus.OK);
    }

    
}
