package com.sharevax.core.controller;

import com.sharevax.core.model.Suggestion;
import com.sharevax.core.model.dto.SuggestionResponseDto;
import com.sharevax.core.service.SuggestionService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin
@RestController
@RequestMapping("/suggestions")
public class SuggestionController {
    private final SuggestionService suggestionService;

    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @Operation(summary = "Get suggetions with country ID {id}")
    @GetMapping("/{countryId}")
    public ResponseEntity<List<Suggestion>> getSuggestionsByCountry(@PathVariable Integer countryId) {
        return ResponseEntity.ok(suggestionService.getSuggestionsByCountryId(countryId));
    }

    @Operation(summary = "set approval status")
    @PostMapping
    public ResponseEntity<Boolean> respondToSuggestion(@RequestBody final SuggestionResponseDto responseDto) {
        return ResponseEntity.ok(suggestionService.setApprovalStatus(responseDto));
    }
}
