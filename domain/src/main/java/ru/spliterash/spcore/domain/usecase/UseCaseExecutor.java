package ru.spliterash.spcore.domain.usecase;

public interface UseCaseExecutor {
    <I, O> O execute(UseCase<I, O> useCase, I input);
}
