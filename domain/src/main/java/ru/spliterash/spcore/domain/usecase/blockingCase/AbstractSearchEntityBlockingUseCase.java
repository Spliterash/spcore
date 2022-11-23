package ru.spliterash.spcore.domain.usecase.blockingCase;

import lombok.RequiredArgsConstructor;
import ru.spliterash.spcore.domain.entity.BaseEntity;
import ru.spliterash.spcore.domain.repo.BlockingCrudRepository;
import ru.spliterash.spcore.domain.repo.obj.SearchFilters;
import ru.spliterash.spcore.domain.repo.obj.SearchResult;
import ru.spliterash.spcore.domain.usecase.UseCase;
import ru.spliterash.spcore.domain.usecase.iv.SearchInputValues;

@RequiredArgsConstructor
public abstract class AbstractSearchEntityBlockingUseCase<F extends SearchFilters, E extends BaseEntity> implements UseCase<SearchInputValues<F>, SearchResult<E>> {
    private final BlockingCrudRepository<E, F> repository;

    @Override
    public SearchResult<E> execute(SearchInputValues<F> input) {
        return repository.search(input.getPagination(), input.getFilters());
    }
}
