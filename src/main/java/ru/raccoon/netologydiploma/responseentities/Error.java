package ru.raccoon.netologydiploma.responseentities;

/**
 * Класс-упаковка для отправки пользователю данных по ошибке
 *
 * @param message Текст ошибки
 * @param id      Идентификатор ошибки
 */
public record Error(String message, Integer id) {
}
