package com.foodie.foodieapp.exceptions;

public class FavouriteNotFoundException extends RuntimeException {
    public FavouriteNotFoundException(String id) {
        super("Favourite not found with ID: " + id);
    }
}
