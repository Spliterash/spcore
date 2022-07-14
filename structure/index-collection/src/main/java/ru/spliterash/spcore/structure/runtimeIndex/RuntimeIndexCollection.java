package ru.spliterash.spcore.structure.runtimeIndex;

import lombok.AllArgsConstructor;
import ru.spliterash.spcore.structure.spLinkedlist.SPLinkedList;

import java.util.*;
import java.util.function.Supplier;
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
public class RuntimeIndexCollection<T, E extends Enum<E>> implements Collection<T> {
    private final Collection<T> collection;

    /**
     * Ключ имя индекса
     * Значения - значения
     */
    private final Map<E, RuntimeIndex<T>> indexes = new HashMap<>();
    private final Map<E, Map<Object, SPLinkedList<T>>> indexed;
    private final Map<T, List<SPLinkedList.LinkedListElement<T>>> toRemoveElements = new HashMap<>();

    public RuntimeIndexCollection(Supplier<Collection<T>> collectionCreate, Class<E> enumClass) {
        this.collection = collectionCreate.get();
        this.indexed = new EnumMap<>(enumClass);
    }

    public static <T, E extends Enum<E>> RuntimeIndexCollection<T, E> withArrayList(Class<E> enumClass) {
        return new RuntimeIndexCollection<>(ArrayList::new, enumClass);
    }

    public static <T, E extends Enum<E>> RuntimeIndexCollection<T, E> withLinkedList(Class<E> enumClass) {
        return new RuntimeIndexCollection<>(LinkedList::new, enumClass);
    }

    public static <T, E extends Enum<E>> RuntimeIndexCollection<T, E> withHashSet(Class<E> enumClass) {
        return new RuntimeIndexCollection<>(HashSet::new, enumClass);
    }

    @Override
    public void clear() {
        collection.clear();

        indexed.clear();
        toRemoveElements.clear();
    }

    public void addIndex(E indexType, RuntimeIndex<T> index) {
        indexes.put(indexType, index);
        for (T t : collection) {
            index(t, indexType, index);
        }
    }

    public List<T> findByIndex(E type, Object value) {
        Map<Object, SPLinkedList<T>> indexByField = indexed.getOrDefault(type, Collections.emptyMap());

        SPLinkedList<T> list = indexByField.get(value);

        if (list == null)
            return Collections.emptyList();
        else
            return StreamSupport.stream(list.spliterator(), false).collect(Collectors.toList());
    }

    private void index(T element, E indexType, RuntimeIndex<T> index) {
        // Получаем значение поля по которому будет индексировать
        Object fieldValue = index.getField(element);
        // Получаем мапу для этого типа индекса по имени
        Map<Object, SPLinkedList<T>> fieldMap = indexed.computeIfAbsent(indexType, s -> new HashMap<>());
        // Получаем список объедков с таким же значением поля и добавляем туда
        SPLinkedList<T> list = fieldMap.computeIfAbsent(fieldValue, o -> new SPLinkedList<>());
        SPLinkedList.LinkedListElement<T> add = list.add(element);
        // Добавляем в массив чтобы чистить удаляемые элементы
        List<SPLinkedList.LinkedListElement<T>> toRemoveList = toRemoveElements.computeIfAbsent(element, o -> new ArrayList<>());
        toRemoveList.add(add);
    }

    @Override
    public boolean add(T t) {
        boolean add = collection.add(t);

        if (add) {
            for (Map.Entry<E, RuntimeIndex<T>> entry : indexes.entrySet()) {
                index(t, entry.getKey(), entry.getValue());
            }
        }

        return add;
    }

    private void removeIndexes(Object obj) {
        List<SPLinkedList.LinkedListElement<T>> linkedListElements = toRemoveElements.remove(obj);

        if (linkedListElements != null)
            for (SPLinkedList.LinkedListElement<T> linkedListElement : linkedListElements) {
                linkedListElement.remove();
            }
    }

    private void checkEmptyLists() {
        for (Iterator<Map<Object, SPLinkedList<T>>> iter = indexed.values().iterator(); iter.hasNext(); ) {
            Map<Object, SPLinkedList<T>> map = iter.next();

            map.values().removeIf(SPLinkedList::isEmpty);

            if (map.isEmpty())
                iter.remove();
        }
    }

    @Override
    public boolean remove(Object o) {
        boolean remove = collection.remove(o);

        if (remove) {
            removeIndexes(o);
            checkEmptyLists();
        }

        return remove;
    }

    // Прокся
    @Override
    public int size() {
        return collection.size();
    }

    @Override
    public boolean isEmpty() {
        return collection.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return collection.contains(o);
    }

    @Override
    public Iterator<T> iterator() {
        return collection.iterator();
    }

    @Override
    public Object[] toArray() {
        return collection.toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return collection.toArray(a);
    }

    @Override
    public boolean containsAll(Collection<?> c) {
        return collection.containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean onlyOne = false;
        for (T t : c) {
            onlyOne = add(t) || onlyOne;
        }

        return onlyOne;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean result = collection.removeAll(c);

        if (result) {
            for (Object o : c) {
                removeIndexes(o);
            }
            checkEmptyLists();
        }

        return result;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        return collection.retainAll(c);
    }

    @AllArgsConstructor
    private static class ObjectIndexes<T> {
        private final String indexName;
        private final RuntimeIndex<T> getter;
    }
}
