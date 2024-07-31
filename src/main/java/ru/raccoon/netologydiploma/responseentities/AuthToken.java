package ru.raccoon.netologydiploma.responseentities;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Класс-упаковка для отправки пользователю ответа с токеном аутентификации
 */
@AllArgsConstructor
@Data
public class AuthToken {

    @JsonProperty("auth-token")
    private String token;
}
