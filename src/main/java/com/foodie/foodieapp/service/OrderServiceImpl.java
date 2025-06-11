package com.foodie.foodieapp.service;

import com.foodie.foodieapp.domain.Order;
import com.foodie.foodieapp.domain.OrderItem;
import com.foodie.foodieapp.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderRepository orderRepository;

    private String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @Override
    public Order getOrder() {
        return orderRepository.findByUserEmail(getUserEmail());
    }

    @Override
    public Order addItemToOrder(String restaurantId, OrderItem item) {
        String userEmail = getUserEmail();
        Order order = orderRepository.findByUserEmail(userEmail);
    
        if(order == null){
            order = new Order();
            order.setUserEmail(userEmail);
            order.setRestaurantId(restaurantId);
         // Set initial payment status
        } else if(!order.getRestaurantId().equals(restaurantId)){
            return null;
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
    public Order updateItemQuantity(String dishId, String action) {
        String userEmail = getUserEmail();
        Order order = orderRepository.findByUserEmail(userEmail);
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
                            orderRepository.deleteByUserEmail(userEmail);
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
    public Order removeItemFromOrder(String dishId) {
        String userEmail = getUserEmail();
        Order order = orderRepository.findByUserEmail(userEmail);
        if(order == null) return null;

        List<OrderItem> items = order.getItems();

        if(items.size() == 1 && items.getFirst().getDishId().equals(dishId)){
            orderRepository.deleteByUserEmail(userEmail);
            return null;
        }
        items.removeIf(item -> item.getDishId().equals(dishId));

        updateTotalAmount(order);
        return orderRepository.save(order);
    }

    @Override
    public void clearOrder() {
        orderRepository.deleteByUserEmail(getUserEmail());
    }

    private void updateTotalAmount(Order order){
        double total = 0;
        for(OrderItem item : order.getItems()){
            total += item.getPrice() * item.getQuantity();
        }
        order.setTotalAmount(total);
    }
}
