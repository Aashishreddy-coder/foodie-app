package com.foodie.foodieapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.foodie.foodieapp.domain.Favourites;
import com.foodie.foodieapp.exceptions.FavouriteAlreadyExistsException;
import com.foodie.foodieapp.exceptions.FavouriteNotFoundException;
import com.foodie.foodieapp.exceptions.InvalidFavouriteDataException;
import com.foodie.foodieapp.repository.FavouritesRepository;

@ExtendWith(MockitoExtension.class)
public class FavouritesServiceTest {

    @Mock
    private FavouritesRepository favouriteRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private FavouritesServiceImpl favouritesService;

    private Favourites testFavourite;
    private String userEmail = "test@test.com";
    private String dishId = "dish123";

    @BeforeEach
    void setUp() {
        testFavourite = new Favourites();
        testFavourite.setDishId(dishId);
    }

    private void setupSecurityContext() {
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    void saveDishes_ShouldSaveFavourite_WhenValidData() {
        // given
        setupSecurityContext();
        when(favouriteRepository.existsByUserEmailAndDishId(userEmail, dishId)).thenReturn(false);
        when(favouriteRepository.save(any(Favourites.class))).thenReturn(testFavourite);

        // when
        Favourites saved = favouritesService.saveDishes(testFavourite);

        // then
        assertNotNull(saved);
        assertEquals(dishId, saved.getDishId());
        verify(favouriteRepository).save(any(Favourites.class));
    }

    @Test
    void saveDishes_ShouldThrowException_WhenFavouriteAlreadyExists() {
        // given
        setupSecurityContext();
        when(favouriteRepository.existsByUserEmailAndDishId(userEmail, dishId)).thenReturn(true);

        // when & then
        assertThrows(FavouriteAlreadyExistsException.class, () -> {
            favouritesService.saveDishes(testFavourite);
        });
    }

    @Test
    void getAllDishes_ShouldReturnFavourites_WhenFavouritesExist() {
        // given
        setupSecurityContext();
        List<Favourites> favouritesList = new ArrayList<>();
        favouritesList.add(testFavourite);
        when(favouriteRepository.findAllByUserEmail(userEmail)).thenReturn(favouritesList);

        // when
        List<Favourites> found = favouritesService.getAllDishes();

        // then
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(dishId, found.get(0).getDishId());
    }

    @Test
    void getAllDishes_ShouldThrowException_WhenNoFavouritesExist() {
        // given
        setupSecurityContext();
        when(favouriteRepository.findAllByUserEmail(userEmail)).thenReturn(new ArrayList<>());

        // when & then
        assertThrows(FavouriteNotFoundException.class, () -> {
            favouritesService.getAllDishes();
        });
    }

    @Test
    void deleteDishes_ShouldDeleteFavourite_WhenFavouriteExists() {
        // given
        when(favouriteRepository.existsById(dishId)).thenReturn(true);

        // when
        favouritesService.deleteDishes(dishId);

        // then
        verify(favouriteRepository).deleteById(dishId);
    }

    @Test
    void deleteDishes_ShouldThrowException_WhenFavouriteDoesNotExist() {
        // given
        when(favouriteRepository.existsById(dishId)).thenReturn(false);

        // when & then
        assertThrows(FavouriteNotFoundException.class, () -> {
            favouritesService.deleteDishes(dishId);
        });
    }
} 