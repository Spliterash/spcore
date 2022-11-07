package ru.spliterash.spcore.domain.entity

import java.time.Instant


abstract class AbstractBaseEntity(
    override var id: String? = null,
    override var createAt: Instant = Instant.now(),
    override var updateAt: Instant = Instant.now()
) : BaseEntity