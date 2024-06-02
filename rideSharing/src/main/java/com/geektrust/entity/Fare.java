package com.geektrust.entity;

public class Fare {
    public static final float baseFare = 50;
    public static final float additionalKmFee = 6.5F;
    public static final int additionalMinuteFee = 2;
    public static final int serviceTaxPercent = 20;

    public static float calculateRideFare(float rideDistanceInKms, int getRideTimeInMinutes) {
        float grossFare = Float.parseFloat(String.format("%.2f", Fare.baseFare + Fare.additionalKmFee * rideDistanceInKms +
                Fare.additionalMinuteFee * getRideTimeInMinutes));
        float rideFare = grossFare + (grossFare * Fare.serviceTaxPercent) / 100F;
        return rideFare;
    }
}
