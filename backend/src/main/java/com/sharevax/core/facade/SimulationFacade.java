package com.sharevax.core.facade;

import com.sharevax.core.model.*;
import com.sharevax.core.model.RoutePlan;
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

    private final SuggestionService suggestionService;

    public SimulationFacade(SupplyService supplyService, DemandService demandService,
                            CountryService countryService, RouteService routeService,
                            DeliveryService deliveryService, SuggestionService suggestionService, EventService eventService) {

        this.supplyService = supplyService;
        this.demandService = demandService;
        this.countryService = countryService;
        this.routeService = routeService;
        this.deliveryService = deliveryService;
        this.suggestionService = suggestionService;
        this.eventService = eventService;
    }

    public Suggestion createSuggestion(Supply supply, Demand demand) {
        return suggestionService.createSuggestion(supply, demand);
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

    public void saveInitialSuggestion(Demand d, Supply s) {
        demandService.saveDemand(d);
        supplyService.saveSupply(s);
        suggestionService.createSuggestion(s, d);
    }

    public List<Demand> getUnmatchedDemands() {
        return demandService.findUnmatchedDemands();
    }


    public RoutePlan adaptRoute(LineString routeHistory, LineString futureRoute, Harbor destinationHarbor,
                                Date estimatedArrivalDate, Date currentDay, Country arriveCountry) {
        return routeService.adaptRoute(routeHistory, futureRoute, destinationHarbor, estimatedArrivalDate, currentDay, arriveCountry);
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

    public void processEvents() {
        eventService.processEvents();
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

    public void deleteAllSuggestions(){ suggestionService.deleteAllSuggestions();}

    public void wipeOutSuggestions() {
        suggestionService.wipeOutSuggestions();
    }

    public void resetEvents() {
        eventService.resetEvents();
    }
}
