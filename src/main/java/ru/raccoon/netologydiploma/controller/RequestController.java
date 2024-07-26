package ru.raccoon.netologydiploma.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.raccoon.netologydiploma.component.AuthRequest;
import ru.raccoon.netologydiploma.component.AuthToken;
import ru.raccoon.netologydiploma.jwttoken.JwtCore;

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
        auth = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getLogin(), authRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtCore.generateToken(authRequest.getLogin());

        return new ResponseEntity<>(new AuthToken(token), HttpStatus.OK);
    }

    //тестовое маппирование
    @PostMapping("/enot")
    public String enot() {
        return "You are enot";
    }
}
