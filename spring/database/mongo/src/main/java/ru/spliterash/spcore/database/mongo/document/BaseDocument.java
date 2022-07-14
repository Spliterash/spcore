package ru.spliterash.spcore.database.mongo.document;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

import java.time.Instant;

@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
public abstract class BaseDocument {
    private ObjectId id;
    private Instant createAt;
    private Instant updateAt;
}
