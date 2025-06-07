package com.foodie.foodieapp.exceptions;

public class FavouriteNotFoundException extends RuntimeException {
    public FavouriteNotFoundException(String userEmail) {
        super("Favourite not found with userEmail: " + userEmail);
    }
}
