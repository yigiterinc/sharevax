package com.sharevax.core.controller;

import com.sharevax.core.model.dto.DeliveryDto;
import com.sharevax.core.service.DeliveryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/deliveries")
public class DeliveryController {

    private final DeliveryService deliveryService;

    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @Operation(summary = "Get a list of all deliveries")
    @GetMapping
    public ResponseEntity<List<DeliveryDto>> getAllDeliveries() {
        return ResponseEntity.ok(deliveryService.getAllDeliveries());
    }

    @Operation(summary = "Get a list of all incomplete deliveries")
    @GetMapping("/active")
    public ResponseEntity<List<DeliveryDto>> getActiveDeliveries() {
        /**
         * Can be used for the map
         */
        return ResponseEntity.ok(deliveryService.getActiveDeliveries());
    }
}
