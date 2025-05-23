package com.foodie.foodieapp.service;

import com.foodie.foodieapp.domain.Order;
import com.foodie.foodieapp.domain.OrderItem;
import com.foodie.foodieapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public Order getOrder(String userId) {
        return orderRepository.findByUserId(userId);
    }

    @Override
    public Order addItemToOrder(String userId, String restaurantId, OrderItem item) {
        Order order = orderRepository.findByUserId(userId);

        if(order == null){
            order = new Order();
            order.setUserId(userId);
            order.setRestaurantId(restaurantId);
        } else if(!order.getRestaurantId().equals(restaurantId)){
            order.setItems(new ArrayList<>());
            order.setRestaurantId(restaurantId);
        }

        for(OrderItem existingItem : order.getItems()){
            if(existingItem.getDishId().equals(item.getDishId())){
                return order;
            }
        }
        order.getItems().add(item);

        updateTotalAmount(order);
        return orderRepository.save(order);
    }

    @Override
    public Order updateItemQuantity(String userId, String dishId, String action) {
        Order order = orderRepository.findByUserId(userId);
        if(order == null) return null;

        List<OrderItem> items = order.getItems();

        for(OrderItem item : items){
            if(item.getDishId().equals(dishId)){
                if("increment".equalsIgnoreCase(action)){
                    item.setQuantity(item.getQuantity() + 1);
                } else if("decrement".equalsIgnoreCase(action)){
                    int currentQuantity = item.getQuantity();
                    if(currentQuantity > 1){
                        item.setQuantity(currentQuantity - 1);
                    } else {
                        if(items.size() == 1){
                            orderRepository.deleteByUserId(userId);
                            return null;
                        }
                        else{
                            items.remove(item);
                        }
                    }
                }
                break;
            }
        }
        updateTotalAmount(order);
        return orderRepository.save(order);
    }

    @Override
    public Order removeItemFromOrder(String userId, String dishId) {
        Order order = orderRepository.findByUserId(userId);
        if(order == null) return null;

        List<OrderItem> items = order.getItems();

        if(items.size() == 1 && items.getFirst().getDishId().equals(dishId)){
            orderRepository.deleteByUserId(userId);
            return null;
        }
        items.removeIf(item -> item.getDishId().equals(dishId));

        updateTotalAmount(order);
        return orderRepository.save(order);
    }

    @Override
    public void clearOrder(String userId) {
        orderRepository.deleteByUserId(userId);
    }

    private void updateTotalAmount(Order order){
        double total = 0;

        for(OrderItem item : order.getItems()){
            total += item.getPrice() * item.getQuantity();
        }
        order.setTotalAmount(total);
    }
}
