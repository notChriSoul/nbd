package org.example.vms;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("Performance")
@NoArgsConstructor
public class Performance extends VirtualMachine {

    private String GPU;
    private final boolean NVMeStorage = true;

    public Performance(int id, boolean isAvailable, int CPUCores, double RAM, double storageSpace
    , String GPU) {
        super(id, isAvailable, CPUCores, RAM, storageSpace);
        this.GPU = GPU;
    }

    @Override
    public double calculateRentalPrice() {
        return 2;
    }
}
