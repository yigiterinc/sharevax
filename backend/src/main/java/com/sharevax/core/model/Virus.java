package com.sharevax.core.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Builder
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "virus")
public class Virus {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "r_value", nullable = false)
    private float rValue;

    @Column(name = "incubation_period", nullable = false)
    private float incubationPeriod;

    @Column(name = "infection_period", nullable = false)
    private float infectionPeriod;

    public Virus() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

}