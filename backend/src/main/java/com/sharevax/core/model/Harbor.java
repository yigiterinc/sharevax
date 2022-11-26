package com.sharevax.core.model;

import lombok.*;
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

    @Column(name = "coordinate", nullable = false)
    private Point coordinate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Country country;

    @Column(name = "status", nullable = false)
    private HarborStatus status;

    @OneToMany(mappedBy = "startHarbor", fetch = FetchType.LAZY)
    private List<Delivery> outgoingDeliveries;

    @OneToMany(mappedBy = "destinationHarbor", fetch = FetchType.LAZY)
    private List<Delivery> incomingDeliveries;

    enum HarborStatus {
        AVAILABLE,
        CLOSED,
        FULL
    }
}