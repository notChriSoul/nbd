package org.example.Repositories;

import org.bson.Document;
import org.example.vms.*;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class VirtualMachineRepositoryTest {

    private static VirtualMachineRepository vmRepository;

    @BeforeEach
    public void setUp() {
        vmRepository = new VirtualMachineRepository();
        vmRepository.initDbConnection();

    }

    @AfterEach
    public void tearDown() {
        vmRepository.getDatabase().getCollection("virtual_machines", VirtualMachine.class).deleteMany(new Document());
        vmRepository.close();
    }

    @Test
    public void findById_VirtualMachineInDB_VMReturned() {
        Normal vm = new Normal(1, 4, 16.0, 500.0);
        vmRepository.add(vm);
        VirtualMachine foundVM = vmRepository.findById(1);
        assertNotNull(foundVM, "Virtual machine should exist in the database.");
        assertEquals(vmRepository.findById(1).getId(), foundVM.getId());
    }

    @Test
    public void findAll_TwoVMsInDB_TwoVMsListReturned() {
        Normal vm1 = new Normal(2, 4, 16.0, 500.0);
        Performance vm2 = new Performance(3, 8, 32.0, 1000.0, "NVIDIA RTX 3090");
        vmRepository.add(vm1);
        vmRepository.add(vm2);

        List<VirtualMachine> foundVMs = vmRepository.findAll();
        assertEquals(2, foundVMs.size(), "There should be 2 virtual machines in the database.");
    }

    @Test
    public void add_ValidVM_VMAdded() {
        Pro_oVirt vm = new Pro_oVirt(4, 16, 64.0, 2000.0, 2);
        vmRepository.add(vm);

        VirtualMachine foundVM = vmRepository.findById(4);
        assertNotNull(foundVM, "Virtual machine should be added to the database.");
        assertEquals(4, foundVM.getId(), "ID of the added virtual machine should match.");
    }

    @Test
    public void delete_VMInDB_VMRemoved() {
        Normal vm = new Normal(5, 2, 8.0, 250.0);
        vmRepository.add(vm);

        assertNotNull(vmRepository.findById(5), "Virtual machine should exist before deletion.");
        vmRepository.delete(vm);
        assertNull(vmRepository.findById(5), "Virtual machine should be removed after deletion.");
    }

    @Test
    public void update_ValidVM_VMUpdated() {
        Performance vm = new Performance(6, 8, 32.0, 1000.0, "NVIDIA GTX 1660");
        vmRepository.add(vm);

        // Update GPU
        String newGPU = "NVIDIA RTX 3080";
        vm.setGPU(newGPU);
        vmRepository.update(vm);

        VirtualMachine updatedVM = vmRepository.findById(6);
        assertNotNull(updatedVM, "Updated virtual machine should be retrievable.");
        assertEquals(newGPU, ((Performance) updatedVM).getGPU(), "GPU should be updated correctly.");
    }
}
