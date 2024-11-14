package org.example.vms;

import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@NoArgsConstructor
@BsonDiscriminator(key = "_type", value = "normal")
public class Normal extends VirtualMachine {

    @BsonProperty("ssdSataStorage")
    private final boolean SSDSATAStorage = true;

    @BsonCreator
    public Normal(@BsonProperty("id") int id, @BsonProperty("isAvailable") boolean isAvailable,
                  @BsonProperty("ram") int CPUCores, @BsonProperty("cpuCores") double RAM,
                  @BsonProperty("storageSpace") double storageSpace) {
        super(id, isAvailable, CPUCores, RAM, storageSpace);
    }

    @Override
    public double calculateRentalPrice() {
        return 1;
    }


}
