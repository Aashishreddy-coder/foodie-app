package com.foodie.foodieapp.dto;

import lombok.Data;




@Data
public class RegisterRequest {

    private String name;
    private String email;
    private String password;
    private String phone;
    private String address;
   
}
