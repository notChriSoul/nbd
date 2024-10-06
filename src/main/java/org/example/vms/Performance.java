package org.example.vms;

public class Performance extends VirtualMachine {

    private String GPU;
    private final boolean NVMeStorage;

    public Performance(int id, boolean isAvailable, int CPUCores, double RAM, double storageSpace
    , String GPU) {
        super(id, isAvailable, CPUCores, RAM, storageSpace);
            this.NVMeStorage = true;
    }

    public String getGPU() {
        return GPU;
    }

    public boolean isNVMeStorage() {
        return NVMeStorage;
    }

    @Override
    public double calculateRentalPrice() {
        return 0;
    }
}
