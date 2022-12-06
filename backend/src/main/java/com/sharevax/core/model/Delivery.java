package com.sharevax.core.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.*;
import org.locationtech.jts.geom.LineString;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@ToString
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "start_harbor_id", nullable = false)
    @JsonManagedReference
    private Harbor startHarbor;

    @ManyToOne
    @JoinColumn(name = "destination_harbor_id", nullable = false)
    @JsonManagedReference
    private Harbor destinationHarbor;

    @Column(name = "estimated_arrival_date")
    private Date estimatedArrivalDate;

    @Column(name = "delivery_created_date", nullable = false)
    private Date createdAt;

    @Column(name = "delivery_updated_date")
    private Date updatedAt;

    @Column(name = "delivery_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus = DeliveryStatus.IN_TIME;

    @Column(name = "days_to_next_harbor")
    private int remainingDaysToNextHarbor;

    // store route history in coordinates
    @Column(name = "route_history")
    private LineString routeHistory;

    @Column(name = "future_route")
    private LineString futureRoute;

    @OneToOne
    private Supply supply;

    @OneToOne
    private Demand demand;

    public enum DeliveryStatus {
        IN_TIME,
        DELAYED,
        DELIVERED
    }
}
