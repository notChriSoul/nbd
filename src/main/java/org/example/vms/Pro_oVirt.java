package org.example.vms;

public class Pro_oVirt extends VirtualMachine {

    private final boolean NUMA_architecture;
    private int NUMA_nodes;
    public Pro_oVirt(int id, boolean isAvailable, int CPUCores, double RAM, double storageSpace,
                     int NUMA_nodes) {
        super(id, isAvailable, CPUCores, RAM, storageSpace);
        this.NUMA_architecture = true;
    }

    public boolean isNUMA_architecture() {
        return NUMA_architecture;
    }

    public int getNUMA_nodes() {
        return NUMA_nodes;
    }

    @Override
    public double calculateRentalPrice() {
        return 5;
    }
}