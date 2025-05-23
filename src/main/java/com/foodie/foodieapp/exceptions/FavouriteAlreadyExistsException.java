package com.foodie.foodieapp.exceptions;

public class FavouriteAlreadyExistsException extends RuntimeException {
    public FavouriteAlreadyExistsException(Long userId, String dishId) {
        super("Favourite already exists for userId: " + userId + " and for dishId: " + dishId);
    }
}
