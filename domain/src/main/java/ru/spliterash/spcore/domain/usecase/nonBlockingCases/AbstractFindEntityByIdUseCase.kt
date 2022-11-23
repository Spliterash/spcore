package ru.spliterash.spcore.domain.usecase.nonBlockingCases

import lombok.RequiredArgsConstructor
import ru.spliterash.spcore.domain.entity.BaseEntity
import ru.spliterash.spcore.domain.exceptions.DomainException
import ru.spliterash.spcore.domain.repo.CrudRepository
import ru.spliterash.spcore.domain.usecase.NonBlockingUseCase

@RequiredArgsConstructor
abstract class AbstractFindEntityByIdUseCase<E : BaseEntity?>(
    protected val repository: CrudRepository<E, *>
) : NonBlockingUseCase<String, E> {

    override suspend fun execute(id: String): E {
        return repository.findById(id) ?: throw getNotFoundException(id)
    }

    protected abstract fun getNotFoundException(id: String): DomainException
}