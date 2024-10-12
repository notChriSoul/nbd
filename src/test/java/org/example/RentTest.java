package org.example;

import static org.junit.jupiter.api.Assertions.*;
import org.example.DAO.RentDAO;
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
        // Create RentDAO and set up initial Rent object
        RentDAO rentDAO = new RentDAO();

        // Create a new Rent and save it
        Rent rent = new Rent();
        rent.setBeginTime(LocalDateTime.now());
        rent.setEndTime(LocalDateTime.now().plusDays(2));
        rent.setRentCost(100.0);
        rent.setArchive(false);
        rentDAO.saveRent(rent);

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
}