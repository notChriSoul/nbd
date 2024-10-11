package org.example.DAO;

import org.example.vms.VirtualMachine;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

public class VirtualMachineDAO {

    private static SessionFactory sessionFactory;

    // Statyczny blok inicjalizujący konfigurację Hibernate
    static {
        sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
    }

    // CREATE
    public void saveVirtualMachine(VirtualMachine vm) {
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

    // READ
    public VirtualMachine getVirtualMachine(int id) {
        try (Session session = sessionFactory.openSession()) {
            return session.get(VirtualMachine.class, id);
        }
    }

    // UPDATE
    public void updateVirtualMachine(VirtualMachine vm) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            session.update(vm);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    // DELETE
    public void deleteVirtualMachine(int id) {
        Transaction transaction = null;
        try (Session session = sessionFactory.openSession()) {
            transaction = session.beginTransaction();
            VirtualMachine vm = session.get(VirtualMachine.class, id);
            if (vm != null) {
                session.delete(vm);
                transaction.commit();
            }
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }
}
