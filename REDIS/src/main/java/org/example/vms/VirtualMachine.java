package org.example.vms;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.codecs.pojo.annotations.BsonCreator;
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

    @BsonProperty("rented")
    @Setter
    private int rented = 0;

    @BsonProperty("cpuCores")
    @Setter
    private int CPUCores;

    @BsonProperty("ram")
    @Setter
    private double RAM;

    @BsonProperty("storageSpace")
    @Setter
    private double StorageSpace;

    @BsonCreator
    public VirtualMachine(@BsonProperty("id") int id,
                          @BsonProperty("ram") int CPUCores, @BsonProperty("cpuCores") double RAM,
                          @BsonProperty("storageSpace") double storageSpace) {
        this.id = id;
        this.rented = 0;
        this.CPUCores = CPUCores;
        this.RAM = RAM;
        StorageSpace = storageSpace;
    }

    public abstract double calculateRentalPrice();

}

