package ru.spliterash.spcore.domain.repo.obj;

import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@NoArgsConstructor
public class SearchFilters {
    @Builder.Default
    private String sortBy = "id";
    @Builder.Default
    private Direction direction = Direction.ASC;
    private DateRange createAt;
    private DateRange updateAt;

    public enum Direction {
        ASC, DESC
    }
}
