package org.example.vms;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonDiscriminator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

@Getter
@NoArgsConstructor
@BsonDiscriminator(key = "_type")
public abstract class VirtualMachine
{

    @BsonId
    private int id;

    @BsonProperty("isAvailable")
    @Setter
    private boolean isAvailable;

    @BsonProperty("cpuCores")
    @Setter
    private int CPUCores;

    @BsonProperty("ram")
    @Setter
    private double RAM;

    @BsonProperty("storageSpace")
    @Setter
    private double StorageSpace;


    public VirtualMachine(@BsonProperty("id") int id, @BsonProperty("isAvailable") boolean isAvailable,
                          @BsonProperty("ram") int CPUCores, @BsonProperty("cpuCores") double RAM,
                          @BsonProperty("storageSpace") double storageSpace) {
        this.id = id;
        this.isAvailable = isAvailable;
        this.CPUCores = CPUCores;
        this.RAM = RAM;
        StorageSpace = storageSpace;
    }

    public abstract double calculateRentalPrice();

}

