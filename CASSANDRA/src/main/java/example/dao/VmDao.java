package example.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Delete;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import example.model.vms.Normal;
import example.model.vms.Pro;
import example.model.vms.VirtualMachine;
import example.provider.vmGetByIdProvider;

@Dao
public interface VmDao {
    @QueryProvider(providerClass = vmGetByIdProvider.class,
    entityHelpers = {Normal.class, Pro.class})
    VirtualMachine findById(String id);

    @QueryProvider(providerClass = vmGetByIdProvider.class,
            entityHelpers = {Normal.class, Pro.class})
    void create(VirtualMachine vm);

    @Delete(entityClass = VirtualMachine.class, customWhereClause = "vm_id =: id")
    void remove(String id);
}
