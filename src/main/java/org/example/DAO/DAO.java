package org.example.DAO;

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
