package ru.spliterash.spcore.domain.usecase.iv;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import lombok.extern.jackson.Jacksonized;
import org.jetbrains.annotations.Nullable;
import ru.spliterash.spcore.domain.repo.obj.Pagination;
import ru.spliterash.spcore.domain.repo.obj.SearchFilters;

@Getter
@SuperBuilder
@AllArgsConstructor
@Jacksonized
public class SearchInputValues<F extends SearchFilters> {
    @Nullable
    private final Pagination pagination;
    @Nullable
    private final F filters;
}
