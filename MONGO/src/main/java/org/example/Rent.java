package org.example;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.vms.VirtualMachine;
import java.time.Duration;

@Getter
@NoArgsConstructor
public class Rent {


    private int rentId;

    @Setter
    private LocalDateTime beginTime;
    @Setter
    private LocalDateTime endTime;

    @Setter
    private double rentCost;

    private boolean isArchive;

    private Client client;

    private VirtualMachine VM;

    public void setArchive(boolean archive) {
        isArchive = archive;
    }

    public long getRentDays() {
        // Obliczamy różnicę między czasami w dniach
        return Duration.between(beginTime, endTime).toDays();

    }

    public Rent(int rentId, LocalDateTime beginTime, Client client, VirtualMachine VM) {
        this.rentId = rentId;
        this.beginTime = beginTime;
        this.client = client;
        this.VM = VM;
        this.isArchive = false;
    }

    public void endRent()
    {
        setEndTime(LocalDateTime.now());
        setRentCost(getRentDays() * getVM().calculateRentalPrice());
        setArchive(true);
    }

}
