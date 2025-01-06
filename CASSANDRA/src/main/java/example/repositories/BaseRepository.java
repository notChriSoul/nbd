package example.repositories;

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

    public void createVmTable() {}

    public void createRentTable() {}

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
