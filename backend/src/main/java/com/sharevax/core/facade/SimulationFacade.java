package com.sharevax.core.facade;

import com.sharevax.core.model.*;
import com.sharevax.core.repository.DeliveryRepository;
import com.sharevax.core.service.*;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Service
public class SimulationFacade {
    private final SupplyService supplyService;
    private final DemandService demandService;
    private final CountryService countryService;
    private final DeliveryService deliveryService;
    private final RouteService routeService;
    private final DeliveryRepository deliveryRepository;

    public SimulationFacade(SupplyService supplyService, DemandService demandService,
                            CountryService countryService, RouteService routeService,
                            DeliveryService deliveryService,
                            DeliveryRepository deliveryRepository) {

        this.supplyService = supplyService;
        this.demandService = demandService;
        this.countryService = countryService;
        this.routeService = routeService;
        this.deliveryService = deliveryService;
        this.deliveryRepository = deliveryRepository;
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
                                   Supply supply, Demand demand) {

        // init futureRoute = [startHarbor - destinationHarbor]
        LineString futureRoute = routeService.getLineString(startHarbor,destinationHarbor);

        // init routeHistory = startHarbor
        Coordinate startHarborCordinate = new Coordinate(startHarbor.getCoordinate().getX(), startHarbor.getCoordinate().getY());
        Coordinate[] routeHistoryCoordinates = new Coordinate[] {startHarborCordinate, startHarborCordinate}; // number of points in LineString must be 0 or >= 2
        LineString routeHistory = new GeometryFactory().createLineString(routeHistoryCoordinates);

        // calculate the estimatedArrivalDate
        int days = routeService.getTotalDeliveryDays(startHarbor,destinationHarbor);  // calculate the delivery time bewteen two harbor
        Date estimatedArrivalDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * days);

        int remainingDaysToNextHarbor = routeService.getDaysToNextStop(routeHistory,futureRoute);
        return deliveryService.createDelivery(startHarbor, destinationHarbor, estimatedArrivalDate, supply, demand, routeHistory,futureRoute,remainingDaysToNextHarbor);
    }

    public List<Demand> getUnmatchedDemands() {
        return demandService.findUnmatchedDemands();
    }

    /**
     * update the route for all the deliveries every day [v2:according to the current harbor situation]
     */
    public void updateShipLocations(){
        // Update delivery routes every day
        // Update fields routeHistory, futureRoute and remainingDaysToNextHarbor
        // Also set updatedAt field of delivery after updating.
        List<Delivery> deliveries = deliveryRepository.findActiveDeliveries();
        for(Delivery delivery: deliveries){
            delivery.setUpdatedAt(new Date());
            int dayCounter = delivery.getRemainingDaysToNextHarbor();
            dayCounter--;

            // update the route for the transport that has arrived
            if(dayCounter<=0){
                try{
                    LineString routeHistory = delivery.getRouteHistory();
                    LineString futureRoute = delivery.getFutureRoute();
                    Coordinate arriveAT =  futureRoute.getCoordinateN(0);

                    // calculate the updated route history
                    List<Coordinate> routeHistoryList = new ArrayList<Coordinate>();
                    routeHistoryList.addAll(Arrays.asList(routeHistory.getCoordinates()));
                    routeHistoryList.add(arriveAT);
                    routeHistory = new GeometryFactory().createLineString(routeHistoryList.toArray(new Coordinate[routeHistoryList.size()]));

                    // The shipment has arrived at the end of the line
                    if(routeHistory.getEndPoint().equals(delivery.getDestinationHarbor().getCoordinate())){
                        delivery.setDeliveryStatus(Delivery.DeliveryStatus.DELIVERED);
                    }
                    else{
                        // calculate the future route
                        futureRoute = routeService.getLineStringFromPoints(routeHistory.getEndPoint(),delivery.getDestinationHarbor().getCoordinate());
                        dayCounter = routeService.getDaysToNextStop(routeHistory,futureRoute);
                        delivery.setFutureRoute(futureRoute);

                    }

                    // check if delayed
                    int wholeDuration = routeService.getTotalDeliveryDays(routeHistory,futureRoute);
                    Date newEstimatedArrivalDate = new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * wholeDuration);
                    if(delivery.getEstimatedArrivalDate().compareTo(newEstimatedArrivalDate)<0){
                        delivery.setDeliveryStatus(Delivery.DeliveryStatus.DELAYED);
                    }
                    else{
                        delivery.setDeliveryStatus(Delivery.DeliveryStatus.IN_TIME);
                    }

                    // save the updated route history
                    delivery.setRouteHistory(routeHistory);
                    deliveryRepository.save(delivery);
                }
                catch (NullPointerException e){
                    System.out.println(e.getMessage());
                }
            }
            delivery.setRemainingDaysToNextHarbor(dayCounter);
            delivery.setUpdatedAt(new Date());
            deliveryRepository.save(delivery);
        }



    }
    public List<Supply> getUnmatchedSupplies() {
        return supplyService.findUnmatchedSupplies();
    }
}
