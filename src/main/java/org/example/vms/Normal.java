package org.example.vms;


import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@DiscriminatorValue("Normal")
@NoArgsConstructor
public class Normal extends VirtualMachine {

    private final boolean SSDSATAStorage = true;

    public Normal(int id, boolean isAvailable, int CPUCores, double RAM, double storageSpace) {
        super(id, isAvailable, CPUCores, RAM, storageSpace);
    }

    @Override
    public double calculateRentalPrice() {
        return 1;
    }


}
