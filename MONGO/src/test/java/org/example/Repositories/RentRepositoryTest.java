package org.example.Repositories;

import com.mongodb.MongoCommandException;
import com.mongodb.MongoWriteException;
import org.bson.Document;
import org.example.Client;
import org.example.Rent;
import org.example.vms.Normal;
import org.example.vms.VirtualMachine;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RentRepositoryTest {

    private static RentRepository rentRepository;
    private static VirtualMachineRepository vmRepository;
    private static ClientRepository clientRepository;

    private VirtualMachine testVM;
    private Client testClient;
    private Rent testRent;

    @BeforeEach
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
        testVM = new Normal(1, 4, 16.0, 200.0);
        vmRepository.add(testVM);
    }

    @AfterEach
    public void tearDown() {
        rentRepository.getDatabase().getCollection("rents", Rent.class).deleteMany(new Document());
        rentRepository.getDatabase().getCollection("archived", Rent.class).deleteMany(new Document());
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
    void testEndRent() {
        if (testRent == null) {
            testAddRent(); // Add a rent if it doesn't exist
        }

        // Delete rent
        rentRepository.endRent(testRent);

        // Verify rent is deleted
        Rent deletedRent = rentRepository.findById(testRent.getId());
        assertNull(deletedRent);

        // Verify Client rental count decreased
        Client updatedClient = clientRepository.findById(testClient.getPersonalID());
        assertEquals(0, updatedClient.getCurrentRentsNumber());
    }
    @Test
    void testArchiveRent() {
        Client Client = new Client("234", "John", "Doe");
        clientRepository.add(Client);
        Normal normal = new Normal(15432, 4, 16.0, 200.0);
        vmRepository.add(normal);
        Rent archiveRent = new Rent(132, Client, normal, LocalDateTime.now());
        rentRepository.add(archiveRent);
        archiveRent.endRent();
        Assertions.assertEquals(0, archiveRent.getRentDays());
        archiveRent.setEndTime(LocalDateTime.now().minusDays(1));
        archiveRent.endRent();
        Assertions.assertEquals(0, archiveRent.getRentDays());

        rentRepository.endRent(archiveRent);
    }

    @Test
    public void update_UpdatedRent_RentUpdated(){
        Client client = new Client ("11111111110", "Firstname", "Lastname");
        Normal normal = new Normal(1, 4, 16.0, 200.0);
        Rent rent = new Rent(10000, client, normal, LocalDateTime.now());
        LocalDateTime endTime = LocalDateTime.now().truncatedTo(ChronoUnit.SECONDS).plusHours(10);
        rentRepository.add(rent);
        rent.setEndTime(endTime);
        rentRepository.update(rent);
        Assertions.assertEquals(endTime, rentRepository.findById(10000).getEndTime());
    }

    @Test
    void testRentSameVWTwiceJsonValidation(){
        Normal normal = new Normal(3, 4, 16.0, 200.0);
        vmRepository.add(normal);
        Rent testRent1 = new Rent(2, testClient, normal, LocalDateTime.now());
        Rent testRent2 = new Rent(3, testClient, normal, LocalDateTime.now());
        Assertions.assertDoesNotThrow(() -> rentRepository.add(testRent1));
        Assertions.assertThrows(MongoCommandException.class, () -> rentRepository.add(testRent2));
    }

    @Test
    void testClientMaxNumberOfRents(){
        Normal normal = new Normal(3, 4, 16.0, 200.0);
        Normal notNormal = new Normal(4, 4, 16.0, 200.0);
        Normal notNormal2 = new Normal(5, 4, 16.0, 200.0);
        vmRepository.add(normal);
        Rent testRent1 = new Rent(2, testClient, normal, LocalDateTime.now());
        Rent testRent2 = new Rent(3, testClient, notNormal, LocalDateTime.now());
        Rent testRent3 = new Rent(4, testClient, notNormal2, LocalDateTime.now());
        Assertions.assertDoesNotThrow(() -> rentRepository.add(testRent1));
        Assertions.assertDoesNotThrow(() -> rentRepository.add(testRent2));
        Assertions.assertThrows(MongoWriteException.class,() -> rentRepository.add(testRent3));
    }
}
