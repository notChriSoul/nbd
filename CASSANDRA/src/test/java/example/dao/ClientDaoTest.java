package example.dao;

import com.datastax.oss.driver.api.core.CqlSession;
import example.mapper.ClientMapperBuilder;
import example.mapper.VmMapperBuilder;
import example.model.Client;
import example.model.vms.Normal;
import example.model.vms.Pro;
import example.repositories.BaseRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;


class ClientDaoTest {
    private static ClientDao clientDao;

    @BeforeAll
    static void setUp() {
        BaseRepository baseRepository = new BaseRepository();
        baseRepository.initSession();
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
        Client client = clientDao.findClientById("2");
        String newName = "moe";
        client.setFirstName(newName);
        clientDao.update(client);
        Client updatedClient = clientDao.findClientById("2");
        System.out.println(updatedClient.getFirstName());
        Assertions.assertEquals(newName, updatedClient.getFirstName());
    }

    @Test
    void deleteClient() {
        Client client = new Client( "3", "joe", "doe");
        clientDao.create(client);
        Client deletedClient = clientDao.findClientById("3");
        Assertions.assertNotNull(deletedClient);
        clientDao.delete(deletedClient);
        deletedClient = clientDao.findClientById("3");
        Assertions.assertNull(deletedClient);
    }
}