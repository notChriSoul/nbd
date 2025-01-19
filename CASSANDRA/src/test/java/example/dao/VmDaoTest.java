package example.dao;

import com.datastax.oss.driver.api.core.CqlSession;
import example.db.BaseRepository;
import example.mapper.VmMapperBuilder;
import example.model.vms.Normal;
import example.model.vms.Pro;
import example.model.vms.VirtualMachine;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class VmDaoTest {

    private static VmDao vmDao;

    @BeforeAll
    static void setUp() {
        BaseRepository baseRepository = new BaseRepository();
        baseRepository.initSession();
        baseRepository.createVmTable();
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
        Pro pro = new Pro("2",1, "Pro", false, 1, 1, 1);
        vmDao.create(pro);
    }

    @Test
    void findById() {
        VirtualMachine vm = vmDao.findById("1");
        System.out.println(vm);
    }

    @Test
    void delete() {
        Normal normal = new Normal("32", 2,"Normal", false, 3, 1, 1);
        vmDao.create(normal);
        VirtualMachine normal2 = vmDao.findById("32");
        assertNotNull(normal2);
        vmDao.remove("32");
        VirtualMachine vm = vmDao.findById("32");
        assertNull(vm);
        System.out.println(vm);
    }
}