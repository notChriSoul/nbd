package example.db;

import com.datastax.oss.driver.api.core.CqlIdentifier;
import com.datastax.oss.driver.api.core.CqlSession;
import com.datastax.oss.driver.api.core.cql.ResultSet;
import com.datastax.oss.driver.api.core.cql.Row;
import com.datastax.oss.driver.api.core.cql.SimpleStatement;
import com.datastax.oss.driver.api.core.metadata.schema.ClusteringOrder;
import com.datastax.oss.driver.api.core.type.DataTypes;
import com.datastax.oss.driver.api.querybuilder.SchemaBuilder;
import com.datastax.oss.driver.api.querybuilder.schema.CreateKeyspace;
import example.schemas.ClientSchema;
import example.schemas.VmSchema;
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
                .withKeyspace(CqlIdentifier.fromCql("rent_a_vm"))
                .build();
    }

    public void initKeyspace() {
        CreateKeyspace keyspace = SchemaBuilder.createKeyspace(CqlIdentifier.fromCql("rent_a_vm"))
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
        SimpleStatement createClients = SchemaBuilder.createTable(CqlIdentifier.fromCql(ClientSchema.CLIENTS))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql(ClientSchema.PERSONAL_ID), DataTypes.TEXT)
                .withClusteringColumn(CqlIdentifier.fromCql(ClientSchema.TYPE), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql(ClientSchema.FIRST_NAME), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql(ClientSchema.LAST_NAME), DataTypes.TEXT)
                .withClusteringOrder(CqlIdentifier.fromCql(ClientSchema.TYPE), ClusteringOrder.ASC)
                .build();
        session.execute(createClients);
    }

    public void createVmTable() {
        SimpleStatement createVms = SchemaBuilder.createTable(CqlIdentifier.fromCql(VmSchema.VMS))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql(VmSchema.ID), DataTypes.TEXT)
                .withClusteringColumn(CqlIdentifier.fromCql(VmSchema.DISCRIMINATOR), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql(VmSchema.IS_RENTED), DataTypes.BOOLEAN)
                .withColumn(CqlIdentifier.fromCql(VmSchema.RAM), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql(VmSchema.SOTRAGE), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql(VmSchema.RENTAL_PRICE), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql(VmSchema.CORES), DataTypes.INT)
                .withColumn(CqlIdentifier.fromCql(VmSchema.SOCKETS), DataTypes.INT)
                .build();

        session.execute(createVms);
    }

    public void createRentTable() {
        SimpleStatement createRentsByClient = SchemaBuilder.createTable(CqlIdentifier.fromCql("rents_by_client"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql(ClientSchema.PERSONAL_ID), DataTypes.TEXT)
                .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.BIGINT)
                .withColumn(CqlIdentifier.fromCql("vm_id"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("begin_time"), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql("end_time"), DataTypes.TIMESTAMP) .withColumn(CqlIdentifier.fromCql("rent_cost"), DataTypes.DOUBLE)
                .build();

        SimpleStatement createRentsByVm = SchemaBuilder.createTable (CqlIdentifier.fromCql("rents_by_vm"))
                .ifNotExists()
                .withPartitionKey(CqlIdentifier.fromCql("vm_id"), DataTypes.TEXT)
                .withClusteringColumn(CqlIdentifier.fromCql("rent_id"), DataTypes.BIGINT)
                .withColumn(CqlIdentifier.fromCql("personal_id"), DataTypes.TEXT)
                .withColumn(CqlIdentifier.fromCql("begin_time"), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql("end_time"), DataTypes.TIMESTAMP)
                .withColumn(CqlIdentifier.fromCql("rent_cost"), DataTypes.DOUBLE)
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
