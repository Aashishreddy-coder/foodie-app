package com.foodie.foodieapp.exceptions;

public class FavouriteNotFoundException extends RuntimeException {
    public FavouriteNotFoundException(Long id) {
        super("Favourite not found with ID: " + id);
    }
}
