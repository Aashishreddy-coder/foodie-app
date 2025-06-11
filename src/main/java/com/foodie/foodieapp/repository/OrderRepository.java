package com.foodie.foodieapp.repository;

import com.foodie.foodieapp.domain.Order;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrderRepository extends MongoRepository<Order, String> {
    Order findByUserEmail(String userEmail);

    void deleteByUserEmail(String userEmail);

    


}
