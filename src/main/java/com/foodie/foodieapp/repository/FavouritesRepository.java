package com.foodie.foodieapp.repository;

import com.foodie.foodieapp.domain.Favourites;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouritesRepository extends MongoRepository<Favourites, String> {

    boolean existsByUserEmailAndDishId(String userEmail, String dishId);
    List<Favourites> findAllByUserEmail(String userEmail);
}
