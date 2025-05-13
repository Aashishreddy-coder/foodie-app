package com.foodie.foodieapp.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.foodie.foodieapp.domain.AppUser;

import com.foodie.foodieapp.jwt.JwtUtil;
import com.foodie.foodieapp.service.UserService;
import com.foodie.foodieapp.dto.LoginRequest;

@RestController
@RequestMapping("/api/users")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AppUser> registerUser(@RequestBody AppUser user)  {
        
            AppUser savedUser = userService.save(user);
            return new ResponseEntity<>(savedUser, HttpStatus.CREATED);
        
    }

    @PostMapping("/login")
    public ResponseEntity<String> loginUser(@RequestBody LoginRequest loginRequest)  {
        AppUser user = userService.findByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());
        if (user != null) {
            String token = jwtUtil.generateToken(user);
            return new ResponseEntity<>(token, HttpStatus.OK);
        }
        return new ResponseEntity<>("Invalid credentials", HttpStatus.NOT_FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id)  {
        AppUser user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}


