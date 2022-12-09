package com.sharevax.core.facade;

import com.sharevax.core.model.*;
import com.sharevax.core.repository.DeliveryRepository;
import com.sharevax.core.service.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.*;

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
                                   Supply supply, Demand demand, Date simulatedTodayDate) {

        // init futureRoute = [startHarbor - destinationHarbor]
        LineString futureRoute = routeService.getLineString(startHarbor,destinationHarbor);

        // init routeHistory = startHarbor
        Coordinate startHarborCordinate = new Coordinate(startHarbor.getCoordinate().getX(), startHarbor.getCoordinate().getY());
        Coordinate[] routeHistoryCoordinates = new Coordinate[] {startHarborCordinate, startHarborCordinate}; // number of points in LineString must be 0 or >= 2
        LineString routeHistory = new GeometryFactory().createLineString(routeHistoryCoordinates);

        // calculate the estimatedArrivalDate
        int days = routeService.getTotalDeliveryDays(startHarbor,destinationHarbor);  // calculate the delivery time bewteen two harbor
        Date estimatedArrivalDate = getEstimatedDate(days, simulatedTodayDate);

        int remainingDaysToNextHarbor = routeService.getDaysToNextStop(routeHistory,futureRoute);
        return deliveryService.createDelivery(startHarbor, destinationHarbor, estimatedArrivalDate, supply, demand, routeHistory,futureRoute,remainingDaysToNextHarbor, simulatedTodayDate);
    }

    public List<Demand> getUnmatchedDemands() {
        return demandService.findUnmatchedDemands();
    }

    private Date getEstimatedDate(int duration,Date simulatedTodayDate){
        Calendar calendar = new GregorianCalendar();
        calendar.setTime(simulatedTodayDate);
        calendar.add(calendar.DATE, duration);
        return calendar.getTime();
    }

    /**
     * update the route for all the deliveries every day [v2:according to the current harbor situation]
     */
    public void updateShipLocations(Date simulatedTodayDate){
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
                        //futureRoute = routeService.getLineStringFromPoints(routeHistory.getEndPoint(),delivery.getDestinationHarbor().getCoordinate());
                        ImmutablePair<LineString,LineString> routePair = routeService.adaptRoute(routeHistory,futureRoute);
                        routeHistory = routePair.getLeft();
                        futureRoute = routePair.getRight();
                        dayCounter = routeService.getDaysToNextStop(routeHistory,futureRoute);
                        delivery.setRouteHistory(routeHistory);
                        delivery.setFutureRoute(futureRoute);

                        // calculate the updated delivery date
                        int wholeDuration = routeService.getRemainingDeliveryDays(routeHistory,futureRoute);
                        Date newEstimatedArrivalDate = getEstimatedDate(wholeDuration,simulatedTodayDate);
                        delivery.setCurrentArrivalDate(newEstimatedArrivalDate);
                        System.out.println("Current arrival date:"+newEstimatedArrivalDate);

                        // check if delayed
                        long from = delivery.getEstimatedArrivalDate().getTime();
                        long to = newEstimatedArrivalDate.getTime();
                        int dayDiff = (int) ((to - from) / (1000 * 60 * 60 * 24));
                        if(dayDiff>1){
                            delivery.setDeliveryStatus(Delivery.DeliveryStatus.DELAYED);
                        }
                        else{
                            delivery.setDeliveryStatus(Delivery.DeliveryStatus.IN_TIME);
                        }
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
            delivery.setUpdatedAt(simulatedTodayDate);
            deliveryRepository.save(delivery);
        }
    }

    public List<Supply> getUnmatchedSupplies() {
        return supplyService.findUnmatchedSupplies();
    }
}
