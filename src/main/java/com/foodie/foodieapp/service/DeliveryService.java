package com.foodie.foodieapp.service;

import java.util.List;

import com.foodie.foodieapp.domain.Delivery;

public interface DeliveryService {

    Delivery createDeliveryFromSession(String sessionId);
    Delivery getDeliveryBySessionId(String sessionId);
    List<Delivery> getDeliveriesByUserEmailAndDeliveryStatus( String deliveryStatus);

    Delivery updateDeliveryStatus(String deliveryId, String status);





}
