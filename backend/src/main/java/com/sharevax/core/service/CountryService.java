package com.sharevax.core.service;

import com.sharevax.core.model.Country;
import com.sharevax.core.model.dto.VaccineStatisticDto;
import com.sharevax.core.repository.CountryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CountryService {
    private final CountryRepository countryRepository;

    public CountryService(CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    public Country getCountryById(Integer countryId) {
        return countryRepository.findById(countryId).orElseThrow(() -> new RuntimeException("Country not found"));
    }

    public List<Country> getAllCountries() {
        return countryRepository.findAll();
    }

    public void setCountryVaccineInfo(VaccineStatisticDto vaccineOverview){
        Country country = countryRepository.findById(vaccineOverview.getCountryId()).orElseThrow(()-> new RuntimeException("Country not found"));
        country.setPopulation(vaccineOverview.getPopulation());
        country.setVaccinationRate(vaccineOverview.getVaccinationRate());
        country.setDailyVaccineConsumption(vaccineOverview.getDailyVaccineConsumption());
        country.setVaccineStock(vaccineOverview.getVaccineStock());
        country.setVaccineProduction(vaccineOverview.getVaccineProduction());
        countryRepository.save(country);

    }

}
