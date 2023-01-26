package com.sharevax.core.service;

import com.sharevax.core.model.Country;
import com.sharevax.core.model.Demand;
import com.sharevax.core.model.Demand.Urgency;
import com.sharevax.core.model.VaccineType;
import com.sharevax.core.model.dto.CreateDemandDto;
import com.sharevax.core.repository.DemandRepository;
import java.math.BigInteger;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DemandService {
    private final DemandRepository demandRepository;
    private final CountryService countryService;

    public DemandService(DemandRepository demandRepository, CountryService countryService1) {
        this.demandRepository = demandRepository;
        this.countryService = countryService1;
    }

    public Demand createDemand(CreateDemandDto createDemandDto) {
        Demand demand = Demand.builder()
                .country(countryService.getCountryById(createDemandDto.getCountryId()))
                .quantity(createDemandDto.getQuantity())
                .build();

        demand.setVaccineType(VaccineType.valueOf(createDemandDto.getVaccineType()));
        demand.setUrgency(Urgency.valueOf(createDemandDto.getUrgency()));

        return demandRepository.save(demand);
    }

    public Demand getDemandById(Integer id) {
        return demandRepository.findById(id).orElseThrow(() -> new RuntimeException("Demand not found"));
    }

    public List<Demand> getAllDemands() {
        return demandRepository.findAll();
    }

    public List<Country> getProvidingCountries() {
        var existingDemands = demandRepository.findAll();

        var countries = existingDemands.stream()
                .map(Demand::getCountry)
                .distinct()
                .toList();

        return countries;
    }

    public List<Demand> findUnmatchedDemands() {
        return demandRepository.findUnmatchedDemands();
    }

    public void deleteAll() {
        demandRepository.deleteAll();
    }

    public void decreaseQuantity(Integer demandId, BigInteger quantity) {
        Demand demand = getDemandById(demandId);
        demand.setQuantity(demand.getQuantity().subtract(quantity));
        if(demand.getQuantity().compareTo(BigInteger.valueOf(0)) == 1){   // greater than 0
            demandRepository.save(demand);
        }
        else{
            //demandRepository.deleteById(demandId);
        }
    }
}
