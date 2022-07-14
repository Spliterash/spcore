package ru.spliterash.spcore.structure.runtimeIndex;

import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.spliterash.spcore.structure.spLinkedlist.SPLinkedList;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static ru.spliterash.spcore.structure.runtimeIndex.RuntimeIndexCollectionTest.TestIndex.KEY_1;
import static ru.spliterash.spcore.structure.runtimeIndex.RuntimeIndexCollectionTest.TestIndex.KEY_2;

public class RuntimeIndexCollectionTest {
    @Test
    public void complexTest() throws NoSuchFieldException, IllegalAccessException {
        RuntimeIndexCollection<Data, TestIndex> collection = RuntimeIndexCollection.withArrayList(TestIndex.class);

        Field indexed = collection.getClass().getDeclaredField("indexed");
        indexed.setAccessible(true);

        //noinspection unchecked
        Map<String, Map<Object, SPLinkedList<Data>>> indexedValue = (Map<String, Map<Object, SPLinkedList<Data>>>) indexed.get(collection);


        collection.addIndex(KEY_1, d -> d.key1);
        collection.addIndex(KEY_2, d -> d.key2);

        Data data = new Data("1", "1");

        collection.add(data);
        collection.add(new Data("1", "2"));
        collection.add(new Data("2", "2"));

        Assertions.assertEquals(3, collection.size());

        List<Data> key1Search = collection.findByIndex(KEY_1, "1");
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

        Assertions.assertEquals(0, indexedValue.size());
        for (Map<Object, SPLinkedList<Data>> value : indexedValue.values()) {
            Assertions.assertEquals(0, value.size());
        }
    }

    enum TestIndex {
        KEY_1, KEY_2
    }

    @AllArgsConstructor
    public static class Data {
        private final String key1;
        private final String key2;
    }
}
