package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.bson.Document;
import org.example.Repositories.ClientRepository;
import org.example.Repositories.RentRepository;
import org.example.Repositories.VirtualMachineRepository;
import org.example.vms.Normal;
import org.example.vms.VirtualMachine;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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



    @Test
    @DisplayName("Test adding a new Rent no mongo")
    void testAddRentNoMongo() throws InterruptedException, JsonProcessingException {

        testRent = new Rent(151, testClient, testVM, LocalDateTime.now());

        sendRentAsync(testRent);

    }


}