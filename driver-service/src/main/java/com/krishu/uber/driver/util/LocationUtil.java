package com.krishu.uber.driver.util;

// Utility class providing geographic calculations using the Haversine formula
public class LocationUtil {

    // Radius of Earth in kilometers
    private static final double EARTH_RADIUS_KM = 6371.0;

    private LocationUtil() {
        // Private constructor to prevent instantiation of utility class
    }

    /**
     * Calculates the great-circle distance between two geographic coordinates
     * on Earth using the Haversine formula.
     *
     * Formula:
     *   a = sin²(Δlat/2) + cos(lat1) * cos(lat2) * sin²(Δlon/2)
     *   c = 2 * atan2(√a, √(1−a))
     *   distance = R * c
     *
     * @param lat1 Latitude of point 1 in degrees
     * @param lon1 Longitude of point 1 in degrees
     * @param lat2 Latitude of point 2 in degrees
     * @param lon2 Longitude of point 2 in degrees
     * @return Distance in kilometers rounded to 2 decimal places
     */
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        // Convert degrees to radians
        double lat1Rad = Math.toRadians(lat1);
        double lon1Rad = Math.toRadians(lon1);
        double lat2Rad = Math.toRadians(lat2);
        double lon2Rad = Math.toRadians(lon2);

        // Differences in latitude and longitude
        double dLat = lat2Rad - lat1Rad;
        double dLon = lon2Rad - lon1Rad;

        // Haversine formula
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(lat1Rad) * Math.cos(lat2Rad)
                * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        double distance = EARTH_RADIUS_KM * c;

        // Round to 2 decimal places for clean display (e.g., 2.34 km)
        return Math.round(distance * 100.0) / 100.0;
    }
}
