package ru.spliterash.spcore.domain.repo;

import ru.spliterash.spcore.domain.entity.BaseEntity;
import ru.spliterash.spcore.domain.repo.obj.Pagination;
import ru.spliterash.spcore.domain.repo.obj.SearchFilters;
import ru.spliterash.spcore.domain.repo.obj.SearchResult;

import java.util.Optional;

public interface CrudRepository<E extends BaseEntity, F extends SearchFilters> {
    E save(E e);

    void delete(String id);

    Optional<E> findById(String id);

    SearchResult<E> search(Pagination pagination, F filters);

    boolean exist(String id);
}
