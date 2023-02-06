package com.sharevax.core.facade;

import com.sharevax.core.model.*;
import com.sharevax.core.serializer.RoutePlanDto;
import com.sharevax.core.service.*;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class SimulationFacade {
    private final SupplyService supplyService;
    private final DemandService demandService;
    private final CountryService countryService;
    private final DeliveryService deliveryService;
    private final RouteService routeService;
    private final EventService eventService;

    public SimulationFacade(SupplyService supplyService, DemandService demandService,
                            CountryService countryService, RouteService routeService,
                            DeliveryService deliveryService, EventService eventService) {

        this.supplyService = supplyService;
        this.demandService = demandService;
        this.countryService = countryService;
        this.routeService = routeService;
        this.deliveryService = deliveryService;
        this.eventService = eventService;
    }

    public List<Supply> getAllSupplies() {
        return supplyService.getAllSupplies();
    }

    public List<Demand> getAllDemands() {
        return demandService.getAllDemands();
    }

    public List<Country> getProvidingCountries() {
        return demandService.getProvidingCountries();
    }

    public List<Country> getRequestingCountries() {
        return supplyService.getRequestingCountries();
    }

    public List<Country> getAllCountries() {
        return countryService.getAllCountries();
    }

    public double findShortestDistanceBetweenDemandAndSupply(Demand demand, Supply supply) {
        return routeService.findShortestDistanceBetweenDemandAndSupply(demand, supply);
    }

    public Delivery createDelivery(Harbor startHarbor, Harbor destinationHarbor,
                                   Supply supply, Demand demand, Date createdAt) {
        return deliveryService.createDelivery(startHarbor, destinationHarbor, supply, demand, createdAt);
    }

    public List<Demand> getUnmatchedDemands() {
        return demandService.findUnmatchedDemands();
    }


    public RoutePlanDto updateShipRoute(LineString routeHistory, LineString futureRoute) {
        return routeService.updateShipRoutes(routeHistory, futureRoute);
    }

    public List<Delivery> findActiveDeliveries() {
        return deliveryService.findActiveDeliveries();
    }

    public void saveDelivery(Delivery delivery) {
        deliveryService.save(delivery);
    }

    public List<Supply> getUnmatchedSupplies() {
        return supplyService.findUnmatchedSupplies();
    }

    public void resetCountryData() {
        countryService.resetCountryData();
    }

    public void deleteAllDeliveries() {
        deliveryService.deleteAll();
    }

    public void deleteAllSupplies() {
        supplyService.deleteAll();
    }

    public void deleteAllDemands() {
        demandService.deleteAll();
    }

    public void processEvents() {
        eventService.processEvents();
    }

}
