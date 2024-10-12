package org.example;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import org.example.vms.VirtualMachine;
import java.time.Duration;

@Entity
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rentId;

    private LocalDateTime beginTime;
    private LocalDateTime endTime;

    private double rentCost;

    private boolean isArchive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ClientId")
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)  // Many rents can be associated with one VM
    @JoinColumn(name = "VmId")
    private VirtualMachine VM;

    // Optimistic locking version
    @Version
    private int version;

    public Rent() {

    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

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

    public void setBeginTime(LocalDateTime beginTime) {
        this.beginTime = beginTime;
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
