package com.sharevax.core.controller;

import com.sharevax.core.service.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@CrossOrigin
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

    @Operation(summary = "Current Date")
    @GetMapping("/current-day")
    public ResponseEntity<Date> getDay() {
        return ResponseEntity.ok(simulationService.getDay());
    }

}
