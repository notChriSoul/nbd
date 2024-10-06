package org.example;

import java.time.LocalDateTime;
import org.example.vms.VirtualMachine;
import java.time.Duration;

public class Rent {

    private final int rentId;

    private final LocalDateTime beginTime;
    private LocalDateTime endTime;

    private double rentCost;

    private boolean isArchive;

    private final Client client;

    private final VirtualMachine VM;


    public LocalDateTime getBeginTime() {
        return beginTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public VirtualMachine getVM() {
        return VM;
    }

    public int getRentId() {
        return rentId;
    }

    public double getRentCost() {
        return rentCost;
    }

    public boolean isArchive() {
        return isArchive;
    }

    public Client getClient() {
        return client;
    }

    public void setArchive(boolean archive) {
        isArchive = archive;
    }

    public long getRentDays() {
        // Obliczamy różnicę między czasami w dniach
        return Duration.between(beginTime, endTime).toDays();

    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public Rent(int rentId, LocalDateTime beginTime, Client client, VirtualMachine VM) {
        this.rentId = rentId;
        this.beginTime = beginTime;
        this.client = client;
        this.VM = VM;
        this.isArchive = false;
    }

    public void setRentCost(double rentCost) {
        this.rentCost = rentCost;
    }

    public void endRent()
    {
        setEndTime(LocalDateTime.now());
        setRentCost(getRentDays() * getVM().calculateRentalPrice());
        setArchive(true);
    }

}
