package com.sharevax.core.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigInteger;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "demand")
public class Demand {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    private Country country;

    @Column(name = "vaccine_type", nullable = false)
    @Enumerated(EnumType.STRING)
    private VaccineType vaccineType;

    @Column(name = "quantity", nullable = false)
    private BigInteger quantity;

    @Column(name = "urgency", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Urgency urgency = Urgency.NORMAL;

    public enum Urgency {
        NORMAL,
        URGENT,
        CRITICAL
    }
}
