package com.krishu.uber.ride.util;

// Utility class computing dynamic trip fares and distance calculations
public class FareCalculator {

    // Standard Uber pricing parameters
    public static final double BASE_FARE = 50.0;        // Base flag-drop fare in INR
    public static final double RATE_PER_KM = 12.0;      // Standard per-kilometer rate in INR
    public static final double MINIMUM_FARE = 50.0;     // Minimum fare chargeable
    private static final double EARTH_RADIUS_KM = 6371.0;

    private FareCalculator() {
        // Utility class, private constructor
    }

    /**
     * Calculates estimated fare for a given distance and surge multiplier.
     * Formula:
     *   Fare = max(MINIMUM_FARE, (BASE_FARE + (distanceKm * RATE_PER_KM)) * surgeMultiplier)
     *
     * @param distanceKm Estimated trip distance in kilometers
     * @param surgeMultiplier Demand multiplier (e.g., 1.0 for standard, 1.5 for peak hours)
     * @return Calculated fare rounded to 2 decimal places
     */
    public static double calculateFare(double distanceKm, double surgeMultiplier) {
        if (distanceKm < 0) {
            distanceKm = 0.0;
        }
        if (surgeMultiplier <= 0) {
            surgeMultiplier = 1.0;
        }

        double rawFare = (BASE_FARE + (distanceKm * RATE_PER_KM)) * surgeMultiplier;
        double finalFare = Math.max(MINIMUM_FARE, rawFare);

        return Math.round(finalFare * 100.0) / 100.0;
    }

    /**
     * Calculates distance between pickup and dropoff coordinates using the Haversine formula.
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS_KM * c;
        return Math.round(distance * 100.0) / 100.0;
    }
}
