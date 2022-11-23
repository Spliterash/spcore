package ru.spliterash.spcore.database.mongo.repository

import kotlinx.coroutines.reactor.awaitSingle
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.core.ResolvableType
import org.springframework.data.mongodb.core.ReactiveMongoTemplate
import org.springframework.data.mongodb.core.query.Query
import ru.spliterash.spcore.domain.repo.SingletonRepository
import java.util.*

abstract class AbstractMongoSingletonRepository<T>(
    private val mongoTemplate: ReactiveMongoTemplate,
    private val collectionName: String
) : SingletonRepository<T> {
    private val documentClass: Class<T>

    init {
        val resolvableType = ResolvableType.forClass(AbstractMongoSingletonRepository::class.java, javaClass)
        @Suppress("UNCHECKED_CAST")
        documentClass = resolvableType.generics[0].type as Class<T>
    }

    override suspend fun get(): T? {
        return mongoTemplate.findOne(Query(), documentClass, collectionName).awaitSingleOrNull()
    }

    override suspend fun set(t: T) {
        mongoTemplate.remove(Query(), collectionName).awaitSingleOrNull()
        mongoTemplate.save(t, collectionName).awaitSingle()
    }
}