package com.foodie.foodieapp.repository;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.mongodb.core.MongoTemplate;

import com.foodie.foodieapp.domain.Order;

@DataMongoTest
public class OrderRepositoryTest {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private OrderRepository orderRepository;

    private Order testOrder;
    private String userId = "user123";

    @BeforeEach
    void setUp() {
        // Clear existing data
        mongoTemplate.dropCollection(Order.class);
        
        testOrder = new Order();
        testOrder.setUserEmail(userId);
        testOrder.setTotalAmount(100.0);
        mongoTemplate.save(testOrder);
    }

    @Test
    void findByUserId_ShouldReturnOrders_WhenOrdersExist() {
        // when
        Order found = orderRepository.findByUserEmail(userId);

        // then
        assertNotNull(found);
        assertEquals(userId, found.getUserEmail());
        assertEquals(100.0, found.getTotalAmount());
    }

    @Test
    void findByUserId_ShouldReturnEmptyList_WhenNoOrdersExist() {
        // when
        Order found = orderRepository.findByUserEmail("nonexistentUser");

        // then
        assertNull(found);
    }

    @Test
    void deleteByUserId_ShouldDeleteOrders_WhenOrdersExist() {
        // when
        orderRepository.deleteByUserEmail(userId);

        // then
        Order deleted = orderRepository.findByUserEmail(userId);
        assertNull(deleted);

    }
    
} 