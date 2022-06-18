package ru.spliterash.spcore.domain.repo.obj;

import lombok.Value;

@Value
public class Pagination {
    long skip;
    long limit;

    public static Pagination ofPage(long pageSize, long pageNum) {
        return new Pagination(
                pageNum * pageSize,
                pageSize
        );
    }
}
