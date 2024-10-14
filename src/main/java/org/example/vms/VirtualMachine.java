package org.example.vms;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "vm_type", discriminatorType = DiscriminatorType.STRING)
@NoArgsConstructor
public abstract class VirtualMachine
{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Setter
    private boolean isAvailable;
    @Setter
    private int CPUCores;
    @Setter
    private double RAM;
    @Setter
    private double StorageSpace;

    // Optimistic locking version field
    @Setter
    @Version
    private int version;

    public VirtualMachine(int id, boolean isAvailable, int CPUCores, double RAM, double storageSpace) {
        this.id = id;
        this.isAvailable = isAvailable;
        this.CPUCores = CPUCores;
        this.RAM = RAM;
        StorageSpace = storageSpace;
    }

    public abstract double calculateRentalPrice();

}

