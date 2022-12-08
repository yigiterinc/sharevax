package com.sharevax.core.controller;

import com.sharevax.core.service.SimulationService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin
@RestController
@RequestMapping("/simulation")
public class SimulationController {

    private final SimulationService simulationService;

    public SimulationController(SimulationService simulationService) {
        this.simulationService = simulationService;
    }

    @Operation(summary = "Represents a day has passed. For simplified implementation")
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
