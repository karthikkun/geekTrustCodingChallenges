package com.geektrust;

import com.geektrust.entity.CommandHandler;
import com.geektrust.entity.Location;
import com.geektrust.entity.RideManager;
import com.geektrust.exception.RiderNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CommandHandlerTest {
    private RideManager rideManager;
    private CommandHandler commandHandler;

    @BeforeEach
    void setUp() {
        rideManager = new RideManager();
        commandHandler = new CommandHandler(rideManager);
    }

    @Test
    void testAddDriver() {
        String[] cmd = {"ADD_DRIVER", "D1", "10.0", "20.0"};
        commandHandler.executeCommand(cmd);
        assertTrue(rideManager.getAvailableDrivers().contains("D1"));
    }

    @Test
    void testAddRider() {
        String[] cmd = {"ADD_RIDER", "R1", "15.0", "25.0"};
        commandHandler.executeCommand(cmd);
        assertNotNull(rideManager.getRiders().get("R1"));
    }

    @Test
    void testMatchDrivers() {
        rideManager.addDriver("D1", new Location(10.0f, 20.0f));
        rideManager.addRider("R1", new Location(10.0f, 20.0f));
        String[] cmd = {"MATCH", "R1"};
        commandHandler.executeCommand(cmd);
        assertTrue(rideManager.getMatches().containsKey("R1"));
    }

    @Test
    void testStartRide() throws RiderNotFoundException {
        rideManager.addDriver("D1", new Location(10.0f, 20.0f));
        rideManager.addRider("R1", new Location(10.0f, 20.0f));
        rideManager.matchDrivers("R1");
        String[] cmd = {"START_RIDE", "RIDE1", "1", "R1"};
        commandHandler.executeCommand(cmd);
        assertTrue(rideManager.getRides().containsKey("RIDE1"));
    }

    @Test
    void testStopRide() throws RiderNotFoundException {
        rideManager.addDriver("D1", new Location(10.0f, 20.0f));
        rideManager.addRider("R1", new Location(10.0f, 20.0f));
        rideManager.matchDrivers("R1");
        rideManager.startRide("RIDE1", "R1", 1);
        String[] cmd = {"STOP_RIDE", "RIDE1", "30.0", "40.0", "15"};
        commandHandler.executeCommand(cmd);
        assertEquals("STOP", rideManager.getRides().get("RIDE1").getState().toString());
    }

    @Test
    void testGenerateBill() throws RiderNotFoundException {
        rideManager.addDriver("D1", new Location(10.0f, 20.0f));
        rideManager.addRider("R1", new Location(10.0f, 20.0f));
        rideManager.matchDrivers("R1");
        rideManager.startRide("RIDE1", "R1", 1);
        rideManager.stopRide("RIDE1", new Location(30.0f, 40.0f), 15);
        String[] cmd = {"BILL", "RIDE1"};
        commandHandler.executeCommand(cmd);
        assertTrue(rideManager.getOutput().contains("BILL"));
    }

    @Test
    void testRiderNotFoundException() {
        String[] cmd = {"MATCH", "R999"}; // R999 does not exist
        Exception exception = assertThrows(RuntimeException.class, () -> {
            commandHandler.executeCommand(cmd);
        });
        String expectedMessage = "Rider not found";
        String actualMessage = exception.getMessage();
        assertTrue(actualMessage.contains(expectedMessage));
    }
}
