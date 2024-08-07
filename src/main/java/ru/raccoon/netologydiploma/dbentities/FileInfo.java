package ru.raccoon.netologydiploma.dbentities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String filename;

    @NotNull
    private int size;

    @NotNull
    private byte[] file;

    @NotNull
    private String owner;

    public FileInfo(String filename, int size, @NotNull byte[] file, String owner) {
        this.filename = filename;
        this.size = size;
        this.file = file;
        this.owner = owner;
    }
}
