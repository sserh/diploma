package ru.raccoon.netologydiploma.dbentities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.UniqueElements;

import java.util.Set;

/**
 * Класс, соотносящийся с таблицей, требующейся для реализации аутентификации по-умолчанию
 */
@Data
@Entity(name = "users")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull @UniqueElements
    private String username;
    @NotNull
    private String password;
    @NotNull
    private boolean enabled;

    @OneToMany(
            fetch = FetchType.EAGER, cascade = CascadeType.ALL,
            mappedBy = "appUser"
    )
    private Set<Authority> authorities;

}
