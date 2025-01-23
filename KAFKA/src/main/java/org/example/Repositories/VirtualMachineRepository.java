package org.example.Repositories;

import com.mongodb.client.model.Updates;
import org.example.vms.Normal;
import org.example.vms.Performance;
import org.example.vms.Pro_oVirt;
import org.example.vms.VirtualMachine;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.conversions.Bson;

import java.util.ArrayList;

public class VirtualMachineRepository extends AbstractMongoRepository {

    public VirtualMachine findById(Object id) {
        Bson filter = Filters.eq("_id", id);
        MongoCollection<VirtualMachine> collection = getDatabase().getCollection("virtual_machines", VirtualMachine.class);
        FindIterable<VirtualMachine> vms = collection.find(filter);
        return vms.first();
    }

    public ArrayList<VirtualMachine> findAll() {
        MongoCollection<VirtualMachine> collection = getDatabase().getCollection("virtual_machines", VirtualMachine.class);
        return collection.find().into(new ArrayList<>());
    }

    public void update(VirtualMachine vm) {
        Bson filter = Filters.eq("_id", vm.getId());
        MongoCollection<VirtualMachine> collection = getDatabase().getCollection("virtual_machines", VirtualMachine.class);
        collection.replaceOne(filter, vm);
    }

    public void add(VirtualMachine vm) {
        MongoCollection<VirtualMachine> collection = getDatabase().getCollection("virtual_machines", VirtualMachine.class);
        collection.insertOne(vm);
    }

    public void delete(VirtualMachine vm) {
        Bson filter = Filters.eq("_id", vm.getId());
        MongoCollection<VirtualMachine> collection = getDatabase().getCollection("virtual_machines", VirtualMachine.class);
        collection.findOneAndDelete(filter);
    }

}
