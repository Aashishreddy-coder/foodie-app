package com.foodie.foodieapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodie.foodieapp.domain.AppUser;

import com.foodie.foodieapp.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public AppUser save(AppUser user)  {
      
        
    
        return userRepository.save(user);
    }

    @Override
    public AppUser getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    @Override
    public AppUser updateUser(AppUser user){

       

        return userRepository.save(user);
      
    }

    @Override
    public void deleteUser(Long id)  {

      

        userRepository.deleteById(id);
      
    }

    @Override
    public AppUser findByEmailAndPassword(String email, String password)  {
        return userRepository.findByEmailAndPassword(email, password).orElse(null);
    }

}   
