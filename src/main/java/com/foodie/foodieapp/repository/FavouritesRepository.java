package com.foodie.foodieapp.repository;

import com.foodie.foodieapp.domain.Favourites;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouritesRepository extends MongoRepository<Favourites, String> {

    boolean existsByUserIdAndDishId(Long userId, String dishId);
    List<Favourites> findAllByUserId(Long userId);
}
