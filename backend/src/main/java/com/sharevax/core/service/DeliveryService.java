package com.sharevax.core.service;

import com.sharevax.core.model.Delivery;
import com.sharevax.core.model.Demand;
import com.sharevax.core.model.Harbor;
import com.sharevax.core.model.Supply;
import com.sharevax.core.repository.DeliveryRepository;
import com.sharevax.core.repository.SupplyRepository;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;
@Service
public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DemandService demandService;
    private final SupplyRepository supplyRepository;


    public DeliveryService(DeliveryRepository deliveryRepository, DemandService demandService, SupplyRepository supplyRepository) {
        this.deliveryRepository = deliveryRepository;
        this.demandService = demandService;
        this.supplyRepository = supplyRepository;
    }

    /**TODO
     * Matching all demands and supplys in the database
     */
    public void matching(){

    }

    /** TODO
     * Find the shortest route between the two harbours
     * @param startHarbor
     * @param destinationHarbor
     * @return the detail of the route [maybe create a class is better TODO]
     */
    private HashMap findRoute(Harbor startHarbor, Harbor destinationHarbor){
        HashMap<String, Object> result = new HashMap<>();

        Integer day = 100;
        LineString route = null;

        result.put("Total duration",day);
        result.put("Route",route);
        return result;
    }

    //TODO
    /**
     * decrease the counter, stands for one day is gone
     * If the ship has reached its station, recalculate the future route and reset the counter
     */
    public void updateClock(){

    }


    /** TODO
     * update the future route of the delivery, according to the current harbor situation
     * @param deliveryID the uid from the delivery needed to update
     */
    public void updateRoute(Integer deliveryID){

    }

    /** TODO
     * Create and ship orders for matched suppliers and demanders
     * @param demand
     * @param supply
     * @return delivery
     */
    public Delivery createDelivery(Demand demand, Supply supply) {
        Delivery delivery = new Delivery();
        delivery.setDemand(demand);
        delivery.setSupply(supply);
        delivery.setCreatedAt(new Date());
        delivery.setUpdatedAt(new Date());
        delivery.setDeliveryStatus(Delivery.DeliveryStatus.IN_TIME);

        //TODO: findRoute(countryS,countryR) and getDays(harbour1,harbour2)
        delivery.setStartHarbor(supply.getCountry().getHarbors().get(0));
        delivery.setDestinationHarbor(demand.getCountry().getHarbors().get(0));
        delivery.setEstimatedArrivalDate(new Date());
        delivery.setRemainingDaysToNextHarbor(100);
//        delivery.setRouteHistory(new LineString());
//        delivery.setFutureRoute(new LineString());

        return deliveryRepository.save(delivery);
    }

    /**
     *
     * @return a list of all incomplete deliveries
     */
    public List<Delivery> getAllRunningDeliveries() {
        List<Delivery> deliveryList = deliveryRepository.findAll()
                                        .stream()
                                        .filter( d -> d.getDeliveryStatus()!= Delivery.DeliveryStatus.DELIVERED)
                                        .collect(Collectors.toList());
        return deliveryList;
    }

    /**
     *
     * @param id uid of the delivery
     * @return the details of the delivery
     */
    public Delivery getDeliveryById(Integer id) {
        return deliveryRepository.findById(id).orElseThrow(() -> new RuntimeException("Delivery not found"));
    }

    /**
     *
     * @param id uid of the country
     * @return A list of all order information for this country
     */
    public List<Delivery> getDeliveriesByCountryID(Integer id) {
        List<Delivery> deliveryList = deliveryRepository.findAll()
                .stream()
                .filter( d -> d.getSupply().getId() == id || d.getDemand().getId() == id)
                .collect(Collectors.toList());
        return deliveryList;
    }

}
