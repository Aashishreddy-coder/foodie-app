package com.foodie.foodieapp.service;

import com.foodie.foodieapp.domain.Favourites;
import com.foodie.foodieapp.exceptions.FavouriteAlreadyExistsException;
import com.foodie.foodieapp.exceptions.FavouriteNotFoundException;
import com.foodie.foodieapp.exceptions.InvalidFavouriteDataException;
import com.foodie.foodieapp.exceptions.UserFavouritesNotFoundException;
import com.foodie.foodieapp.repository.FavouritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavouritesServiceImpl implements FavouritesService {

    @Autowired
    private FavouritesRepository favouriteRepository;










    @Override
    public Favourites saveDishes(Favourites favourites) {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        if(userEmail == null){
            throw new InvalidFavouriteDataException("User Email must not be null");
        }

        if(favourites.getDishId() == null){
            throw new InvalidFavouriteDataException("Dish ID must not be null");
        }

        if(favouriteRepository.existsByUserEmailAndDishId(userEmail, favourites.getDishId())){
            throw new FavouriteAlreadyExistsException(userEmail, favourites.getDishId());
        }
        favourites.setUserEmail(userEmail);
        return favouriteRepository.save(favourites);

    }

    @Override
    public List<Favourites> getAllDishes() {
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        List<Favourites> favouritesList = favouriteRepository.findAllByUserEmail(userEmail);


        

        if(favouritesList.isEmpty()){
            throw new FavouriteNotFoundException(userEmail);
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
