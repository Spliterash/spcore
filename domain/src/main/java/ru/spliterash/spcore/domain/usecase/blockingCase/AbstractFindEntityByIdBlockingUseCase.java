package ru.spliterash.spcore.domain.usecase.blockingCase;

import lombok.RequiredArgsConstructor;
import ru.spliterash.spcore.domain.entity.BaseEntity;
import ru.spliterash.spcore.domain.exceptions.DomainException;
import ru.spliterash.spcore.domain.repo.BlockingCrudRepository;
import ru.spliterash.spcore.domain.usecase.UseCase;

@RequiredArgsConstructor
public abstract class AbstractFindEntityByIdBlockingUseCase<E extends BaseEntity> implements UseCase<String, E> {
    protected final BlockingCrudRepository<E, ?> repository;

    @Override
    public E execute(String id) {
        E result = repository.findById(id);
        if (result == null)
            throw getNotFoundException(id);

        return result;
    }

    protected abstract DomainException getNotFoundException(String id);
}
