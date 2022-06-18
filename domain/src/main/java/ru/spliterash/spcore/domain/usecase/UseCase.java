package ru.spliterash.spcore.domain.usecase;

public interface UseCase<I, O> {
    O execute(I input);
}
