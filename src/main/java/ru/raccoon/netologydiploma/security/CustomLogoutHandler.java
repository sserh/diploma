package ru.raccoon.netologydiploma.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import ru.raccoon.netologydiploma.repository.TokenRepository;

import java.io.IOException;

@Service
public class CustomLogoutHandler implements LogoutHandler {

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String token = request.getHeader("auth-token").substring(7);
        if (TokenRepository.isTokenEnabled(token)) {
            TokenRepository.disabledToken(token);
        } else {
            try {
                response.sendError(1002, "Пользователь не найден");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
