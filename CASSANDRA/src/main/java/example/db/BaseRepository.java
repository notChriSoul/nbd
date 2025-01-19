package example.db;

import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import example.schemas.CQLSchema;
import lombok.Getter;

import java.net.InetSocketAddress;

public class BaseRepository implements AutoCloseable {
    @Getter
    private static CqlSession session;

    public void initSession() {
        session = CqlSession.builder()
                .addContactPoint(new InetSocketAddress("cassandra1", 9042))
                .addContactPoint(new InetSocketAddress("cassandra2", 9043))
                .withLocalDatacenter("dc1")
                .withAuthCredentials("cassandra", "cassandrapassword")
                .withKeyspace(CQLSchema.RENT_A_VM_NAMESPACE)
                .build();
    }

    public void initKeyspace() {
        CreateKeyspace keyspace = SchemaBuilder.createKeyspace(CQLSchema.RENT_A_VM_NAMESPACE)
                .ifNotExists()
                .withSimpleStrategy(2)
                .withDurableWrites(true);
        SimpleStatement createKeyspace = keyspace.build();
        session.execute(createKeyspace);
    }

    public void createTables() {
        createClientTable();
        createVmTable();
        createRentTable();
    }

    public void createClientTable() {
        SimpleStatement createClients = SchemaBuilder.createTable(CQLSchema.CLIENTS)
                .ifNotExists()
                .withPartitionKey(CQLSchema.PERSONAL_ID, DataTypes.TEXT)
                .withClusteringColumn(CQLSchema.TYPE, DataTypes.TEXT)
                .withColumn(CQLSchema.FIRST_NAME, DataTypes.TEXT)
                .withColumn(CQLSchema.LAST_NAME, DataTypes.TEXT)
                .withClusteringOrder(CQLSchema.TYPE, ClusteringOrder.ASC)
                .build();
        session.execute(createClients);
    }

    public void createVmTable() {
        SimpleStatement createVms = SchemaBuilder.createTable(CQLSchema.VMS)
                .ifNotExists()
                .withPartitionKey(CQLSchema.VM_ID, DataTypes.TEXT)
                .withClusteringColumn(CQLSchema.DISCRIMINATOR, DataTypes.TEXT)
                .withColumn(CQLSchema.IS_RENTED, DataTypes.BOOLEAN)
                .withColumn(CQLSchema.RAM, DataTypes.INT)
                .withColumn(CQLSchema.SOTRAGE, DataTypes.INT)
                .withColumn(CQLSchema.RENTAL_PRICE, DataTypes.INT)
                .withColumn(CQLSchema.CORES, DataTypes.INT)
                .withColumn(CQLSchema.SOCKETS, DataTypes.INT)
                .build();

        session.execute(createVms);
    }

    public void createRentTable() {
        SimpleStatement createRentsByClient = SchemaBuilder.createTable(CQLSchema.RENTS_BY_CLIENT)
                .ifNotExists()
                .withPartitionKey(CQLSchema.PERSONAL_ID, DataTypes.TEXT)
                .withClusteringColumn(CQLSchema.RENT_ID, DataTypes.TEXT)
                .withColumn(CQLSchema.VM_ID, DataTypes.TEXT)
                .withColumn(CQLSchema.BEGIN_TIME, DataTypes.TIMESTAMP)
                .withColumn(CQLSchema.END_TIME, DataTypes.TIMESTAMP)
                .withColumn(CQLSchema.RENT_COST, DataTypes.DOUBLE)
                .build();

        SimpleStatement createRentsByVm = SchemaBuilder.createTable(CQLSchema.RENTS_BY_VM)
                .ifNotExists()
                .withPartitionKey(CQLSchema.VM_ID, DataTypes.TEXT)
                .withClusteringColumn(CQLSchema.RENT_ID, DataTypes.TEXT)
                .withColumn(CQLSchema.PERSONAL_ID, DataTypes.TEXT)
                .withColumn(CQLSchema.BEGIN_TIME, DataTypes.TIMESTAMP)
                .withColumn(CQLSchema.END_TIME, DataTypes.TIMESTAMP)
                .withColumn(CQLSchema.RENT_COST, DataTypes.DOUBLE)
                .build();

        session.execute(createRentsByClient);
        session.execute(createRentsByVm);
    }

    public void checkConnection() {
        try {
            ResultSet resultSet = session.execute("SELECT release_version FROM system.local");
            Row row = resultSet.one();
            if (row != null) {
                System.out.println("Connected to Cassandra. Release Version: " + row.getString("release_version"));
            }
        } catch (Exception e) {
            System.out.println("Unable to connect to Cassandra: " + e.getMessage());
        }
    }

    @Override
    public void close() {
        session.close();
    }
}
