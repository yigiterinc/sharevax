package com.sharevax.core.model;

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
@Table(name = "delivery")
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "start_harbor_id", nullable = false)
    private Harbor startHarbor;

    @ManyToOne
    @JoinColumn(name = "destination_harbor_id", nullable = false)
    private Harbor destinationHarbor;

    @Column(name = "estimated_arrival_date", nullable = false)
    private Date estimatedArrivalDate;

    @Column(name = "delivery_created_date", nullable = false)
    private Date createdAt;

    @Column(name = "delivery_updated_date")
    private Date updatedAt;

    @Column(name = "delivery_status", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryStatus deliveryStatus;

    @Column(name = "days_to_next_harbor", nullable = false)
    private int remainingDaysToNextHarbor;

    // store route history in coordinates
    @Column(name = "route_history", nullable = false)
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
