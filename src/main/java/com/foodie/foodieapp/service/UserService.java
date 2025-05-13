package com.foodie.foodieapp.service;

import com.foodie.foodieapp.domain.AppUser;


public interface UserService {

   AppUser save(AppUser user) ;

    AppUser getUserById(Long id) ;

    AppUser updateUser(AppUser user) ;

    


    void deleteUser(Long id) ;

    AppUser findByEmailAndPassword(String email, String password) ;



}
