package ru.raccoon.netologydiploma.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.raccoon.netologydiploma.responseentities.AuthRequest;
import ru.raccoon.netologydiploma.responseentities.AuthToken;
import ru.raccoon.netologydiploma.service.AuthService;

@RestController
@RequestMapping("/")
public class AuthController {

    private final AuthService authService;

    @Autowired
    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    /** Метод, отправляющий пользователю информацию о результатах его запроса на осуществление входа
     * @param authRequest Запрос пользователя
     * @return Возвращает результат обработки запроса сервисом авторизации
     */
    @PostMapping("login")
    public ResponseEntity<AuthToken> login(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest.getLogin(), authRequest.getPassword());
    }

    //тестовое маппирование
    @PostMapping("enot")
    public String enot() {
        return "You are enot";
    }
}
