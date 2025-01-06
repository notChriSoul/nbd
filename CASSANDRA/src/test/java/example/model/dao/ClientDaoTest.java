package example.model.dao;

import com.datastax.oss.driver.api.core.CqlSession;
import example.dao.ClientDao;
import example.mapper.ClientMapper;
import example.mapper.ClientMapperBuilder;
import example.model.Client;
import example.repositories.BaseRepository;
import org.junit.jupiter.api.Test;


class ClientDaoTest {


    @Test
    void test() {
        BaseRepository baseRepository = new BaseRepository();
        baseRepository.initSession();
        CqlSession session = baseRepository.getSession();
        Client client = new Client( "2", "joe", "doe");

        ClientDao clientDao = new ClientMapperBuilder(session).build().clientDao();

        clientDao.create(client);



    }

    @Test
    void getClientById() {
        BaseRepository baseRepository = new BaseRepository();
        baseRepository.initSession();
        CqlSession session = baseRepository.getSession();

        ClientDao clientDao = new ClientMapperBuilder(session).build().clientDao();

        Client client = clientDao.findClientById("2");
        System.out.println(client.getFirstName());
    }
}