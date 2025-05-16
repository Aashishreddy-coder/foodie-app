package com.foodie.foodieapp.repository;

import com.foodie.foodieapp.domain.Favourites;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavouritesRepository extends MongoRepository<Favourites, Long> {

    boolean existsByUserIdAndDishId(Long userId, Long dishId);
    List<Favourites> findAllByUserId(Long userId);
}
