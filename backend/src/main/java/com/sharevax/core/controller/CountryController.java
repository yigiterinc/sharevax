package com.sharevax.core.controller;

import com.sharevax.core.model.Country;
import com.sharevax.core.model.dto.HomeSummaryDto;
import com.sharevax.core.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/countries")
public class CountryController {
    final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @Operation(summary = "Get information from all countries")
    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @Operation(summary = "Get information from the country which name is {countryName}")
    @GetMapping("{countryName}/summary")
    public ResponseEntity<HomeSummaryDto> getSummary(@PathVariable String countryName) {
        return ResponseEntity.ok(countryService.getHomeCountrySummary(countryName));
    }


}
