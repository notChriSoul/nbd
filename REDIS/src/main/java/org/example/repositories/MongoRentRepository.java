package org.example.repositories;

import com.mongodb.client.ClientSession;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.Updates;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.example.Rent;
import org.example.Client;
import org.example.vms.*;


import java.util.ArrayList;

public class MongoRentRepository extends AbstractMongoRepository {
    public Rent findById(Object id) {
        Bson filter = Filters.eq("_id", id);
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        FindIterable<Rent> rents = collection.find(filter);
        return rents.first();
    }

    public ArrayList<Rent> findAll() {
        MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
        return collection.find().into(new ArrayList<>());
    }


    public void add(Rent rent) {
        if (rent == null) {
            throw new NullPointerException("Rent is null");
        } else if (rent.getClient() == null) {
            throw new NullPointerException("Client is null");
        } else if (rent.getVM() == null) {
            throw new NullPointerException("VM is null");
        }
        ClientSession clientSession = getMongoClient().startSession();
        try {
            clientSession.startTransaction();

            MongoCollection<Rent> rentsCollection = getDatabase().getCollection("rents", Rent.class);
            rentsCollection.insertOne(clientSession, rent);

            MongoCollection<VirtualMachine> vmCollection = getDatabase().getCollection("virtual_machines", VirtualMachine.class);
            Bson filter = Filters.eq("_id", rent.getVM().getId());
            Bson updates = Updates.inc("rented", 1);
            vmCollection.findOneAndUpdate(clientSession, filter, updates);
            getDatabase().getCollection("virtual_machines").find(filter).forEach(doc ->System.out.println(doc.toJson()));

            MongoCollection<Client> clientsCollection = getDatabase().getCollection("clients", Client.class);
            Bson clientFilter = Filters.eq("_id", rent.getClient().getPersonalID());
            Bson clientUpdates = Updates.inc("currentRentsNumber", 1);
            clientsCollection.updateOne(clientSession, clientFilter, clientUpdates);

            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
            throw e;
        } finally {
            clientSession.close();
        }
    }


    public void update(Rent rent) {
        ClientSession clientSession = getMongoClient().startSession();
        try {
            clientSession.startTransaction();

            Bson rentFilter = Filters.eq("_id", rent.getId());
            MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
            Bson rentUpdates = Updates.combine(
                    Updates.set("client", rent.getClient()),
                    Updates.set("vM", rent.getVM()),
                    Updates.set("beginTime", rent.getBeginTime()),
                    Updates.set("endTime", rent.getEndTime()),
                    Updates.set("rentCost", rent.getRentCost())
            );
            collection.findOneAndUpdate(clientSession, rentFilter, rentUpdates);

            if (rent.getEndTime() != null) {
                MongoCollection<VirtualMachine> vmCollection = getDatabase().getCollection("vehicles", VirtualMachine.class);
                Bson vehicleFilter = Filters.eq("_id", rent.getVM().getId());
                Bson vehicleUpdates = Updates.inc("rented", -1);
                vmCollection.updateOne(clientSession, vehicleFilter, vehicleUpdates);

                MongoCollection<Client> clientsCollection = getDatabase().getCollection("clients", Client.class);
                Bson clientFilter = Filters.eq("_id", rent.getClient().getPersonalID());
                Bson clientUpdates = Updates.inc("currentRentsNumber", -1);
                clientsCollection.updateOne(clientSession, clientFilter, clientUpdates);
            }

            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
            throw e;
        } finally {
            clientSession.close();
        }
    }


    public void endRent(Rent rent) {
        ClientSession clientSession = getMongoClient().startSession();
        try {
            clientSession.startTransaction();

            MongoCollection<Rent> collection = getDatabase().getCollection("rents", Rent.class);
            Bson docFilter = Filters.and(Filters.eq("_id", rent.getId()), Filters.eq("vM._id", rent.getVM().getId()), Filters.eq("client._id", rent.getClient().getPersonalID()));
            Rent archivedRent =  collection.findOneAndDelete(clientSession, docFilter);

            if (rent.getEndTime() != null) {
                MongoCollection<VirtualMachine> vmCollection = getDatabase().getCollection("virtual_machines", VirtualMachine.class);
                Bson vmFilter = Filters.eq("_id", rent.getVM().getId());
                Bson updates = Updates.inc("rented", -1);
                vmCollection.updateOne(clientSession, vmFilter, updates);

                MongoCollection<Client> clientsCollection = getDatabase().getCollection("clients", Client.class);
                Bson clientFilter = Filters.eq("_id", rent.getClient().getPersonalID());
                Bson clientUpdates = Updates.inc("currentRentsNumber", -1);
                clientsCollection.updateOne(clientSession, clientFilter, clientUpdates);
            }
            MongoCollection<Rent> archived = getDatabase().getCollection("archived", Rent.class);
            Document date = new Document("$set", new Document("endTime", rent.getEndTime()));
            archived.insertOne(archivedRent);
            archived.updateOne(docFilter, date);


            clientSession.commitTransaction();
        } catch (Exception e) {
            clientSession.abortTransaction();
            throw e;
        } finally {
            clientSession.close();
        }
    }

}
