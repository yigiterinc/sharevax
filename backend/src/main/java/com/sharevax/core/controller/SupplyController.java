package com.sharevax.core.controller;

import com.sharevax.core.model.Supply;
import com.sharevax.core.model.dto.CreateSupplyDto;
import com.sharevax.core.service.SupplyService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/supplies")
public class SupplyController {
    private final SupplyService supplyService;

    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @Operation(summary = "Get a list of all supplies")
    @GetMapping
    public ResponseEntity<List<Supply>> getAllSupplies() {
        return ResponseEntity.ok(supplyService.getAllSupplies());
    }

    @Operation(summary = "Get info from supply with {id}")
    @GetMapping("/{id}")
    public ResponseEntity<Supply> getSupplyById(@PathVariable Integer id) {
        return ResponseEntity.ok(supplyService.getSupplyById(id));
    }

    @Operation(summary = "Create a supply")
    @PostMapping
    public ResponseEntity<Supply> createSupply(@RequestBody final CreateSupplyDto createSupplyDto) {
        return ResponseEntity.ok(supplyService.createSupply(createSupplyDto));

    }
}
