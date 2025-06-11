package com.foodie.foodieapp.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.foodie.foodieapp.domain.Delivery;

import java.util.List;

@Repository
public interface DeliveryRepository extends MongoRepository<Delivery, String> {
    Delivery findBySessionIdAndUserEmail(String sessionId, String userEmail);
    List<Delivery> findByUserEmailAndDeliveryStatus(String userEmail, String deliveryStatus);



}