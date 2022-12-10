package ru.spliterash.spcore.structure.runtimeIndex;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.spliterash.spcore.structure.runtimeIndex.defaultCollection.DefaultRuntimeIndexCollection;
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
        DefaultRuntimeIndexCollection<Data, TestIndex> indexCollection = new DefaultRuntimeIndexCollection<>(TestIndex.class);

        Field indexed = indexCollection.getClass().getDeclaredField("fieldIndex");
        indexed.setAccessible(true);

        //noinspection unchecked
        Map<String, Map<Object, SPLinkedList<Data>>> indexedValue = (Map<String, Map<Object, SPLinkedList<Data>>>) indexed.get(indexCollection);

        Data data = new Data("1", "1");

        indexCollection.add(data);
        indexCollection.add(new Data("1", "2"));
        indexCollection.add(new Data("2", "2"));

        Assertions.assertEquals(3, indexCollection.size());

        List<Data> key1Search = indexCollection.findByIndex(KEY_1, "1");
        Assertions.assertEquals(2, key1Search.size());

        List<Data> key2Search = indexCollection.findByIndex(KEY_2, "1");
        Assertions.assertEquals(1, key2Search.size());

        indexCollection.remove(data);

        key2Search = indexCollection.findByIndex(KEY_2, "1");
        Assertions.assertEquals(0, key2Search.size());


        ArrayList<Data> copy = new ArrayList<>(indexCollection.toCollection());

        for (Data data1 : copy) {
            indexCollection.remove(data1);
        }

        Assertions.assertEquals(0, indexedValue.size());
        for (Map<Object, SPLinkedList<Data>> value : indexedValue.values()) {
            Assertions.assertEquals(0, value.size());
        }
    }

    @RequiredArgsConstructor
    enum TestIndex implements RuntimeIndex<Data> {
        KEY_1(d -> d.key1),
        KEY_2(d -> d.key2);

        private final RuntimeIndex<Data> index;

        @Override
        public Object getField(Data data) {
            return index.getField(data);
        }
    }

    @AllArgsConstructor
    public static class Data {
        private final String key1;
        private final String key2;
    }
}
