package com.sharevax.core.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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

    public enum HarborStatus {
        AVAILABLE,
        CLOSED,
        FULL
    }

    public boolean isAvailable() {
        return status == HarborStatus.AVAILABLE;
    }

    public boolean isFull() {
        return status == HarborStatus.FULL;
    }

    public boolean isClosed() {
        return status == HarborStatus.CLOSED;
    }
}