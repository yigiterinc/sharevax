package com.sharevax.core.service;

import com.sharevax.core.facade.SimulationFacade;
import com.sharevax.core.model.Country;
import com.sharevax.core.model.Delivery;
import com.sharevax.core.model.Delivery.DeliveryStatus;
import com.sharevax.core.model.Demand;
import com.sharevax.core.model.Supply;
import com.sharevax.core.model.RoutePlan;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.locationtech.jts.geom.LineString;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SimulationService {

    private static int DAY_COUNTER = 0;

    private final SimulationFacade simulationFacade;

    // Coefficient of distance between two harbors
    private static double DISTANCE_FACTOR = 1;

    // The coefficient of urgency in calculating the urgency score
    private static double URGENCY_FACTOR = 1;

    private static double GIVEN_URGENCY_FACTOR = 1;

    // How much we take into account the Vaccination Rate of the country when calculating urgency
    private static final double VACCINATION_RATE_FACTOR = 1;

    // Daily vaccine consumption / stock ratio to determine if the country is in need of vaccines, increase urgency
    private static final double DAILY_VAX_CONSUMPTION_TO_STOCK_FACTOR = 1;


    public SimulationService(SimulationFacade simulationFacade) {
        this.simulationFacade = simulationFacade;
    }

    public void simulateDay() {
        DAY_COUNTER++;
        simulationFacade.processEvents();
        updateShipLocations();

        updateVaccineStocksAndVaccinationRates();

        matchSupplyAndDemand();
    }

    public void resetSimulation() {
        DAY_COUNTER = 0;
        resetDatabaseState();
    }

    private void resetDatabaseState() {
        // delete all deliveries
        simulationFacade.deleteAllDeliveries();
        simulationFacade.deleteAllSuggestions();
        // delete all supplies
        simulationFacade.deleteAllSupplies();

        // delete all demands
        simulationFacade.deleteAllDemands();

        // reset country data
        simulationFacade.resetCountryData();
    }

    public void matchSupplyAndDemand() {
        // Get current set of supplies
        // Get current set of demands
        // Match them intelligently

        var supplies = simulationFacade.getUnmatchedSupplies();
        var demands = simulationFacade.getUnmatchedDemands();

        if (supplies.isEmpty() || demands.isEmpty()) {
            return;
        }

        Map<Demand.Urgency, Integer> enumToUrgencyScore = new HashMap<>(){{
                put(Demand.Urgency.NORMAL, 1);
                put(Demand.Urgency.URGENT, 2);
                put(Demand.Urgency.CRITICAL, 3);
        }};

        HashMap<Demand, Integer> givenDemandUrgencies = demands.stream()
                .collect(HashMap::new, (m, v) -> m.put(v, enumToUrgencyScore.get(v.getUrgency())), HashMap::putAll);

        HashMap<Demand, Double> demandToVaccinationRates = new HashMap<>();
        for (Demand demand : demands) {
            demandToVaccinationRates.put(demand, demand.getCountry().getVaccinationRate());
        }

        HashMap<Demand, Double> demandToDailyVaccineConsumptionToStock = new HashMap<>();
        for (Demand demand : demands) {
            demandToDailyVaccineConsumptionToStock.put(demand,
                    demand.getCountry()
                            .getDailyVaccineConsumption().doubleValue() / demand.getCountry().getVaccineStock().doubleValue());
        }

        var estimatedUrgencyScores = getEstimatedUrgencyScores(
                givenDemandUrgencies,
                demandToVaccinationRates,
                demandToDailyVaccineConsumptionToStock);

        var distanceScores = getDistanceScoresForPairs(supplies, demands);
        var matchedBestPairs = findBestPairs(demands, estimatedUrgencyScores, distanceScores);

        for (var demand : matchedBestPairs.keySet()) {
            var supply = matchedBestPairs.get(demand);
            if (supply != null) {
                boolean isSameCountry = supply.getCountry().getId() == demand.getCountry().getId();
                if (!isSameCountry) {
                    System.out.println("Matched " + demand + " with " + supply);
                    simulationFacade.createSuggestion(supply, demand);
                }
            }
        }
    }

    private HashMap<Demand, Supply> findBestPairs(List<Demand> demands,
                               HashMap<Demand, Double> estimatedUrgencyScores,
                               HashMap<Demand, ImmutablePair<Double, Supply>> distanceScores) {
        // Sort demands by distance and urgency
        // Use cumulative score to sort demands
        HashMap<Demand, Double> demandToCumulativeScore = new HashMap<>();
        HashMap<Demand, Supply> demandToClosestSupply = new HashMap<>();

        for (Demand demand : demands) {
            var urgencyScore = estimatedUrgencyScores.get(demand);
            var distanceScore = distanceScores.get(demand).getLeft();
            demandToCumulativeScore.put(demand, calculateCumulativeScore(urgencyScore, distanceScore));
            demandToClosestSupply.put(demand, distanceScores.get(demand).getRight());
        }

        var maxCumulativeScore = demandToCumulativeScore.values().stream().max(Double::compareTo).get();
        var minCumulativeScore = demandToCumulativeScore.values().stream().min(Double::compareTo).get();

        // Normalize the cumulative scores based on max and min
        for (Demand demand : demands) {
            var cumulativeScore = demandToCumulativeScore.get(demand);
            var normalizedCumulativeScore = (cumulativeScore - minCumulativeScore) / (maxCumulativeScore - minCumulativeScore);
            demandToCumulativeScore.put(demand, normalizedCumulativeScore);
        }

        // Sort descending based on demandToCumulativeScore
        demands.sort((d1, d2) -> demandToCumulativeScore.get(d2).compareTo(demandToCumulativeScore.get(d1)));

        // Match demands to supplies
        HashMap<Demand, Supply> demandToSupply = new HashMap<>();

        // Until all supplies are matched, or all demands are matched
        while (!demands.isEmpty() && !demandToClosestSupply.isEmpty()) {
            // Get the demand with the highest cumulative score
            var demand = demands.get(0);
            var supply = demandToClosestSupply.get(demand);

            // Match the demand to the supply
            demandToSupply.put(demand, supply);

            // Remove the demand from the list of demands
            demands.remove(demand);

            // Remove the supply from the list of supplies
            demandToClosestSupply.remove(demand);
        }

        return demandToSupply;
    }

    private double calculateCumulativeScore(double urgencyScore, double distanceScore) {
        return urgencyScore * URGENCY_FACTOR + distanceScore * DISTANCE_FACTOR;
    }

    private HashMap<Demand, Double> getEstimatedUrgencyScores(HashMap<Demand, Integer> givenDemandUrgencies,
                                                              HashMap<Demand, Double> demandToVaccinationRates,
                                        HashMap<Demand, Double> demandToDailyVaccineConsumptionToStock) {
        /**
         * Calculate the urgency score for each demand, based on the following factors:
         * 1. Given urgency scores
         * 2. Demand to vaccination rates
         * 3. Demand to daily vaccine consumption to stock
         * Normalize these values and calculate the score,
         * Based on the defined static coefficients
         */
        HashMap<Demand, Double> demandToUrgencyScores = new HashMap<>();
        // Normalize each score factor
        double maxGivenUrgencyScore = givenDemandUrgencies.values().stream().mapToDouble(Integer::doubleValue).max().getAsDouble();

        double maxVaccinationRate = demandToVaccinationRates.values().stream().mapToDouble(Double::doubleValue).max().getAsDouble();
        double maxDailyVaccineConsumptionToStock = demandToDailyVaccineConsumptionToStock.values().stream().mapToDouble(Double::doubleValue).max().getAsDouble();

        for (Demand demand : givenDemandUrgencies.keySet()) {
            double urgencyScore = calculateUrgencyScore(givenDemandUrgencies.get(demand), maxGivenUrgencyScore,
                    demandToVaccinationRates.get(demand), maxVaccinationRate,
                    demandToDailyVaccineConsumptionToStock.get(demand), maxDailyVaccineConsumptionToStock);

            demandToUrgencyScores.put(demand, urgencyScore);
        }

        return demandToUrgencyScores;
    }

    private double calculateUrgencyScore(Integer givenUrgency, double maxGivenUrgency,
                                         Double vaccinationRate, double maxVaccinationRate,
                                         Double dailyVaccineConsumptionToStock, double maxDailyVaccineConsumptionToStock) {
         
        return givenUrgency / maxGivenUrgency * GIVEN_URGENCY_FACTOR +
                // lower the vaccination rate, higher the score should be
                (maxVaccinationRate - vaccinationRate) / maxVaccinationRate * VACCINATION_RATE_FACTOR +
                dailyVaccineConsumptionToStock / maxDailyVaccineConsumptionToStock * DAILY_VAX_CONSUMPTION_TO_STOCK_FACTOR;
    }

    private HashMap<Demand, ImmutablePair<Double, Supply>> getDistanceScoresForPairs(List<Supply> supplies,
                                                                                     List<Demand> demands) {
        /**
         * Calculate the distance score for each supply-demand pair which is inverse of the shortest distance
         * Return the demand -> (distance score, closestSupply) pair
         */
        HashMap<Demand, ImmutablePair<Double, Supply>> distanceScores = new HashMap<>();
        for (Demand demand : demands) {
            double minDistance = Double.MAX_VALUE;
            Supply closestSupply = null;

            // For each demand we find the closest supply
            for (Supply supply : supplies) {
                double distance = simulationFacade.findShortestDistanceBetweenDemandAndSupply(demand, supply);
                if (distance < minDistance) {
                    minDistance = distance;
                    closestSupply = supply;
                }
            }

            var distanceScore = calculateDistanceScore(minDistance);

            distanceScores.put(demand, new ImmutablePair<>(distanceScore, closestSupply));
        }

        // Normalize the distance scores to be between 0 and 1, 1 being the best and closest pair
        double maxDistanceScore = distanceScores.values().stream().mapToDouble(ImmutablePair::getLeft).max().getAsDouble();
        for (Demand demand : distanceScores.keySet()) {
            distanceScores.put(demand,
                    new ImmutablePair<>(distanceScores.get(demand).getLeft() / maxDistanceScore,
                            distanceScores.get(demand).getRight()));
        }

        return distanceScores;
    }

    private double calculateDistanceScore(double shortestDistanceBetweenHarbors) {
        // Score is the inverse of the distance
        return 1 / shortestDistanceBetweenHarbors;
    }

    private void updateVaccineStocksAndVaccinationRates() {
        // update vaccine stock based on vaccine production and consumption
        var countries = simulationFacade.getAllCountries();
        for (Country country : countries) {
            var production = country.getDailyVaccineProduction();

            var dailyConsumption = country.getDailyVaccineConsumption();
            var stock = country.getVaccineStock();
            var consumption = dailyConsumption.compareTo(stock) < 0 ? dailyConsumption : stock;

            BigInteger netVaccine = stock.add(production).subtract(consumption);
            if (netVaccine.compareTo(BigInteger.ZERO) < 0) {
                country.setVaccineStock(BigInteger.ZERO);
            } else {
                country.setVaccineStock(netVaccine);
            }

            var vaccinationRate = country.getVaccinationRate();
            double updatedVaccinationRate = vaccinationRate
                    + consumption.doubleValue() / country.getPopulation().doubleValue();

            if (updatedVaccinationRate > 1) {
                country.setVaccinationRate(1);
            } else {
                country.setVaccinationRate(updatedVaccinationRate);
            }
        }
    }

    /**
     *
     * @return simulated current date according to the offset DAY_COUNTER
     */
    public Date getCurrentDate() {

        // get real date
        LocalDateTime realTodayDate = LocalDateTime.now();

        // simulated date according to the offset DAY_COUNTER
        LocalDateTime simulatedTodayDate = realTodayDate.plusDays(DAY_COUNTER);

        // convert LocalDateTime to Date
        ZoneId zoneId = ZoneId.systemDefault();
        ZonedDateTime zonedDateTime = simulatedTodayDate.atZone(zoneId);

        return Date.from(zonedDateTime.toInstant());
    }

    public int getDayCounter() {
        return DAY_COUNTER;
    }

    private void updateShipLocations() {

        List<Delivery> deliveries = simulationFacade.findActiveDeliveries();

        for (Delivery delivery : deliveries) {

            int dayCounter = delivery.getDaysToNextPoint();
            dayCounter--;

            if (isDelayed(delivery.getEstimatedArrivalDate())) {
                delivery.setDeliveryStatus(DeliveryStatus.DELAYED);
            }

            // update the route for the transport that has arrived at next Point
            if (dayCounter <= 0) {

                LineString routeHistory = delivery.getRouteHistory();
                LineString futureRoute = delivery.getFutureRoute();

                RoutePlan routePlan = simulationFacade.adaptRoute(routeHistory, futureRoute, delivery.getDestinationHarbor());
                routeHistory = routePlan.getRouteHistory();
                futureRoute = routePlan.getFutureRoute();
                dayCounter = routePlan.getDuration();
                var destinationHarbor = routePlan.getDestinationHarbor();

                if (futureRoute.isEmpty()) { // arrive at the destination
                    delivery.setDeliveryStatus(Delivery.DeliveryStatus.DELIVERED);
                }
                delivery.setRouteHistory(routeHistory);
                delivery.setFutureRoute(futureRoute);
                delivery.setDestinationHarbor(destinationHarbor);
            }

            delivery.setDaysToNextPoint(dayCounter);
            delivery.setUpdatedAt(getCurrentDate());

            simulationFacade.saveDelivery(delivery);
        }
    }

    private boolean isDelayed(Date estimatedArrivalDate) {

        long todayMillisecond = getCurrentDate().getTime();
        long estimatedArrivalMillisecond = estimatedArrivalDate.getTime();

        long millisecondDiff = todayMillisecond - estimatedArrivalMillisecond;
        int dayDiff = (int) TimeUnit.MILLISECONDS.toDays(millisecondDiff);

        if (dayDiff > 1) {
            return true;
        }
        return false;
    }
}
