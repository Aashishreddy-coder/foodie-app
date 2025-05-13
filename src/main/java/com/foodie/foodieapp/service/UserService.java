package com.foodie.foodieapp.service;

    import com.foodie.foodieapp.domain.AppUser;
    import com.foodie.foodieapp.exceptions.UserAlreadyExistsException;
    import com.foodie.foodieapp.exceptions.InvalidCredentialsException;
public interface UserService {

   AppUser save(AppUser user) throws UserAlreadyExistsException;

    AppUser getUserById(Long id) ;

    AppUser updateUser(AppUser user) ;

    


    void deleteUser(Long id) ;

    AppUser findByEmailAndPassword(String email, String password) throws InvalidCredentialsException;



}
