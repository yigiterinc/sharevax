package com.sharevax.core.controller;

import com.sharevax.core.service.DeliveryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {
    private final DeliveryService deliveryService;

    public TestController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }

    @GetMapping
    @RequestMapping("/matching}")
    public ResponseEntity<String> matching() {
        deliveryService.matching();
        return ResponseEntity.ok("Matching completed");
    }

    @GetMapping
    @RequestMapping("/updateClock}")
    public ResponseEntity<String> updateClock() {
        deliveryService.updateClock();
        return ResponseEntity.ok("One day is gone");
    }
}
