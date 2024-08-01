package ru.raccoon.netologydiploma.repository;

import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.raccoon.netologydiploma.dbentities.FileInfo;

import java.util.List;

/**
 * Интерфейс, предоставляющий доступ к данным БД с помощью SQL-запросов, обёрнутых в методы
 */
@Repository
public interface IFileJpaRepository extends JpaRepository<FileInfo, Long> {

    /** Метод получения списка записей с информацией о файлах
     * @param limit Ограничение, устанавливаемое для возвращаемого списка
     * @return Возвращает список записей о файлах
     */
    List<FileInfo> findByOrderByFilenameAsc(Limit limit);

    /** Метод получения записи с информацией о файле по названию файла в информации о файле
     * @param filename Информация о файле
     * @return Возвращает информацию о файле
     */
    FileInfo findByFilename(@NotNull String filename);

}
