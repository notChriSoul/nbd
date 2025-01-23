package example.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import example.model.vms.Normal;
import example.model.vms.Pro;
import example.model.vms.VirtualMachine;
import example.provider.vmGetByIdProvider;

import java.util.List;

@Dao
public interface VmDao {
    @StatementAttributes(consistencyLevel = "ONE")
    @QueryProvider(providerClass = vmGetByIdProvider.class,
    entityHelpers = {Normal.class, Pro.class})
    VirtualMachine findById(String id);

    @StatementAttributes(consistencyLevel = "ONE")
    @QueryProvider(providerClass = vmGetByIdProvider.class,
            entityHelpers = {Normal.class, Pro.class})
    List<VirtualMachine> findAll();

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = vmGetByIdProvider.class,
            entityHelpers = {Normal.class, Pro.class})
    void create(VirtualMachine vm);

    @Delete(entityClass = VirtualMachine.class, customWhereClause = "vm_id =: id")
    void remove(String id);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = vmGetByIdProvider.class
    , entityHelpers = {Normal.class, Pro.class})
    void update(VirtualMachine vm);
}
