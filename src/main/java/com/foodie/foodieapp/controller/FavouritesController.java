package com.foodie.foodieapp.controller;

import com.foodie.foodieapp.domain.Favourites;
import com.foodie.foodieapp.exceptions.FavouriteAlreadyExistsException;
import com.foodie.foodieapp.exceptions.FavouriteNotFoundException;
import com.foodie.foodieapp.exceptions.InvalidFavouriteDataException;
import com.foodie.foodieapp.exceptions.UserFavouritesNotFoundException;
import com.foodie.foodieapp.service.FavouritesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/favourites")
public class FavouritesController {

    @Autowired
    private FavouritesService favouriteService;

    @PostMapping("/save")
    public ResponseEntity<?> saveDishes(@RequestBody Favourites favourites){
        try {
            favouriteService.saveDishes(favourites);
            return new ResponseEntity<>("Dish saved successfully", HttpStatus.CREATED);
        } catch (InvalidFavouriteDataException | FavouriteAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/get/{userId}")
    public ResponseEntity<?> getAllDishes(@PathVariable Long userId){
        try {
            List<Favourites> dishes = favouriteService.getAllDishes(userId);
            return new ResponseEntity<>(dishes, HttpStatus.OK);
        } catch (UserFavouritesNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDishes(@PathVariable Long id){
        try {
            favouriteService.deleteDishes(id);
            return new ResponseEntity<>("Dish deleted successfully", HttpStatus.OK);
        } catch (FavouriteNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("An unexpected error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
