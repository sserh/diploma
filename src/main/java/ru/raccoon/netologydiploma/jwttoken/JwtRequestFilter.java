package ru.raccoon.netologydiploma.jwttoken;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.raccoon.netologydiploma.repository.TokenRepository;
import ru.raccoon.netologydiploma.utils.UtilClass;

import java.io.IOException;
import java.util.Date;

/**
 * Класс, реализующий фильтр доступа по предоставленному пользователем токену
 */
@Component
public class JwtRequestFilter extends OncePerRequestFilter {

    private final UserDetailsService userDetailsService;
    private final String secret;

    public JwtRequestFilter(UserDetailsService userDetailsService, @Value("${token.secret}") String secret) {
        this.userDetailsService = userDetailsService;
        this.secret = secret;
    }

    /**
     * Метод реализующий фильтр по токену. Метод нужен для включения в цепочку фильтров web-безопасности
     *
     * @param request  Запрос от пользователя
     * @param response Ответ на запрос
     * @param chain    Параметр передачи управления
     * @throws ServletException Обработка исключений сервлета
     * @throws IOException      Обработка общих исключений при работе с потоками ввода/вывода
     */
    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {
        //получаем header с предполагаемым токеном из запроса
        final String authorizationHeader = request.getHeader("Auth-Token");

        String username = null;
        String jwt = null;

        //проверяем, что это действительно токен
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            try {
                username = extractUsername(jwt);
            } catch (SignatureException e) {
                //если извлечь пользователя из токена не удалось, сообщаем об этом
                UtilClass.sendTokenError(response, "Структура токена нарушена", 10050);
                //и прекращаем выполнение фильтрации
                return;
            }
        }

        //если токен действителен, то сверяем, что пользователь из токена совпадает с пользователем, уже осуществившим вход
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            var userDetails = this.userDetailsService.loadUserByUsername(username);

            if (validateToken(jwt, userDetails)) {
                var usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //подтверждаем аутентификацию
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            } else {
                //если валидность токена не подтверждена, то сообщаем об этом
                UtilClass.sendTokenError(response, "Токен недействителен", 10060);
                //и прекращаем выполнение фильтрации
                return;
            }
        }
        //передаём управление следующем фильтру
        chain.doFilter(request, response);
    }

    /**
     * Метод, извлекающий имя пользователя из токена
     *
     * @param token Токен
     * @return Возвращает имя пользователя, извлечённое из токена
     */
    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(secret.getBytes()).build().parseClaimsJws(token).getBody().getSubject();
    }

    /**
     * Метод, возвращающий признак действительности токена - имя пользователя в токене совпадает с авторизованным пользователем, время действия токена не истекло
     * и держатель этого токена не осуществлял выход
     *
     * @param token       Токен
     * @param userDetails Параметры авторизованного пользователя
     * @return Возвращает true, если токен действительный и false, если токен недействительный
     */
    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token) && TokenRepository.isTokenEnabled(token));
    }

    /**
     * Метод, возвращающий признак того, что срок действия токена ещё не истёк
     *
     * @param token Токен
     * @return Возвращает true, если срок действия пользователя не истёк и false, если срок действия токена истёк
     */
    public boolean isTokenExpired(String token) {
        final Claims claims = Jwts.parser().setSigningKey(secret.getBytes()).build().parseClaimsJws(token).getBody();
        return claims.getExpiration().before(new Date());
    }
}
