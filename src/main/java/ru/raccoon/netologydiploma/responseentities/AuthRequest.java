package ru.raccoon.netologydiploma.responseentities;

import lombok.Data;

/**
 * Класс-упаковка для данных запроса входа от пользователя
 */
@Data
public class AuthRequest {
    private String login, password;
}
