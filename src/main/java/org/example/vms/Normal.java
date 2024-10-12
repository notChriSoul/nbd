package org.example.vms;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Normal")
public class Normal extends VirtualMachine {

    private boolean SSDSATAStorage = true;

    public Normal(int id, boolean isAvailable, int CPUCores, double RAM, double storageSpace) {
        super(id, isAvailable, CPUCores, RAM, storageSpace);
    }

    public Normal() {

    }

    @Override
    public double calculateRentalPrice() {
        return 1;
    }

    public boolean isSSDSATAStorage() {
        return SSDSATAStorage;
    }


}
