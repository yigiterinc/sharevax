package com.sharevax.core.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HomeSummaryDto {
    
    private String countryName;
    private BigInteger dailyVaccineProduction;
    private BigInteger dailyVaccineConsumption;
}
