package com.geektrust.entity;

import com.geektrust.exception.RiderNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

import static com.geektrust.entity.Literals.*;

public class CommandHandler {

    private static RideManager rm;
    private static final Map<String, Consumer<String[]>> commandMap = new HashMap<>();
    public CommandHandler(RideManager rideManager) {
        this.rm = rideManager;
        initializeCommandMap();
    }

    private static void initializeCommandMap() {
        commandMap.put(ADD_DRIVER, CommandHandler::handleAddDriver);
        commandMap.put(ADD_RIDER, CommandHandler::handleAddRider);
        commandMap.put(MATCH, CommandHandler::handleMatchDrivers);
        commandMap.put(START_RIDE, CommandHandler::handleStartRide);
        commandMap.put(STOP_RIDE, CommandHandler::handleStopRide);
        commandMap.put(BILL, CommandHandler::handleGenerateBill);
    }

    public static void executeCommand(String[] cmd) {
        commandMap.getOrDefault(cmd[0], CommandHandler::handleInvalidCommand).accept(cmd);
    }

    private static void handleAddDriver(String[] cmd) {
        String driverId = cmd[1];
        Location location = new Location(Float.parseFloat(cmd[2]), Float.parseFloat(cmd[3]));
        rm.addDriver(driverId, location);
    }

    private static void handleAddRider(String[] cmd) {
        String riderId = cmd[1];
        Location location = new Location(Float.parseFloat(cmd[2]), Float.parseFloat(cmd[3]));
        rm.addRider(riderId, location);
    }

    private static void handleMatchDrivers(String[] cmd) {
        String riderId = cmd[1];
        try {
            rm.matchDrivers(riderId);
        } catch (RiderNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static void handleStartRide(String[] cmd) {
        String rideId = cmd[1];
        int N = Integer.parseInt(cmd[2]);
        String riderId = cmd[3];
        rm.startRide(rideId, riderId, N);
    }

    private static void handleStopRide(String[] cmd) {
        String rideId = cmd[1];
        Location location = new Location(Float.parseFloat(cmd[2]), Float.parseFloat(cmd[3]));
        int rideTimeInMinutes = Integer.parseInt(cmd[4]);
        rm.stopRide(rideId, location, rideTimeInMinutes);
    }

    private static void handleGenerateBill(String[] cmd) {
        String rideId = cmd[1];
        rm.generateBill(rideId);
    }

    private static void handleInvalidCommand(String[] cmd) {
        throw new IllegalArgumentException("Invalid command: " + cmd[0]);
    }
}
