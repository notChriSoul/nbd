package org.example.Repositories;

import org.bson.Document;
import org.example.Client;
import org.example.Rent;
import org.example.vms.Normal;
import org.example.vms.VirtualMachine;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RentRepositoryTest {

    private static RentRepository rentRepository;
    private static VirtualMachineRepository vmRepository;
    private static ClientRepository clientRepository;

    private VirtualMachine testVM;
    private Client testClient;
    private Rent testRent;

    @BeforeAll
    void setup() {
        // Initialize repositories
        rentRepository = new RentRepository();
        rentRepository.initDbConnection();

        vmRepository = new VirtualMachineRepository();
        vmRepository.initDbConnection();

        clientRepository = new ClientRepository();
        clientRepository.initDbConnection();

        // Insert sample Client
        testClient = new Client("123456", "John", "Doe");
        clientRepository.add(testClient);

        // Insert sample Virtual Machine
        testVM = new Normal(1,true, 4, 16.0, 200.0);
        vmRepository.add(testVM);
    }

    @AfterAll
    public static void tearDown() {
        rentRepository.getDatabase().getCollection("rents", Rent.class).deleteMany(new Document());
        rentRepository.close();
        clientRepository.getDatabase().getCollection("clients", Client.class).deleteMany(new Document());
        clientRepository.close();
        vmRepository.getDatabase().getCollection("virtual_machines", VirtualMachine.class).deleteMany(new Document());
        vmRepository.close();

    }

    @Test
    @DisplayName("Test adding a new Rent")
    void testAddRent() {
        testRent = new Rent(1, testClient, testVM, LocalDateTime.now());

        // Add rent
        rentRepository.add(testRent);

        // Retrieve rent by ID
        Rent retrievedRent = rentRepository.findById(testRent.getId());

        assertNotNull(retrievedRent);
        assertEquals(testRent.getClient().getPersonalID(), retrievedRent.getClient().getPersonalID());
        assertEquals(testRent.getVM().getId(), retrievedRent.getVM().getId());
    }

    @Test
    @DisplayName("Test deleting a Rent")
    void testDeleteRent() {
        if (testRent == null) {
            testAddRent(); // Add a rent if it doesn't exist
        }

        // Delete rent
        rentRepository.delete(testRent);

        // Verify rent is deleted
        Rent deletedRent = rentRepository.findById(testRent.getId());
        assertNull(deletedRent);

        // Verify Client rental count decreased
        Client updatedClient = clientRepository.findById(testClient.getPersonalID());
        assertEquals(0, updatedClient.getCurrentRentsNumber());
    }

    @AfterAll
    void cleanup() {
        rentRepository.close();
        vmRepository.close();
        clientRepository.close();
    }
}
