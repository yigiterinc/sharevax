package com.sharevax.core.service;

import com.sharevax.core.model.Country;
import com.sharevax.core.model.dto.HomeSummaryDto;
import com.sharevax.core.repository.CountryRepository;
import com.sharevax.core.util.CasingUtil;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public void resetCountryData() {
        var countries = countryRepository.findAll();

        Map<Integer, Double> countryInitialVaccinationRate = new HashMap<>();
        countryInitialVaccinationRate.put(1, 0.9);
        countryInitialVaccinationRate.put(2, 0.81);
        countryInitialVaccinationRate.put(3, 0.7);
        countryInitialVaccinationRate.put(4, 0.75);
        countryInitialVaccinationRate.put(5, 0.9);
        countryInitialVaccinationRate.put(6, 0.68);
        countryInitialVaccinationRate.put(7, 0.8);
        countryInitialVaccinationRate.put(8, 0.32);
        countryInitialVaccinationRate.put(9, 0.18);

        for (var country : countries) {
            country.setVaccinationRate(countryInitialVaccinationRate.get(country.getId()));
            country.setVaccineStock(new BigInteger("0"));
        }

        countryRepository.saveAll(countries);
    }

}
