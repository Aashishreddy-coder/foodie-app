package com.foodie.foodieapp.service;

import com.foodie.foodieapp.domain.Favourites;

import java.util.List;

public interface FavouritesService {
    Favourites saveDishes(Favourites favourites);

    void deleteDishes(String id);

    List<Favourites> getAllDishes(Long userId);
}
