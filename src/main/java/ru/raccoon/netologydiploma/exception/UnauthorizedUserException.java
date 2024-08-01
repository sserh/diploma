package ru.raccoon.netologydiploma.exception;

import lombok.Getter;
import ru.raccoon.netologydiploma.responseentities.Error;

@Getter
public class UnauthorizedUserException extends RuntimeException {

    private final Error error;

    public UnauthorizedUserException(Error error) {
        this.error = error;
    }
}
