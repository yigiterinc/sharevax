package com.sharevax.core.controller;

import com.sharevax.core.model.Delivery;
import com.sharevax.core.model.dto.DeliveryDto;
import com.sharevax.core.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    public ResponseEntity<List<DeliveryDto>> getAllDeliveries() {
        return ResponseEntity.ok(deliveryService.getAllDeliveries());
    }

    @GetMapping("/active")
    public ResponseEntity<List<DeliveryDto>> getActiveDeliveries() {
        /**
         * Can be used for the map
         */
        return ResponseEntity.ok(deliveryService.getActiveDeliveries());
    }
}
