package com.sharevax.core.controller;

import com.sharevax.core.service.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @Operation(summary = "Represents a day has passed: Add day_country and do matching")
    @PatchMapping("/increment-day")
    public void incrementDay() {
        simulationService.simulateDay();
    }

    @Operation(summary = "TODO: do not use")
    @GetMapping("/current-day")
    public ResponseEntity<Integer> getDay() {
        return ResponseEntity.ok(simulationService.getDay());
    }

}
