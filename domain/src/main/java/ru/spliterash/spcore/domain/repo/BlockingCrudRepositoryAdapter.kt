package ru.spliterash.spcore.domain.repo

import kotlinx.coroutines.runBlocking
import ru.spliterash.spcore.domain.entity.BaseEntity
import ru.spliterash.spcore.domain.repo.obj.Pagination
import ru.spliterash.spcore.domain.repo.obj.SearchFilters
import ru.spliterash.spcore.domain.repo.obj.SearchResult

open class BlockingCrudRepositoryAdapter<E : BaseEntity, F : SearchFilters>(
    protected val repository: BlockingCrudRepository<E, F>
) : BlockingCrudRepository<E, F> {
    override fun save(e: E): E = runBlocking {
        repository.save(e)
    }

    override fun delete(id: String) = runBlocking {
        repository.delete(id)
    }

    override fun findById(id: String): E? = runBlocking {
        repository.findById(id)
    }

    override fun findByIds(ids: Collection<String>): List<E> = runBlocking {
        repository.findByIds(ids)
    }

    override fun exist(id: String): Boolean = runBlocking {
        repository.exist(id)
    }

    override fun search(pagination: Pagination?, filters: F?): SearchResult<E> = runBlocking {
        repository.search(pagination, filters)
    }
}