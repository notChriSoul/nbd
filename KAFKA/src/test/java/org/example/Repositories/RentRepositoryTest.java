package org.example.Repositories;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mongodb.MongoCommandException;
import com.mongodb.MongoWriteException;
import org.bson.Document;
import org.example.Client;
import org.example.KafkaConsument;
import org.example.KafkaProducent;
import org.example.Rent;
import org.example.vms.Normal;
import org.example.vms.VirtualMachine;
import org.junit.jupiter.api.*;

import static org.example.KafkaProducent.sendRentAsync;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutionException;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class RentRepositoryTest {

    private static RentRepository rentRepository;
    private static VirtualMachineRepository vmRepository;
    private static ClientRepository clientRepository;

    private VirtualMachine testVM;
    private Client testClient;
    private Rent testRent;

    @BeforeEach
    void setup() throws ExecutionException, InterruptedException {
        // Initialize repositories

        KafkaProducent producent = new KafkaProducent();

        rentRepository = new RentRepository();
        rentRepository.initDbConnection();

        vmRepository = new VirtualMachineRepository();
        vmRepository.initDbConnection();

        clientRepository = new ClientRepository();
        clientRepository.initDbConnection();

        // Insert sample Client
        testClient = new Client("123456", "John", "Doe");
        //clientRepository.add(testClient);

        // Insert sample Virtual Machine
        testVM = new Normal(1, 4, 16.0, 200.0);
       // vmRepository.add(testVM);
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
    void testAddRent() throws InterruptedException, JsonProcessingException {
        testRent = new Rent(2, testClient, testVM, LocalDateTime.now());

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
    void testEndRent() throws InterruptedException, JsonProcessingException {
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
    public void update_UpdatedRent_RentUpdated() throws InterruptedException, JsonProcessingException {
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









    @Test
    @DisplayName("Test adding a new Rent no mongo")
    void testAddRentNoMongo() throws InterruptedException, JsonProcessingException {


        KafkaConsument consument = new KafkaConsument(2); // liczba konsumentów
        consument.initConsumers();
        consument.consumeTopicByAllConsumers();

        testRent = new Rent(1505, testClient, testVM, LocalDateTime.now());

        sendRentAsync(testRent);



        // Retrieve rent by ID
        Rent retrievedRent = rentRepository.findById(testRent.getId());

        assertNotNull(retrievedRent);
        assertEquals(testRent.getClient().getPersonalID(), retrievedRent.getClient().getPersonalID());
        assertEquals(testRent.getVM().getId(), retrievedRent.getVM().getId());

    }

    @Test
    @DisplayName("Test adding a new Rent with Kafka and MongoDB")
    void testAddRentWithKafkaAndMongo() throws InterruptedException, JsonProcessingException {

        // Initialize KafkaConsumer
        KafkaConsument consument = new KafkaConsument(1); // Jednego konsumenta dla prostoty
        consument.initConsumers();
        new Thread(consument::consumeTopicByAllConsumers).start();

        // Create and send Rent
        Rent testRent = new Rent(1506, testClient, testVM, LocalDateTime.now());
        KafkaProducent.sendRentAsync(testRent);

        // Wait for the message to be processed
        Rent retrievedRent = null;
        int retryCount = 0;
        while (retrievedRent == null && retryCount < 10) { // 10 prób z odstępami
            retrievedRent = rentRepository.findById(testRent.getId());
            Thread.sleep(500); // Poczekaj 500 ms między próbami
            retryCount++;
        }

        // Assertions
        assertNotNull(retrievedRent, "Rent should be saved in MongoDB.");
        assertEquals(testRent.getClient().getPersonalID(), retrievedRent.getClient().getPersonalID());
        assertEquals(testRent.getVM().getId(), retrievedRent.getVM().getId());

    }







}
