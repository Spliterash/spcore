package ru.spliterash.spcore.structure.runtimeIndex.redisson;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import redis.embedded.RedisServer;
import ru.spliterash.spcore.structure.runtimeIndex.RuntimeIndexCollection;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static ru.spliterash.spcore.structure.runtimeIndex.redisson.RedissonRuntimeIndexCollectionTest.TestIndex.KEY_2;

public class RedissonRuntimeIndexCollectionTest {
    private static RedisServer redisServer;
    private static RedissonClient client;

    @BeforeAll
    public static void initRedis() {
        int port = 2312;
        redisServer = new RedisServer(port);
        redisServer.start();

        Config config = new Config();
        config.useSingleServer()
                .setAddress("redis://localhost:" + port);

        client = Redisson.create(config);
    }

    @AfterAll
    public static void stopRedis() {
        redisServer.stop();
    }

    @Test
    public void complexTest() {
        RuntimeIndexCollection<Data, TestIndex> collection = new RedissonRuntimeIndexCollection<>(TestIndex.class, "path:", client);


        collection.addIndex(TestIndex.KEY_1, d -> d.key1);
        collection.addIndex(KEY_2, d -> d.key2);

        Data data = new Data("1", "1");

        collection.add(data);
        collection.add(new Data("1", "2"));
        collection.add(new Data("2", "2"));

        Assertions.assertEquals(3, collection.size());

        List<Data> key1Search = collection.findByIndex(TestIndex.KEY_1, "1");
        Assertions.assertEquals(2, key1Search.size());

        List<Data> key2Search = collection.findByIndex(KEY_2, "1");
        Assertions.assertEquals(1, key2Search.size());

        collection.remove(data);

        key2Search = collection.findByIndex(KEY_2, "1");
        Assertions.assertEquals(0, key2Search.size());


        ArrayList<Data> copy = new ArrayList<>(collection);

        for (Data data1 : copy) {
            collection.remove(data1);
        }
    }

    enum TestIndex {
        KEY_1, KEY_2
    }

    @Getter
    @EqualsAndHashCode
    @AllArgsConstructor
    public static class Data implements Serializable {
        private final String key1;
        private final String key2;
    }
}
