package com.foodie.foodieapp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.foodie.foodieapp.domain.Favourites;
import com.foodie.foodieapp.exceptions.FavouriteAlreadyExistsException;
import com.foodie.foodieapp.exceptions.FavouriteNotFoundException;
import com.foodie.foodieapp.exceptions.InvalidFavouriteDataException;
import com.foodie.foodieapp.service.FavouritesService;

@ExtendWith(MockitoExtension.class)
public class FavouritesControllerTest {

    @Mock
    private FavouritesService favouriteService;

    @InjectMocks
    private FavouritesController favouritesController;

    private Favourites testFavourites;
    private String testFavouriteId = "fav123";
    private String testDishId = "dish123";
    private String testUserEmail = "test@example.com";

    @BeforeEach
    void setUp() {
        // Setup test favourites
        testFavourites = new Favourites();
        testFavourites.setId(testFavouriteId);
        testFavourites.setDishId(testDishId);
        testFavourites.setUserEmail(testUserEmail);
    }



    @Test
    void saveDishes_ShouldReturnBadRequest_WhenInvalidData() throws Exception {
        // given
        doThrow(new InvalidFavouriteDataException("Invalid data"))
            .when(favouriteService).saveDishes(any(Favourites.class));

        // when
        ResponseEntity<?> response = favouritesController.saveDishes(testFavourites);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid data", response.getBody());
    }

    @Test
    void saveDishes_ShouldReturnConflict_WhenAlreadyExists() throws Exception {
        // given
        doThrow(new FavouriteAlreadyExistsException(testUserEmail, testDishId))
            .when(favouriteService).saveDishes(any(Favourites.class));

        // when
        ResponseEntity<?> response = favouritesController.saveDishes(testFavourites);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        assertEquals("Favourite already exists for userEmail: " + testUserEmail + " and for dishId: " + testDishId, 
            response.getBody());
    }

    @Test
    void getAllDishes_ShouldReturnDishes_WhenExists() throws Exception {
        // given
        List<Favourites> expectedFavourites = new ArrayList<>();
        expectedFavourites.add(testFavourites);
        when(favouriteService.getAllDishes()).thenReturn(expectedFavourites);

        // when
        ResponseEntity<?> response = favouritesController.getAllDishes();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody() instanceof List);
        List<?> responseList = (List<?>) response.getBody();
        assertEquals(1, responseList.size());
        assertEquals(testFavourites, responseList.get(0));
    }

  

    @Test
    void deleteDishes_ShouldReturnSuccess() {
        // given
        doNothing().when(favouriteService).deleteDishes(any());

        // when
        ResponseEntity<?> response = favouritesController.deleteDishes(testFavouriteId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Dish deleted successfully", response.getBody());
        verify(favouriteService).deleteDishes(testFavouriteId);
    }
} 