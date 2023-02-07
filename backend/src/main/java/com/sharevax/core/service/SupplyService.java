package com.sharevax.core.service;

import com.sharevax.core.model.Country;
import com.sharevax.core.model.Demand;
import com.sharevax.core.model.Supply;
import com.sharevax.core.model.VaccineType;
import com.sharevax.core.model.dto.CreateSupplyDto;
import com.sharevax.core.repository.CountryRepository;
import com.sharevax.core.repository.SupplyRepository;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.List;

@Service
public class SupplyService {

    private final SupplyRepository supplyRepository;
    private final CountryService countryService;

    public SupplyService(SupplyRepository supplyRepository, CountryService countryService) {
        this.supplyRepository = supplyRepository;
        this.countryService = countryService;
    }

    public Supply createSupply(final CreateSupplyDto createSupplyDto) {
        Supply supply = new Supply();
        supply.setCountry(countryService.getCountryById(createSupplyDto.getCountryId()));
        supply.setVaccineType(VaccineType.valueOf(createSupplyDto.getVaccineType()));
        supply.setQuantity(createSupplyDto.getQuantity());
        supply.setUnitPrice(createSupplyDto.getUnitPrice());
        supply.setExpirationDate(createSupplyDto.getExpirationDate());
        supplyRepository.save(supply);
        return supply;
    }

    public void saveSupply(Supply supply) {
        supplyRepository.save(supply);
    }

    public List<Supply> getAllSupplies() {
        return supplyRepository.findAll();
    }

    public Supply getSupplyById(Integer id) {
        return supplyRepository.findById(id).orElseThrow(() -> new RuntimeException("Supply not found with provided id"));
    }

    public List<Country> getRequestingCountries() {
        var allRequests =  supplyRepository.findAll();

        var countries = allRequests.stream()
                .map(Supply::getCountry)
                .distinct()
                .toList();

        return countries;
    }

    public List<Supply> findUnmatchedSupplies() {
        return supplyRepository.findUnmatchedSupplies();
    }

    public void deleteAll() {
        supplyRepository.deleteAll();
    }

    public void decreaseQuantity(Integer supplyId, BigInteger quantity) {
        Supply supply = getSupplyById(supplyId);
        supply.setQuantity(supply.getQuantity().subtract(quantity));
        supplyRepository.save(supply);
    }
}
