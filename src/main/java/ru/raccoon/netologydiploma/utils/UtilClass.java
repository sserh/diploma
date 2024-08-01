package ru.raccoon.netologydiploma.utils;

import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import ru.raccoon.netologydiploma.responseentities.Error;

import java.io.IOException;

/**
 * Вспомогательный класс
 */
public class UtilClass {

    private static final Gson gson = new Gson();

    /** Метод, отправляющий клиенту описание ошибки, возникшей при фильтрации запроса
     * @param response Ответ клиенту
     * @param message Текст сообщения
     * @param id Идентификатор сообщения
     * @throws IOException
     */
    public static void sendTokenError(HttpServletResponse response, String message, int id) throws IOException {
        Error error = new Error(message, id);
        String errorString = gson.toJson(error);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setStatus(HttpStatus.UNAUTHORIZED.value());
        response.getWriter().print(errorString);
    }
}
