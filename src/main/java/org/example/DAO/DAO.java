package org.example.DAO;

import org.example.Client;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import javax.persistence.OptimisticLockException;

public interface DAO<T> {

        // CREATE - Save new client
        public void save(T object);
        // READ - Get client by ID
        public T get(int id);
        // UPDATE - Update client with optimistic locking
        public void update(T object);
        // DELETE - Delete client with optimistic locking
        public void delete(int id);

}
