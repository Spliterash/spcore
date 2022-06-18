package ru.spliterash.spcore.domain.exceptions;

import lombok.Getter;

@Getter
public abstract class DomainException extends RuntimeException {
    private final String reason;

    public DomainException(String reason, String message) {
        super(message);
        this.reason = reason;
    }

    public DomainException(String reason, String message, Throwable cause) {
        super(message, cause);
        this.reason = reason;
    }
}
