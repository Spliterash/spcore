package ru.spliterash.spcore.domain.usecase.blockingCase;

import lombok.RequiredArgsConstructor;
import ru.spliterash.spcore.domain.repo.BlockingCrudRepository;
import ru.spliterash.spcore.domain.usecase.UseCase;

@RequiredArgsConstructor
public abstract class AbstractCheckEntityExistBlockingUseCase implements UseCase<String, Boolean> {
    protected final BlockingCrudRepository<?, ?> repository;

    @Override
    public Boolean execute(String id) {
        return repository.exist(id);
    }
}
