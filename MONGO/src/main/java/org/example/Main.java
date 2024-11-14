package org.example;

import org.example.Repositories.VirtualMachineRepository;
import org.example.vms.Normal;
import org.example.vms.Performance;
import org.example.vms.Pro_oVirt;
import org.example.vms.VirtualMachine;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        // Initialize the repository
        VirtualMachineRepository vmRepo = new VirtualMachineRepository();
        vmRepo.initDbConnection(); // Make sure database connection is initialized

        // Test data
        Normal normalVM = new Normal(1, true, 4, 16.0, 256.0);
        Performance performanceVM = new Performance(2, true, 8, 32.0, 512.0, "NVIDIA RTX 3090");
        Pro_oVirt proOVirtVM = new Pro_oVirt(3, false, 16, 64.0, 1024.0, 4);

        // Step 1: Add Virtual Machines
        System.out.println("Adding virtual machines...");
        vmRepo.add(normalVM);
        vmRepo.add(performanceVM);
        vmRepo.add(proOVirtVM);

        // Step 2: Retrieve all VMs
        System.out.println("\nRetrieving all virtual machines:");
        ArrayList<VirtualMachine> allVMs = vmRepo.findAll();
        for (VirtualMachine vm : allVMs) {
            System.out.println(vm.getClass().getSimpleName() + " - ID: " + vm.getId() + ", Available: " + vm.isAvailable());
        }

        // Step 3: Find a specific VM by ID
        System.out.println("\nFinding a virtual machine by ID (ID = 2):");
        VirtualMachine foundVM = vmRepo.findById(2);
        if (foundVM != null) {
            System.out.println("Found VM: " + foundVM.getClass().getSimpleName() + " - ID: " + foundVM.getId());
        } else {
            System.out.println("VM not found!");
        }

        // Step 4: Delete a Virtual Machine
        System.out.println("\nDeleting virtual machine with ID = 1");
        vmRepo.delete(normalVM);

        // Verify deletion
        System.out.println("\nRetrieving all virtual machines after deletion:");
        allVMs = vmRepo.findAll();
        for (VirtualMachine vm : allVMs) {
            System.out.println(vm.getClass().getSimpleName() + " - ID: " + vm.getId() + ", Available: " + vm.isAvailable());
        }

        // Close the connection
        vmRepo.close();
    }
}
