package com.foodie.foodieapp.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.foodie.foodieapp.domain.Order;
import com.foodie.foodieapp.domain.OrderItem;
import com.foodie.foodieapp.dto.ApiResponse;
import com.foodie.foodieapp.service.OrderService;

@ExtendWith(MockitoExtension.class)
public class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @InjectMocks
    private OrderController orderController;

    private Order testOrder;
    private OrderItem testOrderItem;
    private String testRestaurantId = "rest123";
    private String testDishId = "dish123";
    private String testDishName = "Test Dish";

    @BeforeEach
    void setUp() {
        // Setup test order item
        testOrderItem = new OrderItem();
        testOrderItem.setDishId(testDishId);
        testOrderItem.setDishName(testDishName);
        testOrderItem.setPrice(10.99);
        testOrderItem.setQuantity(1);

        // Setup test order
        testOrder = new Order();
        testOrder.setId("order123");
        testOrder.setRestaurantId(testRestaurantId);
        List<OrderItem> items = new ArrayList<>();
        items.add(testOrderItem);
        testOrder.setItems(items);
        testOrder.setTotalAmount(10.99);
    }

    @Test
    void getOrder_ShouldReturnOrder_WhenExists() {
        // given
        when(orderService.getOrder()).thenReturn(testOrder);

        // when
        ResponseEntity<ApiResponse<Order>> response = orderController.getOrder();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Order fetched successfully", response.getBody().getMessage());
        assertEquals(testOrder, response.getBody().getData());
    }

    @Test
    void getOrder_ShouldReturnNotFound_WhenEmpty() {
        // given
        when(orderService.getOrder()).thenReturn(null);

        // when
        ResponseEntity<ApiResponse<Order>> response = orderController.getOrder();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Order is empty", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void addItemToOrder_ShouldReturnCreatedOrder_WhenValidData() {
        // given
        when(orderService.addItemToOrder(any(), any())).thenReturn(testOrder);

        // when
        ResponseEntity<ApiResponse<Order>> response = orderController.addItemToOrder(
            testRestaurantId, testOrderItem);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Item added to order", response.getBody().getMessage());
        assertEquals(testOrder, response.getBody().getData());
    }

    @Test
    void addItemToOrder_ShouldReturnBadRequest_WhenDifferentRestaurant() {
        // given
        when(orderService.addItemToOrder(any(), any())).thenReturn(null);

        // when
        ResponseEntity<ApiResponse<Order>> response = orderController.addItemToOrder(
            testRestaurantId, testOrderItem);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Cannot add items from different restaurants", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void updateItemQuantity_ShouldReturnUpdatedOrder_WhenValidData() {
        // given
        when(orderService.updateItemQuantity(any(), any())).thenReturn(testOrder);

        // when
        ResponseEntity<ApiResponse<Order>> response = orderController.updateItemQuantity(
            testDishId, "increment");

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Item quantity updated", response.getBody().getMessage());
        assertEquals(testOrder, response.getBody().getData());
    }

    @Test
    void updateItemQuantity_ShouldReturnNotFound_WhenOrderNotExists() {
        // given
        when(orderService.updateItemQuantity(any(), any())).thenReturn(null);

        // when
        ResponseEntity<ApiResponse<Order>> response = orderController.updateItemQuantity(
            testDishId, "increment");

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("error", response.getBody().getStatus());
        assertEquals("Order not found", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void removeItemFromOrder_ShouldReturnUpdatedOrder_WhenItemRemoved() {
        // given
        when(orderService.removeItemFromOrder(any())).thenReturn(testOrder);

        // when
        ResponseEntity<ApiResponse<Order>> response = orderController.removeItemFromOrder(testDishId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Item removed from order", response.getBody().getMessage());
        assertEquals(testOrder, response.getBody().getData());
    }

    @Test
    void removeItemFromOrder_ShouldReturnSuccess_WhenLastItemRemoved() {
        // given
        when(orderService.removeItemFromOrder(any())).thenReturn(null);

        // when
        ResponseEntity<ApiResponse<Order>> response = orderController.removeItemFromOrder(testDishId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Order cleared as the last item was removed", response.getBody().getMessage());
        assertNull(response.getBody().getData());
    }

    @Test
    void clearOrder_ShouldReturnSuccess() {
        // when
        ResponseEntity<ApiResponse<Void>> response = orderController.clearOrder();

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("success", response.getBody().getStatus());
        assertEquals("Order cleared", response.getBody().getMessage());
        assertNull(response.getBody().getData());
        verify(orderService).clearOrder();
    }
} 