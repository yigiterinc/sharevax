package com.sharevax.core.controller;

import com.sharevax.core.model.Delivery;
import com.sharevax.core.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {
    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public ResponseEntity<List<Delivery>> getAllRunningDeliveries() {
        return ResponseEntity.ok(deliveryService.getAllRunningDeliveries());
    }

    @GetMapping
    @RequestMapping("/UID/{id}")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable Integer id) {
        return ResponseEntity.ok(deliveryService.getDeliveryById(id));
    }

    @GetMapping
    @RequestMapping("/country/{country_id}")
    public ResponseEntity<List<Delivery>> getDeliveriesByCountryID(@PathVariable Integer country_id) {
        return ResponseEntity.ok(deliveryService.getDeliveriesByCountryID(country_id));
    }

}
