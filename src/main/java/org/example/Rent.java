package org.example;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.vms.VirtualMachine;
import java.time.Duration;

@Getter
@Entity
@NoArgsConstructor
public class Rent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rentId;

    @Setter
    private LocalDateTime beginTime;
    @Setter
    private LocalDateTime endTime;

    @Setter
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
