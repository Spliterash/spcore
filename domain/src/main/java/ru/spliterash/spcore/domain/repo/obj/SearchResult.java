package ru.spliterash.spcore.domain.repo.obj;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;
@Getter
@RequiredArgsConstructor
public final class SearchResult<T> {
    private final List<T> list;
    private final long count;
}
