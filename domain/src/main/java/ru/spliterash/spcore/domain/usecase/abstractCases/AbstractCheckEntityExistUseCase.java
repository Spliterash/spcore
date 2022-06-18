package ru.spliterash.spcore.domain.usecase.abstractCases;

import lombok.RequiredArgsConstructor;
import ru.spliterash.spcore.domain.repo.CrudRepository;
import ru.spliterash.spcore.domain.usecase.UseCase;

@RequiredArgsConstructor
public abstract class AbstractCheckEntityExistUseCase implements UseCase<String, Boolean> {
    protected final CrudRepository<?, ?> repository;

    @Override
    public Boolean execute(String id) {
        return repository.exist(id);
    }
}
