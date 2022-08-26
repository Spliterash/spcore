package ru.spliterash.spcore.structure.runtimeIndex.redisson;

import lombok.Getter;
import org.redisson.api.RMap;
import org.redisson.api.RSet;
import org.redisson.api.RSetMultimap;
import org.redisson.api.RedissonClient;
import ru.spliterash.spcore.structure.runtimeIndex.RuntimeIndex;
import ru.spliterash.spcore.structure.runtimeIndex.RuntimeIndexCollection;

import java.util.*;

/**
 * Реализация индекс коллекции, но для редиски
 * <p>
 * Стоит учитывать, что этот объект создаётся на всех серверах одинаково
 * <p>
 * Так же, только <b>ИММУТАБЕЛЬНЫЕ!!!!</b> объекты с переопределённым <b>HASHCODE</b>!!!
 *
 * @param <T>
 * @param <E>
 */
public class RedissonRuntimeIndexCollection<T, E extends Enum<E> & RuntimeIndex<T>> implements RuntimeIndexCollection<T, E> {
    private final RedissonClient client;

    /**
     * Хранит в себе элементы колекции
     * <p>
     * Прямые изменения в обход коллекции могут сломать всё
     * Использовать ТОЛЬКО для получения данных
     */
    @Getter
    private final RMap<Integer, T> elementsSet;
    private final String startPath;
    private final List<E> enums;

    /**
     * @param startPath Путь до значения, заканчивающийся на :
     * @param client    Клиент редиски
     */
    public RedissonRuntimeIndexCollection(Class<E> enumClass, String startPath, RedissonClient client) {
        this.enums = Arrays.asList(enumClass.getEnumConstants());
        this.client = client;
        this.startPath = startPath;

        this.elementsSet = client.getMap(startPath + "map");
    }

    /**
     * Ключ это хеш обьекта, значение это хеш элемента
     */
    private RSetMultimap<Integer, Integer> getIndexMap(E type) {
        return client.getSetMultimap(startPath + "indexes:" + type.name());
    }

    @Override
    public List<T> findByIndex(E type, Object value) {
        RSetMultimap<Integer, Integer> indexValues = getIndexMap(type);

        Set<Integer> foundObjectIndexes = indexValues.getAll(value.hashCode());
        Map<Integer, T> foundedElements = elementsSet.getAll(foundObjectIndexes);

        return new ArrayList<>(foundedElements.values());
    }

    @Override
    public Optional<T> findByIndexFirst(E type, Object value) {
        RSetMultimap<Integer, Integer> indexValues = getIndexMap(type);

        RSet<Integer> foundedObjectIndexes = indexValues.get(value.hashCode());

        Iterator<Integer> singleIterator = foundedObjectIndexes.iterator(1);
        if (!singleIterator.hasNext())
            return Optional.empty();

        Integer hash = singleIterator.next();
        T element = elementsSet.get(hash);

        return Optional.ofNullable(element);
    }

    @Override
    public boolean add(T t) {
        int objHash = t.hashCode();

        // Если элемент уже есть, то нет смысла обновлять индексы
        if (!elementsSet.fastPut(objHash, t))
            return false;

        for (E index : enums) {
            RSetMultimap<Integer, Integer> elementIndexMap = getIndexMap(index);

            Object fieldValue = index.getField(t);
            int fieldHash = fieldValue.hashCode();

            elementIndexMap.put(fieldHash, objHash);
        }

        return true;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object o) {
        if (elementsSet.fastRemove(o.hashCode()) == 0)
            return false;

        for (E index : enums) {
            RSetMultimap<Integer, Integer> elementIndexMap = getIndexMap(index);

            Object field = index.getField((T) o);

            int hashCode = field.hashCode();

            elementIndexMap.fastRemoveAsync(hashCode);
        }

        return true;
    }

    @Override
    public void clear() {
        elementsSet.deleteAsync();
        for (E e : enums) {
            getIndexMap(e).delete();
        }
    }

    // Снизу просто прокси для коллекции
    @Override
    public int size() {
        return elementsSet.size();
    }

    @Override
    public boolean isEmpty() {
        return elementsSet.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return elementsSet.containsKey(o.hashCode());
    }

    @Override
    public Iterator<T> iterator() {
        return elementsSet.values().iterator();
    }

    @Override
    public Object[] toArray() {
        return elementsSet.values().toArray();
    }

    @Override
    public <T1> T1[] toArray(T1[] a) {
        return elementsSet.values().toArray(a);
    }


    @Override
    public boolean containsAll(Collection<?> c) {
        return elementsSet.values().containsAll(c);
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean oneAdded = false;
        for (T t : c) {
            oneAdded = oneAdded || add(t);
        }
        return oneAdded;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        boolean oneRemoved = false;
        for (Object o : c) {
            oneRemoved = oneRemoved || remove(o);
        }

        return oneRemoved;
    }

    @Override
    public boolean retainAll(Collection<?> c) {
        throw new UnsupportedOperationException("Im too lazy for retain all");
    }
}
