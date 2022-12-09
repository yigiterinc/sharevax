package com.sharevax.core.service;

import com.sharevax.core.model.Country;
import com.sharevax.core.model.dto.HomeSummaryDto;
import com.sharevax.core.model.dto.VaccineStatisticDto;
import com.sharevax.core.repository.CountryRepository;
import com.sharevax.core.util.CasingUtil;
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

    //TODO it should return the world wide production/consumption rate
    public HomeSummaryDto getHomeCountrySummary(String countryName) {
        String nameToCamelCase = CasingUtil.toCamelCase(countryName);
        Country country = countryRepository.findByName(nameToCamelCase).orElseThrow(() -> new RuntimeException("Country not found"));
        return new HomeSummaryDto(country.getName(), country.getDailyVaccineProduction(),
                country.getDailyVaccineConsumption());
    }

    public void setCountryVaccineInfo(VaccineStatisticDto vaccineOverview, String countryName){
        String nameToCamelCase = CasingUtil.toCamelCase(countryName);
        Country country = countryRepository.findByName(nameToCamelCase).orElseThrow(() -> new RuntimeException("Country not found"));
        country.setPopulation(vaccineOverview.getPopulation());
        country.setVaccinationRate(vaccineOverview.getVaccinationRate());
        country.setDailyVaccineConsumption(vaccineOverview.getDailyVaccineConsumption());
        country.setVaccineStock(vaccineOverview.getVaccineStock());
        country.setDailyVaccineProduction(vaccineOverview.getVaccineProduction());
        countryRepository.save(country);
    }

    public Country getCountryInfo(String countryName) {
        String nameToCamelCase = CasingUtil.toCamelCase(countryName);
        Country country = countryRepository.findByName(nameToCamelCase).orElseThrow(() -> new RuntimeException("Country not found"));
        return country;
    }
}
