package com.sharevax.core.model;

import java.util.Date;
import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "supply")
public class Supply {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    private Country country;

    @Column(name = "vaccine_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private VaccineType vaccineType;

    @Column(name = "quantity", nullable = false)
    private BigInteger quantity;

    @Column(name = "expiration_date", nullable = false)
    private Date expirationDate;

    @Column(name = "unit_price")
    @Builder.Default
    private double unitPrice = 0.0;
}
