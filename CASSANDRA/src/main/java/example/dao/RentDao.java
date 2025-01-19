package example.dao;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.QueryProvider;
import com.datastax.oss.driver.api.mapper.annotations.StatementAttributes;
import example.model.Rent;
import example.provider.RentProvider;

import java.util.List;

@Dao
public interface RentDao {
    @StatementAttributes(consistencyLevel = "QUORUM")//todo sprawdzic czy to dobrze
    @QueryProvider(providerClass = RentProvider.class)
    void create(Rent rent); //works

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = RentProvider.class)
    List<Rent> findAllByTable(CqlIdentifier table);

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = RentProvider.class)
    List<Rent> findByClientId(String clientID); //works

    @StatementAttributes(consistencyLevel = "ONE", pageSize = 100)
    @QueryProvider(providerClass = RentProvider.class)
    List<Rent> findByVMachineId(String vmID); //works

    @StatementAttributes(consistencyLevel = "QUORUM", pageSize = 100)
    @QueryProvider(providerClass = RentProvider.class)
    void endRent(Rent rent); //works
}
