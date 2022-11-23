package ru.spliterash.spcore.domain.usecase.blockingCase;

import lombok.RequiredArgsConstructor;
import ru.spliterash.spcore.domain.entity.BaseEntity;
import ru.spliterash.spcore.domain.repo.BlockingCrudRepository;
import ru.spliterash.spcore.domain.usecase.UseCase;

@RequiredArgsConstructor
public abstract class AbstractDeleteEntityByIdBlockingUseCase<E extends BaseEntity> implements UseCase<String, Void> {
    protected final BlockingCrudRepository<E, ?> repository;

    @Override
    public Void execute(String id) {
        repository.delete(id);

        return null;
    }
}
