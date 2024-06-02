package com.geektrust;

import com.geektrust.entity.CommandHandler;
import com.geektrust.entity.RideManager;

import java.io.BufferedReader;
import java.io.FileReader;

public class Main {

    public static void main(String[] args) {
        RideManager rideManager = new RideManager();
        CommandHandler commandHandler = new CommandHandler(rideManager);

        try (BufferedReader reader = new BufferedReader(new FileReader(args[0]))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] cmd = line.split(" ");
                commandHandler.executeCommand(cmd);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.print(rideManager.getOutput());
    }
}
