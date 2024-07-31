package ru.raccoon.netologydiploma.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import ru.raccoon.netologydiploma.repository.TokenRepository;

import java.io.IOException;

/**
 * Класс описывает обработчик, который выполняется при событии вызова логаута
 */
@Service
public class CustomLogoutHandler implements LogoutHandler {

    /** Переопределённый метод выхода пользователя
     * @param request Запрос на выход от пользователя
     * @param response Ответ на запрос пользователя
     * @param authentication Данные по текущей аутентификации
     */
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        //получим токен авторизации от пользователя, запросившего выход
        String token = request.getHeader("auth-token").substring(7);
        //если токен существует и активен
        if (TokenRepository.isTokenEnabled(token)) {
            //помечаем его неактивным
            TokenRepository.disabledToken(token);
        } else {
            try {
                //иначе отправляем ошибку
                response.sendError(1002, "Пользователь не существует или выход уже был осуществлён");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
