package com.foodie.foodieapp.controller;

import com.foodie.foodieapp.domain.Order;
import com.foodie.foodieapp.domain.OrderItem;
import com.foodie.foodieapp.dto.ApiResponse;
import com.foodie.foodieapp.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    @Autowired
    private OrderService orderService;

    @GetMapping("/get/{userId}")
    public ResponseEntity<ApiResponse<Order>> getOrder(@PathVariable String userId){
        Order order = orderService.getOrder(userId);
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Order is empty", null));
        }
        return ResponseEntity
                .ok(new ApiResponse<>("success", "Order fetched successfully", order));
    }

    @PostMapping("/add/{userId}")
    public ResponseEntity<ApiResponse<Order>> addItemToOrder(
            @PathVariable String userId,
            @RequestParam String restaurantId,
            @RequestBody OrderItem item
    ){
        Order order = orderService.addItemToOrder(userId, restaurantId, item);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", "Item added to order", order));
    }

    @PutMapping("/update/{userId}")
    public ResponseEntity<ApiResponse<Order>> updateItemQuantity(
            @PathVariable String userId,
            @RequestParam String dishId,
            @RequestParam String action
    ){
        Order updatedOrder = orderService.updateItemQuantity(userId, dishId, action);
        if(updatedOrder == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Order not found", null));
        }
        return ResponseEntity
                .ok(new ApiResponse<>("success", "Item quantity updated", updatedOrder));
    }

    @DeleteMapping("/remove/{userId}")
    public ResponseEntity<ApiResponse<Order>> removeItemFromOrder(
            @PathVariable String userId,
            @RequestParam String dishId
    ){
        Order updatedOrder = orderService.removeItemFromOrder(userId, dishId);
        if(updatedOrder == null){
            return ResponseEntity.ok(
                    new ApiResponse<>("success", "Order cleared as the last item was removed", null)
            );
        }
        return ResponseEntity
                .ok(new ApiResponse<>("success", "Item removed from order", updatedOrder));
    }

    @DeleteMapping("/clear/{userId}")
    public ResponseEntity<ApiResponse<Void>> clearOrder(@PathVariable String userId){
        orderService.clearOrder(userId);
        return ResponseEntity
                .ok(new ApiResponse<>("success", "Order cleared", null));
    }
}
