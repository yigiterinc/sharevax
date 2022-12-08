package com.sharevax.core.service;

import com.sharevax.core.model.Country;
import com.sharevax.core.model.Demand;
import com.sharevax.core.model.Harbor;
import com.sharevax.core.model.Supply;
import eu.europa.ec.eurostat.jgiscotools.feature.Feature;
import eu.europa.ec.eurostat.jgiscotools.util.GeoDistanceUtil;
import eu.europa.ec.eurostat.searoute.SeaRouting;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.locationtech.jts.geom.*;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class RouteService {

    private final int SPEED_FACTOR = 1000;
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

        return getDistanceFromRoute(route);
    }

    /**
     * TODO v2 block-situation
     * @param harbor1 start Harbor
     * @param harbor2 destination Harbor
     * @return LineString
     */
    public LineString getLineString(Harbor harbor1, Harbor harbor2){

        // turn the Route into LineString
        Feature route = getRoute(harbor1.getCoordinate(),harbor2.getCoordinate());
        Coordinate[] coordinates = route.getGeometry().getCoordinates();
        LineString lineString = new GeometryFactory().createLineString(coordinates);

        return lineString;
    }

    public LineString getLineStringFromPoints(Point p1, Point p2){
        // turn the Route into LineString
        Feature route = getRoute(p1,p2);
        Coordinate[] coordinates = route.getGeometry().getCoordinates();
        LineString lineString = new GeometryFactory().createLineString(coordinates);
        return lineString;
    }

    public int getDaysToNextStop(LineString routeHistory,LineString futureRoute){
        Point last = routeHistory.getEndPoint();
        Point next = futureRoute.getPointN(0);
        double distance = getDistanceFromRoute(getRoute(last,next));
        int days = (int)distance/SPEED_FACTOR;
        return days;
    }

    public int getTotalDeliveryDays(Harbor harbor, Harbor harbor2){
        double distance = getDistanceBetweenHarbors(harbor,harbor2);
        int days = (int)distance/SPEED_FACTOR;
        System.out.println("Total Days:"+ days +" Distance:"+distance);
        return days;
    }

    public int getTotalDeliveryDays(LineString routeHistory,LineString futureRoute){
        Point last = routeHistory.getEndPoint();
        Point destination = futureRoute.getEndPoint();
        double distance = getDistanceFromRoute(getRoute(last,destination));
        int days = (int)distance/SPEED_FACTOR;
        return days;
    }

    private Feature getRoute(Point start, Point end) {
        double long1 = start.getX();
        double lat1 = start.getY();

        double long2 = end.getX();
        double lat2 = end.getY();

        return seaRouting.getRoute(long1, lat1, long2, lat2);
    }

    private double getDistanceFromRoute(Feature route) {
        MultiLineString geometry = (MultiLineString) route.getGeometry();
        return GeoDistanceUtil.getLengthGeoKM(geometry);
    }
}
