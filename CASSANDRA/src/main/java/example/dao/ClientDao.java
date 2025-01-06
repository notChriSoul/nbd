package example.dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import example.model.Client;
import example.schemas.ClientSchema;

@Dao
public interface ClientDao {
    @Insert
    void create(Client client);

    @Query("SELECT * FROM " + ClientSchema.CLIENTS + " WHERE personal_id = :id")
    Client findClientById(String id);

    @Update(ifExists = true)
    void update(Client client);

    @Delete()
    void delete(Client client);

}
