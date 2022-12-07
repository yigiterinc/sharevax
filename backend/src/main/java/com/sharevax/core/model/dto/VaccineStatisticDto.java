package com.sharevax.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigInteger;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class VaccineStatisticDto{
    private BigInteger population;
    private double vaccinationRate;
    private BigInteger dailyVaccineConsumption;
    private BigInteger vaccineStock;
    private BigInteger vaccineProduction;
}