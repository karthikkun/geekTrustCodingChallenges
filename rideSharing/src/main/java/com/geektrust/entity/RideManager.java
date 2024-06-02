package com.geektrust.entity;

import com.geektrust.enums.RideState;
import com.geektrust.exception.RiderNotFoundException;

import java.util.*;

import static com.geektrust.entity.Fare.calculateRideFare;
import static com.geektrust.entity.Literals.*;
import static com.geektrust.util.Util.COMPARATOR;

public class RideManager {
    private static final int RESULT_LIMIT = 5;
    private static final int RADIUS_OF_SEARCH = 5;
    private static Map<String, Location> drivers;
    private static Set<String> availableDrivers;
    private static Map<String, Location> riders;
    private static Map<String, RideInfo> rides;
    private static Map<String, List<String>> matches;
    private final StringBuilder out = new StringBuilder();

    public RideManager() {
        drivers = new HashMap<>();
        riders = new HashMap<>();
        availableDrivers = new HashSet<>();
        rides = new HashMap<>();
        matches = new HashMap<>();
    }

    public static List<String> fetchNearbyDrivers(String riderId) throws RiderNotFoundException {
        Location riderLocation = getRiderLocation(riderId);
        List<Map.Entry<String, Double>> distances = calculateDistances(riderLocation);
        return getNearestDrivers(distances);
    }

    private static Location getRiderLocation(String riderId) throws RiderNotFoundException {
        return Optional.ofNullable(riders.get(riderId)).orElseThrow(RiderNotFoundException::new);
    }

    private static List<Map.Entry<String, Double>> calculateDistances(Location riderLocation) {
        List<Map.Entry<String, Double>> distances = new ArrayList<>();
        for (String driverId : availableDrivers) {
            distances.add(new AbstractMap.SimpleEntry<>(driverId, riderLocation.distanceTo(drivers.get(driverId))));
        }
        distances.sort(COMPARATOR);
        return distances;
    }
    private static List<String> getNearestDrivers(List<Map.Entry<String, Double>> distances) {
        List<String> resultSet = new ArrayList<>();
        for (int i = 0; i < Math.min(distances.size(), RESULT_LIMIT); ++i) {
            if (distances.get(i).getValue() > RADIUS_OF_SEARCH) break;
            resultSet.add(distances.get(i).getKey());
        }
        return resultSet;
    }

    public void addDriver(String driverId, Location location) {
        drivers.put(driverId, location);
        availableDrivers.add(driverId);
    }

    public void addRider(String riderId, Location location) {
        riders.put(riderId, location);
    }

    public void matchDrivers(String riderId) throws RiderNotFoundException {
        List<String> driverIds = fetchNearbyDrivers(riderId);
        matches.put(riderId, driverIds);
        appendMatchResult(driverIds);
    }

    private void appendMatchResult(List<String> driverIds) {
        if (driverIds.isEmpty()) {
            out.append(NO_DRIVERS_AVAILABLE);
        } else {
            out.append(DRIVERS_MATCHED);
            for (String driverId : driverIds) {
                out.append(" ").append(driverId);
            }
        }
        out.append("\n");
    }

    public void startRide(String rideId, String riderId, int N) {
        if (isInvalidRide(rideId, riderId, N)) {
            out.append(INVALID_RIDE);
        } else {
            initiateRide(rideId, riderId, N);
        }
        out.append("\n");
    }

    private boolean isInvalidRide(String rideId, String riderId, int N) {
        return rides.containsKey(rideId) || matches.get(riderId).size() < N;
    }

    private void initiateRide(String rideId, String riderId, int N) {
        String driverId = matches.get(riderId).get(N - 1);
        availableDrivers.remove(driverId);
        rides.put(rideId, new RideInfo(riderId, driverId, riders.get(riderId)));
        out.append(RIDE_STARTED).append(" ").append(rideId);
    }

    public void stopRide(String rideId, Location destination, int rideTimeInMinutes) {
        RideInfo rideInfo = rides.get(rideId);
        if (isInvalidStopRide(rideId)) {
            out.append(INVALID_RIDE);
        } else {
            completeRide(rideId, rideInfo, destination, rideTimeInMinutes);
        }
        out.append("\n");
    }

    private boolean isInvalidStopRide(String rideId) {
        return (!rides.containsKey(rideId) || rides.get(rideId).getState() == RideState.STOP);
    }

    private void completeRide(String rideId, RideInfo rideInfo, Location destination, int rideTimeInMinutes) {
        rideInfo.setRideTimeInMinutes(rideTimeInMinutes);
        rideInfo.setState(RideState.STOP);
        rideInfo.setDestination(destination);
        updateLocations(rideInfo);
        out.append(RIDE_STOPPED).append(" ").append(rideId);
    }

    private void updateLocations(RideInfo rideInfo) {
        riders.put(rideInfo.getRiderId(), rideInfo.getDestination());
        drivers.put(rideInfo.getDriverId(), rideInfo.getDestination());
        availableDrivers.add(rideInfo.getDriverId());
    }

    public void generateBill(String rideId) {
        RideInfo rideInfo = rides.get(rideId);
        if (rideInfo == null) {
            out.append(INVALID_RIDE);
        } else if (rideInfo.getState() == RideState.START) {
            out.append(RIDE_NOT_COMPLETED);
        } else {
            createBill(rideId, rideInfo);
        }
        out.append("\n");
    }

    private void createBill(String rideId, RideInfo rideInfo) {
        float rideFare = calculateRideFare(rideInfo.getRideDistanceInKms(), rideInfo.getRideTimeInMinutes());
        rideInfo.setRideFare(rideFare);
        out.append(BILL).append(" ").append(rideId).append(" ").append(rideInfo.getDriverId()).append(" ")
                .append(String.format("%.2f", rideInfo.getRideFare()));
    }

    public String getOutput() {
        return out.toString();
    }

    // Getter methods for testing
    public Set<String> getAvailableDrivers() {
        return availableDrivers;
    }

    public Map<String, Location> getRiders() {
        return riders;
    }

    public Map<String, RideInfo> getRides() {
        return rides;
    }

    public Map<String, List<String>> getMatches() {
        return matches;
    }
}
