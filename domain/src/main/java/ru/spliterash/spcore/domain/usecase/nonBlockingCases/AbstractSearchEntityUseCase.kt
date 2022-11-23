package ru.spliterash.spcore.domain.usecase.nonBlockingCases

import lombok.RequiredArgsConstructor
import ru.spliterash.spcore.domain.entity.BaseEntity
import ru.spliterash.spcore.domain.repo.CrudRepository
import ru.spliterash.spcore.domain.repo.obj.SearchFilters
import ru.spliterash.spcore.domain.repo.obj.SearchResult
import ru.spliterash.spcore.domain.usecase.NonBlockingUseCase
import ru.spliterash.spcore.domain.usecase.iv.SearchInputValues

@RequiredArgsConstructor
abstract class AbstractSearchEntityUseCase<F : SearchFilters, E : BaseEntity>(
    private val repository: CrudRepository<E, F>
) : NonBlockingUseCase<SearchInputValues<F>, SearchResult<E>> {
    override suspend fun execute(input: SearchInputValues<F>): SearchResult<E> {
        return repository.search(input.pagination, input.filters)
    }
}