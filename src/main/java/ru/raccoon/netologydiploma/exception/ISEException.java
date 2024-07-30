package ru.raccoon.netologydiploma.exception;

import lombok.Getter;

/**
 * Класс исключения InternalServerError
 */
@Getter
public class ISEException extends RuntimeException {

    private final ExceptionData exceptionData;

    public ISEException(ExceptionData exceptionData) {
        this.exceptionData = exceptionData;
    }
}
