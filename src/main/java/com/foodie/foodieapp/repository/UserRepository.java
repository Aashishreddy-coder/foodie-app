package com.foodie.foodieapp.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.foodie.foodieapp.domain.AppUser;


public interface UserRepository extends MongoRepository<AppUser, String> {

    Optional<AppUser> findByEmail(String email);
    

    Optional<AppUser> findByEmailAndPassword(String email, String password);

    




}
