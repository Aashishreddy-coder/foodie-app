package com.foodie.foodieapp.exceptions;

public class FavouriteAlreadyExistsException extends RuntimeException {
    public FavouriteAlreadyExistsException(String userEmail, String dishId) {
        super("Favourite already exists for userEmail: " + userEmail + " and for dishId: " + dishId);
    }
}
