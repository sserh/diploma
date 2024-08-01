package ru.raccoon.netologydiploma.service;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.raccoon.netologydiploma.exception.BadRequestException;
import ru.raccoon.netologydiploma.jwttoken.JwtCore;
import ru.raccoon.netologydiploma.repository.TokenRepository;
import ru.raccoon.netologydiploma.responseentities.AuthToken;
import ru.raccoon.netologydiploma.responseentities.Error;

@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtCore jwtCore;

    public AuthService(AuthenticationManager authenticationManager, JwtCore jwtCore) {
        this.authenticationManager = authenticationManager;
        this.jwtCore = jwtCore;
    }

    /**
     * Метод, осуществляющий аутентификацию пользователя и выдачу пользователю токена
     *
     * @param login    Логин, указанный пользователем в запросе
     * @param password Пароль, указанный пользователем в запросе
     * @return Возвращает токен аутентификации или ошибку о неверных данных для аутентификации
     */
    public ResponseEntity<AuthToken> login(String login, String password) {

        Authentication auth;
        //пробуем авторизоваться
        try {
            auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(login, password));
        } catch (AuthenticationException e) {
            //если авторизация не прошла успешно, то отправляем ошибку
            throw new BadRequestException(new Error(e.getMessage(), 1001));
        }
        //если авторизация прошла успешна, то устанавливаем контекст безопасности и генерируем токен
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtCore.generateToken(login);
        //добавляем новый токен в мапу
        TokenRepository.addNewToken(token);

        return new ResponseEntity<>(new AuthToken(token), HttpStatus.OK);
    }
}
