package ru.raccoon.netologydiploma.exception;

import lombok.Getter;
import ru.raccoon.netologydiploma.responseentities.Error;

/**
 * Класс исключения BadRequest
 */
@Getter
public class BadRequestException extends RuntimeException {

    private final Error error;

    public BadRequestException(Error error) {
        this.error = error;
    }
}
