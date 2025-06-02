package com.foodie.foodieapp.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.foodie.foodieapp.domain.Favourites;

@DataMongoTest
public class FavouritesRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private FavouritesRepository favouritesRepository;

    private Favourites testFavourite;
    private Long userId = 123L;
    private String dishId = "dish123";

    @BeforeEach
    void setUp() {
        testFavourite = new Favourites();
        testFavourite.setUserId(userId);
        testFavourite.setDishId(dishId);
        mongoTemplate.save(testFavourite);
    }

    @Test
    void existsByUserIdAndDishId_ShouldReturnTrue_WhenFavouriteExists() {
        // when
        boolean exists = favouritesRepository.existsByUserIdAndDishId(userId, dishId);

        // then
        assertTrue(exists);
    }

    @Test
    void existsByUserIdAndDishId_ShouldReturnFalse_WhenFavouriteDoesNotExist() {
        // when
        boolean exists = favouritesRepository.existsByUserIdAndDishId(999L, "nonexistentDish");

        // then
        assertFalse(exists);
    }

    @Test
    void findAllByUserId_ShouldReturnFavourites_WhenFavouritesExist() {
        // when
        List<Favourites> found = favouritesRepository.findAllByUserId(userId);

        // then
        assertFalse(found.isEmpty());
        assertEquals(userId, found.get(0).getUserId());
        assertEquals(dishId, found.get(0).getDishId());
    }
} 