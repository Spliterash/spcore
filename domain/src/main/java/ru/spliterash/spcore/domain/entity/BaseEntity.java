package ru.spliterash.spcore.domain.entity;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Instant;

public interface BaseEntity {
    @Nullable
    String getId();

    void setId(@Nullable String var1);

    @NotNull
    Instant getCreateAt();

    void setCreateAt(@NotNull Instant var1);

    @NotNull
    Instant getUpdateAt();

    void setUpdateAt(@NotNull Instant var1);
}
