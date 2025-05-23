package com.foodie.foodieapp.repository;

import com.foodie.foodieapp.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    Order findByUserId(String userId);

    void deleteByUserId(String userId);
}
