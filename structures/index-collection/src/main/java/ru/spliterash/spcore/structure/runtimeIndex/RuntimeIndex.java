package ru.spliterash.spcore.structure.runtimeIndex;

@FunctionalInterface
public interface RuntimeIndex<T> {
    Object getField(T t);
}
