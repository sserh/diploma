package ru.raccoon.netologydiploma.exception;

import lombok.Getter;
import ru.raccoon.netologydiploma.responseentities.Error;

/**
 * Класс исключения InternalServerError
 */
@Getter
public class ISEException extends RuntimeException {

    private final Error error;

    public ISEException(Error error) {
        this.error = error;
    }
}
