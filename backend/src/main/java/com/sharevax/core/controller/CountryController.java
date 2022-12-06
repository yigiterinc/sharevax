package com.sharevax.core.controller;

import com.sharevax.core.model.Country;
import com.sharevax.core.model.dto.HomeSummaryDto;
import com.sharevax.core.repository.CountryRepository;
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

    @GetMapping("{countryName}/summary")
    public ResponseEntity<HomeSummaryDto> getSummary(@PathVariable String countryName) {
        return ResponseEntity.ok(countryService.getHomeCountrySummary(countryName));
    }
}
