package ru.spliterash.spcore.domain.repo

import ru.spliterash.spcore.domain.entity.BaseEntity
import ru.spliterash.spcore.domain.repo.obj.Pagination
import ru.spliterash.spcore.domain.repo.obj.SearchFilters
import ru.spliterash.spcore.domain.repo.obj.SearchResult

interface BlockingCrudRepository<E : BaseEntity?, F : SearchFilters?> {
     fun save(e: E): E
     fun delete(id: String): Boolean
     fun findById(id: String): E?
     fun search(pagination: Pagination?, filters: F?): SearchResult<E>
     fun exist(id: String): Boolean
}