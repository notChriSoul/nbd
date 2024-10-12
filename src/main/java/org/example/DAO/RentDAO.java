package org.example.DAO;

import jakarta.persistence.Query;
import org.example.Rent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import javax.persistence.OptimisticLockException;
import java.util.List;


public class RentDAO {
    private static SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public void saveRent(Rent rent) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(rent);
            transaction.commit();
        } catch (OptimisticLockException ole) {
            // Handle optimistic locking exception
            if (transaction != null) {
                transaction.rollback();
            }
            throw ole; // Rethrow for further handling
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public Rent getRent(int rentId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Rent.class, rentId);
        }
    }

    public void updateRent(Rent rent) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(rent);
            transaction.commit();
        } catch (OptimisticLockException ole) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw ole; // Rethrow for further handling
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public void deleteRent(int rentId) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Rent rent = session.get(Rent.class, rentId);
            if (rent != null) {
                session.delete(rent);
                transaction.commit();
            }
        } catch (OptimisticLockException ole) {
            if (transaction != null) {
                transaction.rollback();
            }
            throw ole; // Rethrow for further handling
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Rent> getActiveRentsByClient(int clientId) {
        try (Session session = sessionFactory.openSession()) {
            return session.createQuery("FROM Rent r WHERE r.client.id = :clientId AND r.isArchive = false", Rent.class)
                    .setParameter("clientId", clientId)
                    .getResultList();
        }
    }
}
