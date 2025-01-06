package example.dao;

import com.datastax.oss.driver.api.mapper.annotations.Dao;
import com.datastax.oss.driver.api.mapper.annotations.Insert;
import com.datastax.oss.driver.api.mapper.annotations.Query;
import example.model.Client;
import example.schemas.ClientSchema;

@Dao
public interface ClientDao {
    @Insert
    void create(Client client);

    @Query("SELECT * FROM " + ClientSchema.CLIENTS + " WHERE personal_id = :id")
    Client findClientById(String id);

}
