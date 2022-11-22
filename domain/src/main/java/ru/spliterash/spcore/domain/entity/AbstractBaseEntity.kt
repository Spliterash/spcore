package ru.spliterash.spcore.domain.entity

import ru.spliterash.spcore.domain.repo.IdField
import java.time.Instant


abstract class AbstractBaseEntity(
    @IdField
    override var id: String? = null,
    override var createAt: Instant = Instant.now(),
    override var updateAt: Instant = Instant.now()
) : BaseEntity