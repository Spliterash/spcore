package ru.spliterash.spcore.structure.runtimeIndex;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RuntimeIndexCollection<T, E extends Enum<E> & RuntimeIndex<T>> extends Collection<T> {
    List<T> findByIndex(E type, Object value);

    Optional<T> findByIndexFirst(E type, Object value);
}
