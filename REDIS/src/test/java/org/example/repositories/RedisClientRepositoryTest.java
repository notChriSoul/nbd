package org.example.repositories;

import org.example.Client;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

public class RedisClientRepositoryTest {
    private static final RedisClientRepository REDIS_CLIENT_REPOSITORY = new RedisClientRepository();

    @BeforeEach
    public void setUp() {
        REDIS_CLIENT_REPOSITORY.clearCache();
    }

    @AfterAll
    public static void tearDown() {
        REDIS_CLIENT_REPOSITORY.clearCache();
        REDIS_CLIENT_REPOSITORY.close();
    }

    @Test
    public void findById_ClientInDB_ClientReturned() {
        Client client = new Client("11111111110", "Firstname", "Lastname");
        REDIS_CLIENT_REPOSITORY.add(client);
        Client foundClient = REDIS_CLIENT_REPOSITORY.findById("11111111110");
        Assertions.assertEquals(client, foundClient);
    }

    @Test
    public void findAll_TwoClientsInDB_TwoClientsListReturned() {
        Client client1 = new Client("11111111110", "Firstname", "Lastname");
        Client client2 = new Client("11111111112", "Firstname", "Lastname");
        REDIS_CLIENT_REPOSITORY.add(client1);
        REDIS_CLIENT_REPOSITORY.add(client2);
        List<Client> addedClients = List.of(client1, client2);
        List<Client> foundClients = REDIS_CLIENT_REPOSITORY.findAll();
        Assertions.assertEquals(2, foundClients.size());
        Assertions.assertEquals(addedClients.getFirst(),foundClients.getFirst());
        Assertions.assertEquals(addedClients.getLast(),foundClients.getLast());
    }

    @Test
    public void add_ValidClient_ClientAdded() {
        Client client = new Client("11111111113", "Firstname", "Lastname");
        REDIS_CLIENT_REPOSITORY.add(client);
        Client foundClient = REDIS_CLIENT_REPOSITORY.findById("11111111113");
        Assertions.assertEquals(client, foundClient);
    }

    @Test
    public void update_UpdateClient_ClientUpdated() {
        Client client = new Client("11111111114", "Firstname", "Lastname");
        REDIS_CLIENT_REPOSITORY.add(client);
        client.setFirstName("AltFirstname");
        REDIS_CLIENT_REPOSITORY.update(client);
        Assertions.assertEquals(client.getFirstName(), REDIS_CLIENT_REPOSITORY.findById("11111111114").getFirstName());
    }

    @Test
    public void delete_ClientInDB_ClientDeleted() {
        Client client = new Client("11111111115", "Firstname", "Lastname");
        REDIS_CLIENT_REPOSITORY.add(client);
        Assertions.assertNotNull(REDIS_CLIENT_REPOSITORY.findById("11111111115"));
        REDIS_CLIENT_REPOSITORY.delete(client);
        Assertions.assertNull(REDIS_CLIENT_REPOSITORY.findById("11111111115"));
    }
}