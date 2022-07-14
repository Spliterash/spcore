package ru.spliterash.spcore.database.mongo.repository;

import lombok.AllArgsConstructor;
import org.springframework.core.ResolvableType;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import ru.spliterash.spcore.domain.repo.SingletonRepository;

import java.util.Optional;

@AllArgsConstructor
public abstract class AbstractMongoSingletonRepository<T> implements SingletonRepository<T> {
    private final MongoTemplate mongoTemplate;
    private final String collectionName;
    private final Class<T> documentClass;

    public AbstractMongoSingletonRepository(MongoTemplate mongoTemplate, String collectionName) {
        this.mongoTemplate = mongoTemplate;
        this.collectionName = collectionName;

        ResolvableType resolvableType = ResolvableType.forClass(AbstractMongoSingletonRepository.class, getClass());

        //noinspection unchecked
        documentClass = (Class<T>) resolvableType.getGeneric(0).getType();
    }

    @Override
    public Optional<T> get() {
        return Optional.ofNullable(mongoTemplate.findOne(new Query(), documentClass, collectionName));
    }

    @Override
    public void set(T t) {
        mongoTemplate.remove(new Query(), collectionName);
        mongoTemplate.save(t, collectionName);
    }
}
