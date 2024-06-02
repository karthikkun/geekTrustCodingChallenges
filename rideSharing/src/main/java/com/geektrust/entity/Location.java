package com.geektrust.entity;

public class Location {
    private float x;
    private float y;
    private static final int SQUARE_EXPONENT = 2;
    public Location(float x, float y) {
        this.x = x;
        this.y = y;
    }
    public float X() {
        return this.x;
    }

    public float Y() {
        return this.y;
    }

    public double distanceTo(Location loc) {
        return Math.sqrt(Math.pow(this.X() - loc.X(), SQUARE_EXPONENT) + Math.pow(this.Y() - loc.Y(), SQUARE_EXPONENT));
    }
}
