package ru.raccoon.netologydiploma.dbentities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Класс, соотносящийся с таблицей в БД, требующейся для реализации аутентификации по-умолчанию
 */
@Data
@Entity(name = "authorities")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Authority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String username;
    @NotNull
    private String authority;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "user_fk")
    private User appUser;
}
