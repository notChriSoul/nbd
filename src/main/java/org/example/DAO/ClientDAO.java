package org.example.DAO;

import org.example.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.OptimisticLockException;

public class ClientDAO {

    private static SessionFactory sessionFactory;

    // Static block for initializing Hibernate configuration
    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    // CREATE - Save new client
    public void saveClient(Client client) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(client);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    // READ - Get client by ID
    public Client getClient(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(Client.class, id);
        }
    }

    // UPDATE - Update client with optimistic locking
    public void updateClient(Client client) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(client);  // Version field is checked here
            transaction.commit();
        } catch (OptimisticLockException ole) {
            System.out.println("OptimisticLockException during update: Another transaction has updated this client.");
            if (transaction != null) {
                transaction.rollback();
            }
            ole.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }



    // DELETE - Delete client with optimistic locking
    public void deleteClient(int id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            Client client = session.get(Client.class, id);
            session.delete(client);  // Version field is checked here
            transaction.commit();
        } catch (OptimisticLockException ole) {
            System.out.println("OptimisticLockException during delete: Another transaction has updated this client.");
            if (transaction != null) {
                transaction.rollback();
            }
            ole.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
