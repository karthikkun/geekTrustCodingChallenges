package com.geektrust.entity;

import com.geektrust.enums.RideState;

public class RideInfo {
    private String riderId;
    private String driverId;
    private RideState state;
    private Location source;
    private Location destination;
    private int rideTimeInMinutes;
    private float rideFare;

    public RideInfo(String riderId, String driverId, Location source) {
        this.riderId = riderId;
        this.driverId = driverId;
        this.source = source;
        this.state = RideState.START;
    }

    public RideState getState() {
        return state;
    }

    public void setState(RideState state) {
        this.state = state;
    }

    public int getRideTimeInMinutes() {
        return rideTimeInMinutes;
    }

    public void setRideTimeInMinutes(int rideTimeInMinutes) {
        this.rideTimeInMinutes = rideTimeInMinutes;
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId) {
        this.driverId = driverId;
    }

    public float getRideFare() {
        return rideFare;
    }

    public void setRideFare(float rideFare) {
        this.rideFare = rideFare;
    }

    public float getRideDistanceInKms() {
        return Float.parseFloat(String.format("%.2f", this.source.distanceTo(destination)));
    }

    public Location getSource() {
        return source;
    }

    public void setSource(Location source) {
        this.source = source;
    }

    public Location getDestination() {
        return destination;
    }

    public void setDestination(Location destination) {
        this.destination = destination;
    }

    public String getRiderId() {
        return riderId;
    }
}
