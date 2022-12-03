package com.sharevax.core.controller;

import com.sharevax.core.model.Supply;
import com.sharevax.core.model.dto.CreateSupplyDto;
import com.sharevax.core.service.SupplyService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/supplies")
public class SupplyController {
    private final SupplyService supplyService;

    public SupplyController(SupplyService supplyService) {
        this.supplyService = supplyService;
    }

    @GetMapping
    public ResponseEntity<List<Supply>> getAllSupplies() {
        return ResponseEntity.ok(supplyService.getAllSupplies());
    }

    @GetMapping
    @RequestMapping("/{id}")
    public ResponseEntity<Supply> getSupplyById(@PathVariable Integer id) {
        return ResponseEntity.ok(supplyService.getSupplyById(id));
    }

    @PostMapping
    public ResponseEntity<Supply> createSupply(@RequestBody final CreateSupplyDto createSupplyDto) {
        supplyService.createSupply(createSupplyDto);
        return ResponseEntity.ok().build();
    }
}
