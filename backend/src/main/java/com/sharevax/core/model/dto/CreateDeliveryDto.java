package com.sharevax.core.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.sharevax.core.model.Delivery;
import com.sharevax.core.model.Demand;
import com.sharevax.core.model.Harbor;
import com.sharevax.core.model.Supply;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.locationtech.jts.geom.LineString;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class CreateDeliveryDto {
    private Harbor startHarbor;
    private Harbor destinationHarbor;
    private Date estimatedArrivalDate;
    private Date createdAt;
    private Date updatedAt;
    private Delivery.DeliveryStatus deliveryStatus;
    private int remainingDaysToNextHarbor;
    private LineString routeHistory;
    private LineString futureRoute;

    private Supply supply;
    private Demand demand;
}
