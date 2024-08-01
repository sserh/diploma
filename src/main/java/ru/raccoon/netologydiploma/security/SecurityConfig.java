package ru.raccoon.netologydiploma.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HttpStatusReturningLogoutSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import ru.raccoon.netologydiploma.jwttoken.JwtRequestFilter;

import javax.sql.DataSource;
import java.util.List;

/**
 * Класс с настройками работы всех сущностей, связанных с авторизацией и аутентификацией пользователя
 */
@Component
@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtRequestFilter jwtRequestFilter;
    private final CustomLogoutHandler customLogoutHandler;

    @Lazy
    @Autowired
    public SecurityConfig(JwtRequestFilter jwtRequestFilter, CustomLogoutHandler customLogoutHandler) {
        this.jwtRequestFilter = jwtRequestFilter;
        this.customLogoutHandler = customLogoutHandler;
    }

    //проверка токена и фильтрация http-запросов по endpoints
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> auth
                        //на endpoint логина можно заходить всем, все остальные endpoints требуют аутентификации
                        .requestMatchers("/cloud/login").permitAll()
                        .anyRequest().authenticated())
                //режим работы сессий отключаем
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                //добавляем фильтрацию по токену
                .addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class)
                //отключаем защиту csrf
                .csrf(AbstractHttpConfigurer::disable)
                //настраиваем cors
                .cors(cors -> cors.configurationSource(request -> {
                    var corsConfiguration = new CorsConfiguration();
                    corsConfiguration.setAllowedOriginPatterns(List.of("*"));
                    corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfiguration.setAllowedHeaders(List.of("*"));
                    corsConfiguration.setAllowCredentials(true);
                    return corsConfiguration;
                }))
                //реализуем logout
                .logout(lOut -> lOut
                        .invalidateHttpSession(true)
                        .clearAuthentication(true)
                        //назначаем endpoint для logout
                        .logoutUrl("/cloud/logout")
                        //добавляем обработчик на событие вызова logout
                        .addLogoutHandler(customLogoutHandler)
                        //при успешном logout отправляем клиенту статус 200
                        .logoutSuccessHandler(new HttpStatusReturningLogoutSuccessHandler(HttpStatus.OK)));

        return http.build();
    }

    //менеджер аутентификации, используемый для осуществления входа пользователя
    @Bean
    public AuthenticationManager authenticationManager(JdbcDaoImpl userDetailsService, PasswordEncoder encoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder);
        return new ProviderManager(provider);
    }

    //будем использовать дефолтную JDBC-аутентификацию
    @Bean
    public JdbcDaoImpl userDetailsService(DataSource dataSource) {
        JdbcDaoImpl jdbcDaoImpl = new JdbcDaoImpl();
        if (dataSource != null) {
            jdbcDaoImpl.setDataSource(dataSource);
        } else {
            throw new RuntimeException("DataSource has not been set");
        }
        return jdbcDaoImpl;
    }

    //используем BCrypt-шифрование
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }


}
