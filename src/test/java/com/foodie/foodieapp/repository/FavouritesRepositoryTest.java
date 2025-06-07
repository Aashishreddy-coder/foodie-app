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
    private String userEmail = "test@test.com";
    private String dishId = "dish123";

    @BeforeEach
    void setUp() {
        testFavourite = new Favourites();
        testFavourite.setUserEmail(userEmail);
        testFavourite.setDishId(dishId);
        mongoTemplate.save(testFavourite);
    }

    @Test
    void existsByUserIdAndDishId_ShouldReturnTrue_WhenFavouriteExists() {
        // when
        boolean exists = favouritesRepository.existsByUserEmailAndDishId(userEmail, dishId);

        // then
        assertTrue(exists);
    }

    @Test
    void existsByUserIdAndDishId_ShouldReturnFalse_WhenFavouriteDoesNotExist() {
        // when
        boolean exists = favouritesRepository.existsByUserEmailAndDishId("nonexistent@test.com", "nonexistentDish");

        // then
        assertFalse(exists);
    }

    @Test
    void findAllByUserId_ShouldReturnFavourites_WhenFavouritesExist() {
        // when
        List<Favourites> found = favouritesRepository.findAllByUserEmail(userEmail);

        // then
        assertFalse(found.isEmpty());
        assertEquals(userEmail, found.get(0).getUserEmail());
        assertEquals(dishId, found.get(0).getDishId());
    }
} 