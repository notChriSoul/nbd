package org.example.repositories;

import lombok.Getter;
import redis.clients.jedis.DefaultJedisClientConfig;
import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisClientConfig;
import redis.clients.jedis.JedisPool;

import java.util.ResourceBundle;

public abstract class AbstractRedisRepository implements AutoCloseable {
    private JedisPool pool;
    private int port;
    private String host;

    private void initJedisPool() {
        JedisClientConfig clientConfig = DefaultJedisClientConfig.builder().build();
        ResourceBundle rb = ResourceBundle.getBundle("redis");
        host = rb.getString("host");
        port = Integer.parseInt(rb.getString("port"));
        pool = new JedisPool(new HostAndPort(host, port), clientConfig);
    }

    JedisPool getPool() {
        if (pool == null) {
            initJedisPool();
        }
        return pool;
    }
    @Override
    public void close() throws Exception {
        pool.close();
    }

}
