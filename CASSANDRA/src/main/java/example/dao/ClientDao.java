package example.dao;

import com.datastax.oss.driver.api.mapper.annotations.*;
import example.model.Client;
import example.schemas.SchemaConst;

@Dao
public interface ClientDao {
    @Insert
    void create(Client client);

    @Query("SELECT * FROM " + SchemaConst.CLIENTS + " WHERE personal_id = :id")
    Client findClientById(String id);

    @Update(ifExists = true)
    void update(Client client);

    @Delete(entityClass = Client.class, customWhereClause = "personal_id = :id")
    void delete(String id);

}
