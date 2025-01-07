package example.dao;

import com.datastax.oss.driver.api.core.CqlSession;
import example.mapper.ClientMapperBuilder;
import example.mapper.VmMapperBuilder;
import example.model.vms.Normal;
import example.model.vms.Pro;
import example.model.vms.VirtualMachine;
import example.repositories.BaseRepository;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VmDaoTest {

    private static VmDao vmDao;

    @BeforeAll
    static void setUp() {
        BaseRepository baseRepository = new BaseRepository();
        baseRepository.initSession();
        CqlSession session = baseRepository.getSession();
        vmDao = new VmMapperBuilder(session).build().VmDao();
    }

    @Test
    void addNormal() {
        Normal normal = new Normal("1", 1,"Normal", false, 1, 1, 1);
        vmDao.create(normal);
    }


    @Test
    void addPro() {
        Pro pro = new Pro("1",1, "Pro", false, 1, 1, 1);
        vmDao.create(pro);
    }

    @Test
    void findById() {
        VirtualMachine vm = vmDao.findById("1");
        System.out.println(vm);
    }
}