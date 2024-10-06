package org.example.vms;

public class Normal extends VirtualMachine {

    private boolean SSDSATAStorage;

    public Normal(int id, boolean isAvailable, int CPUCores, double RAM, double storageSpace) {
        super(id, isAvailable, CPUCores, RAM, storageSpace);
        boolean SSDSATAStorage = true;
    }

    @Override
    public double calculateRentalPrice() {
        return 1;
    }

    public boolean isSSDSATAStorage() {
        return SSDSATAStorage;
    }


}
