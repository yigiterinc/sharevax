package com.sharevax.core.service;

import com.sharevax.core.model.Delivery;
import com.sharevax.core.model.Demand;
import com.sharevax.core.model.Harbor;
import com.sharevax.core.model.Supply;
import com.sharevax.core.model.dto.DeliveryDto;
import com.sharevax.core.repository.DeliveryRepository;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;

    public DeliveryService(DeliveryRepository deliveryRepository) {
        this.deliveryRepository = deliveryRepository;
    }

    public void createDelivery(Delivery delivery) {
        deliveryRepository.save(delivery);
    }

    public Delivery createDelivery(Harbor startHarbor, Harbor destinationHarbor, Date estimatedArrivalDate,
                                   Supply supply, Demand demand, LineString routeHistory, LineString futureRoute,int remainingDaysToNextHarbor) {

        Delivery delivery = Delivery.builder()
                .startHarbor(startHarbor)
                .destinationHarbor(destinationHarbor)
                .estimatedArrivalDate(estimatedArrivalDate)
                .supply(supply)
                .createdAt(new Date())
                .deliveryStatus(Delivery.DeliveryStatus.IN_TIME)
                .demand(demand)
                .routeHistory(routeHistory)
                .futureRoute(futureRoute)
                .remainingDaysToNextHarbor(remainingDaysToNextHarbor)
                .updatedAt(new Date())
                .build();

        return deliveryRepository.save(delivery);
    }

    public List<DeliveryDto> getAllDeliveries() {
        var deliveries =  deliveryRepository.findAll();
        var deliveryDtos = deliveries.stream().map(DeliveryDto::from).toList();
        return deliveryDtos;
    }

    public List<DeliveryDto> getActiveDeliveries() {
        var deliveries =  deliveryRepository.findActiveDeliveries();
        var deliveryDtos = deliveries.stream().map(DeliveryDto::from).toList();
        return deliveryDtos;
    }
}
