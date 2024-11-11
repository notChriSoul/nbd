package org.example.vms;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("Pro_oVirt")
@NoArgsConstructor
public class Pro_oVirt extends VirtualMachine {

    private final boolean NUMA_architecture = true;

    private int NUMA_nodes;

    public Pro_oVirt(int id, boolean isAvailable, int CPUCores, double RAM, double storageSpace,
                     int NUMA_nodes) {
        super(id, isAvailable, CPUCores, RAM, storageSpace);
        this.NUMA_nodes = NUMA_nodes;
    }

    @Override
    public double calculateRentalPrice() {
        return 5;
    }
}