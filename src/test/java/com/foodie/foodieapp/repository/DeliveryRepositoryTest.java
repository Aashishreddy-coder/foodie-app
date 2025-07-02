package com.foodie.foodieapp.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.foodie.foodieapp.domain.Delivery;

@DataMongoTest
public class DeliveryRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private DeliveryRepository deliveryRepository;

    private Delivery testDelivery;
    private String sessionId = "session123";
    private String userEmail = "test@test.com";
    private String deliveryStatus = "PENDING";

    @BeforeEach
    void setUp() {
        // Clear existing data
        mongoTemplate.dropCollection(Delivery.class);
        
        testDelivery = new Delivery();
        testDelivery.setSessionId(sessionId);
        testDelivery.setUserEmail(userEmail);
        testDelivery.setDeliveryStatus(deliveryStatus);
        mongoTemplate.save(testDelivery);
    }

    @Test
    void findBySessionIdAndUserEmail_ShouldReturnDelivery_WhenDeliveryExists() {
        // when
        Delivery found = deliveryRepository.findBySessionIdAndUserEmail(sessionId, userEmail);

        // then
        assertNotNull(found);
        assertEquals(sessionId, found.getSessionId());
        assertEquals(userEmail, found.getUserEmail());
        assertEquals(deliveryStatus, found.getDeliveryStatus());
    }

    @Test
    void findBySessionIdAndUserEmail_ShouldReturnNull_WhenDeliveryDoesNotExist() {
        // when
        Delivery found = deliveryRepository.findBySessionIdAndUserEmail("nonexistentSession", "nonexistent@test.com");

        // then
        assertNull(found);
    }

    @Test
    void findByUserEmailAndDeliveryStatus_ShouldReturnDeliveries_WhenDeliveriesExist() {
        // when
        List<Delivery> found = deliveryRepository.findByUserEmailAndDeliveryStatus(userEmail, deliveryStatus);

        // then
        assertFalse(found.isEmpty());
        assertEquals(1, found.size());
        assertEquals(userEmail, found.get(0).getUserEmail());
        assertEquals(deliveryStatus, found.get(0).getDeliveryStatus());
    }

    @Test
    void findByUserEmailAndDeliveryStatus_ShouldReturnEmptyList_WhenNoDeliveriesExist() {
        // when
        List<Delivery> found = deliveryRepository.findByUserEmailAndDeliveryStatus("nonexistent@test.com", "COMPLETED");

        // then
        assertTrue(found.isEmpty());
    }
}