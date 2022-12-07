package com.sharevax.core.facade;

import com.sharevax.core.model.*;
import com.sharevax.core.service.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class SimulationFacade {
    private final SupplyService supplyService;
    private final DemandService demandService;
    private final CountryService countryService;
    private final DeliveryService deliveryService;
    private final RouteService routeService;

    public SimulationFacade(SupplyService supplyService, DemandService demandService,
                            CountryService countryService, RouteService routeService,
                            DeliveryService deliveryService) {

        this.supplyService = supplyService;
        this.demandService = demandService;
        this.countryService = countryService;
        this.routeService = routeService;
        this.deliveryService = deliveryService;
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
                                   Date estimatedArrivalDate,
                                   Supply supply, Demand demand) {
        LineString futureRoute = routeService.getShortestRoute(startHarbor,destinationHarbor);

        Coordinate startHarborCordinate = new Coordinate(startHarbor.getCoordinate().getX(), startHarbor.getCoordinate().getY());
        Coordinate[] routeHistoryCoordinates = new Coordinate[] {startHarborCordinate, startHarborCordinate}; // number of points in LineString must be 0 or >= 2
        LineString routeHistory = new GeometryFactory().createLineString(routeHistoryCoordinates);

        return deliveryService.createDelivery(startHarbor, destinationHarbor, estimatedArrivalDate, supply, demand, routeHistory,futureRoute);
    }

    public List<Demand> getUnmatchedDemands() {
        return demandService.findUnmatchedDemands();
    }

    public List<Supply> getUnmatchedSupplies() {
        return supplyService.findUnmatchedSupplies();
    }
}
