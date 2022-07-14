package ru.spliterash.spcore.database.mongo.repository;

import org.springframework.core.ResolvableType;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.spliterash.spcore.database.mongo.query.DefaultSearchQuery;
import ru.spliterash.spcore.database.mongo.document.BaseDocument;
import ru.spliterash.spcore.domain.entity.BaseEntity;
import ru.spliterash.spcore.domain.commons.Mapper;
import ru.spliterash.spcore.domain.repo.CrudRepository;
import ru.spliterash.spcore.domain.repo.obj.Pagination;
import ru.spliterash.spcore.domain.repo.obj.SearchFilters;
import ru.spliterash.spcore.domain.repo.obj.SearchResult;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public abstract class AbstractMappingMongoRepository<D extends BaseDocument, E extends BaseEntity, F extends SearchFilters> implements CrudRepository<E, F> {
    protected final MongoTemplate mongoTemplate;
    protected final Mapper<D, E> mapper;
    protected final Class<D> documentClass;


    public AbstractMappingMongoRepository(MongoTemplate mongoTemplate, Mapper<D, E> mapper) {
        this.mongoTemplate = mongoTemplate;
        this.mapper = mapper;

        ResolvableType resolvableType = ResolvableType.forClass(AbstractMappingMongoRepository.class, getClass());

        //noinspection unchecked
        documentClass = (Class<D>) resolvableType.getGeneric(0).getType();
    }

    @Override
    public E save(E e) {
        return mapper.map(mongoTemplate.save(mapper.inverseMap(e)));
    }

    @Override
    public void delete(String id) {
        mongoTemplate.remove(wrap(Criteria.where("_id").is(id)), documentClass);
    }

    @Override
    public Optional<E> findById(String id) {
        D entity = mongoTemplate.findById(id, documentClass);

        return Optional
                .ofNullable(entity)
                .map(mapper::map);
    }

    @Override
    public SearchResult<E> search(Pagination pagination, F filters) {
        return search(pagination, getSearchQuery(filters));
    }

    @Override
    public boolean exist(String id) {
        return mongoTemplate.exists(wrap(Criteria.where("_id").is(id)), documentClass);
    }

    protected SearchResult<E> search(Pagination pagination, DefaultSearchQuery<?> searchQuery) {
        Criteria criteria = searchQuery.getCriteria();
        Sort sort = searchQuery.getSort();

        Query query = new Query(criteria);

        query.with(sort);
        query.skip(pagination.getSkip());
        query.limit((int) pagination.getLimit());

        List<E> list = mongoTemplate.find(query, documentClass).stream().map(mapper::map).collect(Collectors.toList());
        long count = mongoTemplate.count(query.limit(0).skip(0), documentClass);

        return new SearchResult<>(list, count);
    }

    protected DefaultSearchQuery<F> getSearchQuery(F filters) {
        return new DefaultSearchQuery<>(filters);
    }


    protected Query wrap(Criteria criteria) {
        return new Query(criteria);
    }
}
