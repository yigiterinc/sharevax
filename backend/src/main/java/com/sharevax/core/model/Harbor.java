package com.sharevax.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.*;
import org.locationtech.jts.geom.LineString;
import org.hibernate.annotations.Type;
import org.locationtech.jts.geom.Point;


import javax.persistence.*;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Getter
@Setter
@Table(name = "harbor")
public class Harbor {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "coordinate", nullable = false, columnDefinition = "geometry(Point,4326)")
    private Point coordinate;

    @ManyToOne
    @JsonManagedReference
    private Country country;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private HarborStatus status = HarborStatus.AVAILABLE;

    @OneToMany(mappedBy = "startHarbor")
    @JsonBackReference
    private List<Delivery> outgoingDeliveries;

    @OneToMany(mappedBy = "destinationHarbor")
    @JsonBackReference
    private List<Delivery> incomingDeliveries;

    enum HarborStatus {
        AVAILABLE,
        CLOSED,
        FULL
    }
}