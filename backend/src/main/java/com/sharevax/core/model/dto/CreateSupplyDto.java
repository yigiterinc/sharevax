package com.sharevax.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;

@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
public class CreateSupplyDto {

    Integer countryId;
    String vaccineType;
    BigInteger quantity;

    // this is optional in json
    double unitPrice;

    public CreateSupplyDto(Integer countryId, String vaccineType, BigInteger quantity, double unitPrice) {
        this.countryId = countryId;
        this.vaccineType = vaccineType;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
    }

    public CreateSupplyDto() {
    }
}
