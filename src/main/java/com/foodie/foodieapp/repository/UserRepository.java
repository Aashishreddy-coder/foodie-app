package com.foodie.foodieapp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodie.foodieapp.domain.AppUser;

public interface UserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByEmail(String email);

    Optional<AppUser> findByEmailAndPassword(String email, String password);

    




}
