package ru.raccoon.netologydiploma.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;
import ru.raccoon.netologydiploma.dbentities.FileInfo;

@Repository
public class FileRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    public FileRepository(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    /** Метод для добавления в БД новой информации о файле
     * @param fileInfo Информация о файле
     */
    @Transactional
    public void insertWithEntityManager(FileInfo fileInfo) {
        this.entityManager.persist(fileInfo);
        flushAndClearEntity();
    }

    /** Метод для удаления из БД информации о файле
     * @param fileInfo Информация о файле
     */
    @Transactional
    public void deleteWithEntityManager(FileInfo fileInfo) {
        entityManager.remove(fileInfo);
        flushAndClearEntity();
    }

    /** Метод для переименования файла в БД
     * @param fileInfo Информация о файле, который требуется переименовать
     * @param newFileName Новое имя файла
     */
    @Transactional
    public void renameWithEntityManager(FileInfo fileInfo, String newFileName) {
        entityManager.merge(fileInfo);
        fileInfo.setFilename(newFileName);
        flushAndClearEntity();
    }

    /**
     * Метод для очистки менеджера данных
     */
    private void flushAndClearEntity() {
        entityManager.flush();
        entityManager.clear();
    }
}
