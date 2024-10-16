package org.example.DAO;

import org.example.vms.VirtualMachine;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.OptimisticLockException;

public class VirtualMachineDAO implements DAO<VirtualMachine> {

    private static SessionFactory sessionFactory;

    // Static block for initializing Hibernate configuration
    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    // CREATE - Save new VirtualMachine
    public void save(VirtualMachine vm) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.save(vm);
            transaction.commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    // READ - Get VirtualMachine by ID
    public VirtualMachine get(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(VirtualMachine.class, id);
        }
    }

    // UPDATE - Update VirtualMachine with optimistic locking
    public void update(VirtualMachine vm) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(vm);  // Version field is checked here
            transaction.commit();
        } catch (OptimisticLockException ole) {
            System.out.println("OptimisticLockException during update: Another transaction has updated this virtual machine.");
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

    // DELETE - Delete VirtualMachine with optimistic locking
    public void delete(int id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            VirtualMachine vm = session.get(VirtualMachine.class, id);
            session.delete(vm);  // Version field is checked here
            transaction.commit();
        } catch (OptimisticLockException ole) {
            System.out.println("OptimisticLockException during delete: Another transaction has updated this virtual machine.");
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
