package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.bson.Document;
import org.example.Repositories.ClientRepository;
import org.example.Repositories.RentRepository;
import org.example.Repositories.VirtualMachineRepository;
import org.example.vms.Normal;
import org.example.vms.VirtualMachine;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;
import java.util.concurrent.ExecutionException;

import static org.example.KafkaProducent.sendRentAsync;
import static org.junit.jupiter.api.Assertions.*;

class KafkaProducentTest {


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
        clientRepository.add(testClient);

        // Insert sample Virtual Machine
        testVM = new Normal(1, 4, 16.0, 200.0);
        vmRepository.add(testVM);
    }

    @AfterAll
    static void tearDown() {
        rentRepository.getDatabase().getCollection("rents", Rent.class).deleteMany(new Document());
        rentRepository.getDatabase().getCollection("archived", Rent.class).deleteMany(new Document());
        rentRepository.close();
        clientRepository.getDatabase().getCollection("clients", Client.class).deleteMany(new Document());
        clientRepository.close();
        vmRepository.getDatabase().getCollection("virtual_machines", VirtualMachine.class).deleteMany(new Document());
        vmRepository.close();
    }



    @Test
    @DisplayName("Test adding a new Rent no mongo")
    void testAddRentNoMongo() throws InterruptedException, JsonProcessingException {

        testRent = new Rent(151, testClient, testVM, LocalDateTime.now());

        sendRentAsync(testRent);

    }


}