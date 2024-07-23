package ru.raccoon.netologydiploma.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    //фильтрация http-запросов по endpoints
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests(auth -> auth
                        .anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults());
        return http.build();
    }

    //провайдер данных со схемой по-умолчанию и шифрованием
    @Bean
    public DaoAuthenticationProvider authenticationProvider(JdbcDaoImpl userDetailsService, PasswordEncoder encoder){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(encoder);
        return provider;
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
