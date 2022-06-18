package ru.spliterash.spcore.domain.commons;

public interface Mapper<I, O> {
    O map(I i);

    I inverseMap(O i);
}
