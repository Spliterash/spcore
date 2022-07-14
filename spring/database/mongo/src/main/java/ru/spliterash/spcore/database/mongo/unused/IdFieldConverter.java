package ru.spliterash.spcore.database.mongo.unused;

import org.bson.types.ObjectId;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.core.convert.converter.ConditionalGenericConverter;
import ru.spliterash.spcore.domain.repo.IdField;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// До лучших времен: https://github.com/spring-projects/spring-data-mongodb/issues/1815
class IdFieldConverter implements ConditionalGenericConverter {
    @Override
    public boolean matches(TypeDescriptor sourceType, TypeDescriptor targetType) {
        return sourceType.hasAnnotation(IdField.class) || targetType.hasAnnotation(IdField.class);
    }

    @Override
    public Set<ConvertiblePair> getConvertibleTypes() {
        return Stream.of(
                new ConvertiblePair(String.class, ObjectId.class),
                new ConvertiblePair(ObjectId.class, String.class)
        ).collect(Collectors.toSet());
    }

    @Override
    public Object convert(Object source, TypeDescriptor sourceType, TypeDescriptor targetType) {
        if (source instanceof ObjectId)
            return ((ObjectId) source).toHexString();
        else if (source instanceof String)
            return new ObjectId(source.toString());
        else
            throw new IllegalArgumentException("Unknown source");
    }
}
