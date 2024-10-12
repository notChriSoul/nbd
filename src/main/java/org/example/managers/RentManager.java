package org.example.managers;

import jakarta.persistence.OptimisticLockException;
import org.example.Client;
import org.example.DAO.RentDAO;
import org.example.exceptions.MaxRentLimitException;
import org.example.Rent;
import org.example.vms.VirtualMachine;

import java.time.LocalDateTime;
import java.util.List;

public class RentManager {

    private final RentDAO rentDAO;

    public RentManager() {
        this.rentDAO = new RentDAO();
    }

    // Method to create a new Rent using the constructor
    public Rent createRent(LocalDateTime beginTime, Client client, VirtualMachine vm) {
        // Check if the client already has 2 active rents
        if (hasMaxActiveRents(client)) {
            throw new MaxRentLimitException("Client cannot have more than 2 active rents at the same time.");
        }

        // Create and save the Rent
        Rent rent = new Rent(0, beginTime, client, vm); // Using the constructor with rentId set to 0 for new entities
        rentDAO.saveRent(rent); // Assuming saveRent method is implemented in RentDAO

        return rent;
    }

    // Helper method to check if the client has 2 active rents
    private boolean hasMaxActiveRents(Client client) {
        List<Rent> activeRents = rentDAO.getActiveRentsByClient(client.getPersonalId());
        return activeRents.size() >= 2;
    }

    // Method to retrieve a Rent by ID
    public Rent getRent(int rentId) {
        return rentDAO.getRent(rentId);
    }

    // Method to update an existing Rent
    public void updateRent(Rent rent) {
        try {
            rentDAO.updateRent(rent);
        } catch (OptimisticLockException ole) {
            System.out.println("OptimisticLockException during update: Another transaction has updated this rent.");
            ole.printStackTrace();
        }
    }

    // Method to delete a Rent by ID
    public void deleteRent(int rentId) {
        try {
            rentDAO.deleteRent(rentId);
        } catch (OptimisticLockException ole) {
            System.out.println("OptimisticLockException during delete: Another transaction has updated this rent.");
            ole.printStackTrace();
        }
    }

    // Method to get all active rents for a client
    public List<Rent> getActiveRentsForClient(Client client) {
        return rentDAO.getActiveRentsByClient(client.getPersonalId());
    }
}
