package ru.spliterash.spcore.domain.usecase.nonBlockingCases

import lombok.RequiredArgsConstructor
import ru.spliterash.spcore.domain.repo.CrudRepository
import ru.spliterash.spcore.domain.usecase.NonBlockingUseCase

@RequiredArgsConstructor
abstract class AbstractCheckEntityExistUseCase(
    protected val repository: CrudRepository<*, *>
) : NonBlockingUseCase<String, Boolean> {
    override suspend fun execute(id: String): Boolean {
        return repository.exist(id)
    }
}