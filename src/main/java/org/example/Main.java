package org.example;

import org.example.DAO.ClientDAO;
import org.example.DAO.VirtualMachineDAO;
import org.example.vms.*;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

        //TIP Press <shortcut actionId="ShowIntentionActions"/> with your caret at the highlighted text
        // to see how IntelliJ IDEA suggests fixing it.
        ClientDAO clientDAO = new ClientDAO();
        VirtualMachineDAO vmDAO = new VirtualMachineDAO();


        // CREATE - zapis nowego klienta
        Client client = new Client(1, "John", "Doe");
        clientDAO.save(client);

        // READ - pobranie klienta po ID
        Client retrievedClient = clientDAO.get(client.getPersonalId());
        System.out.println("Retrieved client: " + retrievedClient.getFirstName() + " " + retrievedClient.getLastName());

        // UPDATE - aktualizacja danych klienta
        retrievedClient.setFirstName("Jane");
        clientDAO.update(retrievedClient);

        // DELETE - usunięcie klienta
        clientDAO.delete(retrievedClient.getPersonalId());



        // ************** NORMAL VM TEST **************

        // CREATE - save a Normal VM
        Normal normalVM = new Normal(1, true, 4, 16.0, 500.0);
        vmDAO.save(normalVM);

        // READ - fetch Normal VM by ID
        Normal retrievedNormalVM = (Normal) vmDAO.get(normalVM.getId());
        System.out.println("Retrieved Normal VM - CPU Cores: " + retrievedNormalVM.getCPUCores());

        // UPDATE - modify some data for Normal VM
        retrievedNormalVM.setCPUCores(8);  // Updating CPU cores
        vmDAO.update(retrievedNormalVM);

        // DELETE - remove Normal VM
        vmDAO.delete(retrievedNormalVM.getId());

        // ************** PERFORMANCE VM TEST **************

        // CREATE - save a Performance VM
        Performance performanceVM = new Performance(2, true, 8, 32.0, 1000.0, "NVIDIA RTX 3080");
        vmDAO.save(performanceVM);

        // READ - fetch Performance VM by ID
        Performance retrievedPerformanceVM = (Performance) vmDAO.get(performanceVM.getId());
        System.out.println("Retrieved Performance VM - GPU: " + retrievedPerformanceVM.getGPU());

        // UPDATE - modify some data for Performance VM
        retrievedPerformanceVM.setRAM(64.0);  // Updating RAM
        vmDAO.update(retrievedPerformanceVM);

        // DELETE - remove Performance VM
        vmDAO.delete(retrievedPerformanceVM.getId());

        // ************** PRO oVIRT VM TEST **************

        // CREATE - save a Pro_oVirt VM
        Pro_oVirt proOVirtVM = new Pro_oVirt(3, true, 16, 128.0, 2000.0, 4);
        vmDAO.save(proOVirtVM);

        // READ - fetch Pro_oVirt VM by ID
        Pro_oVirt retrievedProOVirtVM = (Pro_oVirt) vmDAO.get(proOVirtVM.getId());
        System.out.println("Retrieved Pro_oVirt VM - NUMA Nodes: " + retrievedProOVirtVM.getNUMA_nodes());

        // UPDATE - modify some data for Pro_oVirt VM
        retrievedProOVirtVM.setStorageSpace(3000.0);  // Updating storage space
        vmDAO.update(retrievedProOVirtVM);

        // DELETE - remove Pro_oVirt VM
        vmDAO.delete(retrievedProOVirtVM.getId());

    }
}

/*
        System.out.printf("Hello and welcome!");

        for (int i = 1; i <= 5; i++) {
            //TIP Press <shortcut actionId="Debug"/> to start debugging your code. We have set one <icon src="AllIcons.Debugger.Db_set_breakpoint"/> breakpoint
            // for you, but you can always add more by pressing <shortcut actionId="ToggleLineBreakpoint"/>.
            System.out.println("i = " + i);
        }
 */