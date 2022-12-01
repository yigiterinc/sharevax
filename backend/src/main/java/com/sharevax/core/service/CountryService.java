package com.sharevax.core.service;

import com.sharevax.core.model.Country;
import com.sharevax.core.repository.CountryRepository;
import org.springframework.stereotype.Service;

@Service
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Country getCountryById(Integer countryId) {
        return countryRepository.findById(countryId).orElseThrow(() -> new RuntimeException("Country not found"));
    }

}
