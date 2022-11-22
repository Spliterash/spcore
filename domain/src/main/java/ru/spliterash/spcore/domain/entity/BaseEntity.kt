package ru.spliterash.spcore.domain.entity

import ru.spliterash.spcore.domain.repo.IdField
import java.time.Instant

interface BaseEntity {
    @get:IdField
    @set:IdField
    var id: String?
    var createAt: Instant
    var updateAt: Instant
}