package com.sharevax.core.service;

import com.sharevax.core.model.Country;
import com.sharevax.core.model.Demand;
import com.sharevax.core.model.Harbor;
import com.sharevax.core.model.Supply;
import com.sharevax.core.model.event.Event;
import com.sharevax.core.model.RoutePlan;
import eu.europa.ec.eurostat.jgiscotools.feature.Feature;
import eu.europa.ec.eurostat.jgiscotools.util.GeoDistanceUtil;
import eu.europa.ec.eurostat.searoute.SeaRouting;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RouteService {

    private final int SPEED_FACTOR = 500;
    private final SeaRouting seaRouting;
    private final EventService eventService;

    public RouteService(EventService eventService) {
        this.seaRouting = new SeaRouting();
        this.eventService = eventService;
    }

    public double findShortestDistanceBetweenDemandAndSupply(Demand demand, Supply supply) {
        /**
         * Find the shortest distance between a demand and a supply
         * Wrapper around getDistanceBetweenCountries for ease of use
         */
        return getDistanceBetweenCountries(demand.getCountry(), supply.getCountry());
    }

    /**
     * Returns the distance between two closest harbors of all demanding and supplying country pairs
     *
     * @param demandingCountries
     * @param providingCountries
     * @return
     */
    public HashMap<ImmutablePair<Country, Country>, Double> findDistancesAmongCountryPairs(
            List<Country> demandingCountries, List<Country> providingCountries) {

        // pair[0] is the demanding country, pair[1] is the providing country
        HashMap<ImmutablePair<Country, Country>, Double> distances = new HashMap<>();

        for (Country demandingCountry : demandingCountries) {
            for (Country providingCountry : providingCountries) {
                var distance = getDistanceBetweenCountries(demandingCountry, providingCountry);
                distances.put(new ImmutablePair<>(demandingCountry, providingCountry), distance);
            }
        }

        return distances;
    }

    // get distance between closest pair of harbors between 2 countries
    public double getDistanceBetweenCountries(Country country, Country country2) {
        List<Harbor> country1Harbors = country.getHarbors();
        List<Harbor> country2Harbors = country2.getHarbors();

        double minDistance = Double.MAX_VALUE;
        for (Harbor harbor : country1Harbors) {
            for (Harbor harbor2 : country2Harbors) {
                double distance = getDistanceBetweenHarbors(harbor, harbor2);
                if (distance < minDistance) {
                    minDistance = distance;
                }
            }
        }
        return minDistance;
    }

    private double getDistanceBetweenHarbors(Harbor harbor, Harbor harbor2) {
        // Utilize searoute library to find distance
        Point harbor1Coordinates = harbor.getCoordinate();
        Point harbor2Coordinates = harbor2.getCoordinate();

        Feature route;
        try {
            route = getRoute(harbor1Coordinates, harbor2Coordinates);
        } catch (Exception e) {
            System.out.println("Error finding route between " + harbor.getName() + " and " + harbor2.getName());
            return Double.MAX_VALUE;
        }

        return getDistance(route);
    }

    public RoutePlan adaptRoute(LineString routeHistory, LineString futureRoute, Harbor destinationHarbor) {

        List<Coordinate> routeHistoryCoordinates = getCoordinates(routeHistory);
        List<Coordinate> futureRouteCoordinates = getCoordinates(futureRoute);

        Coordinate arriveAt = futureRouteCoordinates.get(0);
        double duration = 0;
        while (futureRouteCoordinates.size() > 0) {
            duration += (arriveAt.distance(futureRouteCoordinates.get(0)) * 1000) / SPEED_FACTOR;
            arriveAt = futureRouteCoordinates.get(0);
            routeHistoryCoordinates.add(arriveAt);
            futureRouteCoordinates.remove(0);
            if (duration >= 1) {
                break;
            }
        }

        routeHistoryCoordinates.add(arriveAt);
        var daysToNextStop = 1;
        boolean finalDestinationReached = futureRoute.isEmpty() || futureRoute.isRing();
        if (finalDestinationReached) {
            futureRouteCoordinates = new CoordinateList();
        } else {
            var finalStop = futureRoute.getEndPoint().getCoordinate();
            if (destinationHarbor.isClosed()) {
                // if destination harbor is closed, find the closest harbor and route to it
                destinationHarbor = findClosestOpenHarbor(destinationHarbor);
                finalStop = destinationHarbor.getCoordinate().getCoordinate();
            }

            Feature futurePath = getRoute(getPoint(arriveAt), getPoint(finalStop));
            Coordinate[] coordinates = futurePath.getGeometry().getCoordinates();
            futureRouteCoordinates = Arrays.asList(coordinates).subList(1, coordinates.length);
            futureRouteCoordinates = futureRouteCoordinates.stream()
                    .filter(c -> !routeHistoryCoordinates.contains(c)).toList();
        }

        futureRoute = getLineString(futureRouteCoordinates);
        routeHistory = getLineString(routeHistoryCoordinates);
        daysToNextStop = getDaysToNextPoint(routeHistory.getEndPoint(), futureRoute.getStartPoint());

        return new RoutePlan(routeHistory, futureRoute, daysToNextStop, destinationHarbor);
    }

    public LineString getLineWithAddedPoints(LineString routeHistory, Coordinate startCoordinate,
                                             LineString futureRoute, int daysToNextStop) {
        List<Coordinate> historyCoordinates = new ArrayList<>();
        if (!routeHistory.isEmpty()) {
            historyCoordinates.addAll(getCoordinates(routeHistory));
        } else {
            historyCoordinates.add(startCoordinate);
        }

        Coordinate start = historyCoordinates.get(historyCoordinates.size() - 1);
        Point nextStop = futureRoute.getStartPoint();
        Coordinate coordinate = getMiddlePoint(start, nextStop.getCoordinate(),
                daysToNextStop);
        historyCoordinates.add(coordinate);
        return getLineString(historyCoordinates);
    }

    private Harbor findClosestOpenHarbor(Harbor destinationHarbor) {
        var destinationCountry = destinationHarbor.getCountry();
        var harbors = destinationCountry.getHarbors();
        var destinationCoordinate = destinationHarbor.getCoordinate().getCoordinate();
        var minDistance = Integer.MAX_VALUE;
        Harbor closestHarbor = null;
        for (Harbor harbor : harbors) {
            if (harbor.isClosed()) {
                continue;
            }
            var harborCoordinate = harbor.getCoordinate().getCoordinate();
            var distance = getDistanceInDays(getPoint(destinationCoordinate), getPoint(harborCoordinate));
            if (distance < minDistance) {
                minDistance = distance;
                closestHarbor = harbor;
            }
        }

        return closestHarbor;
    }

    public List<Coordinate> getCoordinates(LineString lineString) {
        if (lineString == null) {
            return new ArrayList<>();
        }

        return new ArrayList<>(Arrays.asList(lineString.getCoordinates()));
    }

    private Point getPoint(Coordinate coordinate) {
        return new GeometryFactory().createPoint(coordinate);
    }

    public LineString getLineString(Feature route) {
        Coordinate[] coordinates = route.getGeometry().getCoordinates();
        return getLineString(List.of(coordinates));
    }

    public LineString getLineString(List<Coordinate> coordinates) {
        if (coordinates.size() == 1) {    // LineString (size must be 0 or >= 2)
            coordinates.add(coordinates.get(0));
        }
        return new GeometryFactory().createLineString(
            coordinates.toArray(new Coordinate[0]));
    }

    private Coordinate getMiddlePoint(Coordinate start, Coordinate end, int daysLeft) {
        if (daysLeft < 1) {
            return start;
        }
        double totalDistance = start.distance(end) * 1000;
        int totalDay = (int) totalDistance / SPEED_FACTOR;
        if (totalDay < 1) {
            return start;
        }
        double x = (end.getX() - start.getX()) * (totalDay - daysLeft) / totalDay + start.getX();
        double y = (end.getY() - start.getY()) * (totalDay - daysLeft) / totalDay + start.getY();
        return new Coordinate(x, y);
    }

    public Feature getRoute(Point start, Point end) {
        double long1 = start.getX();
        double lat1 = start.getY();

        double long2 = end.getX();
        double lat2 = end.getY();

        // find all blocked paths
        List<Event> activeEvents = eventService.getActiveEvents();
        Set<String> blockedStraitNames = eventService.findBlockedStraits(activeEvents);
        Set<String> blockedPassageNames = eventService.findBlockedPassages(activeEvents);
        Set<String> blockedChannelNames = eventService.findBlockedChannels(activeEvents);
        Feature route = seaRouting.getRoute(long1, lat1, long2, lat2,
                blockedChannelNames.contains("SUEZ"), blockedChannelNames.contains("PANAMA"),
                blockedStraitNames.contains("MALACCA"), blockedStraitNames.contains("GIBRALTAR"),
                blockedStraitNames.contains("DOVER"),
                blockedStraitNames.contains("BERING"), blockedStraitNames.contains("MAGELLAN"),
                blockedStraitNames.contains("BAB_EL_MANDEB"),
                blockedChannelNames.contains("KIEL"), blockedChannelNames.contains("CORINTH"),
                blockedPassageNames.contains("NORTHWEST"), blockedPassageNames.contains("NORTHEAST"));

        Geometry geometry = route.getGeometry();
        Coordinate s = geometry.getCoordinate();
        if (s.getX() != (start.getX())) {
            route.setGeometry(geometry.reverse());
        }

        return route;
    }

    private double getDistance(Feature route) {
        MultiLineString geometry = (MultiLineString) route.getGeometry();
        return GeoDistanceUtil.getLengthGeoKM(geometry);
    }

    private double getDistance(Point start, Point end) {
        LineString lineString = getLineString(List.of(start.getCoordinate(), end.getCoordinate()));
        return GeoDistanceUtil.getLengthGeoKM(lineString);
    }

    private double getDistance(Coordinate start, Coordinate end) {
        LineString lineString = getLineString(List.of(start, end));
        return GeoDistanceUtil.getLengthGeoKM(lineString);
    }

    public int getDaysToNextPoint(Point p1, Point p2) {
        double distance = getDistance(p1, p2);
        return (int) (distance / SPEED_FACTOR);
    }

    public int getDaysToNextStop(LineString routeHistory, LineString futureRoute) {
        if (futureRoute.isEmpty()) {
            return 0;
        }

        Point current = routeHistory.getEndPoint();
        Point next = futureRoute.getStartPoint();

        int duration = getDistanceInDays(current, next);

        // correction for two points being too close together
        duration = duration > 0 ? duration : 1;

        return duration;
    }

    public int getDistanceInDays(Feature route) {
        return (int) (getDistance(route) / SPEED_FACTOR);
    }

    public int getDistanceInDays(Harbor harbor, Harbor harbor2) {
        return getDistanceInDays(harbor.getCoordinate(), harbor2.getCoordinate());
    }

    public int getDistanceInDays(Point start, Point end) {
        double distance = getDistance(getRoute(start, end));
        int days = (int) distance / SPEED_FACTOR;
        return days;
    }

}
