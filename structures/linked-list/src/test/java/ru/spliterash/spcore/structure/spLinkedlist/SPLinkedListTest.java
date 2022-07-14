package ru.spliterash.spcore.structure.spLinkedlist;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SPLinkedListTest {
    @Test
    public void testLinkedListAdd() {
        List<String> source = Arrays.asList("str1", "str2", "str3");


        SPLinkedList<String> list = new SPLinkedList<>();

        for (String s : source) {
            list.add(s);
        }

        Assertions.assertIterableEquals(source, list);
    }

    @Test
    public void testDelete() {
        List<String> source = Arrays.asList("str1", "str2", "str3");
        List<String> afterDelete = Arrays.asList("str1", "str3");

        SPLinkedList<String> list = new SPLinkedList<>();

        List<SPLinkedList.LinkedListElement<String>> callbacks = new ArrayList<>();

        for (String s : source) {
            SPLinkedList.LinkedListElement<String> callback = list.add(s);

            callbacks.add(callback);
        }

        Assertions.assertEquals(3, list.size());

        callbacks.remove(1).remove();


        Assertions.assertIterableEquals(afterDelete, list);

        for (SPLinkedList.LinkedListElement<String> callback : callbacks) {
            callback.remove();
        }

        Assertions.assertEquals(0, list.size());

    }
}
