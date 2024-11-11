package org.example.managers;

import org.example.Client;
import org.example.Rent;
import org.example.vms.VirtualMachine;

import java.time.LocalDateTime;
import java.util.List;

public class RentManager {

    // Method to create a new Rent using the constructor
    public Rent createRent(LocalDateTime beginTime, Client client, VirtualMachine vm) {

    }

    // Helper method to check if the client has 2 active rents
    private boolean hasMaxActiveRents(Client client) {

    }

    // Method to retrieve a Rent by ID
    public Rent getRent(int rentId) {

    }

    // Method to update an existing Rent
    public void updateRent(Rent rent) {

    }

    // Method to delete a Rent by ID
    public void deleteRent(int rentId) {

    }

    // Method to get all active rents for a client
    public List<Rent> getActiveRentsForClient(Client client) {

    }
}
