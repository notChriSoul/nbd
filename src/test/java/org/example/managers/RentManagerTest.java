package org.example.managers;

import org.example.Client;
import org.example.DAO.VirtualMachineDAO;
import org.example.Rent;
import org.example.exceptions.MaxRentLimitException;
import org.example.vms.Normal;
import org.example.vms.Performance;
import org.example.vms.VirtualMachine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.concurrent.*;

import static org.junit.jupiter.api.Assertions.*;

class RentManagerTest {

    private RentManager rentManager;
    private int clientId;

    @BeforeEach
    void setUp() {
        // Initialize RentManager, Client, and VirtualMachine before each test
        rentManager = new RentManager();
        ClientManager CM = new ClientManager();
        VirtualMachineDAO vmdao = new VirtualMachineDAO();
        clientId = CM.createClient("Ben","Dover");
        VirtualMachine vm = new Normal(1, true, 4, 16.0, 500.0);
        vmdao.save(vm);
        // Add an initial rent to the client (1 rent already exists)
        rentManager.createRent(LocalDateTime.now().minusDays(1), CM.getClient(clientId), vm);
    }

    @Test
    void testConcurrentRentCreation() throws InterruptedException, ExecutionException {
        ClientManager CM = new ClientManager();
        Client client = CM.getClient(clientId);

        VirtualMachineDAO vmdao = new VirtualMachineDAO();
        Normal normalVM = new Normal(1, true, 4, 16.0, 500.0);
        vmdao.save(normalVM);

        Performance performanceVM = new Performance(2, true, 8, 32.0, 1000.0, "NVIDIA RTX 3080");
        vmdao.save(performanceVM);

        // Create a thread pool to simulate concurrent rent creation
        ExecutorService executorService = Executors.newFixedThreadPool(2);

        // Define two concurrent tasks that attempt to create a new rent for the same client
        Callable<Rent> rentTask1 = () -> rentManager.createRent(LocalDateTime.now(), client, normalVM);
        Callable<Rent> rentTask2 = () -> rentManager.createRent(LocalDateTime.now(), client, performanceVM);

        // Submit the tasks to the executor service
        Future<Rent> future1 = executorService.submit(rentTask1);
        Future<Rent> future2 = executorService.submit(rentTask2);

        // Wait for both tasks to complete
        Rent rent1 = null;
        Rent rent2 = null;

        try {
            rent1 = future1.get(); // This should succeed
        } catch (ExecutionException e) {
            // If the exception is due to business logic, handle it accordingly
            Throwable cause = e.getCause();
            if (cause instanceof MaxRentLimitException) {
                System.out.println("First rent task failed due to max rent limit: " + cause.getMessage());
            } else {
                throw e;
            }
        }

        try {
            rent2 = future2.get(); // This should either succeed or fail due to the max rent limit
        } catch (ExecutionException e) {
            // Handle the exception if thrown
            Throwable cause = e.getCause();
            if (cause instanceof MaxRentLimitException) {
                System.out.println("Second rent task failed due to max rent limit: " + cause.getMessage());
            } else {
                throw e;
            }
        }

        // Assert that at least one of the two rent creations succeeded
        assertTrue(rent1 != null || rent2 != null, "At least one rent creation should succeed.");

        // Assert that no more than 2 active rents exist for the client
        assertEquals(2, rentManager.getActiveRentsForClient(client).size(), "Client should have exactly 2 active rents.");

        // Shutdown the executor service
        executorService.shutdown();
    }
}
