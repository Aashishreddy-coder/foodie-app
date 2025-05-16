package com.foodie.foodieapp.service;

import com.foodie.foodieapp.domain.Favourites;
import com.foodie.foodieapp.repository.FavouritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavouritesServiceImpl implements FavouritesService {

    @Autowired
    private FavouritesRepository favouriteRepository;

    @Override
    public Favourites saveDishes(Favourites favourites) {
        if(!favouriteRepository.existsByUserIdAndDishId(favourites.getUserId(), favourites.getDishId())){
            return favouriteRepository.save(favourites);
        }
        return null;
    }

    @Override
    public void deleteDishes(Long id) {
        favouriteRepository.deleteById(id);
    }

    @Override
    public List<Favourites> getAllDishes(Long userId) {
        return favouriteRepository.findAllByUserId(userId);
    }
}
