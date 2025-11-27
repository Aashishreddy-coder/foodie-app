package com.foodie.foodieapp.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;

import com.foodie.foodieapp.domain.Delivery;
import com.foodie.foodieapp.service.DeliveryService;

@RestController
@RequestMapping("/delivery")
public class DeliveryController {
    @Autowired
    private DeliveryService deliveryService;

    @GetMapping("/session/{sessionId}")
    public ResponseEntity<Delivery> getDeliveryDetails(@PathVariable String sessionId) {
        Delivery delivery = deliveryService.getDeliveryBySessionId(sessionId );
        if (delivery == null) {
            
            delivery = deliveryService.createDeliveryFromSession(sessionId);
        }
        return new ResponseEntity<>(delivery, HttpStatus.OK);
        
    }

    @GetMapping("/delivery-status")
    public ResponseEntity<List<Delivery>> getDeliveriesByUserEmailAndDeliveryStatus(@RequestParam String deliveryStatus) {
        deliveryStatus=deliveryStatus.toUpperCase();
        List<Delivery> deliveries = deliveryService.getDeliveriesByUserEmailAndDeliveryStatus(deliveryStatus);
        return new ResponseEntity<>(deliveries, HttpStatus.OK);
    }

    @PutMapping("/{deliveryId}/status")
    public ResponseEntity<Delivery> updateDeliveryStatus(
            @PathVariable String deliveryId,
            @RequestParam String status) {
        try {
            status = status.toUpperCase();
            Delivery updatedDelivery = deliveryService.updateDeliveryStatus(deliveryId, status);
            return new ResponseEntity<>(updatedDelivery, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}