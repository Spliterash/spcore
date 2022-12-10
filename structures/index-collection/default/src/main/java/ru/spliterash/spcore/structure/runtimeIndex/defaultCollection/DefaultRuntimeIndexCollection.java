package ru.spliterash.spcore.structure.runtimeIndex.defaultCollection;

import lombok.AllArgsConstructor;
import ru.spliterash.spcore.structure.runtimeIndex.RuntimeIndex;
import ru.spliterash.spcore.structure.runtimeIndex.RuntimeIndexCollection;
import ru.spliterash.spcore.structure.spLinkedlist.SPLinkedList;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * Коллекция, которая позволяет добавлять различные индексы на элементы
 * <p>
 * Чтобы можно было быстро!!! найти нужный элемент по какому-то отдельному полю
 *
 * @param <T>
 */
@SuppressWarnings("unused")
public class DefaultRuntimeIndexCollection<T, E extends Enum<E> & RuntimeIndex<T>> implements RuntimeIndexCollection<T, E> {
    /**
     * Ключ имя индекса
     * Значения - значения
     */
    private final Map<E, Map<Object, SPLinkedList<T>>> fieldIndex;
    /**
     * Индекс для индексов
     */
    private final Map<T, List<IndexedValue>> indexIndex = new HashMap<>();
    private final List<E> enums;

    public DefaultRuntimeIndexCollection(Class<E> enumClass) {
        this.enums = Arrays.asList(enumClass.getEnumConstants());
        this.fieldIndex = new EnumMap<>(enumClass);
    }


    @Override
    public void clear() {
        fieldIndex.clear();
        indexIndex.clear();
    }

    private SPLinkedList<T> findByIndexLinked(E type, Object value) {
        Map<Object, SPLinkedList<T>> indexByField = fieldIndex.getOrDefault(type, Collections.emptyMap());

        return indexByField.get(value);
    }

    @Override
    public List<T> findByIndex(E type, Object value) {
        SPLinkedList<T> list = findByIndexLinked(type, value);

        if (list == null)
            return Collections.emptyList();
        else
            return StreamSupport.stream(list.spliterator(), false).collect(Collectors.toList());
    }

    @Override
    public Optional<T> findByIndexFirst(E type, Object value) {
        SPLinkedList<T> list = findByIndexLinked(type, value);

        if (list == null || list.isEmpty())
            return Optional.empty();

        return list.first();
    }

    @Override
    public void reindexField(T object, E index) {
        List<IndexedValue> indexedFields = indexIndex.get(object);
        // Сначала удалим
        for (IndexedValue indexedField : indexedFields) {
            if (indexedField.index == index) {
                removeIndexedValue(indexedField);
                break;
            }
        }
        // Добавим по новой
        index(object, index);
    }

    private void index(T element, E index) {
        // Получаем значение поля по которому будет индексировать
        Object fieldValue = index.getField(element);
        // Получаем мапу для этого типа индекса по имени
        Map<Object, SPLinkedList<T>> fieldMap = fieldIndex.computeIfAbsent(index, s -> new HashMap<>());
        // Получаем список объедков с таким же значением поля и добавляем туда
        SPLinkedList<T> list = fieldMap.computeIfAbsent(fieldValue, o -> new SPLinkedList<>());
        SPLinkedList.LinkedListElement<T> add = list.add(element);
        // Добавляем в массив чтобы чистить удаляемые элементы
        List<IndexedValue> toRemoveList = indexIndex.computeIfAbsent(element, o -> new ArrayList<>());
        IndexedValue value = new IndexedValue(fieldValue, index, fieldMap, list, add);

        toRemoveList.add(value);
    }

    @Override
    public void add(T t) {
        for (E entry : enums) {
            index(t, entry);
        }
    }

    private void removeIndexes(Object obj) {
        //noinspection SuspiciousMethodCalls
        List<IndexedValue> indexedValues = indexIndex.remove(obj);

        if (indexedValues != null) {
            for (IndexedValue value : indexedValues) {
                removeIndexedValue(value);
            }
        }
    }

    private void removeIndexedValue(IndexedValue value) {
        value.node.remove();
        if (value.sameValueList.isEmpty()) {
            value.indexMap.remove(value.value);
        }
    }

    private void checkEmptyLists() {
        for (Iterator<Map<Object, SPLinkedList<T>>> iter = fieldIndex.values().iterator(); iter.hasNext(); ) {
            Map<Object, SPLinkedList<T>> map = iter.next();

            map.values().removeIf(SPLinkedList::isEmpty);

            if (map.isEmpty())
                iter.remove();
        }
    }

    @Override
    public void remove(Object o) {
        removeIndexes(o);
        checkEmptyLists();
    }

    // Прокся
    @Override
    public int size() {
        return indexIndex.size();
    }

    @Override
    public Collection<T> toCollection() {
        return indexIndex.keySet();
    }

    @Override
    public boolean isEmpty() {
        return indexIndex.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return indexIndex.containsKey(o);
    }

    @Override
    public Iterator<T> iterator() {
        return indexIndex.keySet().iterator();
    }

    @AllArgsConstructor
    private static class ObjectIndexes<T> {
        private final String indexName;
        private final RuntimeIndex<T> getter;
    }

    @AllArgsConstructor
    private class IndexedValue {
        private final Object value;
        private final E index;

        private final Map<Object, SPLinkedList<T>> indexMap;
        /**
         * Этот элемент лежит в мапе сверху
         */
        private final SPLinkedList<T> sameValueList;
        private final SPLinkedList.LinkedListElement<T> node;
    }
}
