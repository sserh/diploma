package ru.raccoon.netologydiploma.jwttoken;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * Класс, предоставляющий метод генерации токена аутентификации для пользователя
 */
@Component
@Data
public class JwtCore {

    @Value("${token.secret}")
    private String secret;

    @Value("${token.expirationMs}")
    private int lifetime;

    /**
     * Метод, генерирующий токен на основе логина пользователя
     *
     * @param login Логин пользователя
     * @return Возвращает сгенерированный токен
     */
    public String generateToken(String login) {
        return Jwts.builder()
                //записываем в токен логин
                .subject(login)
                //время начала действия токена
                .issuedAt(new Date(System.currentTimeMillis()))
                //время истечения действия токена
                .expiration(new Date(System.currentTimeMillis() + lifetime))
                //шифруем это всё с включением секрета
                .signWith(Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8)), Jwts.SIG.HS512)
                //и выражаем в виде строки
                .compact();
    }
}
