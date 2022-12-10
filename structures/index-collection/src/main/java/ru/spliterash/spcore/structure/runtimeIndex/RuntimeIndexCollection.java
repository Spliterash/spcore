package ru.spliterash.spcore.structure.runtimeIndex;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RuntimeIndexCollection<T, E extends Enum<E> & RuntimeIndex<T>> extends Iterable<T> {
    void add(T element);

    void remove(T element);

    void clear();

    boolean isEmpty();

    int size();

    boolean contains(T obj);

    /**
     * Возвращает immutable коллекцию
     */
    Collection<T> toCollection();

    List<T> findByIndex(E type, Object value);

    Optional<T> findByIndexFirst(E type, Object value);

    void reindexField(T object, E index);
}
