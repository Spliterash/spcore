package ru.spliterash.spcore.domain.entity

import java.time.Instant

interface BaseEntity {
    var id: String?
    var createAt: Instant
    var updateAt: Instant
}