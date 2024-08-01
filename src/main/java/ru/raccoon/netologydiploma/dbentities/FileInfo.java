package ru.raccoon.netologydiploma.dbentities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * Класс, соотносящийся с таблицей в БД, требующейся для хранения информации о файлах
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "files")
@Component
public class FileInfo {

    @Id
    @NotNull
    private String filename;

    @NotNull
    private int size;

}
