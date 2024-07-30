package ru.raccoon.netologydiploma.exception;

/**
 * Класс для "упаковки" подробностей исключений для отправки их клиенту
 * @param message Сообщение для клиента
 * @param id Идентификатор типа сообщения
 */
public record ExceptionData(String message, Integer id) {
}
