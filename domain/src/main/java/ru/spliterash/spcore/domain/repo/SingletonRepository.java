package ru.spliterash.spcore.domain.repo;

import java.util.Optional;

public interface SingletonRepository<T> {
    Optional<T> get();

    void set(T t);
}
