package ru.spliterash.spcore.domain.repo

import ru.spliterash.spcore.domain.entity.BaseEntity
import ru.spliterash.spcore.domain.repo.obj.Pagination
import ru.spliterash.spcore.domain.repo.obj.SearchFilters
import ru.spliterash.spcore.domain.repo.obj.SearchResult

interface CrudRepository<E : BaseEntity?, F : SearchFilters?> {
    suspend fun save(e: E): E
    suspend fun delete(id: String): Boolean
    suspend fun findById(id: String): E?
    suspend fun search(pagination: Pagination?, filters: F?): SearchResult<E>
    suspend fun exist(id: String): Boolean
}