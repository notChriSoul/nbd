package example.dao;

import com.datastax.oss.driver.api.core.CqlSession;
import example.mapper.ClientMapperBuilder;
import example.model.Client;
import example.db.BaseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class ClientDaoTest {
    private static ClientDao clientDao;

    @BeforeAll
    static void setUp() {
        BaseRepository baseRepository = new BaseRepository();
        baseRepository.initSession();
        baseRepository.createTables();
        CqlSession session = baseRepository.getSession();
        clientDao = new ClientMapperBuilder(session).build().clientDao();
    }

    @Test
    void test() {
        Client client = new Client( "2", "joe", "doe");
        clientDao.create(client);

    }

    @Test
    void getClientById() {
        Client client = clientDao.findClientById("2");
        System.out.println(client.getFirstName());
    }

    @Test
    void updateClient() {
        String id = "21";
        Client client = new Client( id, "joe", "doe");
        clientDao.create(client);
        Client clientNew = clientDao.findClientById(id);
        String newName = "moe";
        clientNew.setFirstName(newName);
        clientDao.update(clientNew);
        Client updatedClient = clientDao.findClientById(id);
        System.out.println(updatedClient.getFirstName());
        Assertions.assertEquals(newName, updatedClient.getFirstName());
    }

    @Test
    void deleteClient() {
        Client client = new Client( "3", "joe", "doe");
        clientDao.create(client);
        Client deletedClient = clientDao.findClientById("3");
        Assertions.assertNotNull(deletedClient);
        clientDao.delete("3");
        deletedClient = clientDao.findClientById("3");
        Assertions.assertNull(deletedClient);
    }
}