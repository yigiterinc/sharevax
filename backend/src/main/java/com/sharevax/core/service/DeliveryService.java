package com.sharevax.core.service;

import com.sharevax.core.model.Delivery;
import com.sharevax.core.model.Demand;
import com.sharevax.core.model.Supply;
import com.sharevax.core.repository.DeliveryRepository;
import com.sharevax.core.repository.SupplyRepository;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

public class DeliveryService {
    private final DeliveryRepository deliveryRepository;
    private final DemandService demandService;
    private final SupplyRepository supplyRepository;


    public DeliveryService(DeliveryRepository deliveryRepository, DemandService demandService, SupplyRepository supplyRepository) {
        this.deliveryRepository = deliveryRepository;
        this.demandService = demandService;
        this.supplyRepository = supplyRepository;
    }

    //TODO
    public void matching(){

    }

    //TODO
//    private Pair getRouteAndDays(Harbor startHarbor, Harbor destinationHarbor){
////        SeaRouting sr = new SeaRouting();
//        Integer day = 100;
//        LineString route = null;
//        return new Pair(route, day);
//    }

    //TODO do remaing_time - -, stands for one day is gone
    public void updateClock(){

    }

    //TODO
    public void updateRoute(Integer deliveryID){

    }


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


    public List<Delivery> getAllRunningDeliveries() {
        List<Delivery> deliveryList = deliveryRepository.findAll()
                                        .stream()
                                        .filter( d -> d.getDeliveryStatus()!= Delivery.DeliveryStatus.DELIVERED)
                                        .collect(Collectors.toList());
        return deliveryList;
    }

    public Delivery getDeliveryById(Integer id) {
        return deliveryRepository.findById(id).orElseThrow(() -> new RuntimeException("Delivery not found"));
    }

    public List<Delivery> getDeliveriesByCountryID(Integer id) {
        List<Delivery> deliveryList = deliveryRepository.findAll()
                .stream()
                .filter( d -> d.getSupply().getId() == id || d.getDemand().getId() == id)
                .collect(Collectors.toList());
        return deliveryList;
    }

}
