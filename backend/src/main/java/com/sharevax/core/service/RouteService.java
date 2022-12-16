package com.sharevax.core.service;

import com.sharevax.core.model.Country;
import com.sharevax.core.model.Demand;
import com.sharevax.core.model.Harbor;
import com.sharevax.core.model.Supply;
import com.sharevax.core.model.route.RoutePlan;
import eu.europa.ec.eurostat.jgiscotools.feature.Feature;
import eu.europa.ec.eurostat.jgiscotools.util.GeoDistanceUtil;
import eu.europa.ec.eurostat.searoute.SeaRouting;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

@Service
public class RouteService {

    private final int SPEED_FACTOR = 200;
    private final CountryService countryService;
    private final SeaRouting seaRouting;

    public RouteService(CountryService countryService) {
        this.countryService = countryService;
        this.seaRouting = new SeaRouting();
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


    // update the route based on ship's velocity
    public RoutePlan adaptRoute(LineString routeHistory, LineString futureRoute) {

        List<Coordinate> routeHistoryList = getCoordinates(routeHistory);
        List<Coordinate> futureRouteList = getCoordinates(futureRoute);

        int duration = 0;

        if (futureRoute.isRing()) {    // arrive at the destination
            Coordinate arriveAt = futureRouteList.get(0);
            routeHistoryList.add(arriveAt);
            futureRouteList = new CoordinateList();
        } else {

            Coordinate start = futureRouteList.get(0);
            while (duration < 1) {

                Coordinate arriveAt = futureRouteList.get(0);
                routeHistoryList.add(arriveAt);
                futureRouteList.remove(0);

                duration = getDeliveryDays(getPoint(start), getPoint(futureRouteList.get(0)));
                if (futureRouteList.size() == 1) {
                    // The last stop must be taken on a separately.
                    // Extreme case:
                    // current distance - the second last stop = 0.5 days
                    // current position - last stop  = 10 days
                    duration = duration > 0 ? duration : 1;
                    break;
                }
            }
        }

        routeHistory = getLineString(routeHistoryList);
        futureRoute = getLineString(futureRouteList);

        return new RoutePlan(routeHistory, futureRoute, duration);
    }

    public List<Coordinate> getCoordinates(LineString lineString) {
        List<Coordinate> coordinates = new ArrayList<Coordinate>();
        coordinates.addAll(Arrays.asList(lineString.getCoordinates()));
        return coordinates;
    }
    private Point getPoint(Coordinate coordinate) {
        return new GeometryFactory().createPoint(coordinate);
    }

    public LineString getLineString(List<Coordinate> coordinates) {
        if (coordinates.size() == 1) {
            coordinates.add(coordinates.get(0));
        }
        return new GeometryFactory().createLineString(
            coordinates.toArray(new Coordinate[coordinates.size()]));
    }

    public LineString getLineString(Harbor startHarbor, Harbor endHarbor) {
        return getLineString(startHarbor.getCoordinate(), endHarbor.getCoordinate());
    }

    public LineString getLineString(Point p1, Point p2) {

        // turn the Route into LineString
        Feature route = getRoute(p1, p2);

        // remove the first one
        Coordinate[] coordinates = route.getGeometry().getCoordinates();
        List<Coordinate> coordinatesList = new ArrayList<Coordinate>();
        coordinatesList.addAll(Arrays.asList(coordinates));
        coordinatesList.remove(0);

        return getLineString(coordinatesList);
    }

    private Feature getRoute(Point start, Point end) {
        double long1 = start.getX();
        double lat1 = start.getY();

        double long2 = end.getX();
        double lat2 = end.getY();

        return seaRouting.getRoute(long1, lat1, long2, lat2);
    }

    private double getDistance(Feature route) {
        MultiLineString geometry = (MultiLineString) route.getGeometry();
        return GeoDistanceUtil.getLengthGeoKM(geometry);
    }


    public int getDaysToNextStop(LineString routeHistory, LineString futureRoute) {

        if (futureRoute.isEmpty()) {
            return 0;
        }

        Point arriveAt = routeHistory.getEndPoint();
        Point nextStop = futureRoute.getStartPoint();

        int duration = getDeliveryDays(arriveAt, nextStop);

        // Automatic correction for two points being too close together
        duration = duration > 0 ? duration : 1;

        return duration;
    }

    public int getDeliveryDays(Harbor harbor, Harbor harbor2) {
        return getDeliveryDays(harbor.getCoordinate(), harbor2.getCoordinate());
    }

    public int getDeliveryDays(Point start, Point end) {
        double distance = getDistance(getRoute(start, end));
        int days = (int) distance / SPEED_FACTOR;
        return days;
    }

}
