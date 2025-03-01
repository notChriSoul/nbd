package org.example.vms;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Data
@NoArgsConstructor
@BsonDiscriminator(key = "_type", value = "proovirt")
public class Pro_oVirt extends VirtualMachine {

    @BsonProperty("NumaArchitecture")
    private final boolean NUMA_architecture = true;

    @BsonProperty("numaNodes")
    private int NUMA_nodes;

    @BsonCreator
    public Pro_oVirt(@BsonProperty("id") int id,
                     @BsonProperty("ram") int CPUCores, @BsonProperty("cpuCores") double RAM,
                     @BsonProperty("storageSpace") double storageSpace,
                     @BsonProperty("numaNodes") int NUMA_nodes) {
        super(id, CPUCores, RAM, storageSpace);
        this.NUMA_nodes = NUMA_nodes;
    }

    @Override
    public double calculateRentalPrice() {
        return 5;
    }
}