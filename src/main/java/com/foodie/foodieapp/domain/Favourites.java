package com.foodie.foodieapp.domain;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "favourites")
public class Favourites {
    @Id
    private String id;
    private String userEmail;
    private String dishId;
}
