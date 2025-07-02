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

import com.foodie.foodieapp.domain.Delivery;
import com.foodie.foodieapp.service.DeliveryService;

@ExtendWith(MockitoExtension.class)
public class DeliveryControllerTest {

    @Mock
    private DeliveryService deliveryService;

    @InjectMocks
    private DeliveryController deliveryController;

    private Delivery testDelivery;
    private String testSessionId = "session123";
    private String testDeliveryId = "delivery123";
    private String testDeliveryStatus = "PENDING";

    @BeforeEach
    void setUp() {
        // Setup test delivery
        testDelivery = new Delivery();
        testDelivery.setId(testDeliveryId);
        testDelivery.setSessionId(testSessionId);
        testDelivery.setDeliveryStatus(testDeliveryStatus);
        testDelivery.setUserEmail("test@example.com");
        testDelivery.setRestaurantId("rest123");
        testDelivery.setRestaurantName("Test Restaurant");
    }

    @Test
    void getDeliveryDetails_ShouldReturnExistingDelivery() {
        // given
        when(deliveryService.getDeliveryBySessionId(testSessionId)).thenReturn(testDelivery);

        // when
        ResponseEntity<Delivery> response = deliveryController.getDeliveryDetails(testSessionId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testDeliveryId, response.getBody().getId());
        assertEquals(testSessionId, response.getBody().getSessionId());
        verify(deliveryService).getDeliveryBySessionId(testSessionId);
        verify(deliveryService, never()).createDeliveryFromSession(any());
    }

    @Test
    void getDeliveryDetails_ShouldCreateNewDelivery_WhenNotExists() {
        // given
        when(deliveryService.getDeliveryBySessionId(testSessionId)).thenReturn(null);
        when(deliveryService.createDeliveryFromSession(testSessionId)).thenReturn(testDelivery);

        // when
        ResponseEntity<Delivery> response = deliveryController.getDeliveryDetails(testSessionId);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testDeliveryId, response.getBody().getId());
        assertEquals(testSessionId, response.getBody().getSessionId());
        verify(deliveryService).getDeliveryBySessionId(testSessionId);
        verify(deliveryService).createDeliveryFromSession(testSessionId);
    }

    @Test
    void getDeliveriesByUserEmailAndDeliveryStatus_ShouldReturnDeliveries() {
        // given
        List<Delivery> expectedDeliveries = new ArrayList<>();
        expectedDeliveries.add(testDelivery);
        when(deliveryService.getDeliveriesByUserEmailAndDeliveryStatus(testDeliveryStatus))
            .thenReturn(expectedDeliveries);

        // when
        ResponseEntity<List<Delivery>> response = deliveryController
            .getDeliveriesByUserEmailAndDeliveryStatus(testDeliveryStatus);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals(testDeliveryId, response.getBody().get(0).getId());
        assertEquals(testDeliveryStatus, response.getBody().get(0).getDeliveryStatus());
    }

    @Test
    void updateDeliveryStatus_ShouldReturnUpdatedDelivery() {
        // given
        String newStatus = "DELIVERED";
        Delivery updatedDelivery = new Delivery();
        updatedDelivery.setId(testDeliveryId);
        updatedDelivery.setDeliveryStatus(newStatus);
        when(deliveryService.updateDeliveryStatus(testDeliveryId, newStatus))
            .thenReturn(updatedDelivery);

        // when
        ResponseEntity<Delivery> response = deliveryController.updateDeliveryStatus(
            testDeliveryId, newStatus);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(testDeliveryId, response.getBody().getId());
        assertEquals(newStatus, response.getBody().getDeliveryStatus());
    }

    @Test
    void updateDeliveryStatus_ShouldReturnNotFound_WhenDeliveryNotExists() {
        // given
        String newStatus = "DELIVERED";
        when(deliveryService.updateDeliveryStatus(testDeliveryId, newStatus))
            .thenThrow(new RuntimeException("Delivery not found"));

        // when
        ResponseEntity<Delivery> response = deliveryController.updateDeliveryStatus(
            testDeliveryId, newStatus);

        // then
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
    }
} 