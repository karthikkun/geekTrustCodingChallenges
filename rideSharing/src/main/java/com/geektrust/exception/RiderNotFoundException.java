package com.geektrust.exception;

public class RiderNotFoundException extends Exception {
    @Override
    public String getMessage() {
        return "Rider not found";
    }
}