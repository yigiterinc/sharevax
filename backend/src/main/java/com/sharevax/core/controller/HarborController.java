package com.sharevax.core.controller;

import com.sharevax.core.model.Harbor;
import com.sharevax.core.model.dto.CreateHarborDto;
import com.sharevax.core.repository.HarborRepository;
import com.sharevax.core.service.CountryService;
import io.swagger.v3.oas.annotations.Operation;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/harbors")
public class HarborController {

    HarborRepository harborRepository;
    CountryService countryService;

    public HarborController(HarborRepository harborRepository, CountryService countryService) {
        this.harborRepository = harborRepository;
        this.countryService = countryService;
    }
//
//    @GetMapping("/test-serialization")
//    public ResponseEntity<Point> testSerialization() {
//        GeometryFactory geometryFactory = new GeometryFactory();
//        Point point = geometryFactory.createPoint(new Coordinate(1, 2));
//        return ResponseEntity.ok(point);
//    }

    @Operation(summary = "Get a list of all harbors")
    @GetMapping
    public ResponseEntity<List<Harbor>> getAllHarbors() {
        return ResponseEntity.ok(harborRepository.findAll());
    }

    @Operation(summary = "Create a harbor")
    @PostMapping
    public Harbor createHarbor(@RequestBody CreateHarborDto createHarborDto) {
        Harbor harbor = Harbor.builder()
                .status(Harbor.HarborStatus.AVAILABLE)
                .name(createHarborDto.getName())
                .country(countryService.getCountryById(createHarborDto.getCountryId()))
                .coordinate(new GeometryFactory().createPoint(new Coordinate(createHarborDto.getLongitude(), createHarborDto.getLatitude())))
                .build();


        return harborRepository.save(harbor);
    }
}
