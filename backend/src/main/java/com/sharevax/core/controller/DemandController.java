package com.sharevax.core.controller;

import com.sharevax.core.model.Demand;
import com.sharevax.core.model.dto.CreateDemandDto;
import com.sharevax.core.service.DemandService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/demands")
public class DemandController {

    private final DemandService demandService;

    public DemandController(DemandService demandService) {
        this.demandService = demandService;
    }

    @Operation(summary = "Get a list of all demands")
    @GetMapping
    public ResponseEntity<List<Demand>> getAllDemands() {
        return ResponseEntity.ok(demandService.getAllDemands());
    }

    @Operation(summary = "Get a info from the demand which id is {id}")
    @GetMapping("/{id}")
    public ResponseEntity<Demand> getDemandById(@PathVariable Integer id) {
        return ResponseEntity.ok(demandService.getDemandById(id));
    }

    @Operation(summary = "Send request to create a demand")
    @PostMapping
    public ResponseEntity<Demand> createDemand(@RequestBody CreateDemandDto demand) {
        return ResponseEntity.ok(demandService.createDemand(demand));
    }
}
