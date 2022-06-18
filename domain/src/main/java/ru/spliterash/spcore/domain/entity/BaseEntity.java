package ru.spliterash.spcore.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import ru.spliterash.spcore.domain.repo.IdField;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class BaseEntity {
    @IdField
    private String id;

    @Builder.Default
    private Instant createAt = Instant.now();
    @Builder.Default
    private Instant updateAt = Instant.now();
}
