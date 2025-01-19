package example.dao;

import com.datastax.oss.driver.api.core.CqlSession;
import example.db.BaseRepository;
import example.mapper.RentMapperBuilder;
import example.model.Rent;
import example.schemas.CQLSchema;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
class RentDaoTest {
    private static RentDao rentDao;

    @BeforeAll
    static void setUpBeforeClass() {
        BaseRepository repo = new BaseRepository();
        repo.initSession();
        repo.createTables();
        CqlSession session = repo.getSession();
        rentDao = new RentMapperBuilder(session).build().getRentDao();
    }

    @Test
    void createRent() {
        String id = UUID.randomUUID().toString();
        Rent rent = Rent.build(id, "2", "1", LocalDateTime.now(), LocalDateTime.now(), 1);

        rentDao.create(rent);
    }

    @Test
    void endRent() {
        String id = UUID.randomUUID().toString();
        Rent rent = Rent.build(id, "2", "1", LocalDateTime.now(), LocalDateTime.now(), 1);

        rent.setEndTime(LocalDateTime.now());

        rentDao.create(rent);
        rentDao.endRent(rent);
    }


    @Test
    public void getRentsByClient() {
        String id = UUID.randomUUID().toString();
        Rent rent = Rent.build(id, "2", "1", LocalDateTime.now(), LocalDateTime.now(), 1);

        rent.setEndTime(LocalDateTime.now());

        rentDao.create(rent);
        rentDao.endRent(rent);

        List<Rent> clientRents = rentDao.findByClientId("2");

        List<Rent> vmRents = rentDao.findByVMachineId("1");

        List<Rent> rents = rentDao.findAllByTable(CQLSchema.RENTS_BY_CLIENT);

        System.out.println("test");

    }
  
}