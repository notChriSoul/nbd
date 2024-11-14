package org.example.vms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@NoArgsConstructor
@BsonDiscriminator(key = "_type", value = "performance")
public class Performance extends VirtualMachine {

    @BsonProperty("gpu")
    private String GPU;
    @BsonProperty("nvmestorage")
    private final boolean NVMeStorage = true;

    @BsonCreator
    public Performance(@BsonProperty("id") int id, @BsonProperty("isAvailable") boolean isAvailable,
                       @BsonProperty("ram") int CPUCores, @BsonProperty("cpuCores") double RAM,
                       @BsonProperty("storageSpace") double storageSpace,
                       @BsonProperty("gpu") String GPU) {
        super(id, isAvailable, CPUCores, RAM, storageSpace);
        this.GPU = GPU;
    }

    @Override
    public double calculateRentalPrice() {
        return 2;
    }
}
