package ru.raccoon.netologydiploma.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

/**
 * Класс, осуществляющий работу с выданными пользователям токенами
 */
@Repository
public class TokenRepository {

    //мапа для хранения информации о токенах
    static Map<String, Boolean> tokens = new HashMap<>();

    /** Метод, добавляющий в мапу новый выданный пользователю токен
     * @param token Токен
     */
    public static void addNewToken(String token) {
        tokens.put(token, true);
    }

    /** Метод, помечающий токен недействительным
     * @param token Токен
     */
    public static void disabledToken(String token) {
        tokens.put(token, false);
    }

    /** Метод, проверяющий действительность токена
     * @param token Токен
     * @return Возвращает признак действительности
     */
    public static boolean isTokenEnabled(String token) {
        //чтобы быть действительным, токен должен существовать и иметь в мапе value true
        return tokens.containsKey(token) && tokens.get(token);
    }
}
