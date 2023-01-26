package com.sharevax.core.service;

import com.sharevax.core.model.Delivery;
import com.sharevax.core.model.Demand;
import com.sharevax.core.model.Harbor;
import com.sharevax.core.model.Suggestion;
import com.sharevax.core.model.Supply;
import com.sharevax.core.model.dto.DeliveryDto;
import com.sharevax.core.repository.DeliveryRepository;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class DeliveryService {

    private final DeliveryRepository deliveryRepository;
    private final RouteService routeService;

    public DeliveryService(DeliveryRepository deliveryRepository, RouteService routeService) {
        this.deliveryRepository = deliveryRepository;
        this.routeService = routeService;
    }

    public void createDelivery(Delivery delivery) {
        deliveryRepository.save(delivery);
    }

    public Delivery createDelivery(Harbor startHarbor, Harbor destinationHarbor,
                                    Date createdAt, Suggestion suggestion) {


        LineString futureRoute = routeService.getLineString(startHarbor, destinationHarbor);
        LineString routeHistory = routeService.getLineString(
            new ArrayList<>(Arrays.asList(startHarbor.getCoordinate().getCoordinate())));

        // calculate the estimatedArrivalDate
        int duration = routeService.getDeliveryDays(startHarbor, destinationHarbor);
        Date estimatedArrivalDate = getEstimatedArrivalDate(duration, createdAt);

        int remainingDaysToNextHarbor = routeService.getDaysToNextStop(routeHistory, futureRoute);

        Delivery delivery = Delivery.builder()
                .startHarbor(startHarbor)
                .destinationHarbor(destinationHarbor)
                .estimatedArrivalDate(estimatedArrivalDate)
                .supply(suggestion.getSupply())
                .createdAt(createdAt)
                .deliveryStatus(Delivery.DeliveryStatus.IN_TIME)
                .demand(suggestion.getDemand())
                .routeHistory(routeHistory)
                .futureRoute(futureRoute)
                .remainingDaysToNextHarbor(remainingDaysToNextHarbor)
                .updatedAt(createdAt)
                .quantity(suggestion.getQuantity())
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

    public DeliveryDto getDelivery(Integer deliveryId) {
        Delivery d = deliveryRepository.findById(deliveryId).orElseThrow(() -> new RuntimeException("Delivery not found"));
        return DeliveryDto.from(d);
    }

    public List<DeliveryDto> getDeliveriesByCountry(Integer countryId) {

        var deliveries =  deliveryRepository.findAll();

        var deliveryDtos = deliveries
            .stream()
            .filter(delivery -> isRelated(countryId, delivery))
            .map(DeliveryDto::from).toList();

        return deliveryDtos;
    }

    public List<Delivery> findActiveDeliveries() {
        return deliveryRepository.findActiveDeliveries();
    }

    public void save(Delivery delivery) {
        deliveryRepository.save(delivery);
    }

    private boolean isRelated(Integer countryId, Delivery delivery) {
        return delivery.getSupply().getId() == countryId ||
            delivery.getDemand().getId() == countryId;
    }

    private Date getEstimatedArrivalDate(int deliveryDays, Date today) {
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(today);
        calendar.add(calendar.DATE, deliveryDays + 1);
        return calendar.getTime();
    }

    public void deleteAll() {
        deliveryRepository.deleteAll();
    }

}
