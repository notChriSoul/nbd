package example.dao;

import com.datastax.oss.driver.api.core.CqlSession;
import example.db.BaseRepository;
import example.mapper.ClientMapperBuilder;
import example.mapper.RentMapper;
import example.mapper.RentMapperBuilder;
import example.mapper.VmMapperBuilder;
import example.model.Client;
import example.model.Rent;
import example.model.vms.Normal;
import example.schemas.CQLSchema;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
class RentDaoTest {
    private static RentDao rentDao;
    private static ClientDao clientDao;
    private static VmDao vmDao;

    @BeforeAll
    static void setUpBeforeClass() {
        BaseRepository repo = new BaseRepository();
        repo.initSession();
        repo.createTables();
        CqlSession session = repo.getSession();
        clientDao = new ClientMapperBuilder(session).build().clientDao();
        vmDao = new VmMapperBuilder(session).build().VmDao();
        rentDao = new RentMapperBuilder(session).build().getRentDao();
    }

    @AfterAll
    static void tearDownAfterClass() {
        BaseRepository repo = new BaseRepository();
        repo.dropAll();
        repo.close();
    }

    @Test
    void createRent() {
        String id = UUID.randomUUID().toString();
        Client client = new Client( "2", "joe", "doe");
        clientDao.create(client);
        Normal normal = new Normal("1", 1,"Normal", false, 1, 1, 1);
        vmDao.create(normal);
        Rent rent = Rent.build(id, client.getPersonalId(), normal.getVmId(), LocalDateTime.now(), LocalDateTime.now(), 1);

        rentDao.create(rent);
    }

    @Test
    void endRent() {
        String id = UUID.randomUUID().toString();
        String clientId = UUID.randomUUID().toString();
        String vmId = UUID.randomUUID().toString();
        Client client = new Client( clientId, "joe", "doe");
        clientDao.create(client);
        Normal normal = new Normal(vmId, 1,"Normal", false, 1, 1, 1);
        vmDao.create(normal);
        Rent rent = Rent.build(id, client.getPersonalId(), normal.getVmId(), LocalDateTime.now(), LocalDateTime.now(), 1);
        rentDao.create(rent);

        Assertions.assertNotNull(rent.getEndTime());
        rent.setEndTime(LocalDateTime.now());

        rentDao.endRent(rent);
        Rent rent1 = rentDao.findByClientId(clientId).getFirst();
        Assertions.assertNotNull(rent1.getEndTime());
    }


    @Test
    public void getRentsByClient() {
        String id = UUID.randomUUID().toString();
        String clientId = UUID.randomUUID().toString();
        String vmId = UUID.randomUUID().toString();
        Client client = new Client( clientId, "joe", "doe");
        clientDao.create(client);
        Normal normal = new Normal(vmId, 1,"Normal", false, 1, 1, 1);
        vmDao.create(normal);
        Rent rent = Rent.build(id, clientId, vmId, LocalDateTime.now(), LocalDateTime.now(), 1);

        rent.setEndTime(LocalDateTime.now());

        rentDao.create(rent);
        rentDao.endRent(rent);

        List<Rent> clientRents = rentDao.findByClientId(clientId);

        List<Rent> vmRents = rentDao.findByVMachineId(vmId);

        List<Rent> rents = rentDao.findAllByTable(CQLSchema.RENTS_BY_CLIENT);

        System.out.println("test");

    }
  
}