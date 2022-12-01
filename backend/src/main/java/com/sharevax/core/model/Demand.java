package com.sharevax.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
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
    private int quantity;

    @Column(name = "urgency", nullable = false)
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Urgency urgency = Urgency.NORMAL;

    enum Urgency {
        NORMAL,
        URGENT,
        CRITICAL
    }
}
