package org.example.Repositories;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;

import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.Client;
import org.junit.jupiter.api.*;

import java.util.List;

public class ClientRepositoryTest {

    private static final ClientRepository clientRepository = new ClientRepository();

    @BeforeEach
    public void setUp() {
        clientRepository.initDbConnection();
        clientRepository.getDatabase().getCollection("clients", Client.class).deleteMany(new Document());
    }

    @AfterEach
    public void tearDown() {
        clientRepository.getDatabase().getCollection("clients", Client.class).deleteMany(new Document());
        clientRepository.close();
    }

    @Test
    public void findById_ClientInDB_ClientReturned() {
        Client client = new Client("11111111110", "Firstname", "Lastname");
        clientRepository.add(client);
        Client foundClient;
        Bson filter = Filters.eq("_id", "11111111110");
        MongoCollection<Client> collection = clientRepository.getDatabase().getCollection("clients", Client.class);
        foundClient = collection.find(filter).first();
        Assertions.assertEquals(clientRepository.findById("11111111110").getPersonalID(), foundClient.getPersonalID());
        Assertions.assertEquals(clientRepository.findById("11111111110").getFirstName(), foundClient.getFirstName());
        Assertions.assertEquals(clientRepository.findById("11111111110").getLastName(), foundClient.getLastName());
    }

    @Test
    public void findAll_TwoClientsInDB_TwoClientsListReturned() {
        Client client1 = new Client("11111111111", "Firstname", "Lastname");
        Client client2 = new Client("11111111112", "Firstname", "Lastname");
        clientRepository.add(client1);
        clientRepository.add(client2);
        List<Client> foundClients = clientRepository.findAll();
        Assertions.assertEquals(foundClients.size(), 2);

    }

    @Test
    public void add_ValidClient_ClientAdded() {
        Client client = new Client("11111111111", "Firstname", "Lastname");
        clientRepository.add(client);
        Assertions.assertEquals(clientRepository.findById("11111111111").getPersonalID(), client.getPersonalID());
    }


    @Test
    public void delete_ClientInDB_ClientRemoved() {
        Client client = new Client("11111111113", "Firstname", "Lastname");
        clientRepository.add(client);
        Assertions.assertNotNull(clientRepository.findById("11111111113"));
        clientRepository.delete(client);
        Assertions.assertNull(clientRepository.findById("11111111113"));
    }

    @Test
    public void update_ValidClient_ClientUpdated() {
        Client client = new Client("11111111111", "Firstname", "Lastname");
        clientRepository.add(client);
        String newFirstName = "NewFirstName";
        String newLastName = "NewLastName";
        client.setFirstName(newFirstName);
        client.setLastName(newLastName);
        clientRepository.update(client);
        System.out.println(clientRepository.findById("11111111111").getFirstName());
        System.out.println(clientRepository.findById("11111111111").getLastName());
        Assertions.assertEquals(clientRepository.findById("11111111111").getFirstName(), newFirstName);
        Assertions.assertEquals(clientRepository.findById("11111111111").getLastName(), newLastName);
    }
}
