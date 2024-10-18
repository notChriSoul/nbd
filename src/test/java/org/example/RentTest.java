package org.example;

import org.example.DAO.ClientDAO;
import org.example.DAO.RentDAO;
import org.example.DAO.VirtualMachineDAO;
import org.example.exceptions.MaxRentLimitException;
import org.example.managers.RentManager;
import org.example.vms.Normal;
import org.example.vms.Performance;
import org.example.vms.Pro_oVirt;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import jakarta.persistence.OptimisticLockException;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertThrows;

public class RentTest {

    private static SessionFactory sessionFactory;

    @BeforeEach
    public void setup() {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    @Test
    public void testOptimisticLocking() {
        ClientDAO clientDAO = new ClientDAO();
        VirtualMachineDAO vmDAO = new VirtualMachineDAO();


        // CREATE - zapis nowego klienta
        Client client = new Client(1, "John", "Doe");
        clientDAO.save(client);

        Normal normalVM = new Normal(1, true, 4, 16.0, 500.0);
        vmDAO.save(normalVM);



        // Create RentDAO and set up initial Rent object
        RentDAO rentDAO = new RentDAO();

        // Create a new Rent and save it
        Rent rent = new Rent(0, LocalDateTime.now(), client, normalVM);

        rentDAO.save(rent);

        // Simulate two sessions (two different users/transactions trying to update the same Rent)

        // First transaction
        Session session1 = sessionFactory.openSession();
        Transaction transaction1 = session1.beginTransaction();
        Rent rent1 = session1.get(Rent.class, rent.getRentId());

        // Second transaction
        Session session2 = sessionFactory.openSession();
        Transaction transaction2 = session2.beginTransaction();
        Rent rent2 = session2.get(Rent.class, rent.getRentId());

        // Make changes in both transactions
        rent1.setRentCost(150.0);  // First transaction updates rent cost
        rent2.setRentCost(200.0);  // Second transaction updates rent cost as well

        // Commit first transaction
        session1.update(rent1);
        transaction1.commit();
        session1.close();

        // Now commit second transaction and expect OptimisticLockException
        rent2.setRentCost(250.0);
        session2.update(rent2);

        assertThrows(OptimisticLockException.class, transaction2::commit, "Optimistic lock should have been thrown");

        // Clean up
        session2.close();
    }

    @Test
    public void testMaxRent() {
        ClientDAO clientDAO = new ClientDAO();
        VirtualMachineDAO vmDAO = new VirtualMachineDAO();


        // CREATE - zapis nowego klienta
        Client client = new Client(1, "John", "Doe");
        clientDAO.save(client);

        Normal normalVM = new Normal(1, true, 4, 16.0, 500.0);
        vmDAO.save(normalVM);

        Performance performanceVM = new Performance(2, true, 8, 32.0, 1000.0, "NVIDIA RTX 3080");
        vmDAO.save(performanceVM);

        Pro_oVirt proOVirtVM = new Pro_oVirt(3, true, 16, 128.0, 2000.0, 4);
        vmDAO.save(proOVirtVM);


        RentManager rm = new RentManager();
        rm.createRent(LocalDateTime.now(), client, normalVM);
        rm.createRent(LocalDateTime.now(), client, performanceVM);
        assertThrows(MaxRentLimitException.class, () -> rm.createRent(LocalDateTime.now(), client, proOVirtVM));

    }
}