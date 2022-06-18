package ru.spliterash.spcore.domain.usecase.abstractCases;

import lombok.RequiredArgsConstructor;
import ru.spliterash.spcore.domain.entity.BaseEntity;
import ru.spliterash.spcore.domain.exceptions.DomainException;
import ru.spliterash.spcore.domain.repo.CrudRepository;
import ru.spliterash.spcore.domain.usecase.UseCase;

@RequiredArgsConstructor
public abstract class AbstractFindEntityByIdUseCase<E extends BaseEntity> implements UseCase<String, E> {
    protected final CrudRepository<E, ?> repository;

    @Override
    public E execute(String id) {
        return repository.findById(id).orElseThrow(() -> getNotFoundException(id));
    }

    protected abstract DomainException getNotFoundException(String id);
}
