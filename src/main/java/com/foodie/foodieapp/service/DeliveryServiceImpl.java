package com.foodie.foodieapp.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.foodie.foodieapp.domain.Delivery;
import com.foodie.foodieapp.repository.DeliveryRepository;
import com.stripe.model.checkout.Session;
import com.stripe.Stripe;

@Service
public class DeliveryServiceImpl implements DeliveryService {
    @Value("${stripe.secretKey}")
    private String secretKey;

    @Autowired
    private DeliveryRepository deliveryRepository;

    private String getUserEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
    
    @Autowired
    private OrderService orderService;

    public Delivery createDeliveryFromSession(String sessionId) {
        // Set Stripe API key
        Stripe.apiKey = secretKey;

   ;
        
        try {
            String userEmail = getUserEmail();
            System.out.println("userEmail: " + userEmail);
            Session session = Session.retrieve(sessionId);
            Map<String, String> metadata = session.getMetadata();
            Delivery delivery = new Delivery();

            LocalDateTime currentTime = LocalDateTime.now();

            String timeInMinutes = metadata.getOrDefault("time", "30");

            LocalDateTime deliveryTime = currentTime.plusMinutes(Long.parseLong(timeInMinutes));


          

           delivery.setOrderId(metadata.get("orderId"));
            delivery.setUserEmail(userEmail);
           
            delivery.setRestaurantId(metadata.get("restaurantId"));
            delivery.setRestaurantName(metadata.get("restaurantName"));
            delivery.setItems(metadata.get("orderItems"));
            delivery.setTotalAmount(metadata.get("amount"));
            delivery.setPaymentStatus("COMPLETED");
            delivery.setSessionId(sessionId);
            delivery.setDeliveryTime(deliveryTime.toString());
            delivery.setTime(timeInMinutes);
            delivery.setDistance((metadata.get("distance")));
            delivery.setDeliveryAddress(metadata.get("address"));
            delivery.setDeliveryStatus("PENDING");   
            delivery.setCurrentTime(currentTime.toString());
    
           
         
         
            return deliveryRepository.save(delivery);
        } catch (Exception e) {
            throw new RuntimeException("Failed to create delivery: " + e.getMessage());
        }
    }

    public Delivery getDeliveryBySessionId(String sessionId) {
        String userEmail = getUserEmail();

        return deliveryRepository.findBySessionIdAndUserEmail(sessionId,userEmail);

    }

    public List<Delivery> getDeliveriesByUserEmailAndDeliveryStatus( String deliveryStatus) {
        String userEmail = getUserEmail();
        return deliveryRepository.findByUserEmailAndDeliveryStatus(userEmail,deliveryStatus);
    }

    @Override
    public Delivery updateDeliveryStatus(String deliveryId, String status) {
        Delivery delivery = deliveryRepository.findById(deliveryId)
            .orElseThrow(() -> new RuntimeException("Delivery not found with id: " + deliveryId));
        
        delivery.setDeliveryStatus(status.toUpperCase());
        return deliveryRepository.save(delivery);
    }
}
