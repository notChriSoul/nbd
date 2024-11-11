package org.example.vms;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@NoArgsConstructor
public abstract class VirtualMachine
{

    private int id;
    @Setter
    private boolean isAvailable;
    @Setter
    private int CPUCores;
    @Setter
    private double RAM;
    @Setter
    private double StorageSpace;


    public VirtualMachine(int id, boolean isAvailable, int CPUCores, double RAM, double storageSpace) {
        this.id = id;
        this.isAvailable = isAvailable;
        this.CPUCores = CPUCores;
        this.RAM = RAM;
        StorageSpace = storageSpace;
    }

    public abstract double calculateRentalPrice();

}

