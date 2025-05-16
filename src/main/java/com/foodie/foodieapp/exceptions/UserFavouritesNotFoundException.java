package com.foodie.foodieapp.exceptions;

public class UserFavouritesNotFoundException extends RuntimeException {
    public UserFavouritesNotFoundException(Long userId) {
        super("No favourites found for user with ID: " + userId);
    }
}
