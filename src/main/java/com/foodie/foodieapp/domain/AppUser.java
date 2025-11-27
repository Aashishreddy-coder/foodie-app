package com.foodie.foodieapp.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document
public class AppUser {

    @Id
    private String id;

    private String name;

    private String email;

    private String password;


    private String phone;

    private String address;

    private String image;
    



     

    



}
