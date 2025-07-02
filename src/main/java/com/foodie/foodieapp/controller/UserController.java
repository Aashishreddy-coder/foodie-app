package com.foodie.foodieapp.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.foodie.foodieapp.domain.AppUser;
import com.foodie.foodieapp.exceptions.UserNotFoundException;
import com.foodie.foodieapp.exceptions.UserAlreadyExistsException;
import com.foodie.foodieapp.exceptions.InvalidCredentialsException;
import com.foodie.foodieapp.jwt.JwtUtil;
import com.foodie.foodieapp.service.UserService;

import java.io.IOException;

import com.foodie.foodieapp.dto.LoginRequest;
import com.foodie.foodieapp.dto.RegisterRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping(value = "/register")
    public ResponseEntity<?> registerUser(
            @RequestPart("user") String userJson,
            @RequestPart("image") MultipartFile image) throws IOException {
    
        ObjectMapper objectMapper = new ObjectMapper();
        RegisterRequest user = objectMapper.readValue(userJson, RegisterRequest.class);
        
    
        try {
            AppUser savedUser = userService.save(user, image);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        } catch (UserAlreadyExistsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) throws InvalidCredentialsException {
        try {
            AppUser user = userService.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
            String token = jwtUtil.generateToken(user);
            return new ResponseEntity<>(token, HttpStatus.OK);
        } catch (InvalidCredentialsException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable Long id) {
        try {
            AppUser user = userService.getUserById(id);
            return new ResponseEntity<>(user, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody AppUser user) {
        try {
            AppUser updatedUser = userService.updateUser(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/update/image")
    public ResponseEntity<?> updateImage(@RequestPart("image") MultipartFile image) throws IOException {
        try {
            AppUser updatedUser = userService.updateImage(image);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } catch (IOException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        try {
            userService.deleteUser(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        } catch (UserNotFoundException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}


