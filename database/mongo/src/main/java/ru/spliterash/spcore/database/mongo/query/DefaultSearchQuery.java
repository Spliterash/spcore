package ru.spliterash.spcore.database.mongo.query;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import ru.spliterash.spcore.database.mongo.utils.MongoUtils;
import ru.spliterash.spcore.domain.repo.obj.SearchFilters;

import java.util.HashSet;
import java.util.Set;

@RequiredArgsConstructor
public class DefaultSearchQuery<F extends SearchFilters> {
    protected final F filters;

    protected Criteria _getCriteria() {
        return null;
    }

    public final Criteria getCriteria() {
        Set<Criteria> criteriaSet = new HashSet<>();

        if (filters.getCreateAt() != null)
            criteriaSet.add(MongoUtils.dateRangeCriteria("createAt", filters.getCreateAt()));

        if (filters.getUpdateAt() != null)
            criteriaSet.add(MongoUtils.dateRangeCriteria("updateAt", filters.getUpdateAt()));

        Criteria criteria = _getCriteria();

        if (criteria != null)
            criteriaSet.add(criteria);

        return MongoUtils.and(criteriaSet);
    }

    // Мето оставленный для переопределения, чтобы сделать что-то с запросом после, например hint накинуть
    protected void postQuery(Query query) {
        // NOTHING
    }


    public Sort getSort() {
        return Sort.by(MongoUtils.mapSort(filters.getDirection()), filters.getSortBy());
    }
}
