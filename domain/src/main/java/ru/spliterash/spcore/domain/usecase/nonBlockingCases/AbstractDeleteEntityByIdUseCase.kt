package ru.spliterash.spcore.domain.usecase.nonBlockingCases

import lombok.RequiredArgsConstructor
import ru.spliterash.spcore.domain.entity.BaseEntity
import ru.spliterash.spcore.domain.repo.CrudRepository
import ru.spliterash.spcore.domain.usecase.NonBlockingUseCase

@RequiredArgsConstructor
abstract class AbstractDeleteEntityByIdUseCase<E : BaseEntity?>(
    protected val repository: CrudRepository<E, *>
) : NonBlockingUseCase<String, Boolean> {

    override suspend fun execute(id: String): Boolean = repository.delete(id)
}