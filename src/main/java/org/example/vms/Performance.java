package org.example.vms;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("Performance")
public class Performance extends VirtualMachine {

    private String GPU;
    private boolean NVMeStorage = true;

    public Performance(int id, boolean isAvailable, int CPUCores, double RAM, double storageSpace
    , String GPU) {
        super(id, isAvailable, CPUCores, RAM, storageSpace);
        this.GPU = GPU;
    }

    public Performance() {

    }

    public String getGPU() {
        return GPU;
    }

    public boolean isNVMeStorage() {
        return NVMeStorage;
    }

    @Override
    public double calculateRentalPrice() {
        return 2;
    }
}
