package com.sharevax.core.service;

import com.sharevax.core.model.Delivery;
import com.sharevax.core.model.Demand;
import com.sharevax.core.model.Harbor;
import com.sharevax.core.model.Supply;
import com.sharevax.core.repository.DeliveryRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

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
                               Supply supply, Demand demand) {

        Delivery delivery = Delivery.builder()
                .startHarbor(startHarbor)
                .destinationHarbor(destinationHarbor)
                .estimatedArrivalDate(estimatedArrivalDate)
                .supply(supply)
                .createdAt(new Date())
                .demand(demand)
                .build();

        return deliveryRepository.save(delivery);
    }
}
