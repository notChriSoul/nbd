package org.example.vms;


import jakarta.persistence.*;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vm_type", discriminatorType = DiscriminatorType.STRING)
public abstract class VirtualMachine
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private boolean isAvailable;
    private int CPUCores;
    private double RAM;
    private double StorageSpace;

    // Optimistic locking version field
    @Version
    private int version;

    public VirtualMachine(int id, boolean isAvailable, int CPUCores, double RAM, double storageSpace) {
        this.id = id;
        this.isAvailable = isAvailable;
        this.CPUCores = CPUCores;
        this.RAM = RAM;
        StorageSpace = storageSpace;
    }

    public VirtualMachine() {

    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public void setCPUCores(int CPUCores) {
        this.CPUCores = CPUCores;
    }

    public void setRAM(double RAM) {
        this.RAM = RAM;
    }

    public void setStorageSpace(double storageSpace) {
        StorageSpace = storageSpace;
    }

    public abstract double calculateRentalPrice();
    public int getId() {
        return id;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public int getCPUCores() {
        return CPUCores;
    }

    public double getRAM() {
        return RAM;
    }

    public double getStorageSpace() {
        return StorageSpace;
    }


}

