package com.sharevax.core.controller;

import com.sharevax.core.model.Country;
import com.sharevax.core.model.dto.VaccineStatisticDto;
import com.sharevax.core.service.CountryService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/countries")
public class CountryController {
    final CountryService countryService;

    public CountryController(CountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping
    public ResponseEntity<List<Country>> getAllCountries() {
        return ResponseEntity.ok(countryService.getAllCountries());
    }

    @PostMapping
    public ResponseEntity<String> getSupplyById(@RequestBody final VaccineStatisticDto vaccineOverview){
        countryService.setCountryVaccineInfo(vaccineOverview);
        return ResponseEntity.ok("Changed");
    }
}
