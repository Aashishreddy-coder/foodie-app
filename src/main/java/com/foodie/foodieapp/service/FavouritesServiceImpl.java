package com.foodie.foodieapp.service;

import com.foodie.foodieapp.domain.Favourites;
import com.foodie.foodieapp.exceptions.FavouriteAlreadyExistsException;
import com.foodie.foodieapp.exceptions.FavouriteNotFoundException;
import com.foodie.foodieapp.exceptions.InvalidFavouriteDataException;
import com.foodie.foodieapp.exceptions.UserFavouritesNotFoundException;
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
        if(favourites.getUserId() == null){
            throw new InvalidFavouriteDataException("User ID must not be null");
        }

        if(favourites.getDishId() == null){
            throw new InvalidFavouriteDataException("Dish ID must not be null");
        }

        if(favouriteRepository.existsByUserIdAndDishId(favourites.getUserId(), favourites.getDishId())){
            throw new FavouriteAlreadyExistsException(favourites.getUserId(), favourites.getDishId());
        }
        return favouriteRepository.save(favourites);
    }

    @Override
    public List<Favourites> getAllDishes(Long userId) {
        List<Favourites> favouritesList = favouriteRepository.findAllByUserId(userId);

        if(favouritesList.isEmpty()){
            throw new UserFavouritesNotFoundException(userId);
        }
        return favouritesList;
    }

    @Override
    public void deleteDishes(String id) {
        if (!favouriteRepository.existsById(id)) {
            throw new FavouriteNotFoundException(id);
        }
        favouriteRepository.deleteById(id);
    }
}
