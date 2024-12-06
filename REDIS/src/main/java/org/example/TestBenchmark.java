package org.example;

import org.bson.Document;
import org.example.repositories.ClientRepository;
import org.example.repositories.MongoClientRepository;
import org.example.repositories.RedisClientRepository;
import org.openjdk.jmh.annotations.*;

import java.util.concurrent.TimeUnit;
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Benchmark)
@Fork(2)
@Warmup(iterations = 5, time = 5, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 10, timeUnit = TimeUnit.SECONDS)
public class TestBenchmark {

    private final MongoClientRepository MONGO_CLIENT_REPOSITORY = new MongoClientRepository();
    private final RedisClientRepository REDIS_CLIENT_REPOSITORY = new RedisClientRepository();
    private final ClientRepository CLIENT_REPOSITORY = new ClientRepository(REDIS_CLIENT_REPOSITORY, MONGO_CLIENT_REPOSITORY);

    private final Client client = new Client("3333333333", "SetupFirstName", "SetupLastName");
    private final Client setupClient = new Client("222222222", "SetupFirstName", "SetupLastName");
    private final String clientPersonalID = setupClient.getPersonalID();

    @Setup(Level.Invocation)
    public void setUp() {
        MONGO_CLIENT_REPOSITORY.getDatabase().getCollection("clients", Client.class).deleteMany(new Document());
        REDIS_CLIENT_REPOSITORY.clearCache();
        CLIENT_REPOSITORY.add(setupClient);
    }

    @TearDown
    public void tearDown() {
        MONGO_CLIENT_REPOSITORY.getDatabase().getCollection("clients", Client.class).deleteMany(new Document());
        REDIS_CLIENT_REPOSITORY.clearCache();
        REDIS_CLIENT_REPOSITORY.close();
    }

    @Benchmark
    public void MongoSet() {
        MONGO_CLIENT_REPOSITORY.add(client);
    }

    @Benchmark
    public Client MongoGet() {
        return MONGO_CLIENT_REPOSITORY.findById(clientPersonalID);
    }

    @Benchmark
    public void RedisSet() {
        REDIS_CLIENT_REPOSITORY.add(client);
    }

    @Benchmark
    public Client RedisGet() {
        return REDIS_CLIENT_REPOSITORY.findById(clientPersonalID);
    }

    @Benchmark
    public void DecoratorSet() {
        CLIENT_REPOSITORY.add(client);
    }

    @Benchmark
    public Client DecoratorGet() {
        return CLIENT_REPOSITORY.findById(clientPersonalID);
    }
}
