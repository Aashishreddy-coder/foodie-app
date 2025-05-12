package com.foodie.foodieapp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.foodie.foodieapp.domain.User;

public interface UserRepository extends JpaRepository<User, Long> {
    

}
