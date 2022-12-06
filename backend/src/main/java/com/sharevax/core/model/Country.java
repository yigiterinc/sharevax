package com.sharevax.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "country")
public class Country {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "id", nullable = false)
  private Integer id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "population", nullable = false)
  private BigInteger population;

  @Column(name = "vaccination_rate", nullable = false)
  private double vaccinationRate;

  @Column(name = "daily_vaccine_consumption", nullable = false)
  private BigInteger dailyVaccineConsumption;

  @Column(name = "vaccine_stock", nullable = false)
  private BigInteger vaccineStock;

  @Column(name = "daily_vaccine_production", nullable = false)
  private BigInteger dailyVaccineProduction;

  @OneToMany(mappedBy = "country", cascade = CascadeType.ALL)
  @JsonBackReference
  private List<Harbor> harbors;
}
