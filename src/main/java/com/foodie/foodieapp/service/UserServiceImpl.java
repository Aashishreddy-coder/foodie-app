package com.foodie.foodieapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.foodie.foodieapp.domain.AppUser;
import com.foodie.foodieapp.exceptions.InvalidCredentialsException;
import com.foodie.foodieapp.exceptions.UserAlreadyExistsException;
import com.foodie.foodieapp.repository.UserRepository;
import com.foodie.foodieapp.exceptions.UserNotFoundException;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public AppUser save(AppUser user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new UserAlreadyExistsException(user.getEmail());
        }
        return userRepository.save(user);
    }

    @Override
    public AppUser getUserById(Long id) {
        return userRepository.findById(id)
            .orElseThrow(() -> new UserNotFoundException("User not found with id: " + id));
    }

    @Override
    public AppUser updateUser(AppUser user) {
        if (!userRepository.existsById(user.getId())) {
            throw new UserNotFoundException("User not found with id: " + user.getId());
        }
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public AppUser findByEmailAndPassword(String email, String password) throws InvalidCredentialsException {
        return userRepository.findByEmailAndPassword(email, password)
            .orElseThrow(() -> new InvalidCredentialsException("Invalid email or password"));
    }

}   
