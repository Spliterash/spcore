package ru.spliterash.spcore.structure.runtimeIndex;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
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

        String initKey = "dataInitKey";
        String initKey2 = "key2Value";
        Data dataToChange = new Data(initKey, initKey2);

        indexCollection.add(data);
        indexCollection.add(dataToChange);
        indexCollection.add(new Data("1", "2"));
        indexCollection.add(new Data("2", "2"));

        Assertions.assertEquals(4, indexCollection.size());

        List<Data> key1Search = indexCollection.findByIndex(KEY_1, "1");
        Assertions.assertEquals(2, key1Search.size());

        List<Data> key2Search = indexCollection.findByIndex(KEY_2, "1");
        Assertions.assertEquals(1, key2Search.size());

        // Тесты с dateToChange
        // Сначала проверим что лежит
        Assertions.assertNotNull(indexCollection.findByIndexFirst(KEY_1, initKey).orElse(null));
        String newDataVal = "newValue";
        dataToChange.key1 = newDataVal;
        // Сразу не появляется
        Assertions.assertNull(indexCollection.findByIndexFirst(KEY_1, newDataVal).orElse(null));
        // Переиндексируем
        indexCollection.reindexField(dataToChange, KEY_1);
        // По старому значению найти не можем
        Assertions.assertNull(indexCollection.findByIndexFirst(KEY_1, initKey).orElse(null));
        // А по новому можем
        Assertions.assertNotNull(indexCollection.findByIndexFirst(KEY_1, newDataVal).orElse(null));

        indexCollection.remove(data);

        key2Search = indexCollection.findByIndex(KEY_2, "1");
        Assertions.assertEquals(0, key2Search.size());


        ArrayList<Data> copy = new ArrayList<>(indexCollection.toCollection());

        for (Data toRemoveData : copy) {
            indexCollection.remove(toRemoveData);
        }

        Assertions.assertEquals(0, indexCollection.size());
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

    @Getter
    @Setter
    @AllArgsConstructor
    public static class Data {
        private String key1;
        private String key2;
    }
}
