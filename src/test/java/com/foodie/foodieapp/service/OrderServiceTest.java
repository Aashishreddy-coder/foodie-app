package com.foodie.foodieapp.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.foodie.foodieapp.domain.Order;
import com.foodie.foodieapp.domain.OrderItem;
import com.foodie.foodieapp.repository.OrderRepository;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrderServiceImpl orderService;

    private Order testOrder;
    private String userEmail = "test@test.com";
    private String restaurantId = "rest123";
    private String dishId = "dish123";
    private String dishName = "Test Dish";

    @BeforeEach
    void setUp() {
        // Setup security context
        when(securityContext.getAuthentication()).thenReturn(authentication);
        when(authentication.getName()).thenReturn(userEmail);
        SecurityContextHolder.setContext(securityContext);

        // Setup test order
        testOrder = new Order();
        testOrder.setUserEmail(userEmail);
        testOrder.setRestaurantId(restaurantId);
        testOrder.setItems(new ArrayList<>());
    }

    private OrderItem createTestOrderItem(String dishId, int quantity, double price) {
        OrderItem item = new OrderItem();
        item.setDishId(dishId);
        item.setDishName(dishName);
        item.setQuantity(quantity);
        item.setPrice(price);
        return item;
    }

    @Test
    void getOrder_ShouldReturnOrder_WhenOrderExists() {
        // given
        when(orderRepository.findByUserEmail(userEmail)).thenReturn(testOrder);

        // when
        Order found = orderService.getOrder();

        // then
        assertNotNull(found);
        assertEquals(userEmail, found.getUserEmail());
        assertEquals(restaurantId, found.getRestaurantId());
    }

    @Test
    void getOrder_ShouldReturnNull_WhenNoOrderExists() {
        // given
        when(orderRepository.findByUserEmail(userEmail)).thenReturn(null);

        // when
        Order found = orderService.getOrder();

        // then
        assertNull(found);
    }

    @Test
    void addItemToOrder_ShouldCreateNewOrder_WhenNoOrderExists() {
        // given
        when(orderRepository.findByUserEmail(userEmail)).thenReturn(null);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        OrderItem newItem = createTestOrderItem(dishId, 1, 10.0);

        // when
        Order result = orderService.addItemToOrder(restaurantId, newItem);

        // then
        assertNotNull(result);
        assertEquals(restaurantId, result.getRestaurantId());
        assertEquals(1, result.getItems().size());
        assertEquals(10.0, result.getTotalAmount());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    void addItemToOrder_ShouldReturnNull_WhenAddingItemFromDifferentRestaurant() {
        // given
        when(orderRepository.findByUserEmail(userEmail)).thenReturn(testOrder);

        OrderItem newItem = createTestOrderItem(dishId, 1, 10.0);

        // when
        Order result = orderService.addItemToOrder("differentRestaurant", newItem);

        // then
        assertNull(result);
    }

    @Test
    void addItemToOrder_ShouldNotAddDuplicateItem() {
        // given
        OrderItem existingItem = createTestOrderItem(dishId, 1, 10.0);
        testOrder.getItems().add(existingItem);

        when(orderRepository.findByUserEmail(userEmail)).thenReturn(testOrder);

        OrderItem newItem = createTestOrderItem(dishId, 1, 10.0);

        // when
        Order result = orderService.addItemToOrder(restaurantId, newItem);

        // then
        assertNotNull(result);
        assertEquals(1, result.getItems().size());
        assertEquals(1, result.getItems().get(0).getQuantity());
    }

    @Test
    void updateItemQuantity_ShouldIncrementQuantity() {
        // given
        OrderItem existingItem = createTestOrderItem(dishId, 1, 10.0);
        testOrder.getItems().add(existingItem);

        when(orderRepository.findByUserEmail(userEmail)).thenReturn(testOrder);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // when
        Order result = orderService.updateItemQuantity(dishId, "increment");

        // then
        assertNotNull(result);
        assertEquals(2, result.getItems().get(0).getQuantity());
        assertEquals(20.0, result.getTotalAmount());
    }

    @Test
    void updateItemQuantity_ShouldDecrementQuantity() {
        // given
        OrderItem existingItem = createTestOrderItem(dishId, 2, 10.0);
        testOrder.getItems().add(existingItem);

        when(orderRepository.findByUserEmail(userEmail)).thenReturn(testOrder);
        when(orderRepository.save(any(Order.class))).thenAnswer(i -> i.getArgument(0));

        // when
        Order result = orderService.updateItemQuantity(dishId, "decrement");

        // then
        assertNotNull(result);
        assertEquals(1, result.getItems().get(0).getQuantity());
        assertEquals(10.0, result.getTotalAmount());
    }

    @Test
    void updateItemQuantity_ShouldDeleteOrder_WhenLastItemQuantityBecomesZero() {
        // given
        OrderItem existingItem = createTestOrderItem(dishId, 1, 10.0);
        testOrder.getItems().add(existingItem);

        when(orderRepository.findByUserEmail(userEmail)).thenReturn(testOrder);

        // when
        Order result = orderService.updateItemQuantity(dishId, "decrement");

        // then
        assertNull(result);
        verify(orderRepository).deleteByUserEmail(userEmail);
    }

   

    @Test
    void removeItemFromOrder_ShouldDeleteOrder_WhenLastItemRemoved() {
        // given
        OrderItem existingItem = createTestOrderItem(dishId, 1, 10.0);
        testOrder.getItems().add(existingItem);

        when(orderRepository.findByUserEmail(userEmail)).thenReturn(testOrder);

        // when
        Order result = orderService.removeItemFromOrder(dishId);

        // then
        assertNull(result);
        verify(orderRepository).deleteByUserEmail(userEmail);
    }

    @Test
    void clearOrder_ShouldDeleteOrder() {
        // given
        // No need to stub findByUserEmail since it's not used in the implementation

        // when
        orderService.clearOrder();

        // then
        verify(orderRepository).deleteByUserEmail(userEmail);
    }
} 