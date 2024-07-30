package ru.raccoon.netologydiploma.repository;

import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class TokenRepository {

    static Map<String, Boolean> tokens = new HashMap<>();

    public static void addNewToken(String token) {
        tokens.put(token, true);
    }

    public static void disabledToken(String token) {
        tokens.put(token, false);
    }

    public static boolean isTokenEnabled(String token) {
        return tokens.containsKey(token) && tokens.get(token);
    }
}
