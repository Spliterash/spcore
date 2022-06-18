package ru.spliterash.spcore.domain.usecase.abstractCases;

import lombok.RequiredArgsConstructor;
import ru.spliterash.spcore.domain.entity.BaseEntity;
import ru.spliterash.spcore.domain.repo.CrudRepository;
import ru.spliterash.spcore.domain.usecase.UseCase;

@RequiredArgsConstructor
public abstract class AbstractDeleteEntityByIdUseCase<E extends BaseEntity> implements UseCase<String, Void> {
    protected final CrudRepository<E, ?> repository;

    @Override
    public Void execute(String id) {
        repository.delete(id);

        return null;
    }
}
