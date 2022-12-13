package ru.spliterash.spcore.database.mongo.repository

import kotlinx.coroutines.*
import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.core.ResolvableType
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import ru.spliterash.spcore.database.mongo.document.BaseDocument
import ru.spliterash.spcore.database.mongo.query.DefaultSearchQuery
import ru.spliterash.spcore.domain.commons.Mapper
import ru.spliterash.spcore.domain.entity.BaseEntity
import ru.spliterash.spcore.domain.repo.CrudRepository
import ru.spliterash.spcore.domain.repo.obj.Pagination
import ru.spliterash.spcore.domain.repo.obj.SearchFilters
import ru.spliterash.spcore.domain.repo.obj.SearchResult
import java.util.*

@Suppress("MemberVisibilityCanBePrivate")
abstract class AbstractMappingMongoRepository<D : BaseDocument, E : BaseEntity, F : SearchFilters>(
    protected val mongoTemplate: ReactiveMongoTemplate,
    protected val mapper: Mapper<D, E>
) : CrudRepository<E, F> {
    protected val documentClass: Class<D>

    init {
        val resolvableType = ResolvableType.forClass(AbstractMongoRepository::class.java, javaClass)
        @Suppress("UNCHECKED_CAST")
        documentClass = resolvableType.getGeneric(0).type as Class<D>
    }

    override suspend fun save(e: E): E =
        mongoTemplate.save(mapper.inverseMap(e))
            .map(mapper::map)
            .awaitSingle()

    override suspend fun delete(id: String): Boolean =
        mongoTemplate.remove(idQuery(id), documentClass)
            .map { it.deletedCount > 0 }
            .awaitSingle()

    override suspend fun findById(id: String): E? =
        mongoTemplate.findById(id, documentClass)
            .awaitSingleOrNull()?.let { mapper.map(it) }

    override suspend fun findByIds(ids: Collection<String>): List<E> =
        mongoTemplate
            .find(wrap(Criteria.where("_id").`in`(ids)), documentClass)
            .collectList()
            .awaitSingle()
            .map(mapper::map)

    override suspend fun search(pagination: Pagination?, filters: F?): SearchResult<E> {
        return search(pagination, getSearchQuery(filters))
    }

    override suspend fun exist(id: String): Boolean {
        return mongoTemplate.exists(idQuery(id), documentClass).awaitSingle()
    }

    protected suspend fun search(pagination: Pagination?, searchQuery: DefaultSearchQuery<*>): SearchResult<E> {
        val criteria = searchQuery.criteria
        val sort = searchQuery.sort
        val query = Query(criteria)
        query.with(sort)
        if (pagination != null) {
            query.skip(pagination.skip)
            query.limit(pagination.limit.toInt())
        }

        return coroutineScope {
            val list = async {
                mongoTemplate.find(query, documentClass).collectList().awaitSingle().map(mapper::map)
            }
            val count = async {
                mongoTemplate.count(query.limit(0).skip(0), documentClass).awaitSingle()
            }

            SearchResult(list.await(), count.await())
        }
    }

    protected fun getSearchQuery(filters: F?): DefaultSearchQuery<F?> {
        return DefaultSearchQuery(filters)
    }

    protected fun wrap(criteria: Criteria): Query {
        return Query(criteria)
    }

    protected fun idQuery(id: String) = wrap(Criteria.where("_id").`is`(id))
}