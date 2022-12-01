package com.sharevax.core.service;

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

    public void createSupply(final CreateSupplyDto createSupplyDto) {
        Supply supply = new Supply();
        supply.setCountry(countryService.getCountryById(createSupplyDto.getCountryId()));
        supply.setVaccineType(VaccineType.valueOf(createSupplyDto.getVaccineType()));
        supply.setQuantity(createSupplyDto.getQuantity());
        supply.setUnitPrice(createSupplyDto.getUnitPrice());

        supplyRepository.save(supply);
    }

    public Supply createSupply(Integer countryId, String vaccineType, BigInteger quantity, double unitPrice) {
        Supply supply = Supply.builder()
                .country(countryService.getCountryById(countryId))
                .vaccineType(VaccineType.valueOf(vaccineType))
                .quantity(quantity)
                .unitPrice(unitPrice)
                .build();

        return supplyRepository.save(supply);
    }

    public List<Supply> getAllSupplies() {
        return supplyRepository.findAll();
    }

    public Supply getSupplyById(Integer id) {
        return supplyRepository.findById(id).orElseThrow(() -> new RuntimeException("Supply not found with provided id"));
    }
}
