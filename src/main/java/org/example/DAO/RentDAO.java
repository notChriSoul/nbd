package org.example.DAO;

import org.example.Rent;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import javax.persistence.OptimisticLockException;



public class RentDAO {

    private static SessionFactory sessionFactory;

    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    // Method to save Rent (no optimistic locking issue during save)
    public void saveRent(Rent rent) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(rent);  // Saving a new resource won't trigger OptimisticLockException
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Method to update Rent with optimistic locking
    public void updateRent(Rent rent) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(rent);  // Hibernate will check version field here
            transaction.commit();
        } catch (OptimisticLockException ole) {
            System.out.println("Optimistic lock exception during update: Another transaction has modified the Rent entity!");
            if (transaction != null) {
                transaction.rollback();
            }
            ole.printStackTrace();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Method to delete Rent with optimistic locking
    public void deleteRent(int id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Rent rent = session.get(Rent.class, id);
            session.delete(rent);  // Hibernate will check version field during delete
            transaction.commit();
        } catch (OptimisticLockException ole) {
            System.out.println("Optimistic lock exception during delete: Another transaction has modified the Rent entity!");
            if (transaction != null) {
                transaction.rollback();
            }
            ole.printStackTrace();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // Method to get Rent by id
    public Rent getRent(int rentId) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Rent.class, rentId);
        }
    }
}
