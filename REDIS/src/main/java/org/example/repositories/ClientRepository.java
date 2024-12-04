package org.example.repositories;

import org.example.Client;

import java.util.ArrayList;

public class ClientRepository {

    private final RedisClientRepository redisClientRepository;
    private final MongoClientRepository mongoClientRepository;

    public ClientRepository(RedisClientRepository redisClientRepository, MongoClientRepository mongoClientRepository) {
        this.redisClientRepository = redisClientRepository;
        this.mongoClientRepository = mongoClientRepository;
    }

    public Client findById(Object id) {
        Client cachedClient = redisClientRepository.findById(id);
        if (cachedClient != null) {
            return cachedClient;
        } else {
            Client client = mongoClientRepository.findById(id);
            if (client != null) {
                redisClientRepository.add(client);
            }
            return client;
        }
    }

    public ArrayList<Client> findAll() {
        ArrayList<Client> clients = mongoClientRepository.findAll();
        clients.forEach(client -> {
            Client cachedClient = redisClientRepository.findById(client.getPersonalID());
            if (cachedClient == null) {
                redisClientRepository.add(client);
            }
        });
        return clients;
    }

    public void add(Client client) {
        mongoClientRepository.add(client);
        redisClientRepository.add(client);
    }

    public void update(Client client) {
        mongoClientRepository.update(client);
        redisClientRepository.update(client);
    }

    public void delete(Client client) {
        mongoClientRepository.delete(client);
        redisClientRepository.delete(client);
    }
}
