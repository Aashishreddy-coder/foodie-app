package com.foodie.foodieapp.service;

import com.foodie.foodieapp.domain.Order;
import com.foodie.foodieapp.domain.OrderItem;

public interface OrderService {
    Order getOrder();

    Order addItemToOrder(String restaurantId, OrderItem item);

    Order updateItemQuantity(String dishId, String action);

    Order removeItemFromOrder(String dishId);

    void clearOrder();
}
