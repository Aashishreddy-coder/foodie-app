package com.foodie.foodieapp.service;

    import org.springframework.web.multipart.MultipartFile;

import com.foodie.foodieapp.domain.AppUser;
import com.foodie.foodieapp.dto.RegisterRequest;
import com.foodie.foodieapp.exceptions.UserAlreadyExistsException;
import com.foodie.foodieapp.exceptions.UserNotFoundException;
import com.foodie.foodieapp.exceptions.InvalidCredentialsException;
    import java.io.IOException;
public interface UserService {

   AppUser save(RegisterRequest user, MultipartFile image) throws UserAlreadyExistsException , IOException;

    AppUser getUserById(Long id) throws UserNotFoundException;

    AppUser updateUser(AppUser user) throws UserNotFoundException;

    


    void deleteUser(Long id) throws UserNotFoundException;

    AppUser findByEmailAndPassword(String email, String password) throws InvalidCredentialsException;

   



}
