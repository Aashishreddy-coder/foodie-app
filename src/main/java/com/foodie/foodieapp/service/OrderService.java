package com.foodie.foodieapp.service;

import com.foodie.foodieapp.domain.Order;
import com.foodie.foodieapp.domain.OrderItem;

public interface OrderService {
    Order getOrder(String userId);

    Order addItemToOrder(String userId, String restaurantId, OrderItem item);

    Order updateItemQuantity(String userId, String dishId, String action);

    Order removeItemFromOrder(String userId, String dishId);

    void clearOrder(String userId);
}
