package ru.raccoon.netologydiploma.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.raccoon.netologydiploma.exception.BadRequestException;
import ru.raccoon.netologydiploma.jwttoken.JwtCore;
import ru.raccoon.netologydiploma.repository.TokenRepository;
import ru.raccoon.netologydiploma.responseentities.AuthRequest;
import ru.raccoon.netologydiploma.responseentities.AuthToken;
import ru.raccoon.netologydiploma.responseentities.Error;

@RestController
@RequestMapping("/")
public class RequestController {

    private final JwtCore jwtCore;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public RequestController(JwtCore jwtCore, AuthenticationManager authenticationManager) {
        this.jwtCore = jwtCore;
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("login")
    public ResponseEntity<AuthToken> login(@RequestBody AuthRequest authRequest) {
        Authentication auth;
        //пробуем авторизоваться
        try {
            auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        } catch (AuthenticationException e) {
            //если авторизация не прошла успешно, то отправляем ошибку
            throw new BadRequestException(new Error(e.getMessage(), 1001));
        }
        //если авторизация прошла успешна, то устанавливаем контекст безопасности и генерируем токен
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtCore.generateToken(authRequest.getLogin());
        //добавляем новый токен в мапу
        TokenRepository.addNewToken(token);

        return new ResponseEntity<>(new AuthToken(token), HttpStatus.OK);
    }

    //тестовое маппирование
    @PostMapping("enot")
    public String enot() {
        return "You are enot";
    }
}
