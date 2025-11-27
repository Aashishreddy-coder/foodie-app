package com.foodie.foodieapp.controller;

import com.foodie.foodieapp.domain.Favourites;
import com.foodie.foodieapp.exceptions.FavouriteAlreadyExistsException;
import com.foodie.foodieapp.exceptions.FavouriteNotFoundException;
import com.foodie.foodieapp.exceptions.InvalidFavouriteDataException;
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
    public ResponseEntity<?> saveDishes(@RequestBody Favourites favourites) throws InvalidFavouriteDataException ,FavouriteAlreadyExistsException{
        
        try{
            favouriteService.saveDishes(favourites);
            return new ResponseEntity<>("Dish saved successfully",HttpStatus.CREATED);
        }catch(InvalidFavouriteDataException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }catch(FavouriteAlreadyExistsException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        }


        

        

    }















    @GetMapping("/getall")
    public ResponseEntity<?> getAllDishes() throws FavouriteNotFoundException{
        try{
            return new ResponseEntity<>(favouriteService.getAllDishes(), HttpStatus.OK);
        }catch(FavouriteNotFoundException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteDishes(@PathVariable String id) {
        favouriteService.deleteDishes(id);
        return new ResponseEntity<>("Dish deleted successfully",HttpStatus.OK);
    }

    
}
