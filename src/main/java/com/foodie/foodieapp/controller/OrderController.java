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

    @GetMapping("/get")
    public ResponseEntity<ApiResponse<Order>> getOrder() {
        Order order = orderService.getOrder();
        if(order == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Order is empty", null));
        }
        return ResponseEntity
                .ok(new ApiResponse<>("success", "Order fetched successfully", order));
    }

    @PostMapping("/add")
    public ResponseEntity<ApiResponse<Order>> addItemToOrder(
            @RequestParam String restaurantId,
            @RequestBody OrderItem item
    ){
        Order order = orderService.addItemToOrder(restaurantId, item);
        if(order == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiResponse<>("error", "Cannot add items from different restaurants", null));
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>("success", "Item added to order", order));
    }

    @PutMapping("/update")
    public ResponseEntity<ApiResponse<Order>> updateItemQuantity(
            @RequestParam String dishId,
            @RequestParam String action
    ){
        Order updatedOrder = orderService.updateItemQuantity(dishId, action);
        if(updatedOrder == null){
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiResponse<>("error", "Order not found", null));
        }
        return ResponseEntity
                .ok(new ApiResponse<>("success", "Item quantity updated", updatedOrder));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse<Order>> removeItemFromOrder(
            @RequestParam String dishId
    ){
        Order updatedOrder = orderService.removeItemFromOrder(dishId);
        if(updatedOrder == null){
            return ResponseEntity.ok(
                    new ApiResponse<>("success", "Order cleared as the last item was removed", null)
            );
        }
        return ResponseEntity
                .ok(new ApiResponse<>("success", "Item removed from order", updatedOrder));
    }

    @DeleteMapping("/clear")
    public ResponseEntity<ApiResponse<Void>> clearOrder() {
        orderService.clearOrder();
        return ResponseEntity
                .ok(new ApiResponse<>("success", "Order cleared", null));
    }
}
