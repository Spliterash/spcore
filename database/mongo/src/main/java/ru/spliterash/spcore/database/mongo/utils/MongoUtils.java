package ru.spliterash.spcore.database.mongo.utils;

import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import ru.spliterash.spcore.domain.repo.obj.DateRange;
import ru.spliterash.spcore.domain.repo.obj.SearchFilters;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@UtilityClass
public class MongoUtils {
    private final Pattern REPLACER = Pattern.compile("[-\\[\\]{}()*+?.,\\\\^$|#s]");

    public Sort.Direction mapSort(SearchFilters.Direction sort) {
        switch (sort) {
            case ASC:
                return Sort.Direction.ASC;
            case DESC:
                return Sort.Direction.DESC;
            default:
                throw new RuntimeException("Unreachable");
        }
    }

    public Criteria regExCriteria(String query, String... fields) {
        String escaped = shieldNotSupportedSymbols(query);
        return new Criteria().orOperator(
                Arrays.stream(fields)
                        .map(f -> Criteria.where(f).regex(escaped, "i"))
                        .collect(Collectors.toList())
        );
    }

    public Criteria and(Criteria... criteria) {
        return and(Arrays.asList(criteria));
    }

    public Criteria and(Collection<Criteria> criteria) {
        if (criteria.isEmpty())
            return new Criteria();
        else
            return new Criteria().andOperator(criteria);
    }

    public String shieldNotSupportedSymbols(String string) {
        return REPLACER.matcher(string).replaceAll("\\\\$0");
    }

    public Criteria dateRangeCriteria(String fieldName, DateRange range) {
        if (range == null || (range.getStartAt() == null && range.getEntAt() == null))
            return new Criteria();

        Set<Criteria> criteriaSet = new HashSet<>();

        if (range.getStartAt() != null)
            criteriaSet.add(Criteria.where(fieldName).gte(range.getStartAt()));
        if (range.getEntAt() != null)
            criteriaSet.add(Criteria.where(fieldName).lte(range.getEntAt()));

        return and(criteriaSet);
    }
}
