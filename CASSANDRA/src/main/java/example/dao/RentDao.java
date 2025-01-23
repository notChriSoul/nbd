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
    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentProvider.class)
    void create(Rent rent);

    @StatementAttributes(consistencyLevel = "ONE")
    @QueryProvider(providerClass = RentProvider.class)
    List<Rent> findAllByTable(CqlIdentifier table);

    @StatementAttributes(consistencyLevel = "ONE")
    @QueryProvider(providerClass = RentProvider.class)
    List<Rent> findByClientId(String clientID);

    @StatementAttributes(consistencyLevel = "ONE")
    @QueryProvider(providerClass = RentProvider.class)
    List<Rent> findByVMachineId(String vmID);

    @StatementAttributes(consistencyLevel = "QUORUM")
    @QueryProvider(providerClass = RentProvider.class)
    void endRent(Rent rent);
}
