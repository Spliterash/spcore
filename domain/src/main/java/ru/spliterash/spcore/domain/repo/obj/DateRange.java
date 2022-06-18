package ru.spliterash.spcore.domain.repo.obj;

import lombok.*;

import java.time.Instant;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public final class DateRange {
    private Instant startAt;
    private Instant entAt;
}
