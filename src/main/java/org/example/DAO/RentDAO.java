package org.example.DAO;

import org.example.Client;
import org.example.Rent;
import org.example.exceptions.MaxRentLimitException;
import org.hibernate.LockMode;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.OptimisticLockException;
import java.util.List;


public class RentDAO implements DAO<Rent> {
    private static SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    public void save(Rent rent) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.get(Client.class, rent.getClient().getPersonalId(), LockMode.OPTIMISTIC_FORCE_INCREMENT);

            int numberOfRents = session.createQuery("FROM Rent r WHERE r.client.id = :clientId AND r.isArchive = false", Rent.class)
                    .setParameter("clientId", rent.getClient().getPersonalId())
                    .getResultList().size();
            if (numberOfRents >= 2) {
                throw new MaxRentLimitException("Client cannot have more than 2 active rents at the same time.");
            }
            session.persist(rent);
            transaction.commit();
        } catch (OptimisticLockException ole) {
            // Handle optimistic locking exception
            if (transaction != null) {
                transaction.rollback();
            }
            throw ole; // Rethrow for further handling
        } catch (MaxRentLimitException mrle) {
            throw new MaxRentLimitException("Client cannot have more than 2 active rents at the same time.");
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public Rent get(int rentId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Rent.class, rentId);
        }
    }

    public void update(Rent rent) {
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
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    public void delete(int rentId) {
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
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
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
