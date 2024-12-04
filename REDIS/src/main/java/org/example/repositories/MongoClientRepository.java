package org.example.repositories;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.conversions.Bson;
import org.example.Client;

import java.util.ArrayList;

public class MongoClientRepository extends AbstractMongoRepository {

    public Client findById(Object id) {
        Bson filter = Filters.eq("_id", id);
        MongoCollection<Client> collection = getDatabase().getCollection("clients", Client.class);
        FindIterable<Client> clients = collection.find(filter);
        return clients.first();
    }

    public ArrayList<Client> findAll() {
        MongoCollection<Client> collection = getDatabase().getCollection("clients", Client.class);
        return collection.find().into(new ArrayList<>());
    }

    public void add(Client client) {
        MongoCollection<Client> collection = getDatabase().getCollection("clients", Client.class);
        collection.insertOne(client);
    }

    public void update(Client client) {
        Bson filter = Filters.eq("_id", client.getPersonalID());
        MongoCollection<Client> collection = getDatabase().getCollection("clients", Client.class);
        Bson updates = Updates.combine(
                Updates.set("firstName", client.getFirstName()),
                Updates.set("lastName", client.getLastName()),
                Updates.set("clientType", client.getCurrentRentsNumber())
        );
        collection.findOneAndUpdate(filter, updates);
    }

    public void delete(Client client) {
        Bson filter = Filters.eq("_id", client.getPersonalID());
        MongoCollection<Client> collection = getDatabase().getCollection("clients", Client.class);
        collection.findOneAndDelete(filter);
    }

}
