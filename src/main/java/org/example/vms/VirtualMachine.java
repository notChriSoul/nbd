package org.example.vms;

public abstract class VirtualMachine
{
    private int id;
    private  boolean isAvailable;
    private  int CPUCores;
    private double RAM;
    private double StorageSpace;


    public VirtualMachine(int id, boolean isAvailable, int CPUCores, double RAM, double storageSpace) {
        this.id = id;
        this.isAvailable = isAvailable;
        this.CPUCores = CPUCores;
        this.RAM = RAM;
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

